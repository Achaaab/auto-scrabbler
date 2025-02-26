package com.github.achaaab.scrabble.model.duplicate;

import com.github.achaaab.scrabble.model.core.Reference;
import com.github.achaaab.scrabble.model.core.Tile;

import java.util.List;

import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static com.github.achaaab.scrabble.tools.Alignment.LEFT;
import static com.github.achaaab.scrabble.tools.Alignment.RIGHT;

/**
 * Duplicate scoring sheet entry.
 *
 * @param number entry number starting from 1
 * @param residual residual tiles
 * @param reject whether this entry is not a move but an exchange of tiles
 * @param draw drawn tiles
 * @param word played word, {@code null} if this entry is an exchange of tiles
 * @param reference reference square and direction, {@code null} if this entry is an exchange of tiles
 * @param score score
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
