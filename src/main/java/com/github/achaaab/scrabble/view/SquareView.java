package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Square;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import static com.github.achaaab.scrabble.model.Award.NONE;
import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.Direction.VERTICAL;
import static com.github.achaaab.scrabble.tools.GeometryUtilities.getFiveBranchStar;
import static com.github.achaaab.scrabble.tools.GeometryUtilities.getPolygon;
import static java.awt.BorderLayout.CENTER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.round;
import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * View of a scrabble board square.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SquareView extends JComponent {

	private static final Color DEFAULT_BACKGROUND = new Color(206, 199, 171);
	private static final Color WORD_DOUBLE_BACKGROUND = new Color(248, 185, 176);
	private static final Color WORD_TRIPLE_BACKGROUND = new Color(251, 108, 82);
	private static final Color LETTER_DOUBLE_BACKGROUND = new Color(197, 216, 214);
	private static final Color LETTER_TRIPLE_BACKGROUND = new Color(55, 153, 184);
	private static final Color CENTRAL_STAR_PAINT = new Color(47, 34, 36);

	public static final int INNER_MARGIN = round(TileView.SIZE * 0.00f);

	private final Square model;
	private final TileView tile;

	/**
	 * Creates the view for a square.
	 *
	 * @param model model of this square
	 * @since 0.0.0
	 */
	public SquareView(Square model) {

		this.model = model;
		tile = new TileView();

		setBorder(createEmptyBorder(INNER_MARGIN, INNER_MARGIN, INNER_MARGIN, INNER_MARGIN));
		setLayout(new BorderLayout());
		add(tile, CENTER);
	}

	@Override
	public void paintComponent(Graphics graphics) {

		tile.setModel(model.getTile());

		var graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

		drawBackground(graphics2d);
		drawStar(graphics2d);
		drawNeighborAwards(graphics2d);
	}

	/**
	 * Draws the background of this square depending on its award.
	 *
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawBackground(Graphics2D graphics) {

		graphics.setPaint(getAwardColor(model));
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Draws a star in this square, if it is empty and central.
	 *
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawStar(Graphics2D graphics) {

		if (model.isEmpty() && model.isCentral()) {

			var width = getWidth();
			var height = getHeight();

			var fiveBranchStar = getFiveBranchStar(width * 0.5, height * 0.5, width * 0.4);
			graphics.setPaint(CENTRAL_STAR_PAINT);
			graphics.fill(fiveBranchStar);
		}
	}

	/**
	 * Draws the awards of neighbor squares, if there are.
	 *
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawNeighborAwards(Graphics2D graphics) {

		var width = getWidth();
		var height = getHeight();
		var halfWidth = width / 2;
		var halfHeight = height / 2;
		var triangleHeight = width * 0.18;
		var halfTriangleBase = width * 0.09;

		drawLeftAward(halfHeight, triangleHeight, halfTriangleBase, graphics);
		drawRightAward(width, halfHeight, triangleHeight, halfTriangleBase, graphics);
		drawTopAward(halfWidth, triangleHeight, halfTriangleBase, graphics);
		drawBottomAward(height, halfWidth, triangleHeight, halfTriangleBase, graphics);
	}

	/**
	 * Draws the award of the left square in this square, if there is one.
	 *
	 * @param halfHeight half of this square height, in pixels
	 * @param triangleHeight height of the triangle to draw
	 * @param halfTriangleBase half base of the triangle to draw
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawLeftAward(
			double halfHeight,
			double triangleHeight, double halfTriangleBase,
			Graphics2D graphics) {

		var leftSquare = model.getPrevious(HORIZONTAL);

		if (leftSquare != null && leftSquare.award() != NONE) {

			var leftTriangle = getPolygon(
					0, triangleHeight, 0,
					halfHeight - halfTriangleBase, halfHeight, halfHeight + halfTriangleBase);

			drawNeighborAward(leftSquare, leftTriangle, graphics);
		}
	}

	/**
	 * Draws the award of the right square in this square, if there is one.
	 *
	 * @param width width of this square, in pixels
	 * @param halfHeight half of this square height, in pixels
	 * @param triangleHeight height of the triangle to draw, in pixels
	 * @param halfTriangleBase half base of the triangle to draw
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawRightAward(
			double width, double halfHeight,
			double triangleHeight, double halfTriangleBase,
			Graphics2D graphics) {

		var rightSquare = model.getNext(HORIZONTAL);

		if (rightSquare != null && rightSquare.award() != NONE) {

			var rightTriangle = getPolygon(
					width, width - triangleHeight, width,
					halfHeight - halfTriangleBase, halfHeight, halfHeight + halfTriangleBase);

			drawNeighborAward(rightSquare, rightTriangle, graphics);
		}
	}

	/**
	 * Draws the award of the top square in this square, if there is one.
	 *
	 * @param halfWidth half of this square width, in pixels
	 * @param triangleHeight height of the triangle to draw, in pixels
	 * @param halfTriangleBase half base of the triangle to draw
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawTopAward(
			int halfWidth,
			double triangleHeight, double halfTriangleBase,
			Graphics2D graphics) {

		var topSquare = model.getPrevious(VERTICAL);

		if (topSquare != null && topSquare.award() != NONE) {

			var topTriangle = getPolygon(
					halfWidth - halfTriangleBase, halfWidth, halfWidth + halfTriangleBase,
					0, triangleHeight, 0);

			drawNeighborAward(topSquare, topTriangle, graphics);
		}
	}

	/**
	 * Draws the award of the bottom square in this square, if there is one.
	 *
	 * @param height height of this square, in pixels
	 * @param halfWidth half of this square width, in pixels
	 * @param triangleHeight height of the triangle to draw, in pixels
	 * @param halfTriangleBase half base of the triangle to draw
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private void drawBottomAward(
			int height, int halfWidth,
			double triangleHeight, double halfTriangleBase,
			Graphics2D graphics) {

		var bottomSquare = model.getNext(VERTICAL);

		if (bottomSquare != null && bottomSquare.award() != NONE) {

			var bottomTriangle = getPolygon(
					halfWidth - halfTriangleBase, halfWidth, halfWidth + halfTriangleBase,
					height, height - triangleHeight, height);

			drawNeighborAward(bottomSquare, bottomTriangle, graphics);
		}
	}

	/**
	 * Draws the award of a neighbor square in this square.
	 *
	 * @param neighborSquare neighbor square
	 * @param shape shape to draw
	 * @param graphics graphics to use
	 * @since 0.0.3
	 */
	private static void drawNeighborAward(Square neighborSquare, Shape shape, Graphics2D graphics) {

		graphics.setPaint(getAwardColor(neighborSquare));
		graphics.fill(shape);
	}

	/**
	 * Returns the color to use for drawing the award of a square.
	 *
	 * @param square any square
	 * @return award color of the specified square
	 * @since 0.0.3
	 */
	private static Color getAwardColor(Square square) {

		return switch (square.award()) {

			case WORD_TRIPLE -> WORD_TRIPLE_BACKGROUND;
			case WORD_DOUBLE -> WORD_DOUBLE_BACKGROUND;
			case LETTER_TRIPLE -> LETTER_TRIPLE_BACKGROUND;
			case LETTER_DOUBLE -> LETTER_DOUBLE_BACKGROUND;
			default -> DEFAULT_BACKGROUND;
		};
	}
}
