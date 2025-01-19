package com.github.achaaab.scrabble.model.core;

import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.model.core.Dictionary.LETTER_COUNT;

/**
 * Scrabble rack.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Rack extends TileCollection {

	public static final int CAPACITY = 7;

	/**
	 * Returns the number of each letter as an array.
	 * The returned array has a length of {@code {@link Dictionary#LETTER_COUNT} + 1}.
	 * The first element of the array represents the number of A tiles.
	 * The second element of the array represents the number of B tiles.
	 * And so on.
	 * The last element of the array represents the number of blank tiles.
	 *
	 * @return letter counts
	 * @since 0.0.0
	 */
	public int[] getLetterCounts() {

		var letterCounts = new int[LETTER_COUNT + 1];

		tiles.stream().
				map(Tile::letterIndex).
				forEach(letterIndex -> letterCounts[letterIndex]++);

		return letterCounts;
	}

	/**
	 * Returns a sample tile for each letter.
	 * <ul>
	 *   <li>The returned array has a length of {@code {@link Dictionary#LETTER_COUNT} + 1}.</li>
	 *   <li>The first element of the array is an A tile if this rack contains at least 1 A tile,
	 *   {@code null} otherwise.</li>
	 *   <li>The second element of the array is an B tile if this rack contains at least 1 B tile,
	 *   {@code null} otherwise.</li>
	 *   <li>And so on.</li>
	 *   <li>The last element of the array is a blank tile if this rack contains at least 1 blank tile,
	 *   {@code null} otherwise.</li>
	 * </ul>
	 *
	 * @return tile samples
	 * @since 0.0.0
	 */
	public Tile[] getTileSamples() {

		var tileSamples = new Tile[LETTER_COUNT + 1];

		for (var tile : tiles) {
			tileSamples[tile.letterIndex()] = tile;
		}

		return tileSamples;
	}

	@Override
	public boolean isFull() {
		return tiles.size() == CAPACITY;
	}

	/**
	 * Fills this rack from the specified bag, picking random tiles in the bag until this rack is full.
	 *
	 * @param bag bag from which to fill this rack
	 * @since 0.0.0
	 */
	public void fill(Bag bag) {

		while (!bag.isEmpty() && !isFull()) {
			add(bag.pickRandom());
		}
	}

	/**
	 * Picks all tiles from this rack. After calling this function, the rack is left empty.
	 *
	 * @return picked tiles
	 * @since 0.0.0
	 */
	public List<Tile> pickAll() {

		var pickedTiles = new ArrayList<>(tiles);
		tiles.clear();
		return pickedTiles;
	}
}
