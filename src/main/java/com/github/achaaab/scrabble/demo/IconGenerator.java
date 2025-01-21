package com.github.achaaab.scrabble.demo;

import com.github.achaaab.scrabble.model.core.Tile;
import com.github.achaaab.scrabble.view.core.TileView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.nio.file.Files.newOutputStream;
import static javax.imageio.ImageIO.write;

/**
 * Script generating the Auto-Scrabbler icon.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.4
 */
public class IconGenerator {

	/**
	 * Generates and writes the Auto-Scrabble icon to the target directory.
	 *
	 * @param arguments none
	 * @throws IOException if an I/O error occurs while generating the Auto-Scrabbler icon.
	 * @since 0.0.4
	 */
	public static void main(String... arguments) throws IOException {

		var a = new Tile('A', 1);
		var s = new Tile('S', 1);

		var aView = new TileView(160);
		var sView = new TileView(160);

		aView.setModel(a);
		sView.setModel(s);

		var icon = new BufferedImage(256, 256, TYPE_INT_ARGB);
		var graphics = icon.createGraphics();

		aView.paint(graphics);

		graphics.translate(96, 96);
		sView.paint(graphics);

		var path = Path.of("target", "icon_256.png");

		try (var outputStream = newOutputStream(path)) {
			write(icon, "png", outputStream );
		}
	}
}
