package com.github.achaaab.scrabble.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.sort;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Combinatorics {

	/**
	 * Swaps the elements at left index and right index.
	 *
	 * @param elements
	 * @param left index of the left element to swap
	 * @param right index of the right element to swap
	 * @since 0.0.0
	 */
	private static <E> void swap(List<E> elements, int left, int right) {

		var leftTile = elements.get(left);
		var rightTile = elements.get(right);

		elements.set(left, rightTile);
		elements.set(right, leftTile);
	}

	/**
	 * Reverses the elements between left index and right index.
	 *
	 * @param elements
	 * @param left left index (inclusive)
	 * @param right right index (inclusive)
	 * @since 0.0.0
	 */
	private static <E> void reverse(List<E> elements, int left, int right) {

		while (left < right) {

			var leftTile = elements.get(left);
			var rightTile = elements.get(right);

			elements.set(left++, rightTile);
			elements.set(right--, leftTile);
		}
	}

	/**
	 * @param elements
	 * @return permutation pivot index, {@code < 0} if there is no more permutation
	 * @since 0.0.0
	 */
	private static <E extends Comparable<E>> int getPivot(List<E> elements) {

		var index = elements.size() - 2;
		var increasing = true;

		while (increasing && index >= 0) {

			var leftTile = elements.get(index);
			var rightTile = elements.get(index + 1);

			if (leftTile.compareTo(rightTile) < 0) {
				increasing = false;
			} else {
				index--;
			}
		}

		return index;
	}

	/**
	 * @param elements
	 * @return set of all non-empty partial permutations
	 * @param <E> element type
	 * @since 0.0.0
	 */
	public static <E extends Comparable<E>> Set<List<E>> getPartialPermutations(List<E> elements) {

		var count = elements.size();
		var permutations = new HashSet<List<E>>();

		var permutation = new ArrayList<>(elements);
		sort(permutation);

		permutations.add(permutation);

		for (var permutationSize = 1; permutationSize < count; permutationSize++) {
			permutations.add(permutation.subList(0, permutationSize));
		}

		int pivot;

		while ((pivot = getPivot(permutation)) >= 0) {

			var index = count - 1;
			var left = permutation.get(pivot);
			var right = permutation.get(index);

			while (right.compareTo(left) <= 0) {
				right = permutation.get(--index);
			}

			permutation = new ArrayList<>(permutation);
			swap(permutation, pivot, index);
			reverse(permutation, pivot + 1, count - 1);

			permutations.add(permutation);

			for (var permutationSize = 1; permutationSize < count; permutationSize++) {
				permutations.add(permutation.subList(0, permutationSize));
			}
		}

		return permutations;
	}
}
