package com.github.achaaab.scrabble.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * Resource utility methods.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.4
 */
public class ResourceUtility {

	private static final ClassLoader CLASS_LOADER = ResourceUtility.class.getClassLoader();

	/**
	 * Loads an image resource.
	 *
	 * @param resourceName name of the image resource to open
	 * @return loaded image resource
	 * @since 0.0.4
	 */
	public static BufferedImage loadImage(String resourceName) {

		var url = CLASS_LOADER.getResource(resourceName);

		try {
			return ImageIO.read(requireNonNull(url));
		} catch (IOException cause) {
			throw new UncheckedIOException(cause);
		}
	}

	/**
	 * Consume the lines from the specified resource. The resource is supposed to be valid UTF-8 text.
	 *
	 * @param resourceName name of the resource to consume
	 * @param consumer line consumer
	 * @throws UncheckedIOException if an I/O error occurs while consuming the resource
	 * @since 0.0.4
	 */
	public static void consumeLines(String resourceName, Consumer<String> consumer) {

		try (var inputStream = CLASS_LOADER.getResourceAsStream(resourceName)) {

			var reader = new InputStreamReader(requireNonNull(inputStream), UTF_8);
			var lines = new BufferedReader(reader).lines();

			lines.forEach(consumer);

		} catch (IOException cause) {

			throw new UncheckedIOException(cause);
		}
	}

	/**
	 * private constructor to prevent instantiation of this utility class
	 *
	 * @since 0.0.0
	 */
	private ResourceUtility() {

	}
}
