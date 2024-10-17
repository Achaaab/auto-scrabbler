package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static com.github.achaaab.scrabble.rules.Evaluator.expandJokers;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests of {@link Evaluator}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
class TestEvaluator {

	@Test
	void testExpandJokers0() {

		var a = new Tile('A', 1);
		var b = new Tile('B', 3);
		var c = new Tile('C', 3);

		var permutation = List.of(a, b, c);
		var expandedPermutations = expandJokers(permutation);

		assertEquals(1, expandedPermutations.size());
	}

	@Test
	void testExpandJokers1() {

		var a = new Tile('A', 1);
		var b = new Tile('B', 3);

		var permutation = List.of(a, b, BLANK);
		var expandedPermutations = expandJokers(permutation);

		assertEquals(26, expandedPermutations.size());
	}

	@Test
	void testExpandJokers2() {

		var a = new Tile('A', 1);
		var b = new Tile('B', 3);

		var permutation = List.of(a, b, BLANK, BLANK);
		var expandedPermutations = expandJokers(permutation);

		System.out.println(expandedPermutations);
		assertEquals(26 * 26, expandedPermutations.size());
	}
}
