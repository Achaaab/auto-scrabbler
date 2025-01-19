package com.github.achaaab.scrabble.model.core;

/**
 * Words layout direction.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public enum Direction {

	HORIZONTAL,
	VERTICAL;

	/**
	 * @return across direction
	 * @since 0.0.0
	 */
	public Direction across() {
		return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
	}
}
