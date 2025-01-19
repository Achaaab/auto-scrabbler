package com.github.achaaab.scrabble.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.util.Map;

import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static com.github.achaaab.scrabble.tools.ResourceUtilities.getDocument;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;
import static java.lang.System.exit;

/**
 * Scrabble main menu.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.1
 */
public class MainMenu extends JMenuBar {

	private final String aboutMessage;
	private final MessagePanel messagePanel;

	/**
	 * Creates the main menu.
	 *
	 * @param messagePanel message panel
	 * @since 1.0.1
	 */
	public MainMenu(MessagePanel messagePanel) {

		this.messagePanel = messagePanel;

		aboutMessage = getDocument("messages/about.html", Map.of(
				"font_size", pixels(0.30)));

		var file = new JMenu(getMessage("file"));
		var help = new JMenu(getMessage("help"));

		var quit = new JMenuItem(getMessage("quit"));
		var about = new JMenuItem(getMessage("about"));

		file.add(quit);

		help.add(about);

		add(file);
		add(help);

		quit.addActionListener(this::quit);
		about.addActionListener(this::about);
	}

	/**
	 * Quits the application.
	 *
	 * @param event action event
	 * @since 1.0.1
	 */
	private void quit(ActionEvent event) {
		exit(0);
	}

	/**
	 * Displays the about message.
	 *
	 * @param event action event
	 * @since 1.0.1
	 */
	private void about(ActionEvent event) {
		messagePanel.display(aboutMessage);
	}
}
