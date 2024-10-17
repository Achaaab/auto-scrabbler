package com.github.achaaab.scrabble.model;

import java.util.List;
import java.util.Set;

import static com.github.achaaab.scrabble.tools.Combinatorics.getPartialPermutations;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Rack extends TileCollection {

	public static final int CAPACITY = 7;

	/**
	 * @return number of tiles on this rack
	 * @since 0.0.0
	 */
	public int getTileCount() {
		return tiles.size();
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Set<List<Tile>> getPermutations() {
		return getPartialPermutations(tiles);
	}

	@Override
	public boolean isFull() {
		return tiles.size() == CAPACITY;
	}
}
