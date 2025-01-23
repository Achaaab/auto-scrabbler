package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.Test;

import static com.github.achaaab.scrabble.model.core.Dictionary.LETTER_COUNT;
import static com.github.achaaab.scrabble.model.core.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	void getLetterCounts() {

		var bag = new Bag();
		bag.addAll(getFrenchTiles());

		var rack = new Rack();

		rack.add(bag.pick(' '));
		rack.add(bag.pick('A'));
		rack.add(bag.pick('B'));
		rack.add(bag.pick('B'));
		rack.add(bag.pick('E'));
		rack.add(bag.pick('E'));
		rack.add(bag.pick('E'));

		var letterCounts = rack.getLetterCounts();
		assertEquals(LETTER_COUNT + 1, letterCounts.length);

		assertEquals(1, letterCounts[0]);
		assertEquals(2, letterCounts[1]);
		assertEquals(0, letterCounts[2]);
		assertEquals(0, letterCounts[3]);
		assertEquals(3, letterCounts[4]);
		assertEquals(0, letterCounts[5]);
		assertEquals(0, letterCounts[LETTER_COUNT - 1]);
		assertEquals(1, letterCounts[LETTER_COUNT]);
	}

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
