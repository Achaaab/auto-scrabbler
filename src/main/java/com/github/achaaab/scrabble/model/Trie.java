package com.github.achaaab.scrabble.model;

import java.util.List;

import static java.lang.Character.toUpperCase;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Trie {

	public static final int LETTER_COUNT = 26;

	private final Trie parent;
	private final Trie[] children;
	private boolean exists;

	/**
	 * @since 0.0.0
	 */
	public Trie() {
		this(null);
	}

	/**
	 * @param parent
	 * @since 0.0.0
	 */
	public Trie(Trie parent) {

		this.parent = parent;

		children = new Trie[LETTER_COUNT];
		exists = false;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Trie parent() {
		return parent;
	}

	/**
	 * @param word
	 * @since 0.0.0
	 */
	public void add(String word) {

		if (word.isEmpty()) {

			exists = true;

		} else {

			var firstLetter = toUpperCase(word.charAt(0));

			var child = children[firstLetter - 'A'];

			if (child == null) {

				child = new Trie(this);
				children[firstLetter - 'A'] = child;
			}

			child.add(word.substring(1));
		}
	}

	/**
	 * @param tiles
	 * @return
	 * @since 0.0.0
	 */
	public Trie getChild(List<Tile> tiles) {

		return tiles.isEmpty() ? this :
				getChild(tiles.getFirst()).getChild(tiles.subList(1, tiles.size()));
	}

	/**
	 * @param tile
	 * @return
	 * @since 0.0.0
	 */
	public Trie getChild(Tile tile) {
		return getChild(tile.letter());
	}

	/**
	 * @param prefix
	 * @return
	 * @since 0.0.0
	 */
	public Trie getChild(String prefix) {

		if (prefix.isEmpty()) {
			return this;
		}

		var firstLetter = prefix.charAt(0);
		return getChild(firstLetter).getChild(prefix.substring(1));
	}

	/**
	 * @param letter
	 * @return
	 * @since 0.0.0
	 */
	public Trie getChild(char letter) {
		return children[toUpperCase(letter) - 'A'];
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Trie[] getChildren() {
		return children;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public boolean isWord() {
		return exists;
	}
}