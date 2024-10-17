package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Duplicate;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.view.DuplicateView;

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

public class Demo3 {

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
		rack.addAll(bag.pickAll('F', 'A', 'N', 'E', 'Z', 'W', 'G'));

		List<Tile> maxPermutation = null;
		Reference maxReference = null;
		var maxScore = 0;
		List<Tile> rackPermutation = null;

		for (var permutation : rack.getPermutations()) {

			for (var reference : board.references()) {

				if (evaluator.isLegit(permutation, reference)) {

					if (permutation.contains(BLANK)) {

						var expandedPermutations = expandJokers(permutation);

						for (var expandedPermutation : expandedPermutations) {

							var score = evaluator.getScore(expandedPermutation, reference);

							if (score > maxScore) {

								maxPermutation = expandedPermutation;
								rackPermutation = permutation;
								maxReference = reference;
								maxScore = score;
							}
						}

					} else {

						var score = evaluator.getScore(permutation, reference);

						if (score > maxScore) {

							maxPermutation = permutation;
							rackPermutation = permutation;
							maxReference = reference;
							maxScore = score;
						}
					}
				}
			}
		}

		if (maxScore > 0) {

			board.play(maxPermutation, maxReference);
			System.out.println("played tiles: " + getWord(maxPermutation));
			var wordTiles = board.getTiles(maxReference);
			System.out.println("playing " + getWord(wordTiles) + " in " + maxReference + " --> " + maxScore);
			rack.removeAll(rackPermutation);

			invokeLater(view::repaint);

		}
	}
}
