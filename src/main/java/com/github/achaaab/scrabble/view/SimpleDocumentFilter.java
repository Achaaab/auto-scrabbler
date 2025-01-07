package com.github.achaaab.scrabble.view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Simple document filter applying a simple String function to inserted characters.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public abstract class SimpleDocumentFilter extends DocumentFilter {

	private final int maxLength;

	/**
	 * Creates a simple document filter with a maximum document length.
	 *
	 * @param maxLength maximum length of the document to filter, {@code -1} to accept any length
	 * @since 0.0.4
	 */
	public SimpleDocumentFilter(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Filters the given string.
	 *
	 * @param string string to filter, can be {@code null}
	 * @return filtered string or {@code null} if the given string is {@code null}
	 * @since 0.0.4
	 */
	protected abstract String filter(String string);

	@Override
	public void insertString(
			FilterBypass filterBypass,
			int offset,
			String string,
			AttributeSet attributeSet)
			throws BadLocationException {

		var filteredString = filter(string);

		if (maxLength != -1) {

			var availableLength = maxLength - filterBypass.getDocument().getLength();
			filteredString = truncate(filteredString, availableLength);
		}

		super.insertString(
				filterBypass,
				offset,
				filteredString,
				attributeSet);
	}

	@Override
	public void replace(
			FilterBypass filterBypass,
			int offset,
			int length,
			String string,
			AttributeSet attributeSet)
			throws BadLocationException {

		var filteredString = filter(string);

		if (maxLength != -1) {

			var availableLength = maxLength - filterBypass.getDocument().getLength() + length;
			filteredString = truncate(filteredString, availableLength);
		}

		super.replace(
				filterBypass,
				offset,
				length,
				filteredString,
				attributeSet);
	}

	/**
	 * Truncates a string if needed, to have at most available length.
	 *
	 * @param string string to truncate
	 * @param availableLength available length
	 * @return truncated string
	 * @since 0.0.4
	 */
	private String truncate(String string, int availableLength) {

		var stringLength = string.length();

		return stringLength <= availableLength ? string :
				string.substring(0, availableLength);
	}
}
