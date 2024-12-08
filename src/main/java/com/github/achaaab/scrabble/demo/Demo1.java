package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Duplicate;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.view.DuplicateView;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static java.util.Comparator.reverseOrder;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo1 {

	public static void main(String[] args) throws InterruptedException, InvocationTargetException {

		var board = new Board();
		var rack = new Rack();
		var bag = new Bag();

		var duplicate = new Duplicate(board, rack, bag);
		var evaluator = new Evaluator(board, FRENCH_ODS9);

		var window = new JFrame("Test Scrabble");
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		var view = new DuplicateView(duplicate);
		var contentPane = window.getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(view);
		window.pack();
		window.setLocation(0, 0);

		invokeAndWait(() -> window.setVisible(true));

		var bestScore = 1000;

		while (true) {

			bag.clear();
			bag.addAll(getFrenchTiles());

			var score = 0;
			var moveIndex = 0;

			while (!rack.isEmpty() || !bag.isEmpty()) {

				rack.fill(bag);

				if (moveIndex < 15) {

					while (rack.getVowelCount() < 2 && bag.getVowelCount() >= 2 ||
							rack.getConsonnantCount() < 2 && bag.getConsonnantCount() >= 2) {

						var tiles = rack.pickAll();
						rack.fill(bag);
						bag.addAll(tiles);
					}
				}

				var bestMoves = evaluator.getMoves(rack).stream().
						sorted(reverseOrder()).
						limit(1).
						toList();

				if (bestMoves.isEmpty()) {
					break;
				}

				var bestMove = bestMoves.getFirst();
				var tiles = bestMove.tiles();
				var reference = bestMove.reference();
				rack.removeAll(tiles);
				board.play(tiles, reference);
				score += bestMove.score();
				moveIndex++;

				invokeAndWait(() -> view.paintImmediately(view.getBounds()));
				System.out.println(bestMove);
			}

			System.out.println("=".repeat(32));
			invokeAndWait(() -> view.paintImmediately(view.getBounds()));

			if (score > bestScore) {

				System.out.println(score);
				break;
			}

			board.clear();
			rack.clear();
		}
	}
}
