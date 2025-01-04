package com.github.achaaab.scrabble.tools;

import java.awt.Polygon;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;

/**
 * Geometry utility functions.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class GeometryUtilities {

	private static final double GOLDEN_RATIO_CONJUGATE = (1 - sqrt(5)) / 2;

	/**
	 * Creates a polygon from a 1-dimension array.
	 *
	 * @param coordinates x coordinates followed by y coordinates
	 * @return create polygon
	 * @since 0.0.3
	 */
	public static Polygon getPolygon(double... coordinates) {

		var vertexCount = coordinates.length / 2;
		var xs = copyOfRange(coordinates, 0, vertexCount);
		var ys = copyOfRange(coordinates, vertexCount, coordinates.length);
		return getPolygon(xs, ys);
	}

	/**
	 * Creates a polygon from a {@code double} array.
	 *
	 * @param xs x coordinates
	 * @param ys y coordinates
	 * @return 0.0.3
	 */
	public static Polygon getPolygon(double[] xs, double[] ys) {

		return new Polygon(
				round(xs),
				round(ys),
				xs.length);
	}

	/**
	 * Creates a regular five branch star inscribed in a specified circle.
	 *
	 * @param x0 x coordinate of the circle center
	 * @param y0 y coordinate of the circle center
	 * @param outerRadius circle radius
	 * @return created star
	 * @since 0.0.0
	 */
	public static Polygon getFiveBranchStar(double x0, double y0, double outerRadius) {

		var innerRadius = (outerRadius * GOLDEN_RATIO_CONJUGATE) / (GOLDEN_RATIO_CONJUGATE - 1);

		var coordinates = new double[20];

		for (var vertexIndex = 0; vertexIndex < 10; vertexIndex++) {

			var radius = vertexIndex % 2 == 0 ? outerRadius : innerRadius;
			coordinates[vertexIndex] = x0 + radius * cos((1 + 2 * vertexIndex) * PI / 10);
			coordinates[vertexIndex + 10] = y0 - radius * sin((1 + 2 * vertexIndex) * PI / 10);
		}

		return getPolygon(coordinates);
	}

	/**
	 * Rounds the given array.
	 *
	 * @param values values to round
	 * @return rounded value
	 * @since 0.0.3
	 */
	private static int[] round(double[] values) {

		return stream(values).
				mapToLong(Math::round).
				mapToInt(Math::toIntExact).
				toArray();
	}
}
