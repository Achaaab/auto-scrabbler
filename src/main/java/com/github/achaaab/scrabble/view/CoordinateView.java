package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.tools.FontUtilities;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;

import static com.github.achaaab.scrabble.view.TileView.SIZE;
import static java.awt.Color.BLACK;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class CoordinateView extends JLabel {

	private static final Font BASE_FONT = FontUtilities.load("lt_superior/LTSuperior-SemiBold.otf");
	private static final Font FONT = BASE_FONT.deriveFont(SIZE * 0.30f);

	public CoordinateView(String coordinate, int horizontalAlignment, int verticalAlignment) {

		super(coordinate);

		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);

		setPreferredSize(new Dimension(SIZE, SIZE));

		setFont(FONT);
		setForeground(BLACK);
	}
}
