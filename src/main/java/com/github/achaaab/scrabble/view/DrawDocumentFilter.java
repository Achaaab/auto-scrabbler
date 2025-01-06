package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Rack;

/**
 * Document filter for drawn letters.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class DrawDocumentFilter extends SimpleDocumentFilter {

	/**
	 * Singleton.
	 *
	 * @since 0.0.4
	 */
	public static final DrawDocumentFilter INSTANCE = new DrawDocumentFilter();

	/**
	 * Private constructor enforcing singleton usage.
	 *
	 * @see #INSTANCE
	 * @since 0.0.4
	 */
	private DrawDocumentFilter() {
		super(Rack.CAPACITY);
	}

	@Override
	protected String filter(String string) {

		return string == null ? null :
				string.toUpperCase().replaceAll("[^A-Z ]+", "");
	}
}
