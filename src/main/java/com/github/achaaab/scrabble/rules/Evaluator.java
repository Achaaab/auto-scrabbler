package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Dictionary;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collector;

import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static java.lang.Math.max;
import static java.lang.Runtime.getRuntime;
import static java.util.Comparator.reverseOrder;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Evaluator {

	public static final int SCRABBLE_LENGTH = 7;
	public static final int SCRABBLE_REWARD = 50;

	/**
	 * @param tiles
	 * @return
	 * @since 0.0.0
	 */
	public static String getWord(List<Tile> tiles) {

		return tiles.stream().map(Tile::letter).collect(Collector.of(
				StringBuilder::new,
				StringBuilder::append,
				StringBuilder::append,
				StringBuilder::toString));
	}

	/**
	 * @param tiles
	 * @return
	 * @since 0.0.0
	 */
	private static int getRawScore(List<Tile> tiles) {
		return tiles.stream().mapToInt(Tile::value).sum();
	}

	/**
	 * @param permutation
	 * @return
	 * @since 0.0.0
	 */
	public static Set<List<Tile>> expandJokers(List<Tile> permutation) {

		var expandedPermutations = new HashSet<List<Tile>>();
		expandJokers(expandedPermutations, new ArrayList<>(permutation));
		return expandedPermutations;
	}

	/**
	 * @param expandedPermutations
	 * @param permutation
	 * @since 0.0.0
	 */
	private static void expandJokers(Set<List<Tile>> expandedPermutations, List<Tile> permutation) {

		var jokerIndex = permutation.indexOf(BLANK);

		if (jokerIndex == -1) {

			expandedPermutations.add(new ArrayList<>(permutation));

		} else {

			for (var letter = 'A'; letter <= 'Z'; letter++) {

				permutation.set(jokerIndex, new Tile(letter, 0));
				expandJokers(expandedPermutations, permutation);
			}

			permutation.set(jokerIndex, BLANK);
		}
	}

	/**
	 * @param future
	 * @return
	 * @param <V>
	 * @since 0.0.0
	 */
	private static <V> V getResult(Future<V> future) {

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException cause) {
			throw new RuntimeException(cause);
		}
	}

	private final Board board;
	private final Dictionary dictionary;

	private final ExecutorService executorService;

	/**
	 * @param board
	 * @param dictionary
	 * @since 0.0.0
	 */
	public Evaluator(Board board, Dictionary dictionary) {

		this.board = board;
		this.dictionary = dictionary;

		var threadCount = max(1, getRuntime().availableProcessors() - 1);
		executorService = newFixedThreadPool(threadCount);
	}

	/**
	 * @param tiles tiles to layout
	 * @param reference tiles position reference
	 * @return whether the given tiles fit at the given reference
	 * @since 0.0.0
	 */
	public boolean isLegit(List<Tile> tiles, Reference reference) {

		var fit = false;
		var adjacentTile = false;
		var centralTile = false;

		var square = reference.square();
		var direction = reference.direction();

		if (square.isEmpty()) {

			var emptySquareCount = 1;
			var tileCount = tiles.size();

			adjacentTile |= square.hasAdjacentTile();
			centralTile |= square.isCentral();

			while (emptySquareCount < tileCount && square.hasNext(direction)) {

				square = square.getNext(direction);
				adjacentTile |= square.hasAdjacentTile();
				centralTile |= square.isCentral();

				if (square.isEmpty()) {
					emptySquareCount++;
				}
			}

			fit = emptySquareCount == tileCount;
		}

		return fit && (adjacentTile || centralTile && tiles.size() > 1);
	}

	/**
	 * @param tiles
	 * @param reference
	 * @return
	 * @since 0.0.0
	 */
	public int getScore(List<Tile> tiles, String reference) {
		return getScore(tiles, board.getReference(reference));
	}

	/**
	 * @param tiles
	 * @param reference
	 * @return score or {@code 0} if invalid
	 * @since 0.0.0
	 */
	public int getScore(List<Tile> tiles, Reference reference) {

		var score = 0;

		var prefixTiles = board.getPreviousTiles(reference);
		var wordScore = getRawScore(prefixTiles);
		var wordMultiplier = 1;

		var square = reference.square();
		var direction = reference.direction();
		var tree = dictionary.getTree().getChild(getWord(prefixTiles));

		for (var tile : tiles) {

			tree = tree.getChild(tile.letter());

			if (tree == null) {

				return 0;

			} else {

				var award = square.award();

				var letterMultiplier = switch (award) {
					default -> 1;
					case LETTER_DOUBLE -> 2;
					case LETTER_TRIPLE -> 3;
				};

				switch (award) {
					case WORD_DOUBLE -> wordMultiplier *= 2;
					case WORD_TRIPLE -> wordMultiplier *= 3;
				}

				prefixTiles = board.getPreviousTiles(square, direction.across());
				var suffixTiles = board.getNextTiles(square, direction.across());

				if (!prefixTiles.isEmpty() || !suffixTiles.isEmpty()) {

					var crossWordMultiplier = switch (award) {
						default -> 1;
						case WORD_DOUBLE -> 2;
						case WORD_TRIPLE -> 3;
					};

					var crossWord = getWord(prefixTiles) + tile.letter() + getWord(suffixTiles);

					if (dictionary.contains(crossWord)) {

						var crossWordScore = crossWordMultiplier *
								(getRawScore(prefixTiles) + letterMultiplier * tile.value() + getRawScore(suffixTiles));

						score += crossWordScore;

					} else {

						return 0;
					}
				}

				wordScore += letterMultiplier * tile.value();

				while (square.hasNext(direction) && (square = square.getNext(direction)).hasTile()) {

					var nextTile = square.getTile();

					tree = tree.getChild(nextTile.letter());

					if (tree == null) {
						return 0;
					}

					wordScore += nextTile.value();
				}
			}
		}

		if (!tree.isWord()) {
			return 0;
		}

		wordScore *= wordMultiplier;
		score += wordScore;

		if (tiles.size() == SCRABBLE_LENGTH) {
			score += SCRABBLE_REWARD;
		}

		return score;
	}

	/**
	 * @param rack
	 * @return
	 * @since 0.0.0
	 */
	public List<Move> getMoves(Rack rack) {

		var references = board.references();
		var permutations = rack.getPermutations();

		var tasks = references.stream().
				map(reference -> createMoveListingTask(reference, permutations)).
				toList();

		try {

			var futures = executorService.invokeAll(tasks);

			return futures.stream().
					map(Evaluator::getResult).
					flatMap(Collection::stream).
					sorted(reverseOrder()).
					toList();

		} catch (InterruptedException interruptedException) {

			throw new RuntimeException(interruptedException);
		}
	}

	/**
	 * @param reference
	 * @param permutations
	 * @return
	 * @since 0.0.0
	 */
	public Callable<List<Move>> createMoveListingTask(Reference reference, Set<List<Tile>> permutations) {
		return () -> getMoves(reference, permutations);
	}

	/**
	 * @param reference
	 * @param permutations
	 * @return
	 * @since 0.0.0
	 */
	public List<Move> getMoves(Reference reference, Set<List<Tile>> permutations) {

		return permutations.stream().
				map(permutation -> getMoves(reference, permutation, permutation)).
				flatMap(Collection::stream).
				toList();
	}

	/**
	 * @param reference
	 * @param rackTiles
	 * @param playedTiles
	 * @return
	 * @since 0.0.0
	 */
	public List<Move> getMoves(Reference reference, List<Tile> rackTiles, List<Tile> playedTiles) {

		var moves = new ArrayList<Move>();

		if (isLegit(rackTiles, reference)) {

			if (playedTiles.contains(BLANK)) {

				var expandedPermutations = expandJokers(playedTiles);

				expandedPermutations.stream().
						map(expandedPermutation -> getMoves(reference, rackTiles, expandedPermutation)).
						flatMap(Collection::stream).
						forEach(moves::add);

			} else {

				var score = getScore(playedTiles, reference);

				if (score > 0) {
					moves.add(new Move(reference, rackTiles, playedTiles, score));
				}
			}
		}

		return moves;
	}
}
