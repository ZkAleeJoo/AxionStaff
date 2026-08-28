package org.zkaleejoo.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.zkaleejoo.managers.StaffActionBarPolicy.ActionBarType;

class StaffActionBarPolicyTest {

    @Test
    void staffModeHasPriorityOverVanish() {
        assertEquals(ActionBarType.STAFF_MODE, StaffActionBarPolicy.select(true, true));
    }

    @Test
    void staffModeRemainsVisibleWhenVanishIsDisabled() {
        assertEquals(ActionBarType.STAFF_MODE, StaffActionBarPolicy.select(true, false));
    }

    @Test
    void standaloneVanishUsesVanishActionBar() {
        assertEquals(ActionBarType.VANISH, StaffActionBarPolicy.select(false, true));
    }

    @Test
    void inactiveStatesUseNoActionBar() {
        assertEquals(ActionBarType.NONE, StaffActionBarPolicy.select(false, false));
    }
}
