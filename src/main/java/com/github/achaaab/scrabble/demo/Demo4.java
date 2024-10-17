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

import static com.github.achaaab.scrabble.model.Dictionary.ENGLISH_CSW21;
import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static com.github.achaaab.scrabble.model.Tile.getEnglishTiles;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static com.github.achaaab.scrabble.rules.Evaluator.expandJokers;
import static com.github.achaaab.scrabble.rules.Evaluator.getWord;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo4 {

	public static void main(String[] args) {

		var board = new Board();
		var rack = new Rack();
		var bag = new Bag();

		var duplicate = new Duplicate(board, rack, bag);
		var evaluator = new Evaluator(board, ENGLISH_CSW21);

		var window = new JFrame("Test Scrabble");
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		var view = new DuplicateView(duplicate);
		var contentPane = window.getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(view);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		bag.addAll(getEnglishTiles());

		var totalScore = 0;

		while (!rack.isEmpty() || !bag.isEmpty()) {

			while (rack.getTileCount() < 7 && !bag.isEmpty()) {
				rack.add(bag.pickRandom());
			}

			System.out.println(getWord(rack.getTiles()));

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
				totalScore += maxScore;

				invokeLater(view::repaint);

			} else {

				break;
			}
		}

		System.out.println("total score: " + totalScore);
	}
}
