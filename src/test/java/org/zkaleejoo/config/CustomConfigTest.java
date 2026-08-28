package org.zkaleejoo.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class CustomConfigTest {

    @Test
    void skipsBundledStatusServersWhenSectionWasCustomized() {
        YamlConfiguration userConfig = new YamlConfiguration();
        userConfig.set("database.status-servers.Testing#1.name", "Testing#1");

        assertTrue(CustomConfig.shouldSkipMissingBundledKey(
                "database.status-servers.survival.name", userConfig));
    }

    @Test
    void keepsBundledStatusServersWhenSectionIsMissing() {
        YamlConfiguration userConfig = new YamlConfiguration();

        assertFalse(CustomConfig.shouldSkipMissingBundledKey(
                "database.status-servers.survival.name", userConfig));
    }
}
