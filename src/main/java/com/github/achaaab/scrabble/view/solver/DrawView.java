package com.github.achaaab.scrabble.view.solver;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

/**
 * Drawn letters view.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class DrawView extends JTextField {

	/**
	 * Creates a new view for drawn letters input.
	 *
	 * @param onUpdate to run if drawn letters have changed
	 * @since 0.0.0
	 */
	public DrawView(Runnable onUpdate) {

		var document = getDocument();

		if (document instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(DrawDocumentFilter.INSTANCE);
		}

		document.addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent event) {
				onUpdate.run();
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				onUpdate.run();
			}

			@Override
			public void changedUpdate(DocumentEvent event) {

			}
		});
	}
}
