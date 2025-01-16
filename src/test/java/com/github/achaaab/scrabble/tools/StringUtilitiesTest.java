package com.github.achaaab.scrabble.tools;

import org.junit.jupiter.api.Test;

import static com.github.achaaab.scrabble.tools.Alignment.BOTTOM_CENTER;
import static com.github.achaaab.scrabble.tools.Alignment.BOTTOM_LEFT;
import static com.github.achaaab.scrabble.tools.Alignment.BOTTOM_RIGHT;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER_CENTER;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER_LEFT;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER_RIGHT;
import static com.github.achaaab.scrabble.tools.Alignment.LEFT;
import static com.github.achaaab.scrabble.tools.Alignment.RIGHT;
import static com.github.achaaab.scrabble.tools.Alignment.TOP_CENTER;
import static com.github.achaaab.scrabble.tools.Alignment.TOP_LEFT;
import static com.github.achaaab.scrabble.tools.Alignment.TOP_RIGHT;
import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of {@link StringUtilities}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.6
 */
class StringUtilitiesTest {

	@Test
	void padLeft() {

		assertEquals("", pad("foo", 0, ' ', LEFT));
		assertEquals("f", pad("foo", 1, ' ', LEFT));
		assertEquals("fo", pad("foo", 2, ' ', LEFT));
		assertEquals("foo", pad("foo", 3, ' ', LEFT));
		assertEquals("foo ", pad("foo", 4, ' ', LEFT));
		assertEquals("foo  ", pad("foo", 5, ' ', LEFT));
	}

	@Test
	void padCenter() {

		assertEquals("", pad("foo", 0, ' ', CENTER));
		assertEquals("f", pad("foo", 1, ' ', CENTER));
		assertEquals("fo", pad("foo", 2, ' ', CENTER));
		assertEquals("foo", pad("foo", 3, ' ', CENTER));
		assertEquals("foo ", pad("foo", 4, ' ', CENTER));
		assertEquals(" foo ", pad("foo", 5, ' ', CENTER));
	}

	@Test
	void padRight() {

		assertEquals("", pad("foo", 0, ' ', RIGHT));
		assertEquals("f", pad("foo", 1, ' ', RIGHT));
		assertEquals("fo", pad("foo", 2, ' ', RIGHT));
		assertEquals("foo", pad("foo", 3, ' ', RIGHT));
		assertEquals(" foo", pad("foo", 4, ' ', RIGHT));
		assertEquals("  foo", pad("foo", 5, ' ', RIGHT));
	}

	@Test
	void padOther() {

		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', TOP_LEFT));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', TOP_CENTER));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', TOP_RIGHT));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', CENTER_LEFT));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', CENTER_CENTER));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', CENTER_RIGHT));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', BOTTOM_LEFT));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', BOTTOM_CENTER));
		assertThrows(IllegalArgumentException.class, () -> pad("foo", 5, ' ', BOTTOM_RIGHT));

		assertEquals("", pad("foo", 0, ' ', TOP_LEFT));
		assertEquals("f", pad("foo", 1, ' ', TOP_LEFT));
		assertEquals("fo", pad("foo", 2, ' ', TOP_LEFT));
		assertEquals("foo", pad("foo", 3, ' ', TOP_LEFT));
	}
}
