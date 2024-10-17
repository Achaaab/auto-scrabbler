package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.view.BoardView;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.util.List;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static com.github.achaaab.scrabble.rules.Evaluator.expandJokers;
import static com.github.achaaab.scrabble.rules.Evaluator.getWord;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo5 {

	public static void main(String[] args) {

		var board = new Board();
		var rack = new Rack();
		var bag = new Bag();

		var evaluator = new Evaluator(board, FRENCH_ODS9);

		var window = new JFrame("Test Scrabble");
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		var boardView = new BoardView(board);
		var contentPane = window.getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(boardView);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		bag.addAll(getFrenchTiles());

		rack.addAll(bag.pickAll("ADEEENN"));
		var tiles = rack.pickAll("ENNEADE");
		board.play(tiles, "H7");
		tiles = bag.pickAll("MOQUR");
		board.play(tiles, "10D");

		rack.addAll(bag.pickAll("AEEFIIJ"));
		tiles = rack.pickAll("JE");
		board.play(tiles, "G7");
		tiles = bag.pickAll("PILER");
		board.play(tiles, "13I");

		rack.addAll(bag.pickAll("VK"));
		tiles = rack.pickAll("KIFAI");
		board.play(tiles, "N10");
		tiles = bag.pickAll("HRO");
		board.play(tiles, "L12");

		rack.addAll(bag.pickAll("AE FE"));

		System.out.println(getWord(rack.getTiles()));

		List<Tile> maxPermutation = null;
		Reference maxReference = null;
		var maxScore = 0;

		for (var permutation : rack.getPermutations()) {

			for (var reference : board.references()) {

				if (evaluator.isLegit(permutation, reference)) {

					if (permutation.contains(BLANK)) {

						var expandedPermutations = expandJokers(permutation);

						for (var expandedPermutation : expandedPermutations) {

							var score = evaluator.getScore(expandedPermutation, reference);

							if (score > maxScore) {

								maxPermutation = expandedPermutation;
								maxReference = reference;
								maxScore = score;
							}
						}

					} else {

						var score = evaluator.getScore(permutation, reference);

						if (score > maxScore) {

							maxPermutation = permutation;
							maxReference = reference;
							maxScore = score;
						}
					}
				}
			}
		}

		board.play(maxPermutation, maxReference);
		var wordTiles = board.getTiles(maxReference);
		System.out.println("playing " + getWord(wordTiles) + " in " + maxReference + " --> " + maxScore);
		invokeLater(boardView::repaint);
	}
}
