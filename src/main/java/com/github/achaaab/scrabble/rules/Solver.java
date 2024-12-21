package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Dictionary;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.sheet.SimpleSheet;
import com.github.achaaab.scrabble.sheet.SimpleSheetEntry;

import java.util.List;

import static java.util.Comparator.reverseOrder;

/**
 * Simple scrabble solver, brute forcing without more insight.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Solver {

	/**
	 * @param move
	 * @return
	 * @since 0.0.0
	 */
	private static SimpleSheetEntry createEntry(Move move) {
		return new SimpleSheetEntry(move.word(), move.reference().toString(), move.score());
	}

	private final List<Tile> tiles;

	private final Board board;
	private final Bag bag;
	private final Rack rack;
	private final SimpleSheet sheet;
	private final Evaluator evaluator;

	/**
	 * @param tiles
	 * @param dictionary
	 * @since 0.0.0
	 */
	public Solver(List<Tile> tiles, Dictionary dictionary) {

		this.tiles = tiles;

		board = new Board();
		bag = new Bag();
		rack = new Rack();
		sheet = new SimpleSheet(this, true, true);
		evaluator = new Evaluator(board, dictionary);

		bag.addAll(tiles);
	}

	/**
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
	 * @param entry
	 * @since 0.0.0
	 */
	private void play(SimpleSheetEntry entry) {

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
	 * @param letters
	 * @since 0.0.0
	 */
	public void change(String letters) {

		bag.addAll(rack.pickAll());
		rack.addAll(bag.pickAll(letters));
	}

	/**
	 * @return best moves sheet
	 * @since 0.0.0
	 */
	public SimpleSheet solve() {

		var bestMoveSheet = new SimpleSheet(this, false, false);

		evaluator.getMoves(rack).stream().
				sorted(reverseOrder()).
				map(Solver::createEntry).
				forEach(bestMoveSheet::add);

		return bestMoveSheet;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Board board() {
		return board;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Bag bag() {
		return bag;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Rack rack() {
		return rack;
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public SimpleSheet sheet() {
		return sheet;
	}
}
