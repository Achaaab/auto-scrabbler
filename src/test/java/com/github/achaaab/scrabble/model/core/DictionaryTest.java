package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests of {@link Dictionary}.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.3
 */
class DictionaryTest {

	@Test
	void trie() {

		var dictionary = new Dictionary("test-dictionary.txt");

		var trie = dictionary.trie();
		assertFalse(trie.isWord());
		assertNull(trie.getChild('a'));

		trie = trie.getChild('f');
		assertFalse(trie.isWord());

		trie = trie.getChild('o');
		assertFalse(trie.isWord());

		trie = trie.getChild('o');
		assertTrue(trie.isWord());

		trie = trie.getChild('o');
		assertTrue(trie.isWord());

		trie = dictionary.trie().getChild('b');
		assertFalse(trie.isWord());

		trie = trie.getChild('a');
		assertFalse(trie.isWord());

		trie = trie.getChild('r');
		assertTrue(trie.isWord());
	}
}
