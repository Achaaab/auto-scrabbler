package com.github.achaaab.scrabble.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests of {@link TileCollection}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.6
 */
class TileCollectionTest {

	@Test
	void add() {

		var tileCollection = new Bag();

		var tile = new Tile('A', 1);
		tileCollection.add(tile);

		assertEquals(1, tileCollection.size());
		assertEquals(tile, tileCollection.pick('A'));
	}
}
