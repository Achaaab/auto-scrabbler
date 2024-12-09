package com.github.achaaab.scrabble.model;

import java.util.List;

import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static com.github.achaaab.scrabble.tools.TextHorizontalAlignment.LEFT;
import static com.github.achaaab.scrabble.tools.TextHorizontalAlignment.RIGHT;

/**
 * @param number entry number starting from 1
 * @param residual
 * @param reject
 * @param draw
 * @param word
 * @param reference
 * @param score
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public record DuplicateSheetEntry(
		int number,
		List<Tile> residual,
		boolean reject,
		List<Tile> draw,
		String word,
		Reference reference,
		int score) {

	/**
	 * @return string representation of the rack state before the move
	 * @since 0.0.0
	 */
	private String getRackString() {

		var rack = new StringBuilder();

		if (!reject) {

			for (var tile : residual) {
				rack.append(tile.isBlank() ? '?' : tile.letter());
			}
		}

		if (!draw.isEmpty()) {

			if (!rack.isEmpty()) {
				rack.append(reject ? '-' : '+');
			}

			for (var tile : draw) {
				rack.append(tile.isBlank() ? '?' : tile.letter());
			}
		}

		return rack.toString();
	}

	@Override
	public String toString() {

		return pad(number + "  ", 4, ' ', RIGHT) +
				pad(getRackString(), 10, ' ', LEFT) +
				pad(word, 16, ' ', LEFT) +
				pad(reference.toString(), 5, ' ', LEFT) +
				pad(Integer.toString(score), 4, ' ', RIGHT);
	}
}
