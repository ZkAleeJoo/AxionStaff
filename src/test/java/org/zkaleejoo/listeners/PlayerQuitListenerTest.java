package org.zkaleejoo.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.junit.jupiter.api.Test;

class PlayerQuitListenerTest {

    @Test
    void restoresStaffInventoryBeforeSavingQuitSnapshot() throws Exception {
        Method sequenceMethod = findQuitSequenceMethod();
        assertNotNull(sequenceMethod, "The quit workflow must expose its inventory operation order.");

        List<String> operations = new ArrayList<>();
        sequenceMethod.invoke(
                null,
                true,
                (Runnable) () -> operations.add("restore"),
                (Runnable) () -> operations.add("snapshot"),
                (Runnable) () -> operations.add("cleanup"));

        assertEquals(List.of("restore", "snapshot", "cleanup"), operations);
    }

    @Test
    void skipsStaffRestoreForPlayersOutsideStaffMode() throws Exception {
        Method sequenceMethod = findQuitSequenceMethod();
        assertNotNull(sequenceMethod, "The quit workflow must expose its inventory operation order.");

        List<String> operations = new ArrayList<>();
        sequenceMethod.invoke(
                null,
                false,
                (Runnable) () -> operations.add("restore"),
                (Runnable) () -> operations.add("snapshot"),
                (Runnable) () -> operations.add("cleanup"));

        assertEquals(List.of("snapshot", "cleanup"), operations);
    }

    @Test
    void restoresInventoryBeforeOtherQuitListenersRun() throws Exception {
        EventHandler handler = PlayerQuitListener.class
                .getDeclaredMethod("onQuit", org.bukkit.event.player.PlayerQuitEvent.class)
                .getAnnotation(EventHandler.class);

        assertNotNull(handler);
        assertEquals(EventPriority.LOWEST, handler.priority());
    }

    private Method findQuitSequenceMethod() {
        for (Method method : PlayerQuitListener.class.getDeclaredMethods()) {
            if (method.getName().equals("runInventoryQuitSequence")) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
}
