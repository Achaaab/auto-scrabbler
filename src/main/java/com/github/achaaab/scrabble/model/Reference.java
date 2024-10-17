package com.github.achaaab.scrabble.model;

import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;

/**
 * @param square
 * @param direction
 * @since 0.0.0
 */
public record Reference(Square square, Direction direction) {

	@Override
	public String toString() {

		var columnString = Integer.toString(square.column() + 1);
		var rowString = Character.toString('A' + square.row());

		return direction == HORIZONTAL ?
				rowString + columnString :
				columnString + rowString;
	}
}
