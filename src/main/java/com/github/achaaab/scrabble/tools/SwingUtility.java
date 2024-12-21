package com.github.achaaab.scrabble.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;

import static com.github.achaaab.scrabble.tools.Toolbox.getRootCause;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Swing utility methods
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SwingUtility {

	private static final Toolkit TOOLKIT = getDefaultToolkit();
	private static final float BASE_RESOLUTION = 72.0f;
	private static final int RESOLUTION = TOOLKIT.getScreenResolution();

	/**
	 * @param size normalized size for 72 DPI resolution
	 * @return scaled and rounded size
	 * @since 0.0.0
	 */
	public static int scale(float size) {
		return round(scaleFloat(size));
	}

	/**
	 * @param size normalized size for 72 DPI resolution
	 * @return scaled size
	 * @since 0.0.0
	 */
	public static float scaleFloat(float size) {
		return size * RESOLUTION / BASE_RESOLUTION;
	}

	/**
	 * @param component
	 * @param fontSize
	 * @since 0.0.0
	 */
	public static void setFontSize(Component component, float fontSize) {

		var originalFont = component.getFont();

		if (originalFont.getSize() != fontSize) {

			var resizedFont = originalFont.deriveFont(fontSize);
			component.setFont(resizedFont);
		}
	}

	/**
	 * @param color
	 * @return
	 * @since 0.0.0
	 */
	public static boolean isDark(Color color) {
		return getLuminance(color) < 128;
	}

	/**
	 * @param color
	 * @return
	 * @since 0.0.0
	 */
	public static double getLuminance(Color color) {

		double luminance;

		var red = color.getRed();
		var green = color.getGreen();
		var blue = color.getBlue();

		var linearizedRed = linearizeColorComponent(red);
		var linearizedGreen = linearizeColorComponent(green);
		var linearizedBlue = linearizeColorComponent(blue);

		var linearizedLuminance = 0.2126 * linearizedRed +
				0.7152 * linearizedGreen +
				0.0722 * linearizedBlue;

		if (linearizedLuminance <= 0.0031308) {
			luminance = 12.92 * linearizedLuminance;
		} else {
			luminance = 1.055 * pow(linearizedLuminance, 1 / 2.4) - 0.055;
		}

		return 255.0 * luminance;
	}

	/**
	 * Linearizes a gamma-compressed color component (red, green or blue).
	 *
	 * @param component color component in range {@code [0, 256[}
	 * @return linearized component in range {@code [0.0, 1.0]}
	 * @since 0.0.0
	 */
	public static double linearizeColorComponent(int component) {

		double linearized;

		var normalized = component / 255.0;

		if (normalized <= 0.04045) {
			linearized = normalized / 12.92;
		} else {
			linearized = pow((normalized + 0.055) / 1.055, 2.4);
		}

		return linearized;
	}

	/**
	 * Displays an exception.
	 *
	 * @param exception exception to display
	 * @since 0.0.0
	 */
	public static void showException(Exception exception) {
		showMessageDialog(null, getRootCause(exception).getMessage(), "Erreur", ERROR_MESSAGE);
	}
}
