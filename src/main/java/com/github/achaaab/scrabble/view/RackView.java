package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Rack;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

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

	private static final int RACK_TILE_SIZE = round(TileView.SIZE * 1.50f);
	public static final Dimension SIZE = new Dimension(RACK_TILE_SIZE * 10, RACK_TILE_SIZE * 2);

	private static final Color BOTTOM_COLOR = new Color(202, 162, 123);
	private static final Color BACK_COLOR = new Color(167, 127, 86);

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

		var bottom  = new Path2D.Float();
		bottom.moveTo(RACK_TILE_SIZE * 0.20, RACK_TILE_SIZE * 2.00);
		bottom.lineTo(RACK_TILE_SIZE * 0.10, RACK_TILE_SIZE * 1.90);
		bottom.lineTo(RACK_TILE_SIZE * 0.14, RACK_TILE_SIZE * 1.50);
		bottom.lineTo(RACK_TILE_SIZE * 0.20, RACK_TILE_SIZE * 1.44);
		bottom.lineTo(RACK_TILE_SIZE * 9.80, RACK_TILE_SIZE * 1.44);
		bottom.lineTo(RACK_TILE_SIZE * 9.86, RACK_TILE_SIZE * 1.50);
		bottom.lineTo(RACK_TILE_SIZE * 9.90, RACK_TILE_SIZE * 1.90);
		bottom.lineTo(RACK_TILE_SIZE * 9.80, RACK_TILE_SIZE * 2.00);
		bottom.lineTo(RACK_TILE_SIZE * 0.20, RACK_TILE_SIZE * 2.00);

		var back = new Path2D.Float();
		back.moveTo(RACK_TILE_SIZE * 0.30, RACK_TILE_SIZE * 1.44);
		back.lineTo(RACK_TILE_SIZE * 0.38, RACK_TILE_SIZE * 0.64);
		back.lineTo(RACK_TILE_SIZE * 0.48, RACK_TILE_SIZE * 0.54);
		back.lineTo(RACK_TILE_SIZE * 9.52, RACK_TILE_SIZE * 0.54);
		back.lineTo(RACK_TILE_SIZE * 9.62, RACK_TILE_SIZE * 0.64);
		back.lineTo(RACK_TILE_SIZE * 9.70, RACK_TILE_SIZE * 1.44);
		back.lineTo(RACK_TILE_SIZE * 0.30, RACK_TILE_SIZE * 1.44);

		graphics.setColor(BOTTOM_COLOR);
		graphics2d.fill(bottom);

		graphics2d.setColor(BACK_COLOR);
		graphics2d.fill(back);

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
