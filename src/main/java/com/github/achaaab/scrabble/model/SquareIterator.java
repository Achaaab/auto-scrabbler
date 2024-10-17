package com.github.achaaab.scrabble.model;

import java.util.Iterator;

import static com.github.achaaab.scrabble.model.Board.SIZE;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SquareIterator implements Iterator<Square> {

	private final Board board;

	private int column;
	private int row;

	/**
	 * @param board
	 * @since 0.0.0
	 */
	public SquareIterator(Board board) {

		this.board = board;

		column = 0;
		row = 0;
	}

	@Override
	public boolean hasNext() {
		return row < SIZE;
	}

	@Override
	public Square next() {

		var next = board.getSquare(column, row);

		if (column + 1 < SIZE) {

			column++;

		} else {

			column = 0;
			row++;
		}

		return next;
	}
}
