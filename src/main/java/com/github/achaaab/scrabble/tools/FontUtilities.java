package com.github.achaaab.scrabble.tools;

import com.github.achaaab.scrabble.view.SquareView;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;
import static java.util.Objects.requireNonNull;

/**
 * Font utility functions.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class FontUtilities {

	/**
	 * Loads a font resource.
	 *
	 * @param resourceName font resource name
	 * @return loaded font
	 * @throws RuntimeException if an error occurs while loading the font
	 * @since 0.0.0
	 */
	public static Font load(String resourceName) {

		var classLoader = SquareView.class.getClassLoader();

		try (var inputStream = classLoader.getResourceAsStream(resourceName)) {

			requireNonNull(inputStream);
			return createFont(TRUETYPE_FONT, inputStream);

		} catch (IOException | FontFormatException cause) {

			throw new RuntimeException(cause);
		}
	}

	/**
	 * Draws the specified string with the specified graphics.
	 *
	 * @param graphics graphics to use for drawing
	 * @param string string to draw
	 * @param alignment string alignment
	 * @param x center of the string on the horizontal axis
	 * @param y center of the string on the vertical axis
	 * @since 0.0.0
	 */
	public static void align(Graphics2D graphics, String string, Alignment alignment, float x, float y) {

		var fontRenderContext = graphics.getFontRenderContext();
		var glyphVector = graphics.getFont().createGlyphVector(fontRenderContext, string);
		var bounds = glyphVector.getPixelBounds(fontRenderContext, 0, 0);

		var stringX = switch (alignment) {

			case LEFT, TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> x;
			case TOP, BOTTOM, CENTER, TOP_CENTER, CENTER_CENTER, BOTTOM_CENTER -> x - bounds.width * 0.5f;
			case RIGHT, TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> x - bounds.width;
		};

		var stringY = switch (alignment) {

			case TOP, TOP_LEFT, TOP_CENTER, TOP_RIGHT -> y + bounds.height;
			case LEFT, RIGHT, CENTER, CENTER_LEFT, CENTER_CENTER, CENTER_RIGHT -> y + bounds.height * 0.5f;
			case BOTTOM, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> y;
		};

		graphics.drawString(string, stringX, stringY);
	}
}
