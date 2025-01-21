package com.github.achaaab.scrabble.view.core;

import com.github.achaaab.scrabble.model.core.Rack;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

import static com.github.achaaab.scrabble.tools.GeometryUtilities.getScaledPolygon;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;
import static java.awt.FlowLayout.CENTER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.lang.Math.round;
import static java.util.stream.IntStream.range;

/**
 * View for a scrabble rack.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RackView extends JComponent {

	private static final int RACK_TILE_SIZE = pixels(1.50);
	public static final Dimension SIZE = new Dimension(RACK_TILE_SIZE * 10, RACK_TILE_SIZE * 2);

	private static final Color BOTTOM_COLOR = new Color(202, 162, 123);
	private static final Color BACK_COLOR = new Color(167, 127, 86);

	private static final Shape BOTTOM = getScaledPolygon(RACK_TILE_SIZE,
			0.20, 0.10, 0.14, 0.20, 9.80, 9.86, 9.90, 9.80,
			2.00, 1.90, 1.50, 1.44, 1.44, 1.50, 1.90, 2.00);

	private static final Shape BACK = getScaledPolygon(RACK_TILE_SIZE,
			0.30, 0.38, 0.48, 9.52, 9.62, 9.70,
			1.44, 0.64, 0.54, 0.54, 0.64, 1.44);

	private final Rack model;
	private final List<TileView> tiles;

	/**
	 * Creates a view for a scrabble rack.
	 *
	 * @param model model of the scrabble rack
	 * @since 0.0.0
	 */
	public RackView(Rack model) {

		this.model = model;

		setMaximumSize(SIZE);
		setMinimumSize(SIZE);
		setPreferredSize(SIZE);

		setLayout(null);

		var tilePanel = new JPanel();
		tilePanel.setOpaque(false);
		tilePanel.setLayout(new FlowLayout(CENTER, round(RACK_TILE_SIZE * 0.10f), 0));
		tilePanel.setSize(new Dimension(round(RACK_TILE_SIZE * 8.80f), RACK_TILE_SIZE));
		tilePanel.setLocation(round(RACK_TILE_SIZE * 0.60f), round(RACK_TILE_SIZE * 0.44f));
		add(tilePanel);

		tiles = range(0, 7).mapToObj(index -> new TileView(RACK_TILE_SIZE)).toList();
		tiles.forEach(tilePanel::add);

		setIgnoreRepaint(true);
	}

	@Override
	public void paintComponent(Graphics graphics) {

		var graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

		graphics.setColor(BOTTOM_COLOR);
		graphics2d.fill(BOTTOM);

		graphics2d.setColor(BACK_COLOR);
		graphics2d.fill(BACK);

		var tileModels = model.getTiles().iterator();

		for (var tile : tiles) {

			if (tileModels.hasNext()) {
				tile.setModel(tileModels.next());
			} else {
				tile.setModel(null);
			}
		}
	}

	/**
	 * @return model of this scrabble rack
	 * @since 0.0.5
	 */
	public Rack model() {
		return model;
	}
}
