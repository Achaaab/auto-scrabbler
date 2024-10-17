package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Duplicate;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.view.DuplicateView;

import javax.swing.JFrame;
import java.awt.FlowLayout;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo0 {

	public static void main(String[] args) {

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
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		bag.addAll(getFrenchTiles());

		var total = 0;

		while (!rack.isEmpty() || !bag.isEmpty()) {

			while (!rack.isFull() && !bag.isEmpty()) {
				rack.add(bag.pickRandom());
			}

			var moves = evaluator.getMoves(rack);

			if (moves.isEmpty()) {

				break;

			} else {

				var move = moves.getFirst();
				board.play(move.tiles(), move.reference());
				total += move.score();
				rack.removeAll(move.rackTiles());
				invokeLater(view::repaint);
			}
		}

		System.out.println("total score: " + total);
	}
}
