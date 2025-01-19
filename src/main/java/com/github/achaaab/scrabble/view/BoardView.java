package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.core.Board;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.GridLayout;

import static com.github.achaaab.scrabble.model.core.Board.SIZE;
import static java.lang.Math.round;
import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;

/**
 * View of a scrabble board.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.3
 */
public class BoardView extends JComponent {

	private static final Color BACKGROUND = new Color(197, 189, 172);
	private static final int GAP = round(TileView.SIZE * 0.03f);

	/**
	 * Creates the view of a scrabble board.
	 *
	 * @param model scrabble board model
	 * @since 0.0.3
	 */
	public BoardView(Board model) {

		setBackground(BACKGROUND);
		setLayout(new GridLayout(SIZE + 2, SIZE + 2, GAP, GAP));

		for (var row = -1; row < SIZE + 1; row++) {

			for (var column = -1; column < SIZE + 1; column++) {

				if (column == -1 && row == -1 ||
						column == SIZE && row == -1 ||
						column == -1 && row == SIZE ||
						column == SIZE && row == SIZE) {

					add(new CoordinateView("", CENTER, CENTER));

				} else if (row == -1) {

					add(new CoordinateView(Integer.toString(1 + column), CENTER, BOTTOM));

				} else if (row == SIZE) {

					add(new CoordinateView(Integer.toString(1 + column), CENTER, TOP));

				} else if (column == -1) {

					add(new CoordinateView(Character.toString('A' + row), RIGHT, CENTER));

				} else if (column == SIZE) {

					add(new CoordinateView(Character.toString('A' + row), LEFT, CENTER));

				} else {

					add(new SquareView(model.getSquare(column, row)));
				}
			}
		}
	}
}
