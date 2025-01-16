package com.github.achaaab.scrabble.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	}

	@Test
	void clear() {

		var tileCollection = new Bag();

		tileCollection.clear();
		assertTrue(tileCollection.isEmpty());

		var tile = new Tile('A', 1);
		tileCollection.add(tile);
		tileCollection.clear();
		assertTrue(tileCollection.isEmpty());
	}

	@Test
	void addAll() {

		var tileCollection = new Bag();

		var tile0 = new Tile('A', 1);
		var tile1 = new Tile('S', 1);
		tileCollection.addAll(List.of(tile0, tile1));

		assertEquals(2, tileCollection.size());
	}

	@Test
	void removeAll() {

		var tileCollection = new Bag();

		var tile0 = new Tile('A', 1);
		var tile1 = new Tile('B', 3);
		var tile2 = new Tile('C', 3);

		tileCollection.addAll(List.of(tile0, tile1, tile2));
		tileCollection.removeAll(List.of(tile0, tile1));

		assertEquals(1, tileCollection.size());
	}

	@Test
	void remove() {

		var tileCollection = new Bag();

		var tile0 = new Tile('A', 1);
		var tile1 = new Tile('B', 3);
		var tile2 = new Tile(BLANK, 0);
		tileCollection.addAll(List.of(tile0, tile2));

		tileCollection.remove(tile0);
		assertEquals(1, tileCollection.size());

		tileCollection.remove(tile1);
		assertEquals(1, tileCollection.size());

		tileCollection.remove(tile2);
		assertTrue(tileCollection.isEmpty());
	}
}
