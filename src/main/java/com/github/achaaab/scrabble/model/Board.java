package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.achaaab.scrabble.model.Award.LETTER_DOUBLE;
import static com.github.achaaab.scrabble.model.Award.LETTER_TRIPLE;
import static com.github.achaaab.scrabble.model.Award.NONE;
import static com.github.achaaab.scrabble.model.Award.WORD_DOUBLE;
import static com.github.achaaab.scrabble.model.Award.WORD_TRIPLE;
import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.Direction.VERTICAL;
import static java.util.stream.StreamSupport.stream;

/**
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
	 * @param square
	 * @return
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
	 * @param column
	 * @param row
	 * @return
	 * @since 0.0.0
	 */
	public Square getSquare(int column, int row) {
		return grid[column][row];
	}

	/**
	 * @param name
	 * @return
	 * @since 0.0.0
	 */
	public Reference getReference(String name) {
		return references.get(name);
	}

	/**
	 * @param square
	 * @param direction
	 * @return
	 * @since 0.0.0
	 */
	public Reference getReference(Square square, Direction direction) {
		return getReference(square.getName(direction));
	}

	/**
	 * @param tiles
	 * @param reference
	 * @since 0.0.0
	 */
	public void play(List<Tile> tiles, String reference) {
		play(tiles, references.get(reference));
	}

	/**
	 * @param tiles
	 * @param reference
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
	 * @param reference
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getPreviousTiles(Reference reference) {
		return getPreviousTiles(reference.square(), reference.direction());
	}

	/**
	 * @param square
	 * @param direction
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getPreviousTiles(Square square, Direction direction) {

		var tiles = new LinkedList<Tile>();

		while (square.hasPreviousTile(direction)) {

			square = square.getPrevious(direction);
			tiles.addFirst(square.getTile());
		}

		return tiles;
	}

	/**
	 * @param reference
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getNextTiles(Reference reference) {
		return getNextTiles(reference.square(), reference.direction());
	}

	/**
	 /**
	 * @param square
	 * @param direction
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getNextTiles(Square square, Direction direction) {

		var tiles = new ArrayList<Tile>();

		while (square.hasNextTile(direction)) {

			square = square.getNext(direction);
			tiles.add(square.getTile());
		}

		return tiles;
	}

	/**
	 * @param reference
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getTiles(String reference) {
		return getTiles(references.get(reference));
	}

	/**
	 * @param reference
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> getTiles(Reference reference) {

		var tiles = new ArrayList<Tile>();

		var square = reference.square();

		if (square.hasTile()) {

			tiles.addAll(getPreviousTiles(reference));
			tiles.addLast(square.getTile());
			tiles.addAll(getNextTiles(reference));
		}

		return tiles;
	}

	@Override
	public Iterator<Square> iterator() {
		return new SquareIterator(this);
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Stream<Square> anchors() {
		return stream(spliterator(), false).filter(Square::isAnchor);
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Collection<Reference> references() {
		return references.values();
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public int getTileCount() {

		var tileCount = 0;

		for (var square : this) {

			if (square.hasTile()) {
				tileCount++;
			}
		}

		return tileCount;
	}

	/**
	 * @since 0.0.0
	 */
	public void clear() {
		forEach(Square::clear);
	}
}
