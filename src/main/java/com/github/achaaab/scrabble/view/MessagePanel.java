package com.github.achaaab.scrabble.view;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URISyntaxException;

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
	 * @since 1.0.1
	 */
	public MessagePanel() {

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
		var buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(ok);

		setLayout(new BorderLayout());

		add(scrollPane(textArea, false), CENTER);
		add(buttonPanel, SOUTH);

		setVisible(false);
		ok.addActionListener(event -> setVisible(false));

		addMouseListener(new MouseAdapter() {
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

		var componentWidth = getWidth();
		var componentHeight = getHeight();
		var width = min(componentWidth, MESSAGE_WIDTH);
		var height = min(componentHeight, MESSAGE_HEIGHT);

		graphics.setColor(VEIL_COLOR);
		graphics.fillRect(0, 0, componentWidth, componentHeight);

		var x00 = (componentWidth - width) / 2;
		var x01 = x00 + MINOR_STROKE;
		var x02 = x00 + MAJOR_STROKE - MINOR_STROKE;
		var x03 = x00 + MAJOR_STROKE;
		var x04 = x00 + CORNER;
		var x05 = x00 + CORNER + MAJOR_STROKE - MINOR_STROKE;
		var x06 = x00 + CORNER + MAJOR_STROKE;
		var x07 = x00 + 2 * CORNER;
		var x08 = x00 + 2 * CORNER + MAJOR_STROKE - MINOR_STROKE;
		var x09 = x00 + 2 * CORNER + MAJOR_STROKE;
		var x10 = x00 + width - 2 * CORNER - MAJOR_STROKE;
		var x11 = x00 + width - 2 * CORNER - MINOR_STROKE;
		var x12 = x00 + width - 2 * CORNER;
		var x13 = x00 + width - CORNER - MAJOR_STROKE;
		var x14 = x00 + width - CORNER - MINOR_STROKE;
		var x15 = x00 + width - CORNER;
		var x16 = x00 + width - MAJOR_STROKE;
		var x17 = x00 + width - MINOR_STROKE;

		var y00 = (componentHeight - height) / 2;
		var y01 = y00 + MAJOR_STROKE - MINOR_STROKE;
		var y02 = y00 + MAJOR_STROKE;
		var y03 = y00 + CORNER;
		var y04 = y00 + CORNER + MAJOR_STROKE - MINOR_STROKE;
		var y05 = y00 + CORNER + MAJOR_STROKE;
		var y06 = y00 + 2 * CORNER;
		var y07 = y00 + 2 * CORNER + MAJOR_STROKE - MINOR_STROKE;
		var y08 = y00 + 2 * CORNER + MAJOR_STROKE;
		var y09 = y00 + height - 2 * CORNER - MAJOR_STROKE;
		var y10 = y00 + height - 2 * CORNER - MAJOR_STROKE + MINOR_STROKE;
		var y11 = y00 + height - 2 * CORNER - MINOR_STROKE;
		var y12 = y00 + height - 2 * CORNER;
		var y13 = y00 + height - CORNER - MAJOR_STROKE;
		var y14 = y00 + height - CORNER - MAJOR_STROKE + MINOR_STROKE;
		var y15 = y00 + height - CORNER - MINOR_STROKE;
		var y16 = y00 + height - CORNER;
		var y17 = y00 + height - MAJOR_STROKE;
		var y18 = y00 + height - MINOR_STROKE;

		var w00 = MINOR_STROKE;
		var w01 = MAJOR_STROKE;
		var w02 = CORNER - MAJOR_STROKE + MINOR_STROKE;
		var w03 = CORNER;
		var w04 = CORNER + MINOR_STROKE;
		var w05 = 2 * CORNER;
		var w06 = width - 4 * CORNER - 2 * MAJOR_STROKE;
		var w07 = width - 4 * CORNER;
		var w08 = width - 2 * CORNER;

		var h00 = MINOR_STROKE;
		var h01 = MAJOR_STROKE;
		var h02 = CORNER - MAJOR_STROKE + MINOR_STROKE;
		var h03 = CORNER;
		var h04 = CORNER + MINOR_STROKE;
		var h05 = CORNER * 2;
		var h06 = height - 4 * CORNER - 2 * MAJOR_STROKE;
		var h07 = height - 4 * CORNER;
		var h08 = height - 2 * CORNER;
		var h09 = height;

		// BACKGROUND
		graphics.setColor(BACKGROUND);
		graphics.fillRect(x00, y06, w03, h07);
		graphics.fillRect(x04, y03, w03, h08);
		graphics.fillRect(x07, y00, w07, h09);
		graphics.fillRect(x10, y03, w03, h08);
		graphics.fillRect(x13, y06, w03, h07);

		// OUTSIDE
		graphics.setColor(MAJOR_STROKE_COLOR);
		graphics.fillRect(x04, y00, w08, h01);
		graphics.fillRect(x16, y03, w01, h08);
		graphics.fillRect(x04, y17, w08, h01);
		graphics.fillRect(x00, y03, w01, h08);
		graphics.fillRect(x03, y03, w05, h01);
		graphics.fillRect(x04, y00, w01, h05);
		graphics.fillRect(x13, y02, w01, h05);
		graphics.fillRect(x12, y03, w05, h01);
		graphics.fillRect(x12, y13, w05, h01);
		graphics.fillRect(x13, y09, w01, h05);
		graphics.fillRect(x04, y09, w01, h05);
		graphics.fillRect(x03, y13, w05, h01);

		// INSIDE
		graphics.fillRect(x03, y06, w03, h01);
		graphics.fillRect(x07, y02, w01, h03);
		graphics.fillRect(x10, y02, w01, h03);
		graphics.fillRect(x15, y06, w03, h01);
		graphics.fillRect(x15, y09, w03, h01);
		graphics.fillRect(x10, y13, w01, h03);
		graphics.fillRect(x07, y16, w01, h03);
		graphics.fillRect(x00, y09, w03, h01);

		// OUTSIDE BORDER
		graphics.setColor(MINOR_STROKE_COLOR);
		graphics.fillRect(x04, y00, w08, h00);
		graphics.fillRect(x17, y03, w00, h08);
		graphics.fillRect(x04, y18, w08, h00);
		graphics.fillRect(x00, y03, w00, h08);

		// OUTSIDE CORNERS
		graphics.fillRect(x01, y03, w03, h00);
		graphics.fillRect(x04, y00, w00, h03);
		graphics.fillRect(x14, y00, w00, h03);
		graphics.fillRect(x14, y03, w03, h00);
		graphics.fillRect(x15, y15, w03, h00);
		graphics.fillRect(x14, y15, w00, h03);
		graphics.fillRect(x04, y15, w00, h03);
		graphics.fillRect(x00, y15, w03, h00);

		graphics.fillRect(x09, y01, w06, h00);
		graphics.fillRect(x16, y08, w00, h06);
		graphics.fillRect(x09, y17, w06, h00);
		graphics.fillRect(x02, y08, w00, h06);

		graphics.fillRect(x02, y07, w03, h00);
		graphics.fillRect(x05, y05, w00, h03);
		graphics.fillRect(x05, y04, w03, h00);
		graphics.fillRect(x08, y01, w00, h04);
		graphics.fillRect(x10, y01, w00, h03);
		graphics.fillRect(x10, y04, w03, h00);
		graphics.fillRect(x13, y04, w00, h03);
		graphics.fillRect(x13, y07, w04, h00);
		graphics.fillRect(x13, y09, w04, h00);
		graphics.fillRect(x13, y10, w00, h03);
		graphics.fillRect(x10, y13, w03, h00);
		graphics.fillRect(x10, y14, w00, h03);
		graphics.fillRect(x08, y13, w00, h04);
		graphics.fillRect(x05, y13, w03, h00);
		graphics.fillRect(x05, y09, w00, h03);
		graphics.fillRect(x02, y09, w03, h00);

		graphics.fillRect(x02, y04, w02, h00);
		graphics.fillRect(x04, y04, w00, h02);
		graphics.fillRect(x03, y06, w02, h00);
		graphics.fillRect(x02, y05, w00, h02);

		graphics.fillRect(x05, y01, w02, h00);
		graphics.fillRect(x07, y01, w00, h02);
		graphics.fillRect(x06, y03, w02, h00);
		graphics.fillRect(x05, y02, w00, h02);

		graphics.fillRect(x11, y01, w02, h00);
		graphics.fillRect(x13, y01, w00, h02);
		graphics.fillRect(x12, y03, w02, h00);
		graphics.fillRect(x11, y02, w00, h02);

		graphics.fillRect(x14, y04, w02, h00);
		graphics.fillRect(x16, y04, w00, h02);
		graphics.fillRect(x15, y06, w02, h00);
		graphics.fillRect(x14, y05, w00, h02);

		graphics.fillRect(x14, y11, w02, h00);
		graphics.fillRect(x16, y11, w00, h02);
		graphics.fillRect(x15, y13, w02, h00);
		graphics.fillRect(x14, y12, w00, h02);

		graphics.fillRect(x11, y15, w02, h00);
		graphics.fillRect(x13, y15, w00, h02);
		graphics.fillRect(x12, y17, w02, h00);
		graphics.fillRect(x11, y16, w00, h02);

		graphics.fillRect(x05, y15, w02, h00);
		graphics.fillRect(x07, y15, w00, h02);
		graphics.fillRect(x06, y17, w02, h00);
		graphics.fillRect(x05, y16, w00, h02);

		graphics.fillRect(x02, y11, w02, h00);
		graphics.fillRect(x04, y11, w00, h02);
		graphics.fillRect(x03, y13, w02, h00);
		graphics.fillRect(x02, y12, w00, h02);
	}
}
