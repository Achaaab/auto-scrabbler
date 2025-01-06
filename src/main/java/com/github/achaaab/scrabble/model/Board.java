package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.achaaab.scrabble.model.Award.LETTER_DOUBLE;
import static com.github.achaaab.scrabble.model.Award.LETTER_TRIPLE;
import static com.github.achaaab.scrabble.model.Award.NONE;
import static com.github.achaaab.scrabble.model.Award.WORD_DOUBLE;
import static com.github.achaaab.scrabble.model.Award.WORD_TRIPLE;
import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.Direction.VERTICAL;

/**
 * Scrabble 15x15 board.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Board implements Iterable<Square> {

	public static final int SIZE = 15;
	public static final int HALF_SIZE = SIZE / 2;

	private static final Set<String> DOUBLE_WORD_SQUARES = Set.of(
			"B2", "B14", "C3", "C13", "D4", "D12", "E5", "E11", "H8",
			"K5", "K11", "L4", "L12", "M3", "M13", "N2", "N14");

	private static final Set<String> TRIPLE_WORD_SQUARES = Set.of(
			"A1", "A8", "A15", "H1", "H15", "O1", "O8", "O15");

	private static final Set<String> DOUBLE_LETTER_SQUARES = Set.of(
			"A4", "A12", "C7", "C9", "D1", "D8", "D15", "G3", "G7", "G9", "G13", "H4", "H12",
			"I3", "I7", "I9", "I13", "L1", "L8", "L15", "M7", "M9", "O4", "O12");

	private static final Set<String> TRIPLE_LETTER_SQUARES = Set.of(
			"B6", "B10", "F2", "F6", "F10", "F14", "J2", "J6", "J10", "J14", "N6", "N10");

	/**
	 * Determines the award given by a square.
	 *
	 * @param square name of a square
	 * @return award given by the specified square
	 * @since 0.0.0
	 */
	private static Award getAward(String square) {

		Award award;

		if (TRIPLE_WORD_SQUARES.contains(square)) {
			award = WORD_TRIPLE;
		} else if (DOUBLE_WORD_SQUARES.contains(square)) {
			award = WORD_DOUBLE;
		} else if (TRIPLE_LETTER_SQUARES.contains(square)) {
			award = LETTER_TRIPLE;
		} else if (DOUBLE_LETTER_SQUARES.contains(square)) {
			award = LETTER_DOUBLE;
		} else {
			award = NONE;
		}

		return award;
	}

	private final Square[][] grid;
	private final Map<String, Reference> references;

	/**
	 * Creates an empty board.
	 *
	 * @since 0.0.0
	 */
	public Board() {

		grid = new Square[SIZE][SIZE];
		references = new HashMap<>();

		for (var column = 0; column < SIZE; column++) {
			for (var row = 0; row < SIZE; row++) {

				var name = Square.getName(column, row);
				var award = getAward(name);
				var square = new Square(this, column, row, award);

				grid[column][row] = square;
				references.put(square.getName(HORIZONTAL), new Reference(square, HORIZONTAL));
				references.put(square.getName(VERTICAL), new Reference(square, VERTICAL));
			}
		}
	}

	/**
	 * Gets the square at the specified column and row.
	 *
	 * @param column column, between 0 and {@link #SIZE} excluded
	 * @param row row, between 0 and {@link #SIZE} excluded
	 * @return square at the specified column and row
	 * @since 0.0.0
	 */
	public Square getSquare(int column, int row) {
		return grid[column][row];
	}

	/**
	 * Gets a reference from its key.
	 *
	 * @param key canonical name of a reference
	 * @return associated reference
	 * @since 0.0.0
	 */
	public Reference getReference(String key) {
		return references.get(key);
	}

	/**
	 * Gets a reference from a square and direction.
	 *
	 * @param square reference square
	 * @param direction reference direction
	 * @return reference
	 * @since 0.0.0
	 */
	public Reference getReference(Square square, Direction direction) {
		return getReference(square.getName(direction));
	}

	/**
	 * Plays the given tiles at the given reference.
	 *
	 * @param tiles tiles to play
	 * @param reference reference indicating the starting square and the layout direction
	 * @since 0.0.0
	 */
	public void play(List<Tile> tiles, Reference reference) {

		var square = reference.square();
		var direction = reference.direction();

		for (var tile : tiles) {

			while (square.hasTile()) {
				square = square.getNext(direction);
			}

			square.setTile(tile);
		}
	}

	/**
	 * Gets the adjacent tiles before a square in a direction.
	 *
	 * @param square square
	 * @param direction direction
	 * @return previous tiles
	 * @since 0.0.0
	 */
	public List<Tile> getPreviousTiles(Square square, Direction direction) {

		var tiles = new LinkedList<Tile>();

		square = square.getPrevious(direction);

		while (square != null && square.hasTile()) {

			tiles.addFirst(square.getTile());
			square = square.getPrevious(direction);
		}

		return tiles;
	}

	/**
	 * Gets the adjacent tiles after a square in a direction.
	 *
	 * @param square square
	 * @param direction direction
	 * @return next tiles
	 * @since 0.0.0
	 */
	public List<Tile> getNextTiles(Square square, Direction direction) {

		var tiles = new ArrayList<Tile>();

		square = square.getNext(direction);

		while (square != null && square.hasTile()) {

			tiles.add(square.getTile());
			square = square.getNext(direction);
		}

		return tiles;
	}

	@Override
	public Iterator<Square> iterator() {
		return new SquareIterator(this);
	}

	/**
	 * Clears this board, removing all the tiles on it.
	 *
	 * @since 0.0.0
	 */
	public void clear() {
		forEach(Square::clear);
	}
}
