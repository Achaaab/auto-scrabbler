package com.github.achaaab.scrabble.view.solver;

import com.github.achaaab.scrabble.model.solver.Solver;
import com.github.achaaab.scrabble.model.solver.SolverSheetEntry;
import com.github.achaaab.scrabble.view.ViewUtilities;
import com.github.achaaab.scrabble.view.core.BoardView;
import com.github.achaaab.scrabble.view.core.RackView;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static com.github.achaaab.scrabble.view.ViewUtilities.showException;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.getWindowAncestor;

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
	private final SolverSheetView sheet;
	private final DrawView draw;

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
		sheet = new SolverSheetView(model.sheet());
		draw = new DrawView(this::updateDraw);

		var solve = new JButton(getMessage("solve"));

		var rackPanel = new Box(Y_AXIS);
		rackPanel.setAlignmentX(CENTER_ALIGNMENT);
		rackPanel.add(rack);

		var drawPanel = new JPanel();
		drawPanel.setLayout(new BorderLayout());
		drawPanel.add(draw, CENTER);
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

			model.replay();
			draw.setText("");

			getWindowAncestor(this).repaint();
		});

		sheet.model().addExceptionListener(ViewUtilities::showException);

		var lettersDocument = draw.getDocument();

		if (lettersDocument instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(DrawDocumentFilter.INSTANCE);
		}

		draw.addActionListener(this::solve);
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
	private void preview(SolverSheetEntry move) {

		model.replay();
		updateDraw();
		model.preview(move);

		board.repaint();
	}

	/**
	 * Plays the specified move then closes the move list dialog.
	 *
	 * @param move move to play
	 * @since 0.0.6
	 */
	private void play(SolverSheetEntry move) {

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

		updateDraw();
	}

	/**
	 * Updates drawn letters on the rack.
	 *
	 * @since 0.0.0
	 */
	private void updateDraw() {

		try {
			model.change(draw.getText());
		} catch (Exception exception) {
			showException(exception);
		} finally {
			rack.repaint();
		}
	}
}
