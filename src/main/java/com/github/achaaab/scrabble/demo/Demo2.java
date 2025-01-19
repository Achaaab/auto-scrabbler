package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.core.Bag;
import com.github.achaaab.scrabble.model.core.Board;
import com.github.achaaab.scrabble.model.duplicate.DuplicateSheet;
import com.github.achaaab.scrabble.model.duplicate.DuplicateSheetEntry;
import com.github.achaaab.scrabble.model.core.Rack;
import com.github.achaaab.scrabble.model.core.Tile;
import com.github.achaaab.scrabble.model.move.Evaluator;
import com.github.achaaab.scrabble.view.BoardView;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.model.core.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.core.Tile.getFrenchTiles;
import static com.github.achaaab.scrabble.tools.Toolbox.toSeconds;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo2 {

	public static void main(String[] args) throws InterruptedException, InvocationTargetException {

		var board = new Board();
		var rack = new Rack();
		var bag = new Bag();

		var evaluator = new Evaluator(board, FRENCH_ODS9);
		var sheet = new DuplicateSheet();

		List<Tile> residual;
		List<Tile> draw;

		var scoreToBeat = 1421;
		var tries = 0;

		var start = now();

		while (sheet.total() <= scoreToBeat) {

			tries++;

			bag.clear();
			sheet.clear();
			board.clear();
			rack.clear();

			bag.addAll(getFrenchTiles());

			while (!rack.isEmpty() || !bag.isEmpty()) {

				residual = new ArrayList<>(rack.getTiles());
				draw = bag.pickRandom(Rack.CAPACITY - rack.size());
				rack.addAll(draw);

				var reject = false;

				if (sheet.entryCount() < 15 && bag.size() >= 7) {

					while (rack.getVowelCount() < 2 && bag.getVowelCount() >= 2 ||
							rack.getConsonantCount() < 2 && bag.getConsonantCount() >= 2) {

						var tiles = rack.pickAll();
						rack.fill(bag);
						bag.addAll(tiles);

						reject = true;
						draw = new ArrayList<>(rack.getTiles());
					}
				}

				var optionalBestMove = evaluator.listMoves(rack).stream().findFirst();

				if (optionalBestMove.isEmpty()) {
					break;
				}

				var bestMove = optionalBestMove.get();
				var tiles = bestMove.tiles();
				var reference = bestMove.reference();
				rack.removeAll(tiles);
				board.play(tiles, reference);

				sheet.add(new DuplicateSheetEntry(
						sheet.entryCount() + 1,
						residual,
						reject,
						draw,
						bestMove.word(),
						reference,
						bestMove.score()));
			}
		}

		var end = now();

		var duration = toSeconds(between(start, end));
		var speed = tries / duration;

		System.out.println(tries + (tries > 1 ? " tries" : "try"));
		System.out.printf("duration: %.2f s%n", duration);
		System.out.printf("speed: %.2f games/s%n", speed);
		System.out.println(sheet);

		var window = new JFrame("Test Scrabble");
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		var view = new BoardView(board);
		var contentPane = window.getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(view);
		window.pack();
		window.setLocation(0, 0);

		invokeAndWait(() -> window.setVisible(true));
	}
}
