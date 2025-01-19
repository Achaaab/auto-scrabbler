package com.github.achaaab.scrabble.view;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Drawn letters view.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class LettersView extends JTextField {

	/**
	 * Filters typed characters:
	 * <ul>
	 *   <li>Switches characters to upper case.</li>
	 *   <li>Removes non upper case letters.</li>
	 * </ul>
	 *
	 * @param string typed characters
	 * @return filtered characters
	 * @since 0.0.0
	 */
	private static String filter(String string) {
		return string.toUpperCase().replaceAll("[^A-Z ]+", "");
	}

	/**
	 * Creates a new view for drawn letters input.
	 *
	 * @since 0.0.0
	 */
	public LettersView() {

		var document = getDocument();

		if (document instanceof AbstractDocument abstractDocument) {

			abstractDocument.setDocumentFilter(new DocumentFilter() {

				@Override
				public void insertString(
						FilterBypass filterBypass,
						int offset,
						String string,
						AttributeSet attributeSet)
						throws BadLocationException {

					super.insertString(filterBypass, offset, filter(string), attributeSet);
				}

				@Override
				public void replace(
						FilterBypass filterBypass,
						int offset,
						int length,
						String string,
						AttributeSet attributeSet)
						throws BadLocationException {

					super.replace(filterBypass, offset, length, filter(string), attributeSet);
				}
			});
		}
	}
}
