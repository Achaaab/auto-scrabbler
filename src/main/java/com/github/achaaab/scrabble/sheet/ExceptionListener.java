package com.github.achaaab.scrabble.sheet;

/**
 * Exception listener primarily implemented by views to catch model exceptions.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public interface ExceptionListener {

	/**
	 * Manages an occurred exception. Usually displays it to the user.
	 *
	 * @param exception occurred exception
	 * @since 0.0.0
	 */
	void exceptionOccurred(Exception exception);
}
