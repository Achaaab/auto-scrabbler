package com.github.achaaab.scrabble.model.core;

import com.github.achaaab.scrabble.tools.Trie;

import static com.github.achaaab.scrabble.tools.ResourceUtility.consumeLines;

/**
 * Scrabble dictionary.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Dictionary {

	public static final int LETTER_COUNT = 26;

	public static final Dictionary FRENCH_ODS9 = new Dictionary("ods9.txt");
	public static final Dictionary ENGLISH_CSW21 = new Dictionary("csw21.txt");

	private final Trie trie;

	/**
	 * Creates a dictionary from a resource.
	 *
	 * @param resourceName name of the dictionary resource
	 * @since 0.0.0
	 */
	public Dictionary(String resourceName) {

		trie = new Trie();

		consumeLines(resourceName, this::add);
	}

	/**
	 * Adds a word into this dictionary.
	 *
	 * @param word word to add
	 * @since 0.0.0
	 */
	private void add(String word) {
		trie.add(word);
	}

	/**
	 * Returns a Trie representation of this dictionary.
	 *
	 * @return word tree
	 * @since 0.0.0
	 */
	public Trie getTrie() {
		return trie;
	}
}
