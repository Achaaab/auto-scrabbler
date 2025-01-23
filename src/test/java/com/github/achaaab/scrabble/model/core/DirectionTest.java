package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.Test;

import static com.github.achaaab.scrabble.model.core.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.core.Direction.VERTICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests of {@link Direction}.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.3
 */
class DirectionTest {

	@Test
	void across() {

		assertEquals(HORIZONTAL, VERTICAL.across());
		assertEquals(VERTICAL, HORIZONTAL.across());
	}
}
