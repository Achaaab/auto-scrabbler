package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;

import java.util.List;

/**
 * @author Jonathan Guéhenneux
 * @param reference
 * @param rackTiles
 * @param tiles
 * @param score
 * @since 0.0.0
 */
public record Move(Reference reference, List<Tile> rackTiles, List<Tile> tiles, int score)
		implements Comparable<Move> {

	@Override
	public int compareTo(Move move) {
		return Integer.compare(score, move.score);
	}
}
