package com.github.achaaab.scrabble.model;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Trie {

	private final Trie[] children;
	private boolean exists;

	/**
	 * @since 0.0.0
	 */
	public Trie() {

		children = new Trie[26];
		exists = false;
	}

	/**
	 * @param word
	 * @since 0.0.0
	 */
	public void add(String word) {

		if (word.isEmpty()) {

			exists = true;

		} else {

			var firstLetter = word.charAt(0);

			var child = children[firstLetter - 'A'];

			if (child == null) {

				child = new Trie();
				children[firstLetter - 'A'] = child;
			}

			child.add(word.substring(1));
		}
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
		return children[letter - 'A'];
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public boolean isWord() {
		return exists;
	}
}
