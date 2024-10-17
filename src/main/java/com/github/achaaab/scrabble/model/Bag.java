package com.github.achaaab.scrabble.model;

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
}
