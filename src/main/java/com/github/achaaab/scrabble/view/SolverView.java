package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.rules.Solver;
import com.github.achaaab.scrabble.sheet.SimpleSheetEntry;
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

/**
 * View for a scrabble solver.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverView extends Box {

	/**
	 * Compare words.
	 *
	 * @param object0 first word to compare
	 * @param object1 second word to compare
	 * @return negative integer, zero, or a positive integer as the specified String is greater than, equal to,
	 * or less than this String, ignoring case considerations.
	 */
	public static int compareWords(Object object0, Object object1) {

		if (object0 instanceof String word0 && object1 instanceof String word1){
			return word0.compareToIgnoreCase(word1);
		}

		throw new IllegalArgumentException("given objects are not words");
	}

	private final Solver model;

	private final BoardView board;
	private final RackView rack;
	private final SimpleSheetView sheet;
	private final JTextField letters;

	private MoveListView moveList;

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
		var solve = new JButton(getMessage("solve"));

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
	 * Finds all the moves with the drawn letters and displays the list in a modal dialog.
	 *
	 * @param event action event
	 * @since 0.0.5
	 */
	private void solve(ActionEvent event) {

		var moves = model.solve();
		moveList = new MoveListView(sheet, moves, this::preview, this::play, this::refresh);
		moveList.setVisible(true);
	}

	/**
	 * Previews the specified move.
	 *
	 * @param move move to preview
	 * @since 0.0.6
	 */
	private void preview(SimpleSheetEntry move) {

		model.replay();
		updateLetters();
		model.preview(move);
		board.repaint();
		rack.repaint();
	}

	/**
	 * Plays the specified move then closes the move list dialog.
	 *
	 * @param move move to play
	 * @since 0.0.6
	 */
	private void play(SimpleSheetEntry move) {

		sheet.model().setLast(move);
		moveList.dispose();
	}

	/**
	 * Refreshes the board and rack.
	 *
	 * @since 0.0.6
	 */
	private void refresh() {

		model.replay();
		board.repaint();
		updateLetters();
	}

	/**
	 * Updates letters on the rack.
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
