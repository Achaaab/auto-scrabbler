package com.github.achaaab.scrabble.view;

import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import static com.github.achaaab.scrabble.tools.Toolbox.getRootCause;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.toIntExact;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * View utility methods
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.1
 */
public class ViewUtilities {

	private static final Toolkit TOOLKIT = getDefaultToolkit();
	private static final int SCREEN_HEIGHT = TOOLKIT.getScreenSize().height;

	/**
	 * Converts a size relative to screen height in pixels.
	 *
	 * @param size in 1/25th of screen height
	 * @return specified size in pixels
	 * @since 1.0.1
	 */
	public static int pixels(double size) {
		return toIntExact(round(size * SCREEN_HEIGHT / 25));
	}

	/**
	 * Converts a size relative to screen height in pixels.
	 *
	 * @param size in 1/25th of screen height
	 * @return specified size in pixels
	 * @since 1.0.1
	 */
	public static float pixelsFloat(double size) {
		return (float) (size * SCREEN_HEIGHT / 25);
	}

	/**
	 * Determines if a color is dark.
	 * A color is considered dark if its luminance is strictly lower than 50%.
	 *
	 * @param color color to test
	 * @return whether the specified color is dark
	 * @since 0.0.0
	 */
	public static boolean isDark(Color color) {
		return getLuminance(color) < 0.5;
	}

	/**
	 * Computes the luminance of a color.
	 *
	 * @param color color whose luminance is to be computed
	 * @return luminance of the specified color in range {@code [0.0, 1.0]}
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

		return luminance;
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

	/**
	 * Creates a scroll pane and adds the given component in it.
	 * This helper aims to adapt the scroll pane components (track, buttons, thumb...) to scale with the
	 * pixel density of the monitor.
	 *
	 * @param component component to add into a scroll pane
	 * @param opaque whether to set the scroll pane opaque
	 * @return created scroll pane
	 * @since 1.0.1
	 */
	public static JScrollPane scrollPane(Component component, boolean opaque) {

		var scrollPane = new JScrollPane(component);

		scrollPane.setBorder(createEmptyBorder());
		scrollPane.setOpaque(opaque);
		scrollPane.getViewport().setOpaque(opaque);
		resizeScrollBars(scrollPane, pixels(0.28), pixels(0.28));

		return scrollPane;
	}

	/**
	 * This method allows to set the size of the vertical and horizontal bars of a scroll pane.
	 *
	 * @param scrollPane scroll pane to customize
	 * @param barThickness thickness of the bars to set, in pixels
	 * @param barButtonSize height of the vertical bar buttons and width of the horizontal bar buttons, in pixels
	 * @since 0.0.5
	 */
	public static void resizeScrollBars(JScrollPane scrollPane, double barThickness, double barButtonSize) {

		var verticalScrollBar = scrollPane.getVerticalScrollBar();
		var horizontalScrollBar = scrollPane.getHorizontalScrollBar();

		verticalScrollBar.setPreferredSize(new Dimension(toIntExact(round(barThickness)), 0));
		horizontalScrollBar.setPreferredSize(new Dimension(0, toIntExact(round(barThickness))));

		for (var scrollBarComponent : verticalScrollBar.getComponents()) {

			if (scrollBarComponent instanceof AbstractButton button) {
				button.setPreferredSize(new Dimension(0, toIntExact(round(barButtonSize))));
			}
		}

		for (var scrollBarComponent : horizontalScrollBar.getComponents()) {

			if (scrollBarComponent instanceof AbstractButton button) {
				button.setPreferredSize(new Dimension(toIntExact(round(barButtonSize)), 0));
			}
		}
	}
}
