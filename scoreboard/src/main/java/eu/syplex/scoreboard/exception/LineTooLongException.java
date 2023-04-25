package eu.syplex.scoreboard.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an exception thrown if a given line is longer than the maximum of allowed chars.
 */
public class LineTooLongException extends Exception {

    /**
     * Instantiates a new {@link LineTooLongException} with the conflicting line and the maximum of allowed chars.
     *
     * @param line     The line
     * @param maxChars The amount of allowed chars
     */
    public LineTooLongException(@NotNull String line, int maxChars) {
        super("The given line is longer than the maximum allowed chars (" + maxChars + ") but there are currently " + line.length() + " chars present.");
    }
}
