package org.zkaleejoo.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActivePunishmentRecordTest {

    @Test
    void permanentRecordNeverExpires() {
        ActivePunishmentRecord record = new ActivePunishmentRecord(
                ActivePunishmentRecord.Type.BAN,
                "AleeJooSw",
                "Console",
                "Testing",
                -1L);

        assertTrue(record.isPermanent());
        assertFalse(record.isExpired(1_000L));
        assertEquals(-1L, record.remainingMillis(1_000L));
    }

    @Test
    void temporaryRecordReportsRemainingTime() {
        ActivePunishmentRecord record = new ActivePunishmentRecord(
                ActivePunishmentRecord.Type.MUTE,
                "AleeJooSw",
                "Staff",
                "Spam",
                15_000L);

        assertFalse(record.isPermanent());
        assertFalse(record.isExpired(10_000L));
        assertEquals(5_000L, record.remainingMillis(10_000L));
    }

    @Test
    void expiredRecordHasNoRemainingTime() {
        ActivePunishmentRecord record = new ActivePunishmentRecord(
                ActivePunishmentRecord.Type.BAN,
                "AleeJooSw",
                "Staff",
                "Expired",
                5_000L);

        assertTrue(record.isExpired(10_000L));
        assertEquals(0L, record.remainingMillis(10_000L));
    }
}
