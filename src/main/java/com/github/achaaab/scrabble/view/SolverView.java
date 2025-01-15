package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.rules.Solver;
import com.github.achaaab.scrabble.tools.SwingUtility;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.github.achaaab.scrabble.sheet.SimpleSheet.INDEX_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.KEY_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.SCORE_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.WORD_COLUMN;
import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static com.github.achaaab.scrabble.tools.SwingUtility.showException;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.util.Comparator.naturalOrder;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.SwingUtilities.getWindowAncestor;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * View for a scrabble solver.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverView extends Box {

	private static final String PLAY_SELECTED_MOVE_ACTION = "play-selected-move";
	private static final String CLOSE_MOVES_DIALOG_ACTION = "close-moves-dialog";

	private final Solver model;

	private final BoardView board;
	private final RackView rack;
	private final SimpleSheetView sheet;
	private final JTextField letters;

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
		var movesView = new SimpleSheetView(moves);
		var table = movesView.table();
		var selectionModel = table.getSelectionModel();
		var tableModel = table.getModel();
		var sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);

		sorter.setComparator(INDEX_COLUMN, naturalOrder());
		sorter.setComparator(WORD_COLUMN, naturalOrder());
		sorter.setComparator(KEY_COLUMN, naturalOrder());
		sorter.setComparator(SCORE_COLUMN, naturalOrder());

		selectionModel.addListSelectionListener(listSelectionEvent -> {

			var index = table.getSelectedRow();

			if (index != -1) {

				index = table.convertRowIndexToModel(index);
				var move = moves.entries().get(index);

				model.replay();
				updateLetters();
				model.preview(move);
				board.repaint();
				rack.repaint();
			}
		});

		var dialog = new JDialog(
				getWindowAncestor(this),
				getMessage("best_moves"),
				APPLICATION_MODAL);

		dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dialog.add(movesView);
		dialog.pack();
		dialog.setLocationRelativeTo(sheet);

		var inputMap = table.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		var actionMap = table.getActionMap();

		inputMap.put(getKeyStroke(VK_ENTER, 0), PLAY_SELECTED_MOVE_ACTION);
		inputMap.put(getKeyStroke(VK_ESCAPE, 0), CLOSE_MOVES_DIALOG_ACTION);

		actionMap.put(PLAY_SELECTED_MOVE_ACTION, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				var index = selectionModel.getMinSelectionIndex();
				var move = moves.entries().get(index);
				sheet.model().setLast(move);
				dialog.dispose();
			}
		});

		actionMap.put(CLOSE_MOVES_DIALOG_ACTION, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				dialog.dispose();
			}
		});

		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent windowEvent) {

				invokeLater(() -> {

					if (moves.getRowCount() > 0) {
						selectionModel.setSelectionInterval(0, 0);
					}
				});
			}

			@Override
			public void windowClosed(WindowEvent windowEvent) {

				model.replay();
				board.repaint();
				updateLetters();
			}
		});

		dialog.setVisible(true);
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
