package com.github.achaaab.scrabble;

import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests of {@link Rack}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
class TestRack {

	@Test
	void testPermutations1() {

		var e = new Tile('E', 1);

		var rack = new Rack();
		rack.add(e);

		assertEquals(
				singleton(List.of(e)),
				rack.getPermutations());
	}

	@Test
	void testPermutations2() {

		var e = new Tile('E', 1);
		var t = new Tile('T', 1);

		var rack = new Rack();
		rack.add(e);
		rack.add(t);

		assertEquals(
				Set.of(
						List.of(e),
						List.of(t),
						List.of(e, t),
						List.of(t, e)),
				rack.getPermutations());
	}

	@Test
	void testPermutations3() {

		var e = new Tile('E', 1);
		var t = new Tile('T', 1);
		var r = new Tile('R', 1);

		var rack = new Rack();
		rack.add(e);
		rack.add(t);
		rack.add(r);

		assertEquals(
				Set.of(
						List.of(e),
						List.of(r),
						List.of(t),
						List.of(e, r),
						List.of(e, t),
						List.of(r, e),
						List.of(r, t),
						List.of(t, e),
						List.of(t, r),
						List.of(e, r, t),
						List.of(e, t, r),
						List.of(r, e, t),
						List.of(r, t, e),
						List.of(t, e, r),
						List.of(t, r, e)),
				rack.getPermutations());
	}

	@Test
	void testPermutations4() {

		var e = new Tile('E', 1);
		var t = new Tile('T', 1);
		var r = new Tile('R', 1);

		var rack = new Rack();
		rack.add(e);
		rack.add(t);
		rack.add(r);
		rack.add(e);

		assertEquals(
				Set.of(
						List.of(e),
						List.of(r),
						List.of(t),
						List.of(e, e),
						List.of(e, r),
						List.of(e, t),
						List.of(r, e),
						List.of(r, t),
						List.of(t, e),
						List.of(t, r),
						List.of(e, e, r),
						List.of(e, e, t),
						List.of(e, r, e),
						List.of(e, r, t),
						List.of(e, t, e),
						List.of(e, t, r),
						List.of(r, e, e),
						List.of(r, e, t),
						List.of(r, t, e),
						List.of(t, e, e),
						List.of(t, e, r),
						List.of(t, r, e),
						List.of(e, e, r, t),
						List.of(e, e, t, r),
						List.of(e, r, e, t),
						List.of(e, r, t, e),
						List.of(e, t, e, r),
						List.of(e, t, r, e),
						List.of(r, e, e, t),
						List.of(r, e, t, e),
						List.of(r, t, e, e),
						List.of(t, e, e, r),
						List.of(t, e, r, e),
						List.of(t, r, e, e)),
				rack.getPermutations());
	}
}
