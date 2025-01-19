package com.github.achaaab.scrabble;

import com.github.achaaab.scrabble.model.solver.SimpleSheetEntry;
import com.github.achaaab.scrabble.model.solver.Solver;
import com.github.achaaab.scrabble.view.MainMenu;
import com.github.achaaab.scrabble.view.MessagePanel;
import com.github.achaaab.scrabble.view.SolverView;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static com.github.achaaab.scrabble.model.core.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.core.Tile.getFrenchTiles;
import static com.github.achaaab.scrabble.tools.ResourceUtilities.loadImage;
import static com.github.achaaab.scrabble.view.ViewUtilities.showException;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverApplication {

	/**
	 * @param arguments none
	 * @since 0.0.0
	 */
	public static void main(String... arguments) {

		try {
			setLookAndFeel(getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			showException(exception);
		}

		var solver = new Solver(getFrenchTiles(), FRENCH_ODS9);
		solver.sheet().add(new SimpleSheetEntry());

		var window = new JFrame("Scrabble solver");
		window.setIconImage(loadImage("icon_256.png"));
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);

		var rootPane = window.getRootPane();

		var message = new MessagePanel();
		message.setLocation(0, 0);
		rootPane.add(message, 0);

		var mainMenu = new MainMenu(message);
		window.add(mainMenu, NORTH);

		var centerPanel = new JPanel();
		var view = new SolverView(solver);
		centerPanel.add(view);
		window.add(centerPanel, CENTER);

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		rootPane.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent event) {
				message.resize();
			}
		});
	}
}
