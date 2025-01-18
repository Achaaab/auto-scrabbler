package com.github.achaaab.scrabble.model;

import org.junit.jupiter.api.Test;

import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests of {@link Bag}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.7
 */
class BagTest {

	@Test
	void isFull() {

		var bag = new Bag();
		assertFalse(bag.isFull());

		bag.addAll(getFrenchTiles());
		assertFalse(bag.isFull());
	}
}
