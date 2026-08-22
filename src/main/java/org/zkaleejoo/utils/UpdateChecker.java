package org.zkaleejoo.utils;

import org.zkaleejoo.AxionStaff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class UpdateChecker {

    private static final String GITHUB_VERSION_URL = "https://gist.githubusercontent.com/ZkAleeJoo/be4bcf6afd0f4981de630ac0c48af74a/raw/AxionStaff";

    private final AxionStaff plugin;

    public UpdateChecker(AxionStaff plugin) {
        this.plugin = plugin;
    }

    public void getVersion(final Consumer<String> consumer) {
        FoliaCompat.runAsync(this.plugin, () -> {
            HttpURLConnection connection = null;
            try {
                URL url = URI.create(GITHUB_VERSION_URL).toURL();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "AxionStaff-UpdateChecker");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int statusCode = connection.getResponseCode();
                if (statusCode < 200 || statusCode >= 300) {
                    plugin.getLogger().warning("Could not check updates from BBB. HTTP " + statusCode);
                    return;
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String latestVersion = reader.readLine();
                    if (latestVersion != null && !latestVersion.isBlank()) {
                        String trimmedVersion = latestVersion.trim();
                        if (plugin.isEnabled()) {
                            FoliaCompat.runGlobal(plugin, () -> consumer.accept(trimmedVersion));
                        }
                    } else {
                        plugin.getLogger().info("The version in BBB is empty.");
                    }
                }
            } catch (Exception exception) {
                plugin.getLogger().info("Could not connect to BBB to check for updates: " + exception.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
