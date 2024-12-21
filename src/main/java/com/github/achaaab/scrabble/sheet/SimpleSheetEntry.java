package com.github.achaaab.scrabble.sheet;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.2
 */
public class SimpleSheetEntry {

	private String word;
	private String key;
	private int score;

	/**
	 * Creates an empty entry.
	 *
	 * @since 0.0.2
	 */
	public SimpleSheetEntry() {
		this(null, null, 0);
	}

	/**
	 * @param word
	 * @param key
	 * @param score
	 * @since 0.0.2
	 */
	public SimpleSheetEntry(String word, String key, int score) {

		this.word = word;
		this.key = key;
		this.score = score;
	}

	/**
	 * @return
	 * @since 0.0.2
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 * @since 0.0.2
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return
	 * @since 0.0.2
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 * @since 0.0.2
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return
	 * @since 0.0.2
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 * @since 0.0.2
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public boolean isComplete() {
		return word != null && !word.isEmpty() && key != null && !key.isEmpty();
	}
}
