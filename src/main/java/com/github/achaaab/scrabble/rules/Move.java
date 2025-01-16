package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;

import java.util.List;

import static com.github.achaaab.scrabble.model.Board.SIZE;
import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static com.github.achaaab.scrabble.tools.Alignment.LEFT;
import static java.lang.Integer.compare;

/**
 * Scrabble move.
 *
 * @param reference square and direction reference
 * @param word played word
 * @param tiles played tiles
 * @param score computed score
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public record Move(Reference reference, String word, List<Tile> tiles, int score)
		implements Comparable<Move> {

	@Override
	public int compareTo(Move move) {
		return compare(score, move.score);
	}

	@Override
	public String toString() {
		return pad(reference.toString(), 5, ' ', LEFT) + pad(word, SIZE, ' ', LEFT) + " → " + score;
	}
}
