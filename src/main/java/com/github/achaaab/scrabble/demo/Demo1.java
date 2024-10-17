package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Reference;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.view.BoardView;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.util.List;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.rules.Evaluator.getWord;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo1 {

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

		rack.addAll(bag.pickAll('F', 'A', 'N', 'E', 'Z', 'W', 'K'));
		var tiles = rack.pickAll('M', 'U', 'S', 'E', 'U', 'M');
		board.play(tiles, "H7");

		rack.addAll(bag.pickAll('D', 'T', 'C', 'N', 'E', 'V'));
		tiles = rack.pickAll('V', 'I', 'D', 'E', 'N', 'T');
		board.play(tiles, "I6");

		bag.add(rack.pick('C'));
		rack.addAll(bag.pickAll('H', 'O', 'A', 'S', 'G', 'B', 'U'));
		tiles = rack.pickAll('B', 'A', 'G', 'O', 'U', 'S');
		board.play(tiles, "13C");

		bag.add(rack.pick('H'));
		rack.addAll(bag.pickAll('I', 'E', 'O', 'P', 'L', 'S', 'R'));
		tiles = rack.pickAll('P', 'R', 'O', 'I', 'E', 'L', 'S');
		board.play(tiles, "I3");

		rack.addAll(bag.pickAll('E', 'R', 'A', 'A', 'S', 'I', 'L'));
		tiles = rack.pickAll('S', 'A', 'L', 'A', 'I', 'R', 'E');
		board.play(tiles, "11E");

		rack.addAll(bag.pickAll('B', 'U', 'I', 'W', 'N', 'L', 'G'));
		tiles = rack.pickAll('B', 'U', 'N', 'G', 'L', 'W');
		board.play(tiles, "F7");

		bag.add(rack.pick('I'));
		rack.addAll(bag.pickAll('A', 'Y', 'C', 'E', 'F', 'T', 'S'));
		tiles = rack.pickAll('E', 'C', 'S', 'T', 'A', 'Y');
		board.play(tiles, "15D");

		bag.add(rack.pick('F'));
		rack.addAll(bag.pickAll('S', 'O', 'L', 'C', 'I', 'E', 'L'));
		tiles = rack.pickAll('C', 'O', 'L', 'L', 'E', 'I', 'S');
		board.play(tiles, "10A");

		rack.addAll(bag.pickAll('D', 'A', 'U', 'U', 'T', 'R', 'O'));
		tiles = rack.pickAll('T', 'R', 'O', 'U', 'D', 'U');
		board.play(tiles, "A4");

		bag.add(rack.pick('A'));
		rack.addAll(bag.pickAll('T', 'I', 'N', 'E', 'E', 'E', 'R'));
		tiles = rack.pickAll('E', 'R', 'E', 'I', 'N', 'T', 'E');
		board.play(tiles, "B2");

		rack.addAll(bag.pickAll('F', 'A', 'R', 'A', 'I', 'P', 'K'));
		tiles = rack.pickAll('P', 'A', 'R', 'I', 'K', 'A');
		board.play(tiles, "3G");

		bag.add(rack.pick('F'));
		rack.addAll(bag.pickAll('T', 'R', 'A', ' ', 'E', 'E', 'A'));
		tiles = rack.pickAll(' ', 'A', 'T', 'E', 'R', 'A', 'E');
		board.play(tiles, "C4");

		rack.addAll(bag.pickAll('N', 'E', 'I', 'I', 'D', 'E', 'V'));
		tiles = rack.pickAll('I', 'E', 'V', 'I', 'E', 'N');
		board.play(tiles, "L3");

		rack.addAll(bag.pickAll('M', 'O', 'A', 'T', 'E', 'N'));
		tiles = rack.pickAll('A', 'M', 'E', 'N', 'T', 'O');
		board.play(tiles, "14I");

		rack.addAll(bag.pickAll('O', 'E', 'Q', ' ', 'N', 'X'));
		tiles = rack.pickAll(' ', 'N', 'O', 'X');
		board.play(tiles, "12L");

		rack.addAll(bag.pickAll('F', 'F', 'Z', 'J'));
		tiles = rack.pickAll('J', 'Z', 'E');
		board.play(tiles, "5K");

		System.out.println(getWord(rack.getTiles()));

		List<Tile> maxTiles = null;
		Reference maxReference = null;
		var maxScore = 0;

		for (var sequence : rack.getPermutations()) {

			for (var reference : board.references()) {

					if (evaluator.isLegit(sequence, reference)) {

						var score = evaluator.getScore(sequence, reference);

						if (score > maxScore) {

							maxTiles = sequence;
							maxReference = reference;
							maxScore = score;
						}
					}
				}
			}

		board.play(maxTiles, maxReference);
		var wordTiles = board.getTiles(maxReference);
		System.out.println("playing " + getWord(wordTiles) + " in " + maxReference + " --> " + maxScore);
		invokeLater(boardView::repaint);
	}
}
