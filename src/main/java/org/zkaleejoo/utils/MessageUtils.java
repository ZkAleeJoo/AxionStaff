package org.zkaleejoo.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

public class MessageUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("(?i)&([0-9A-FK-OR])");
    private static final char COLOR_CHAR = '\u00A7';
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character(COLOR_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static String getColoredMessage(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String color = matcher.group(1);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(toLegacyHexColor(color)));
        }
        message = matcher.appendTail(buffer).toString();

        return LEGACY_COLOR_PATTERN.matcher(message).replaceAll(COLOR_CHAR + "$1");
    }

    public static void broadcastToPlayersOnly(String message) {
        if (message == null || message.isEmpty())
            return;
        String coloredMessage = getColoredMessage(message);
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (player != null) {
                player.sendMessage(coloredMessage);
            }
        }
    }

    public static String stripColor(String message) {
        if (message == null)
            return null;
        return PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(toComponent(message)));
    }

    public static Component legacyToComponentNoItalic(String message) {
        return toComponent(message)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static Component toComponent(String message) {
        return LEGACY_SERIALIZER.deserialize(Objects.requireNonNull(getColoredMessage(message)));
    }

    public static Component createMenuTitle(String rawTitle) {
        return legacyToComponentNoItalic(formatMenuTitle(rawTitle));
    }

    public static String formatMenuTitle(String rawTitle) {
        if (rawTitle == null || rawTitle.isEmpty()) {
            return "";
        }
        return toSmallCaps(rawTitle);
    }

    public static String toSmallCaps(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder sb = new StringBuilder(text.length());
        int i = 0;
        int len = text.length();

        while (i < len) {
            char c = text.charAt(i);

            // Handle &#RRGGBB hex color codes
            if (c == '&' && i + 7 < len && text.charAt(i + 1) == '#') {
                boolean isHex = true;
                for (int j = i + 2; j < i + 8; j++) {
                    char h = text.charAt(j);
                    if (!((h >= '0' && h <= '9') || (h >= 'a' && h <= 'f') || (h >= 'A' && h <= 'F'))) {
                        isHex = false;
                        break;
                    }
                }
                if (isHex) {
                    sb.append(text, i, i + 8);
                    i += 8;
                    continue;
                }
            }

            // Handle §x§r§r§g§g§b§b internally serialized hex codes
            if (c == COLOR_CHAR && i + 13 < len && (text.charAt(i + 1) == 'x' || text.charAt(i + 1) == 'X')) {
                boolean isInternalHex = true;
                for (int j = i + 2; j < i + 14; j += 2) {
                    if (text.charAt(j) != COLOR_CHAR) {
                        isInternalHex = false;
                        break;
                    }
                    char h = text.charAt(j + 1);
                    if (!((h >= '0' && h <= '9') || (h >= 'a' && h <= 'f') || (h >= 'A' && h <= 'F'))) {
                        isInternalHex = false;
                        break;
                    }
                }
                if (isInternalHex) {
                    sb.append(text, i, i + 14);
                    i += 14;
                    continue;
                }
            }

            // Handle standard legacy color codes &c, §c
            if ((c == '&' || c == COLOR_CHAR) && i + 1 < len) {
                char code = text.charAt(i + 1);
                if (isColorCode(code)) {
                    sb.append(c).append(code);
                    i += 2;
                    continue;
                }
            }

            sb.append(toSmallCapChar(c));
            i++;
        }

        return sb.toString();
    }

    public static boolean isColorCode(char c) {
        return (c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'f')
                || (c >= 'A' && c <= 'F')
                || (c >= 'k' && c <= 'o')
                || (c >= 'K' && c <= 'O')
                || c == 'r' || c == 'R' || c == 'x' || c == 'X';
    }

    public static char toSmallCapChar(char c) {
        return switch (c) {
            case 'a', 'A', 'á', 'Á' -> 'ᴀ';
            case 'b', 'B' -> 'ʙ';
            case 'c', 'C' -> 'ᴄ';
            case 'd', 'D' -> 'ᴅ';
            case 'e', 'E', 'é', 'É' -> 'ᴇ';
            case 'f', 'F', 'ғ' -> 'ꜰ';
            case 'g', 'G' -> 'ɢ';
            case 'h', 'H' -> 'ʜ';
            case 'i', 'I', 'í', 'Í' -> 'ɪ';
            case 'j', 'J' -> 'ᴊ';
            case 'k', 'K' -> 'ᴋ';
            case 'l', 'L' -> 'ʟ';
            case 'm', 'M' -> 'ᴍ';
            case 'n', 'N', 'ñ', 'Ñ' -> 'ɴ';
            case 'o', 'O', 'ó', 'Ó' -> 'ᴏ';
            case 'p', 'P' -> 'ᴘ';
            case 'q', 'Q', 'ǫ', 'ϙ' -> 'ꞯ';
            case 'r', 'R' -> 'ʀ';
            case 's', 'S' -> 'ꜱ';
            case 't', 'T' -> 'ᴛ';
            case 'u', 'U', 'ú', 'Ú', 'ü', 'Ü' -> 'ᴜ';
            case 'v', 'V' -> 'ᴠ';
            case 'w', 'W' -> 'ᴡ';
            case 'x', 'X' -> 'x';
            case 'y', 'Y' -> 'ʏ';
            case 'z', 'Z' -> 'ᴢ';
            default -> c;
        };
    }

    private static String toLegacyHexColor(String color) {
        StringBuilder builder = new StringBuilder(14);
        builder.append(COLOR_CHAR).append('x');
        for (char character : color.toCharArray()) {
            builder.append(COLOR_CHAR).append(character);
        }
        return builder.toString();
    }

}
