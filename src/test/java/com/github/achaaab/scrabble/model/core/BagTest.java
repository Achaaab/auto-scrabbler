package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static com.github.achaaab.scrabble.model.core.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	void pickRandom() {

		var bag = new Bag();
		assertThrows(NoSuchElementException.class, bag::pickRandom);

		var tile0 = new Tile('A', 1);
		bag.add(tile0);
		assertEquals(tile0, bag.pickRandom());
		assertTrue(bag.isEmpty());

		var tile1 = new Tile('B', 3);
		var tile2 = new Tile('C', 3);
		var tiles = Set.of(tile1, tile2);
		bag.addAll(tiles);
		assertTrue(tiles.contains(bag.pickRandom()));
		assertEquals(1, bag.size());
	}

	@Test
	void pickRandom1() {

		var bag = new Bag();
		var randomTiles = bag.pickRandom(1);
		assertTrue(randomTiles.isEmpty());

		var tile0 = new Tile('A', 1);
		bag.add(tile0);
		randomTiles = bag.pickRandom(1);
		assertEquals(1, randomTiles.size());
		assertEquals(tile0, randomTiles.getFirst());
		assertTrue(bag.isEmpty());

		var tile1 = new Tile('B', 3);
		var tile2 = new Tile('C', 3);
		var tiles = Set.of(tile1, tile2);
		bag.addAll(tiles);
		randomTiles = bag.pickRandom(1);
		assertEquals(1, randomTiles.size());
		assertTrue(tiles.contains(randomTiles.getFirst()));
		assertEquals(1, bag.size());
	}

	@Test
	void pickRandom2() {

		var bag = new Bag();
		var randomTiles = bag.pickRandom(2);
		assertTrue(randomTiles.isEmpty());

		var tile0 = new Tile('A', 1);
		bag.add(tile0);
		randomTiles = bag.pickRandom(2);
		assertEquals(1, randomTiles.size());
		assertEquals(tile0, randomTiles.getFirst());
		assertTrue(bag.isEmpty());

		var tile1 = new Tile('B', 3);
		var tile2 = new Tile('C', 3);
		var tiles = Set.of(tile1, tile2);
		bag.addAll(tiles);
		randomTiles = bag.pickRandom(2);
		assertTrue(tiles.containsAll(randomTiles));
		assertTrue(randomTiles.containsAll(tiles));
		assertTrue(bag.isEmpty());

		var tile3 = new Tile('D', 2);
		var tile4 = new Tile('E', 1);
		var tile5 = new Tile('F', 4);
		tiles = Set.of(tile3, tile4, tile5);
		bag.addAll(tiles);
		randomTiles = bag.pickRandom(2);
		assertTrue(tiles.containsAll(randomTiles));
		assertEquals(1, bag.size());
	}
}
