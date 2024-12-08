package com.github.achaaab.scrabble.model;

/**
 * @param board
 * @param rack
 * @param bag
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public record Duplicate(Board board, Rack rack, Bag bag) {

	public void change(String changedLetters, String replacementLetters) {

		draw(replacementLetters);
		reject(changedLetters);
	}

	public void play(String playedLetters, String reference, String replacementLetters) {

		var tiles = rack.pickAll(playedLetters);
		board.play(tiles, reference);
		draw(replacementLetters);
	}

	public void play(String playedLetters, String reference) {

		var tiles = bag.pickAll(playedLetters);
		board.play(tiles, reference);
	}

	public void draw(String letters) {
		rack.addAll(bag.pickAll(letters));
	}

	public void reject(String letters) {
		bag.addAll(rack.pickAll(letters));
	}
}
