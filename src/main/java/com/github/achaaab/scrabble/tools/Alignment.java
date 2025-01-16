package com.github.achaaab.scrabble.tools;

/**
 * Horizontal and vertical alignment.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public enum Alignment {

	/**
	 * Equivalent to {@link #TOP_CENTER}, when horizontal alignment is meaningless.
	 *
	 * @since 0.0.6
	 */
	TOP,

	/**
	 * Equivalent to {@link #BOTTOM_CENTER}, when horizontal alignment is meaningless.
	 *
	 * @since 0.0.6
	 */
	BOTTOM,

	/**
	 * Equivalent to {@link #CENTER_RIGHT}, when vertical alignment is meaningless.
	 *
	 * @since 0.0.6
	 */
	RIGHT,

	/**
	 * Equivalent to {@link #CENTER_LEFT}, when vertical alignment is meaningless.
	 *
	 * @since 0.0.6
	 */
	LEFT,

	/**
	 * Equivalent to {@link #CENTER_CENTER}, when only vertical or horizontal alignment has meaning.
	 *
	 * @since 0.0.6
	 */
	CENTER,

	TOP_LEFT,
	TOP_CENTER,
	TOP_RIGHT,
	CENTER_LEFT,
	CENTER_CENTER,
	CENTER_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_CENTER,
	BOTTOM_RIGHT
}
