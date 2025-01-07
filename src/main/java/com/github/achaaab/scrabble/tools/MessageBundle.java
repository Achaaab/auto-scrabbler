package com.github.achaaab.scrabble.tools;

import java.util.ResourceBundle;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

/**
 * Global message bundle.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.5
 */
public class MessageBundle {

	private static final ResourceBundle BUNDLE = getBundle("messages");

	/**
	 * Gets a message from the bundle.
	 *
	 * @param key key of the message
	 * @param arguments optional message arguments
	 * @return message formatted with given arguments, or raw message if no arguments were given
	 * @since 0.0.5
	 */
	public static String getMessage(String key, Object... arguments) {

		var message = BUNDLE.getString(key);

		if (arguments.length > 0) {
			message = format(message, arguments);
		}

		return message;
	}
}
