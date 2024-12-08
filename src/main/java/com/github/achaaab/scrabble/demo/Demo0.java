package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Duplicate;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.rules.Evaluator;
import com.github.achaaab.scrabble.rules.Move;
import com.github.achaaab.scrabble.view.DuplicateView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.util.stream.Collectors;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.joining;
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

		duplicate.draw("DSOGITB");
		duplicate.play("BIGOTS", "H4", "PSTOUT");
		duplicate.play("MKA", "7G");
		duplicate.play("PUTTOS", "K2", "EAALE ");
		duplicate.play("EIER", "2J");

		var bestMoves = evaluator.getMoves(rack).stream().
				sorted(reverseOrder()).
				limit(30).
				toList();

		var bestMove = bestMoves.getFirst();
		var tiles = bestMove.tiles();
		var reference = bestMove.reference();
		rack.removeAll(tiles);
		board.play(tiles, reference);

		System.out.println(bestMoves.stream().map(Move::toString).collect(joining("\n")));

		SwingUtilities.invokeLater(view::repaint);
	}
}
