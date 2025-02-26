package com.github.achaaab.scrabble.model.solver;

import com.github.achaaab.scrabble.model.core.Bag;
import com.github.achaaab.scrabble.model.core.Board;
import com.github.achaaab.scrabble.model.core.Dictionary;
import com.github.achaaab.scrabble.model.core.Rack;
import com.github.achaaab.scrabble.model.core.Tile;
import com.github.achaaab.scrabble.model.move.Evaluator;
import com.github.achaaab.scrabble.model.move.Move;

import java.util.List;

/**
 * Simple scrabble solver, brute forcing without more insight.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Solver {

	/**
	 * Creates an entry from a move.
	 *
	 * @param move move to convert into en entry
	 * @return created entry
	 * @since 0.0.0
	 */
	private static SolverSheetEntry createEntry(Move move) {
		return new SolverSheetEntry(move.word(), move.reference().toString(), move.score());
	}

	private final List<Tile> tiles;

	private final Board board;
	private final Bag bag;
	private final Rack rack;
	private final SolverSheet sheet;
	private final Evaluator evaluator;

	/**
	 * Creates a solver with the specified tiles and dictionary.
	 *
	 * @param tiles set of tiles
	 * @param dictionary dictionary
	 * @since 0.0.0
	 */
	public Solver(List<Tile> tiles, Dictionary dictionary) {

		this.tiles = tiles;

		board = new Board();
		bag = new Bag();
		rack = new Rack();
		sheet = new SolverSheet(true, true);
		evaluator = new Evaluator(board, dictionary);

		bag.addAll(tiles);
	}

	/**
	 * Resets the board, rack and bag then replays the sheet.
	 *
	 * @since 0.0.0
	 */
	public void replay() {

		board.clear();
		rack.clear();
		bag.clear();
		bag.addAll(tiles);

		sheet.entries().forEach(this::play);
	}

	/**
	 * Plays the specified entry.
	 *
	 * @param entry entry to play
	 * @throws IllegalArgumentException if the specified word does not fit at the specified reference
	 * @since 0.0.0
	 */
	private void play(SolverSheetEntry entry) {

		if (entry.isComplete()) {

			var word = entry.getWord();
			var key = entry.getKey();
			var reference = board.getReference(key);
			var square = reference.square();
			var direction = reference.direction();

			var move = evaluator.getMove(square, direction, word, bag);
			var score = move.score();
			var tiles = move.tiles();

			board.play(tiles, reference);
			entry.setScore(score);
		}
	}

	/**
	 * Previews the given entry, playing it on the board.
	 *
	 * @param entry entry to preview
	 * @since 0.0.6
	 */
	public void preview(SolverSheetEntry entry) {

		var word = entry.getWord();
		var key = entry.getKey();
		var reference = board.getReference(key);

		var square = reference.square();
		var direction = reference.direction();

		var move = evaluator.getMove(square, direction, word, rack);
		var tiles = move.tiles();

		board.play(tiles, reference);
	}

	/**
	 * Changes the rack letters, putting back in the bag the letters that were previously on the rack.
	 *
	 * @param letters new letters
	 * @since 0.0.0
	 */
	public void change(String letters) {

		bag.addAll(rack.pickAll());
		rack.addAll(bag.pickAll(letters));
	}

	/**
	 * Finds the best moves with the current board and rack.
	 *
	 * @return best moves sheet
	 * @since 0.0.0
	 */
	public SolverSheet solve() {

		var bestMoveSheet = new SolverSheet(false, false);

		evaluator.listMoves(rack).stream().
				map(Solver::createEntry).
				forEach(bestMoveSheet::add);

		return bestMoveSheet;
	}

	/**
	 * @return scrabble board on which to place the words
	 * @since 0.0.0
	 */
	public Board board() {
		return board;
	}

	/**
	 * @return bag in which to put and pick the tiles
	 * @since 0.0.0
	 */
	public Bag bag() {
		return bag;
	}

	/**
	 * @return rack on which to place the drawn tiles
	 * @since 0.0.0
	 */
	public Rack rack() {
		return rack;
	}

	/**
	 * @return sheet containing the list of played words
	 * @since 0.0.0
	 */
	public SolverSheet sheet() {
		return sheet;
	}
}
