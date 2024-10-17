package com.github.achaaab.scrabble.tools;

import java.awt.Polygon;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toIntExact;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class GeometryUtilities {

	private static final double GOLDEN_RATIO_CONJUGATE = (1 - sqrt(5)) / 2;

	/**
	 * @param x0
	 * @param y0
	 * @param outerRadius
	 * @return
	 * @since 0.0.0
	 */
	public static Polygon getFiveBranchStar(double x0, double y0, double outerRadius) {

		var innerRadius = (outerRadius * GOLDEN_RATIO_CONJUGATE) / (GOLDEN_RATIO_CONJUGATE - 1);

		var xs = new int[] {
				toIntExact(round(x0 + outerRadius * cos(1 * PI / 10))),
				toIntExact(round(x0 + innerRadius * cos(3 * PI / 10))),
				toIntExact(round(x0 + outerRadius * cos(5 * PI / 10))),
				toIntExact(round(x0 + innerRadius * cos(7 * PI / 10))),
				toIntExact(round(x0 + outerRadius * cos(9 * PI / 10))),
				toIntExact(round(x0 + innerRadius * cos(11 * PI / 10))),
				toIntExact(round(x0 + outerRadius * cos(13 * PI / 10))),
				toIntExact(round(x0 + innerRadius * cos(15 * PI / 10))),
				toIntExact(round(x0 + outerRadius * cos(17 * PI / 10))),
				toIntExact(round(x0 + innerRadius * cos(19 * PI / 10)))};

		var ys = new int[] {
				toIntExact(round(y0 - outerRadius * sin(1 * PI / 10))),
				toIntExact(round(y0 - innerRadius * sin(3 * PI / 10))),
				toIntExact(round(y0 - outerRadius * sin(5 * PI / 10))),
				toIntExact(round(y0 - innerRadius * sin(7 * PI / 10))),
				toIntExact(round(y0 - outerRadius * sin(9 * PI / 10))),
				toIntExact(round(y0 - innerRadius * sin(11 * PI / 10))),
				toIntExact(round(y0 - outerRadius * sin(13 * PI / 10))),
				toIntExact(round(y0 - innerRadius * sin(15 * PI / 10))),
				toIntExact(round(y0 - outerRadius * sin(17 * PI / 10))),
				toIntExact(round(y0 - innerRadius * sin(19 * PI / 10)))};

		return new Polygon(xs, ys, 10);
	}
}
