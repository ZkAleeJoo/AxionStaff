package org.zkaleejoo.listeners;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StaffItemsListenerTest {

    @Test
    void silentContainerInspectionIsReadOnlyWhenItemMovementIsDisabled() {
        assertTrue(StaffItemsListener.shouldCancelSilentContainerMove(false));
        assertFalse(StaffItemsListener.shouldSaveSilentContainerChanges(false));
    }

    @Test
    void silentContainerInspectionIsEditableWhenItemMovementIsEnabled() {
        assertFalse(StaffItemsListener.shouldCancelSilentContainerMove(true));
        assertTrue(StaffItemsListener.shouldSaveSilentContainerChanges(true));
    }
}
