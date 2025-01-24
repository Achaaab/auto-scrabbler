package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.achaaab.scrabble.model.core.Dictionary.LETTER_COUNT;
import static com.github.achaaab.scrabble.model.core.Rack.CAPACITY;
import static com.github.achaaab.scrabble.model.core.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
	void getTileSamples() {

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

		var tileSamples = rack.getTileSamples();
		assertEquals(LETTER_COUNT + 1, tileSamples.length);

		assertEquals('A', tileSamples[0].letter());
		assertEquals('B', tileSamples[1].letter());
		assertNull(tileSamples[2]);
		assertNull(tileSamples[3]);
		assertEquals('E', tileSamples[4].letter());
		assertNull(tileSamples[5]);
		assertNull(tileSamples[LETTER_COUNT - 1]);
		assertEquals(' ', tileSamples[LETTER_COUNT].letter());
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

	@Test
	void fill() {

		var bag = new Bag();
		bag.addAll(getFrenchTiles());

		var tileCount = bag.size();

		var rack = new Rack();
		rack.fill(bag);

		assertEquals(CAPACITY, rack.size());
		assertEquals(tileCount - CAPACITY, bag.size());
	}

	@Test
	void fill2() {

		var bag = new Bag();
		bag.add(new Tile('A', 1));
		bag.add(new Tile('B', 3));
		bag.add(new Tile('C', 3));

		var rack = new Rack();
		rack.add(new Tile('D', 2));
		rack.add(new Tile('E', 1));
		rack.add(new Tile('F', 4));
		rack.add(new Tile('G', 2));
		rack.add(new Tile('H', 4));

		rack.fill(bag);

		assertEquals(CAPACITY, rack.size());
		assertEquals(1, bag.size());
	}

	@Test
	void fill3() {

		var bag = new Bag();
		bag.add(new Tile('A', 1));
		bag.add(new Tile('B', 3));
		bag.add(new Tile('C', 3));

		var rack = new Rack();
		rack.add(new Tile('D', 2));
		rack.add(new Tile('E', 1));
		rack.add(new Tile('F', 4));

		rack.fill(bag);

		assertEquals(6, rack.size());
		assertTrue(bag.isEmpty());
	}

	@Test
	void pickAll() {

		var rack = new Rack();

		var tile0 = new Tile('A', 1);
		var tile1 = new Tile('B', 3);
		var tile2 = new Tile('C', 3);
		var tile3 = new Tile('D', 2);
		var tile4 = new Tile('E', 1);
		var tile5 = new Tile('F', 4);
		var tile6 = new Tile('G', 2);

		rack.add(tile0);
		rack.add(tile1);
		rack.add(tile2);
		rack.add(tile3);
		rack.add(tile4);
		rack.add(tile5);
		rack.add(tile6);

		var tiles = rack.pickAll();
		assertEquals(List.of(tile0, tile1, tile2, tile3, tile4, tile5, tile6), tiles);
	}

	@Test
	void pickAllEmpty() {

		var rack = new Rack();
		var tiles = rack.pickAll();
		assertTrue(tiles.isEmpty());
	}
}
