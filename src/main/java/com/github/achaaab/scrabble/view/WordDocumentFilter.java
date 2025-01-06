package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Board;

/**
 * Document filter for played words.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class WordDocumentFilter extends SimpleDocumentFilter {

	/**
	 * Singleton.
	 *
	 * @since 0.0.4
	 */
	public static final WordDocumentFilter INSTANCE = new WordDocumentFilter();

	/**
	 * Private constructor enforcing singleton usage.
	 *
	 * @see #INSTANCE
	 * @since 0.0.4
	 */
	private WordDocumentFilter() {
		super(Board.SIZE);
	}

	@Override
	protected String filter(String string) {
		return string.replaceAll("[^A-Za-z]+", "");
	}
}
