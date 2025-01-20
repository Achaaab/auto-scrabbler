package com.github.achaaab.scrabble.tools;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.awt.Image.SCALE_SMOOTH;
import static java.lang.Math.round;
import static java.lang.Math.toIntExact;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;

/**
 * Resource utility methods.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.4
 */
public class ResourceUtilities {

	private static final ClassLoader CLASS_LOADER = ResourceUtilities.class.getClassLoader();
	private static final Pattern MESSAGE_PARAMETER_PATTERN = compile("\\{\\{(\\s*\\w+\\s*)}}");

	/**
	 * Loads an icon resource and scale it if necessary.
	 *
	 * @param name name of the icon resource to load
	 * @param targetWidth target icon width in pixels
	 * @param targetHeight target icon height in pixels
	 * @return loaded and scaled icon
	 * @since 0.0.0
	 */
	public static ImageIcon loadIcon(String name, double targetWidth, double targetHeight) {

		var image = loadImage(name);
		var scaledImage = scale(image, targetWidth, targetHeight);

		return new ImageIcon(scaledImage);
	}

	/**
	 * @param image image to scale
	 * @param targetWidth target width in pixels
	 * @param targetHeight target height in pixels
	 * @return scaled image
	 */
	private static Image scale(BufferedImage image, double targetWidth, double targetHeight) {

		Image scaledImage = image;
		var width = image.getWidth();
		var height = image.getHeight();

		if (width != targetWidth || height != targetHeight) {

			scaledImage = image.getScaledInstance(
					toIntExact(round(targetWidth)),
					toIntExact(round(targetHeight)),
					SCALE_SMOOTH);
		}

		return scaledImage;
	}

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
	 * Loads a document resource using the current user language,
	 * then replace its placeholders with specified parameters.
	 *
	 * @param name name of the document resource
	 * @param parameters document replacement parameters
	 * @return loaded document with replaced parameters
	 * @since 1.0.1
	 */
	public static String getDocument(String name, Map<String, Object> parameters) {

		var language = Locale.getDefault().getLanguage();
		var resourceNames = new ArrayList<String>();

		var extensionIndex = name.lastIndexOf('.');

		if (extensionIndex == -1) {
			resourceNames.add(name + '_' + language);
		} else {
			resourceNames.add(name.substring(0, extensionIndex) + '_' + language + name.substring(extensionIndex));
		}

		resourceNames.add(name);

		return getUrl(resourceNames).
				map(url -> getDocument(url, parameters)).
				orElseThrow();
	}

	/**
	 * Loads a document and replace its placeholders with specified parameters.
	 *
	 * @param url document URL, not {@code null}
	 * @param parameters message parameters
	 * @return message with variables replaced with given parameters
	 * @since 1.0.1
	 */
	public static String getDocument(URL url, Map<String, Object> parameters) {

		try (
				var inputStream = url.openStream();
				var reader = new BufferedReader(new InputStreamReader(inputStream))) {

			var template = reader.lines().collect(joining());
			var matcher = MESSAGE_PARAMETER_PATTERN.matcher(template);

			return matcher.replaceAll(matchResult -> {

				var parameterName = matcher.group(1).strip();
				return parameters.get(parameterName).toString();
			});

		} catch (IOException cause) {

			throw new UncheckedIOException(cause);
		}
	}

	/**
	 * Finds the resource with given names and returns the first found one.
	 *
	 * @param resourceNames resource potential names
	 * @return first found resource
	 * @since 1.0.1
	 */
	public static Optional<URL> getUrl(List<String> resourceNames) {

		return resourceNames.stream().
				map(CLASS_LOADER::getResource).
				filter(Objects::nonNull).
				findFirst();
	}

	/**
	 * private constructor to prevent instantiation of this utility class
	 *
	 * @since 0.0.0
	 */
	private ResourceUtilities() {

	}
}
