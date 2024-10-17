package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.IntStream.range;

/**
 * @param letter letter on the tile or {@code ' '} for blank tile
 * @param value raw value of the tile
 * @since 0.0.0
 */
public record Tile(char letter, int value) implements Comparable<Tile> {

	public static final Tile BLANK = new Tile(' ', 0);

	/**
	 * @return
	 * @since 0.0.0
	 */
	public static List<Tile> getFrenchTiles() {

		var frenchTiles = new ArrayList<Tile>();

		addTiles(frenchTiles, BLANK, 2);
		addTiles(frenchTiles, 'E', 1, 15);
		addTiles(frenchTiles, 'A', 1, 9);
		addTiles(frenchTiles, 'I', 1, 8);
		addTiles(frenchTiles, 'N', 1, 6);
		addTiles(frenchTiles, 'O', 1, 6);
		addTiles(frenchTiles, 'R', 1, 6);
		addTiles(frenchTiles, 'S', 1, 6);
		addTiles(frenchTiles, 'T', 1, 6);
		addTiles(frenchTiles, 'U', 1, 6);
		addTiles(frenchTiles, 'L', 1, 5);
		addTiles(frenchTiles, 'D', 2, 3);
		addTiles(frenchTiles, 'M', 2, 3);
		addTiles(frenchTiles, 'G', 2, 2);
		addTiles(frenchTiles, 'B', 3, 2);
		addTiles(frenchTiles, 'C', 3, 2);
		addTiles(frenchTiles, 'P', 3, 2);
		addTiles(frenchTiles, 'F', 4, 2);
		addTiles(frenchTiles, 'H', 4, 2);
		addTiles(frenchTiles, 'V', 4, 2);
		addTiles(frenchTiles, 'J', 8, 1);
		addTiles(frenchTiles, 'Q', 8, 1);
		addTiles(frenchTiles, 'K', 10, 1);
		addTiles(frenchTiles, 'W', 10, 1);
		addTiles(frenchTiles, 'X', 10, 1);
		addTiles(frenchTiles, 'Y', 10, 1);
		addTiles(frenchTiles, 'Z', 10, 1);

		return frenchTiles;
	}


	/**
	 * @return
	 * @since 0.0.0
	 */
	public static List<Tile> getEnglishTiles() {

		var englishTiles = new ArrayList<Tile>();

		addTiles(englishTiles, BLANK, 2);
		addTiles(englishTiles, 'E', 1, 12);
		addTiles(englishTiles, 'A', 1, 9);
		addTiles(englishTiles, 'I', 1, 9);
		addTiles(englishTiles, 'O', 1, 8);
		addTiles(englishTiles, 'N', 1, 6);
		addTiles(englishTiles, 'R', 1, 6);
		addTiles(englishTiles, 'T', 1, 6);
		addTiles(englishTiles, 'L', 1, 4);
		addTiles(englishTiles, 'S', 1, 4);
		addTiles(englishTiles, 'U', 1, 4);
		addTiles(englishTiles, 'D', 2, 4);
		addTiles(englishTiles, 'G', 2, 3);
		addTiles(englishTiles, 'B', 3, 2);
		addTiles(englishTiles, 'C', 3, 2);
		addTiles(englishTiles, 'M', 3, 2);
		addTiles(englishTiles, 'P', 3, 2);
		addTiles(englishTiles, 'F', 4, 2);
		addTiles(englishTiles, 'H', 4, 2);
		addTiles(englishTiles, 'V', 4, 2);
		addTiles(englishTiles, 'W', 4, 2);
		addTiles(englishTiles, 'Y', 4, 2);
		addTiles(englishTiles, 'K', 5, 1);
		addTiles(englishTiles, 'J', 8, 1);
		addTiles(englishTiles, 'X', 8, 1);
		addTiles(englishTiles, 'Q', 10, 1);
		addTiles(englishTiles, 'Z', 10, 1);

		return englishTiles;
	}

	/**
	 * @param tiles
	 * @param letter
	 * @param value
	 * @param count
	 * @since 0.0.0
	 */
	private static void addTiles(List<Tile> tiles, char letter, int value, int count) {
		addTiles(tiles, new Tile(letter, value), count);
	}

	/**
	 * @param tiles
	 * @param tile
	 * @param count
	 * @since 0.0.0
	 */
	private static void addTiles(List<Tile> tiles, Tile tile, int count){
		range(0, count).forEach(_ -> tiles.add(tile));
	}

	@Override
	public int compareTo(Tile tile) {
		return Character.compare(letter, tile.letter);
	}
}
