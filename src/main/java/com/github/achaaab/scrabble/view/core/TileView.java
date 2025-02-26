package com.github.achaaab.scrabble.view.core;

import com.github.achaaab.scrabble.model.core.Tile;
import com.github.achaaab.scrabble.tools.FontUtilities;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import static com.github.achaaab.scrabble.tools.FontUtilities.align;
import static com.github.achaaab.scrabble.tools.Alignment.BOTTOM_RIGHT;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER_CENTER;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.round;

/**
 * View of a scrabble tile.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class TileView extends JComponent {

	public static final int SIZE = pixels(1.00);

	public static final Color FOREGROUND = new Color(38, 15, 1);
	public static final Color BACKGROUND = new Color(254, 236, 207);
	private static final Color BLANK_FOREGROUND = new Color(10, 150, 10);

	private static final Font BASE_FONT = FontUtilities.load("fonts/lt_superior/LTSuperior-SemiBold.otf");

	private Tile model;

	private final int size;
	private final Font letterFont;
	private final Font valueFont;
	private final int border;
	private final int outerRadius;
	private final int innerRadius;
	private final int letterX;
	private final int letterY;
	private final int valueX;
	private final int valueY;

	/**
	 * Creates the view for a tile, with default size.
	 * The default size is a fraction off the screen height such as a typical game window can fit on the screen.
	 *
	 * @since 0.0.0
	 */
	public TileView() {
		this(SIZE);
	}

	/**
	 * Creates the view for a tile.
	 *
	 * @param size tile width and height in pixels
	 * @since 0.0.2
	 */
	public TileView(int size) {

		this.size = size;

		letterFont = BASE_FONT.deriveFont(size * 0.70f);
		valueFont = BASE_FONT.deriveFont(size * 0.26f);
		border = round(size * 0.02f);
		outerRadius = round(size * 0.15f);
		innerRadius = round(size * 0.08f);
		letterX = round(size * 0.45f);
		letterY = round(size * 0.45f);
		valueX = round(size * 0.90f);
		valueY = round(size * 0.90f);

		setPreferredSize(new Dimension(size, size));
	}

	/**
	 * @param model model of this tile
	 * @since 0.0.0
	 */
	public void setModel(Tile model) {
		this.model = model;
	}

	@Override
	public void paint(Graphics graphics) {

		if (model != null) {

			var graphics2d = (Graphics2D) graphics;
			graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

			graphics2d.setPaint(FOREGROUND);
			graphics2d.fillRoundRect(0, 0, size, size, outerRadius, outerRadius);
			graphics2d.setPaint(BACKGROUND);
			graphics2d.fillRoundRect(border, border, size - 2 * border, size - 2 * border, innerRadius, innerRadius);

			graphics2d.setPaint(model.isBlank() ?
					BLANK_FOREGROUND :
					FOREGROUND);

			graphics2d.setFont(letterFont);
			align(graphics2d, Character.toString(model.letter()), CENTER_CENTER, letterX, letterY);

			if (!model.isBlank()) {

				graphics2d.setFont(valueFont);
				align(graphics2d, Integer.toString(model.value()), BOTTOM_RIGHT, valueX, valueY);
			}
		}
	}
}
