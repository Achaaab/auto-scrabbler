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
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class FontUtilities {

	/**
	 * @param resourceName
	 * @return
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
	 * @param graphics
	 * @param string
	 * @param alignment
	 * @param x
	 * @param y
	 * @since 0.0.0
	 */
	public static void align(Graphics2D graphics, String string, TextAlignment alignment, float x, float y) {

		var fontRenderContext = graphics.getFontRenderContext();
		var glyphVector = graphics.getFont().createGlyphVector(fontRenderContext, string);
		var bounds = glyphVector.getPixelBounds(fontRenderContext, 0, 0);

		var stringX = switch (alignment) {

			case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> x;
			case TOP_CENTER, CENTER_CENTER, BOTTOM_CENTER -> x - bounds.width * 0.5f;
			case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> x - bounds.width;
		};

		var stringY = switch (alignment) {

			case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> y + bounds.height;
			case CENTER_LEFT, CENTER_CENTER, CENTER_RIGHT -> y + bounds.height * 0.5f;
			case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> y;
		};

		graphics.drawString(string, stringX, stringY);
	}
}
