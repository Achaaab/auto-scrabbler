package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.tools.FontUtilities;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

import static com.github.achaaab.scrabble.tools.FontUtilities.align;
import static com.github.achaaab.scrabble.tools.SwingUtility.scale;
import static com.github.achaaab.scrabble.tools.TextAlignment.BOTTOM_RIGHT;
import static com.github.achaaab.scrabble.tools.TextAlignment.CENTER_CENTER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.round;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class TileView extends JComponent {

	public static final int SIZE = scale(50);

	private static final Font BASE_FONT = FontUtilities.load("lt_superior/LTSuperior-SemiBold.otf");
	private static final Font LETTER_FONT = BASE_FONT.deriveFont(SIZE * 0.60f);
	private static final Font VALUE_FONT = BASE_FONT.deriveFont(SIZE * 0.25f);

	private static final Paint BORDER_COLOR = new Color(0, 0, 0);
	private static final Stroke BORDER_STROKE = new BasicStroke(SIZE * 0.04f);
	private static final int BORDER_X = round(SIZE * 0.015f);
	private static final int BORDER_Y = round(SIZE * 0.015f);
	private static final int BORDER_WIDTH = round(SIZE * 0.97f);
	private static final int BORDER_HEIGHT = round(SIZE * 0.97f);

	private static final Paint BACKGROUND = new Color(254, 236, 207);
	private static final Paint FOREGROUND = new Color(38, 15, 1);
	private static final Paint BLANK_FOREGROUND = new Color(10, 150, 10);

	private static final int CORNER_RADIUS = round(SIZE * 0.15f);
	private static final int LETTER_X = round(SIZE * 0.47f);
	private static final int LETTER_Y = round(SIZE * 0.47f);
	private static final int VALUE_X = round(SIZE * 0.90f);
	private static final int VALUE_Y = round(SIZE * 0.90f);

	private Tile model;

	/**
	 * @since 0.0.0
	 */
	public TileView() {

		setPreferredSize(new Dimension(SIZE, SIZE));
		setOpaque(true);
	}

	/**
	 * @param model
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

			graphics2d.setPaint(BACKGROUND);
			graphics2d.fillRoundRect(0, 0, SIZE - 1, SIZE - 1, CORNER_RADIUS, CORNER_RADIUS);

			graphics2d.setPaint(model.isBlank() ?
					BLANK_FOREGROUND :
					FOREGROUND);

			graphics2d.setFont(LETTER_FONT);
			align(graphics2d, Character.toString(model.letter()), CENTER_CENTER, LETTER_X, LETTER_Y);

			if (!model.isBlank()) {

				graphics2d.setFont(VALUE_FONT);
				align(graphics2d, Integer.toString(model.value()), BOTTOM_RIGHT, VALUE_X, VALUE_Y);
			}

			graphics2d.setStroke(BORDER_STROKE);
			graphics2d.setPaint(BORDER_COLOR);
			graphics2d.drawRoundRect(BORDER_X, BORDER_Y, BORDER_WIDTH, BORDER_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);
		}
	}
}
