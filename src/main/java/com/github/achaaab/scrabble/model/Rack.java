package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.List;

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
	public int[] getLetterCounts() {

		var letterCounts = new int[27];

		tiles.stream().
				map(Tile::letterIndex).
				forEach(letterIndex -> letterCounts[letterIndex]++);

		return letterCounts;
	}

	@Override
	public boolean isFull() {
		return tiles.size() == CAPACITY;
	}

	/**
	 * @param bag
	 * @since 0.0.0
	 */
	public void fill(Bag bag) {

		while (!bag.isEmpty() && !isFull()) {
			add(bag.pickRandom());
		}
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> pickAll() {

		var pickedTiles = new ArrayList<>(tiles);
		tiles.clear();
		return pickedTiles;
	}
}
