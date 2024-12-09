package com.github.achaaab.scrabble.model;

import static com.github.achaaab.scrabble.model.Board.HALF_SIZE;
import static com.github.achaaab.scrabble.model.Board.SIZE;
import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.Direction.VERTICAL;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Square {

	/**
	 * @param column
	 * @param row
	 * @return
	 * @since 0.0.0
	 */
	public static String getName(int column, int row) {
		return getReference(column, row, HORIZONTAL);
	}

	/**
	 * @param column
	 * @param row
	 * @param direction
	 * @return
	 * @since 0.0.0
	 */
	public static String getReference(int column, int row, Direction direction) {

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
	 * @param board
	 * @param column
	 * @param row
	 * @param award
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
	 * @since 0.0.0
	 */
	public void clear() {
		tile = null;
	}

	/**
	 * @return whether this square is empty
	 * @since 0.0.0
	 */
	public boolean isEmpty() {
		return tile == null;
	}

	/**
	 * @return whether this square has a tile
	 * @since 0.0.0
	 */
	public boolean hasTile() {
		return tile != null;
	}

	/**
	 * @return column
	 * @since 0.0.0
	 */
	public int column() {
		return column;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public int row() {
		return row;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Award award() {
		return award;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @param tile
	 * @since 0.0.0
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	/**
	 * @param direction
	 * @return
	 * @since 0.0.0
	 */
	public String getName(Direction direction) {
		return getReference(column, row, direction);
	}

	/**
	 * @param direction
	 * @return whether this square has a next square in the given direction
	 * @since 0.0.0
	 */
	public boolean hasNext(Direction direction) {
		return direction == HORIZONTAL ? column + 1 < SIZE : row + 1 < SIZE;
	}

	/**
	 * @param direction
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
	 * @param direction
	 * @return whether this square has a previous square in the given direction
	 * @since 0.0.0
	 */
	public boolean hasPrevious(Direction direction) {
		return direction == HORIZONTAL ? column > 0 : row  > 0;
	}

	/**
	 * @param direction
	 * @return square previous to this in the given direction, or {@code null} if there is no previous square
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
	 * @param direction
	 * @return whether this square has a previous square with a tile in the given direction
	 * @since 0.0.0
	 */
	public boolean hasPreviousTile(Direction direction) {

		var previousSquare = getPrevious(direction);
		return previousSquare != null && previousSquare.hasTile();
	}

	/**
	 * @param direction
	 * @return whether this square has a next square with a tile in the given direction
	 * @since 0.0.0
	 */
	public boolean hasNextTile(Direction direction) {

		var nextSquare = getNext(direction);
		return nextSquare != null && nextSquare.hasTile();
	}

	/**
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
	 * @return whether this square is the central square
	 * @since 0.0.0
	 */
	public boolean isCentral() {
		return column == HALF_SIZE && row == HALF_SIZE;
	}

	/**
	 * @return whether this square is an anchor
	 * @since 0.0.0
	 */
	public boolean isAnchor() {
		return isEmpty() && (isCentral() || hasAdjacentTile());
	}

	@Override
	public String toString() {
		return getName(column, row);
	}
}
