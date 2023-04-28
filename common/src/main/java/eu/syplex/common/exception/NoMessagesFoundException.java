package eu.syplex.common.exception;

/**
 * Represents an exception thrown when a list of messages to send was not provided.
 */
public class NoMessagesFoundException extends Exception {

	/**
	 * Instantiates a new {@link NoMessagesFoundException}.
	 */
	public NoMessagesFoundException() {
		super("Could not find any messages in the given list!");
	}
}
