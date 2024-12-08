package com.github.achaaab.scrabble.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Dictionary {

	public static final Dictionary FRENCH_ODS9 = new Dictionary("ods9.txt");
	public static final Dictionary ENGLISH_CSW21 = new Dictionary("csw21.txt");

	private final Set<String> words;
	private final Trie trie;

	/**
	 * @param resource
	 * @since 0.0.0
	 */
	public Dictionary(String resource) {

		words = new HashSet<>();
		trie = new Trie();

		var classLoader = getClass().getClassLoader();

		try (var inputStream = classLoader.getResourceAsStream(resource)) {

			requireNonNull(inputStream);
			var reader = new BufferedReader(new InputStreamReader(inputStream));

			reader.lines().
					map(String::toUpperCase).
					forEach(this::add);

		} catch (IOException cause) {

			throw new UncheckedIOException(cause);
		}
	}

	/**
	 * @param word
	 * @since 0.0.0
	 */
	private void add(String word) {

		words.add(word);
		trie.add(word);
	}

	/**
	 * @param word
	 * @return
	 * @since 0.0.0
	 */
	public boolean contains(String word) {
		return words.contains(word);
	}

	/**
	 * @return word tree
	 * @since 0.0.0
	 */
	public Trie getTrie() {
		return trie;
	}
}
