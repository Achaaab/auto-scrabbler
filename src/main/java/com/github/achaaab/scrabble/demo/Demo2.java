package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.view.BoardView;

import javax.swing.JFrame;
import java.awt.FlowLayout;

import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Demo2 {

	/**
	 * @param arguments none
	 * @since 0.0.0
	 */
	public static void main(String... arguments) {

		var board = new Board();

		invokeLater(() -> {

			var window = new JFrame("Test Scrabble");
			window.setDefaultCloseOperation(EXIT_ON_CLOSE);
			var boardView = new BoardView(board);
			var contentPane = window.getContentPane();
			contentPane.setLayout(new FlowLayout());
			contentPane.add(boardView);
			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		});
	}
}
