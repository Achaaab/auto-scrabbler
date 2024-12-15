package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Square;
import com.github.achaaab.scrabble.model.Tile;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import static com.github.achaaab.scrabble.tools.GeometryUtilities.getFiveBranchStar;
import static java.awt.BorderLayout.CENTER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.round;
import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SquareView extends JComponent {

	private static final Paint DEFAULT_BACKGROUND = new Color(206, 199, 171);
	private static final Paint WORD_DOUBLE_BACKGROUND = new Color(248, 185, 176);
	private static final Paint WORD_TRIPLE_BACKGROUND = new Color(251, 108, 82);
	private static final Paint LETTER_DOUBLE_BACKGROUND = new Color(197, 216, 214);
	private static final Paint LETTER_TRIPLE_BACKGROUND = new Color(55, 153, 184);
	private static final Paint CENTRAL_STAR_PAINT = new Color(47, 34, 36);

	public static final int INNER_MARGIN = round(TileView.SIZE * 0.02f);

	private final Square model;
	private final TileView tile;

	/**
	 * @param model
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

		var graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

		var background = switch (model.award()) {

			case WORD_TRIPLE -> WORD_TRIPLE_BACKGROUND;
			case WORD_DOUBLE -> WORD_DOUBLE_BACKGROUND;
			case LETTER_TRIPLE -> LETTER_TRIPLE_BACKGROUND;
			case LETTER_DOUBLE -> LETTER_DOUBLE_BACKGROUND;
			default -> DEFAULT_BACKGROUND;
		};

		var width = getWidth();
		var height = getHeight();

		graphics2d.setPaint(background);
		graphics2d.fillRect(0, 0, width, height);

		tile.setModel(model.getTile());

		if (model.isEmpty() && model.isCentral()) {

			var fiveBranchStar = getFiveBranchStar(width * 0.5, height * 0.5, width * 0.4);
			graphics2d.setPaint(CENTRAL_STAR_PAINT);
			graphics2d.fill(fiveBranchStar);
		}
	}
}
