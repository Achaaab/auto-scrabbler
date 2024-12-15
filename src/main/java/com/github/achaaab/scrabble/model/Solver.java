package com.github.achaaab.scrabble.model;

import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.rules.Move;
import com.github.achaaab.scrabble.sheet.SimpleSheet;
import com.github.achaaab.scrabble.sheet.SimpleSheetEntry;

import java.util.List;

import static java.util.Comparator.reverseOrder;

/**
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
	private final Dictionary dictionary;

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
		this.dictionary = dictionary;

		board = new Board();
		bag = new Bag();
		rack = new Rack();
		sheet = new SimpleSheet(this, true);
		evaluator = new Evaluator(board, dictionary);

		bag.addAll(tiles);
	}

	/**
	 * @since 0.0.0
	 */
	public void replay() {

		board.clear();
		rack.clear();
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
			board.play(word, key, bag);
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

		var bestMoveSheet = new SimpleSheet(this, false);

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
