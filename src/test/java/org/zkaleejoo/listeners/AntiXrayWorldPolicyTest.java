package org.zkaleejoo.listeners;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class AntiXrayWorldPolicyTest {

    @Test
    void allowsMonitoringWhenBlacklistIsEmpty() {
        assertTrue(AntiXrayWorldPolicy.isMonitoringEnabled(Set.of(), "world"));
    }

    @Test
    void blocksConfiguredWorldCaseInsensitively() {
        assertFalse(AntiXrayWorldPolicy.isMonitoringEnabled(Set.of("world_nether"), "WORLD_NETHER"));
    }

    @Test
    void allowsWorldThatIsNotBlacklisted() {
        assertTrue(AntiXrayWorldPolicy.isMonitoringEnabled(Set.of("mines"), "survival"));
    }
}
