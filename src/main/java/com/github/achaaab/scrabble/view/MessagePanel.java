package com.github.achaaab.scrabble.view;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.achaaab.scrabble.tools.GeometryUtilities.getPolygon;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixelsFloat;
import static com.github.achaaab.scrabble.view.ViewUtilities.scrollPane;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Desktop.getDesktop;
import static java.lang.Math.min;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

/**
 * Panel for HTML document displaying.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.1
 */
public class MessagePanel extends JPanel {

	private static final int MESSAGE_WIDTH = pixels(12.00);
	private static final int MESSAGE_HEIGHT = pixels(8.00);
	private static final int CORNER = pixels(0.80);
	private static final int MAJOR_STROKE = pixels(0.20);
	private static final int MINOR_STROKE = pixels(0.05);
	private static final int MARGIN = pixels(1.00);

	private static final Color MAJOR_STROKE_COLOR = new Color(251, 108, 82);
	private static final Color MINOR_STROKE_COLOR = new Color(38, 15, 1);
	private static final Color BACKGROUND = new Color(197, 216, 214);
	private static final Color TEXT_COLOR = new Color(38, 15, 1);
	private static final Color VEIL_COLOR = new Color(0, 0, 0, 192);

	private static final float FONT_SIZE = pixelsFloat(0.40);

	private final JEditorPane textArea;

	/**
	 * Creates a message panel.
	 *
	 * @param window parent window
	 * @since 1.0.1
	 */
	public MessagePanel(JFrame window) {

		var rootPane = window.getRootPane();
		setLocation(0, 0);
		rootPane.add(this, 0);

		setOpaque(false);

		textArea = new JEditorPane();
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setForeground(TEXT_COLOR);
		textArea.setOpaque(false);
		textArea.addHyperlinkListener(this::hyperlinkUpdate);

		var ok = new JButton("OK");
		ok.setHorizontalAlignment(SwingConstants.CENTER);
		ok.setFont(ok.getFont().deriveFont(FONT_SIZE));
		ok.setOpaque(false);

		var buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(ok);

		setLayout(new BorderLayout());

		add(scrollPane(textArea, false), CENTER);
		add(buttonPanel, SOUTH);

		setVisible(false);

		ok.addActionListener(event -> setVisible(false));

		rootPane.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent event) {
				resize();
			}
		});
	}

	/**
	 * Called when a hypertext link is updated.
	 *
	 * @param event event responsible for the update
	 * @since 1.0.1
	 */
	private void hyperlinkUpdate(HyperlinkEvent event) {

		var eventType = event.getEventType();

		if (eventType == ACTIVATED) {

			try {

				var uri = event.getURL().toURI();
				getDesktop().browse(uri);

			} catch (URISyntaxException | IOException cause) {

				throw new RuntimeException(cause);
			}
		}
	}

	/**
	 * Adjusts this component size according to its parent.
	 *
	 * @since 1.0.1
	 */
	public void resize() {

		var size = getParent().getSize();
		var width = size.width;
		var height = size.height;
		setBounds(0, 0, width, height);

		var messageWidth = min(width, MESSAGE_WIDTH);
		var messageHeight = min(height, MESSAGE_HEIGHT);

		setBorder(createEmptyBorder(
				(height - messageHeight) / 2 + MARGIN,
				(width - messageWidth) / 2 + MARGIN,
				(height - messageHeight) / 2 + MARGIN,
				(width - messageWidth) / 2 + MARGIN));
	}

	/**
	 * Displays an HTML document.
	 *
	 * @param htmlDocument HTML document to display
	 * @since 1.0.1
	 */
	public void display(String htmlDocument) {

		invokeLater(() -> {

			resize();
			textArea.setContentType("text/html");
			textArea.setText(htmlDocument);
			setVisible(true);
		});
	}

	@Override
	protected void paintComponent(Graphics graphics) {

		var graphics2d = (Graphics2D) graphics;

		var componentWidth = getWidth();
		var componentHeight = getHeight();
		var width = min(componentWidth, MESSAGE_WIDTH);
		var height = min(componentHeight, MESSAGE_HEIGHT);

		graphics.setColor(VEIL_COLOR);
		graphics.fillRect(0, 0, componentWidth, componentHeight);

		var x0 = (componentWidth - width) / 2;
		var x1 = x0 + CORNER;
		var x2 = x0 + 2 * CORNER;
		var x3 = x0 + width - 2 * CORNER;
		var x4 = x0 + width - CORNER;
		var x5 = x0 + width;

		var y0 = (componentHeight - height) / 2;
		var y1 = y0 + CORNER;
		var y2 = y0 + 2 * CORNER;
		var y3 = y0 + height - 2 * CORNER;
		var y4 = y0 + height - CORNER;
		var y5 = y0 + height;

		var frameOutside = getPolygon(
				x0, x0, x1, x1, x4, x4, x5, x5, x4, x4, x1, x1,
				y4, y1, y1, y0, y0, y1, y1, y4, y4, y5, y5, y4);

		var frameInside = getPolygon(
				x0, x0, x1, x1, x2, x2, x3, x3, x4, x4, x5, x5, x4, x4, x3, x3, x2, x2, x1, x1,
				y3, y2, y2, y1, y1, y0, y0, y1, y1, y2, y2, y3, y3, y4, y4, y5, y5, y4, y4, y3);

		graphics.setColor(BACKGROUND);
		graphics2d.fill(frameInside);

		var defaultStroke = graphics2d.getStroke();

		graphics.setColor(MINOR_STROKE_COLOR);
		graphics2d.setStroke(new BasicStroke(MAJOR_STROKE + 2 * MINOR_STROKE));
		graphics2d.draw(frameOutside);
		graphics2d.draw(frameInside);

		graphics.setColor(MAJOR_STROKE_COLOR);
		graphics2d.setStroke(new BasicStroke(MAJOR_STROKE));
		graphics2d.draw(frameOutside);
		graphics2d.draw(frameInside);

		graphics2d.setStroke(defaultStroke);
	}
}
