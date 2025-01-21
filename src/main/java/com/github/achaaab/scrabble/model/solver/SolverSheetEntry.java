package com.github.achaaab.scrabble.model.solver;

/**
 * Entry in solver sheet.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.2
 */
public class SolverSheetEntry {

	private String word;
	private String key;
	private int score;

	/**
	 * Creates an empty entry.
	 *
	 * @since 0.0.2
	 */
	public SolverSheetEntry() {
		this(null, null, 0);
	}

	/**
	 * Creates an entry from a move.
	 *
	 * @param word played word
	 * @param key square and direction key
	 * @param score score
	 * @since 0.0.2
	 */
	public SolverSheetEntry(String word, String key, int score) {

		this.word = word;
		this.key = key;
		this.score = score;
	}

	/**
	 * @return played word
	 * @since 0.0.2
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word played word
	 * @since 0.0.2
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return square and direction key
	 * @since 0.0.2
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key square and direction key
	 * @since 0.0.2
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return score
	 * @since 0.0.2
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score score
	 * @since 0.0.2
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * An entry is complete if its word and key are non-empty.
	 *
	 * @return whether this entry is complete
	 * @since 0.0.0
	 */
	public boolean isComplete() {
		return word != null && !word.isEmpty() && key != null && !key.isEmpty();
	}

	/**
	 * Clears the word, key and score of this entry.
	 *
	 * @since 1.0.2
	 */
	public void clear() {

		word = null;
		key = null;
		score = 0;
	}

	/**
	 * Copies the specified entry.
	 *
	 * @param entry entry to copy
	 * @since 1.0.2
	 */
	public void copy(SolverSheetEntry entry) {

		word = entry.word;
		key = entry.key;
		score = entry.score;
	}
}
