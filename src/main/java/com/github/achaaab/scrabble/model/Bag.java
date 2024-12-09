package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A bag of tiles.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Bag extends TileCollection {

	private static final Random RANDOM = new Random();

	/**
	 * Picks a random tile from this bag.
	 *
	 * @return picked tile
	 * @since 0.0.0
	 */
	public Tile pickRandom() {

		var tileCount = tiles.size();
		var tileIndex = RANDOM.nextInt(tileCount);
		return tiles.remove(tileIndex);
	}

	/**
	 * Picks random tile from this bag until the specified count is reached or this bag is empty.
	 *
	 * @param count
	 * @return picked tiles
	 * @since 0.0.0
	 */
	public List<Tile> pickRandom(int count) {

		var pickedTiles = new ArrayList<Tile>();

		while (pickedTiles.size() < count && !isEmpty()) {
			pickedTiles.add(pickRandom());
		}

		return pickedTiles;
	}
}
