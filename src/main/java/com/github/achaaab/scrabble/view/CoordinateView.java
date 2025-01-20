package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.tools.FontUtilities;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;

import static com.github.achaaab.scrabble.view.ViewUtilities.isDark;
import static com.github.achaaab.scrabble.view.TileView.SIZE;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixelsFloat;

/**
 * View for a scrabble board coordinate.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class CoordinateView extends JLabel {

	private static final Font BASE_FONT = FontUtilities.load("fonts/lt_superior/LTSuperior-SemiBold.otf");
	private static final Font FONT = BASE_FONT.deriveFont(pixelsFloat(0.30));

	/**
	 * Creates a view for a scrabble board coordinate.
	 *
	 * @param coordinate string representation of the coordinate to display
	 * @param horizontalAlignment horizontal alignment of the coordinate to display
	 * @param verticalAlignment vertical alignment of the coordinate to display
	 * @since 0.0.4
	 */
	public CoordinateView(String coordinate, int horizontalAlignment, int verticalAlignment) {

		super(coordinate);

		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);

		setPreferredSize(new Dimension(SIZE, SIZE));

		setFont(FONT);

		setForeground(isDark(getBackground()) ?
				TileView.BACKGROUND :
				TileView.FOREGROUND);
	}
}
