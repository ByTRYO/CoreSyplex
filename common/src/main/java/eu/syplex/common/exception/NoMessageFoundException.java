package eu.syplex.common.exception;

/**
 * Represents an exception thrown when a message to send was not provided.
 */
public class NoMessageFoundException extends Exception {

	/**
	 * Instantiates a new {@link NoMessageFoundException}.
	 */
	public NoMessageFoundException() {
		super("Could not find the messages to send!");
	}
}
