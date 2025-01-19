package com.github.achaaab.scrabble.model.core;

import static com.github.achaaab.scrabble.model.core.Board.HALF_SIZE;
import static com.github.achaaab.scrabble.model.core.Board.SIZE;
import static com.github.achaaab.scrabble.model.core.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.core.Direction.VERTICAL;

/**
 * Scrabble board square.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Square {

	/**
	 * Gets the name of the square at the specified column and row.
	 *
	 * @param column square column
	 * @param row square row
	 * @return name of the square
	 * @since 0.0.0
	 */
	public static String getKey(int column, int row) {
		return getKey(column, row, HORIZONTAL);
	}

	/**
	 * Gets the key of the reference at the specified column, row and direction.
	 *
	 * @param column reference column
	 * @param row reference row
	 * @param direction reference direction
	 * @return key of the reference
	 * @since 0.0.0
	 */
	public static String getKey(int column, int row, Direction direction) {

		var columnString = Integer.toString(column + 1);
		var rowString = Character.toString('A' + row);

		return direction == HORIZONTAL ?
				rowString + columnString :
				columnString + rowString;
	}

	private final Board board;
	private final int column;
	private final int row;
	private final Award award;

	private Tile tile;

	/**
	 * Creates a square.
	 *
	 * @param board board
	 * @param column square column
	 * @param row square row
	 * @param award square award
	 * @since 0.0.0
	 */
	public Square(Board board, int column, int row, Award award) {

		this.board = board;
		this.column = column;
		this.row = row;
		this.award = award;

		tile = null;
	}

	/**
	 * Clears this square, removing its tile.
	 *
	 * @since 0.0.0
	 */
	public void clear() {
		tile = null;
	}

	/**
	 * Determines if this square does not contain a tile.
	 *
	 * @return whether this square is empty
	 * @since 0.0.0
	 */
	public boolean isEmpty() {
		return tile == null;
	}

	/**
	 * Determines if this square does contain a tile.
	 *
	 * @return whether this square has a tile
	 * @since 0.0.0
	 */
	public boolean hasTile() {
		return tile != null;
	}

	/**
	 * @return column of this square
	 * @since 0.0.0
	 */
	public int column() {
		return column;
	}

	/**
	 * @return row of this square
	 * @since 0.0.0
	 */
	public int row() {
		return row;
	}

	/**
	 * @return award of this square
	 * @since 0.0.0
	 */
	public Award award() {
		return award;
	}

	/**
	 * @return tile of this square, {@code null} if this square is empty
	 * @since 0.0.0
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * Sets a tile for this square.
	 *
	 * @param tile tile to set
	 * @since 0.0.0
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	/**
	 * Determines the name of this square combined with the specified direction.
	 *
	 * @param direction direction to consider
	 * @return name of this square
	 * @since 0.0.0
	 */
	public String getKey(Direction direction) {
		return getKey(column, row, direction);
	}

	/**
	 * Determines if this square has a square next to it along the specified direction.
	 *
	 * @param direction direction to consider
	 * @return whether this square has a square next to it
	 * @since 0.0.0
	 */
	public boolean hasNext(Direction direction) {
		return direction == HORIZONTAL ? column + 1 < SIZE : row + 1 < SIZE;
	}

	/**
	 * Gets the square next to this along the specified direction.
	 *
	 * @param direction direction to consider
	 * @return square next to this in the given direction, or {@code null} if there is no next square
	 * @since 0.0.0
	 */
	public Square getNext(Direction direction) {

		Square next = null;

		if (hasNext(direction)) {

			next = direction == HORIZONTAL ?
					board.getSquare(column + 1, row) :
					board.getSquare(column, row + 1);
		}

		return next;
	}

	/**
	 * Determines if this square has a previous square along the specified direction.
	 *
	 * @param direction direction to consider
	 * @return whether this square has a previous square
	 * @since 0.0.0
	 */
	public boolean hasPrevious(Direction direction) {
		return direction == HORIZONTAL ? column > 0 : row  > 0;
	}

	/**
	 * Gets the previous square along the specified direction.
	 *
	 * @param direction direction to consider
	 * @return previous square in the given direction, or {@code null} if there is no previous square
	 * @since 0.0.0
	 */
	public Square getPrevious(Direction direction) {

		Square previous = null;

		if (hasPrevious(direction)) {

			previous = direction == HORIZONTAL ?
					board.getSquare(column - 1, row) :
					board.getSquare(column, row - 1);
		}

		return previous;
	}

	/**
	 * Determines if this square has a next square in the specified direction and if this next square has a tile.
	 *
	 * @param direction direction to consider
	 * @return whether this square has a next square with a tile in the given direction
	 * @since 0.0.0
	 */
	public boolean hasNextTile(Direction direction) {

		var nextSquare = getNext(direction);
		return nextSquare != null && nextSquare.hasTile();
	}

	/**
	 * Determines if this square has a previous square in the specified direction
	 * and if this previous square has a tile.
	 *
	 * @param direction direction to consider
	 * @return whether this square has a previous square with a tile in the given direction
	 * @since 0.0.0
	 */
	public boolean hasPreviousTile(Direction direction) {

		var previousSquare = getPrevious(direction);
		return previousSquare != null && previousSquare.hasTile();
	}

	/**
	 * Determines if this square has at least one adjacent square (left, right, top or bottom) with a tile.
	 *
	 * @return whether this square has an adjacent square with a tile
	 * @since 0.0.0
	 */
	public boolean hasAdjacentTile() {

		return hasPreviousTile(HORIZONTAL) ||
				hasNextTile(HORIZONTAL) ||
				hasPreviousTile(VERTICAL) ||
				hasNextTile(VERTICAL);
	}

	/**
	 * Determines if this square is the central square of the board, assuming the board has an odd size.
	 *
	 * @return whether this square is the central square
	 * @since 0.0.0
	 */
	public boolean isCentral() {
		return column == HALF_SIZE && row == HALF_SIZE;
	}

	/**
	 * Determines if this square is an anchor square. An anchor square is an empty square adjacent to at least 1 square
	 * with a tile.
	 * A played word must cover at least 1 anchor square.
	 * For the first word to play, the central square is considered as an anchor square.
	 *
	 * @return whether this square is an anchor
	 * @since 0.0.0
	 */
	public boolean isAnchor() {
		return isEmpty() && (isCentral() || hasAdjacentTile());
	}

	@Override
	public String toString() {
		return getKey(column, row);
	}
}
