package org.zkaleejoo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    @Test
    void detailedDurationIncludesHoursMinutesAndSeconds() {
        long duration = (23L * 60L * 60L * 1000L)
                + (59L * 60L * 1000L)
                + (10L * 1000L);

        assertEquals("23 hours 59 minutes 10 seconds",
                TimeUtils.getDetailedDurationString(duration, "Permanent", "days", "hours", "minutes", "seconds"));
    }

    @Test
    void detailedDurationKeepsPermanentLabel() {
        assertEquals("Permanent",
                TimeUtils.getDetailedDurationString(-1L, "Permanent", "days", "hours", "minutes", "seconds"));
    }
}
