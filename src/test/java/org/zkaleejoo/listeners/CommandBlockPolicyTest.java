package org.zkaleejoo.listeners;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class CommandBlockPolicyTest {

    @Test
    void blocksConfiguredCommandWithArguments() {
        assertTrue(CommandBlockPolicy.isBlocked("/tp Steve", Set.of("tp")));
    }

    @Test
    void blocksNamespacedCommandWhenBaseCommandIsConfigured() {
        assertTrue(CommandBlockPolicy.isBlocked("/minecraft:tp Steve", Set.of("tp")));
    }

    @Test
    void doesNotBlockWhenConfiguredCommandsAreEmpty() {
        assertFalse(CommandBlockPolicy.isBlocked("/tp Steve", Set.of()));
    }

    @Test
    void normalizesConfiguredCommandSlashesAndCase() {
        assertTrue(CommandBlockPolicy.isBlocked("/BAN Steve", Set.of("/ban")));
    }
}
