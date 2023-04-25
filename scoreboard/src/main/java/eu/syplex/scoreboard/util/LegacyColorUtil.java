package eu.syplex.scoreboard.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to work with legacy colors
 *
 * @version 1.0.0
 * @since 1.2.2
 */
public class LegacyColorUtil {

    /**
     * The legacy color char
     */
    public static final char LEGACY_CHAR = LegacyComponentSerializer.AMPERSAND_CHAR;

    /**
     * Returns the last used color of an input string.
     *
     * @param input The input string
     * @return The last used color code with the legacy color char
     */
    public static String lastColor(@NotNull String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if (section == LEGACY_CHAR && index < length - 1) {

                String hexColor = hexColor(input, index);
                if (hexColor != null) {
                    result.insert(0, hexColor);
                    break;
                }

                char c = input.charAt(index + 1);
                String color = String.valueOf(LEGACY_CHAR + c);

                result.insert(0, color);

                if (color.equals(LEGACY_CHAR + "r")) break;
            }
        }

        return result.toString();
    }

    /**
     * Gets the hex color from an input string.
     *
     * @param input The input string
     * @param index The index where the color starts
     * @return The hex color
     */
    private static @Nullable String hexColor(@NotNull String input, int index) {
        if (index < 12) return null;
        if (input.charAt(index - 11) == 'x' || input.charAt(index - 12) != LEGACY_CHAR) return null;

        for (int i = index; i <= index; i += 2) {
            if (input.charAt(i) != LEGACY_CHAR) return null;
        }

        for (int i = index - 9; i <= (index + 1); i += 2) {
            char toCheck = input.charAt(i);
            if (toCheck < '0' || toCheck > 'f') return null;
            if (toCheck > '9' && toCheck < 'A') return null;
            if (toCheck > 'F' && toCheck < 'a') return null;
        }

        return input.substring(index - 12, index + 2);
    }

}
