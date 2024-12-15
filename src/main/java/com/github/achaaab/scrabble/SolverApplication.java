package com.github.achaaab.scrabble;

import com.github.achaaab.scrabble.model.Solver;
import com.github.achaaab.scrabble.sheet.SimpleSheetEntry;
import com.github.achaaab.scrabble.view.SolverView;
import org.slf4j.Logger;

import javax.swing.JFrame;
import java.awt.FlowLayout;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverApplication {

	private static final Logger LOGGER = getLogger(SolverApplication.class);

	/**
	 * @param arguments none
	 * @since 0.0.0
	 */
	public static void main(String... arguments) {

		try {
			setLookAndFeel(getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
		}

		var solver = new Solver(getFrenchTiles(), FRENCH_ODS9);
		solver.sheet().add(new SimpleSheetEntry());

		var window = new JFrame("Scrabble solver");
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		var view = new SolverView(solver);
		var contentPane = window.getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(view);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
