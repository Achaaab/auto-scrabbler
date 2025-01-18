package com.github.achaaab.scrabble.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests of {@link Rack}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.7
 */
class RackTest {

	@Test
	void isFull() {

		var bag = new Bag();
		bag.addAll(getFrenchTiles());

		var rack = new Rack();
		assertFalse(rack.isFull());

		rack.add(bag.pick('S'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('C'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('R'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('A'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('B'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('B'));
		assertFalse(rack.isFull());

		rack.add(bag.pick('L'));
		assertTrue(rack.isFull());
	}
}
