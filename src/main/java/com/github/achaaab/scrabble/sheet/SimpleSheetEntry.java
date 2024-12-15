package com.github.achaaab.scrabble.sheet;

public class SimpleSheetEntry {

	private String word;
	private String key;
	private int score;

	public SimpleSheetEntry() {
		this(null, null, 0);
	}

	public SimpleSheetEntry(String word, String key, int score) {

		this.word = word;
		this.key = key;
		this.score = score;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getScore() {
		return score;
	}

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
