package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.rules.Solver;
import com.github.achaaab.scrabble.tools.SwingUtility;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static com.github.achaaab.scrabble.tools.SwingUtility.showException;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showOptionDialog;

/**
 * View for a scrabble solver.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverView extends Box {

	private final Solver model;

	private final BoardView board;
	private final RackView rack;
	private final SimpleSheetView sheet;
	private final JTextField letters;
	private final JButton solve;

	/**
	 * Creates a view for a solver.
	 *
	 * @param model solver model
	 * @since 0.0.0
	 */
	public SolverView(Solver model) {

		super(X_AXIS);

		this.model = model;

		board = new BoardView(model.board());
		rack = new RackView(model.rack());
		sheet = new SimpleSheetView(model.sheet());
		letters = new JTextField();
		solve = new JButton(getMessage("solve"));

		var rackPanel = new Box(Y_AXIS);
		rackPanel.setAlignmentX(CENTER_ALIGNMENT);
		rackPanel.add(rack);

		var drawPanel = new JPanel();
		drawPanel.setLayout(new BorderLayout());
		drawPanel.add(letters, CENTER);
		drawPanel.add(solve, EAST);
		drawPanel.setBorder(createTitledBorder(getMessage("drawn_tiles")));

		var leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(board, CENTER);
		leftPanel.add(rackPanel, SOUTH);

		var rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(sheet, CENTER);
		rightPanel.add(drawPanel, SOUTH);

		add(leftPanel);
		add(rightPanel);

		sheet.model().addTableModelListener(event -> {

			leftPanel.repaint();
			letters.setText("");
		});

		sheet.model().addExceptionListener(SwingUtility::showException);

		var lettersDocument = letters.getDocument();

		if (lettersDocument instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(DrawDocumentFilter.INSTANCE);
		}

		letters.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent event) {
				updateLetters();
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				updateLetters();
			}

			@Override
			public void changedUpdate(DocumentEvent event) {

			}
		});

		letters.addActionListener(this::solve);
		solve.addActionListener(this::solve);
	}

	/**
	 * Finds all the moves with the drawn letters.
	 *
	 * @param event action event
	 * @since 0.0.5
	 */
	private void solve(ActionEvent event) {

		var sheet = model.solve();
		var sheetView = new SimpleSheetView(sheet);
		var options = new String[] { "OK" };

		showOptionDialog(this, sheetView, getMessage("best_moves"),
				DEFAULT_OPTION, PLAIN_MESSAGE, null, options, options[0]);
	}

	/**
	 * Updates letters.
	 *
	 * @since 0.0.0
	 */
	private void updateLetters() {

		try {
			model.change(letters.getText());
		} catch (Exception exception) {
			showException(exception);
		} finally {
			rack.repaint();
		}
	}
}
