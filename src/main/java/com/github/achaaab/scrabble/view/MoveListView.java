package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.sheet.SimpleSheet;
import com.github.achaaab.scrabble.sheet.SimpleSheetEntry;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.achaaab.scrabble.sheet.SimpleSheet.INDEX_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.KEY_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.SCORE_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.WORD_COLUMN;
import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.util.Comparator.naturalOrder;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.SwingUtilities.getWindowAncestor;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * Move list view, displaying a list of playable moves and allowing interaction (move selection, move playing...).
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.6
 */
public class MoveListView extends JDialog {

	private static final String PLAY_SELECTED_MOVE_ACTION = "play-selected-move";
	private static final String CLOSE_MOVES_DIALOG_ACTION = "close-moves-dialog";

	private final SimpleSheet moves;
	private final JTable table;

	/**
	 * Creates a move list dialog view.
	 *
	 * @param parent parent component, which will used for dialog positioning and modality
	 * @param moves playable move sheet
	 * @param selectedMoveConsumer selected move consumer
	 * @param playedMoveConsumer played move consumer
	 * @param onClosed action to run when this dialog is closed
	 * @since 0.0.6
	 */
	public MoveListView(
			Component parent,
			SimpleSheet moves,
			Consumer<SimpleSheetEntry> selectedMoveConsumer,
			Consumer<SimpleSheetEntry> playedMoveConsumer,
			Runnable onClosed) {

		super(
				getWindowAncestor(parent),
				getMessage("best_moves"),
				APPLICATION_MODAL);

		this.moves = moves;

		var movesView = new SimpleSheetView(moves);
		table = movesView.table();
		var selectionModel = table.getSelectionModel();
		var tableModel = table.getModel();
		var sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);

		sorter.setComparator(INDEX_COLUMN, naturalOrder());
		sorter.setComparator(WORD_COLUMN, SolverView::compareWords);
		sorter.setComparator(KEY_COLUMN, naturalOrder());
		sorter.setComparator(SCORE_COLUMN, naturalOrder());

		selectionModel.addListSelectionListener(listSelectionEvent ->
				getSelectedEntry().ifPresent(selectedMoveConsumer));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(movesView);
		pack();
		setLocationRelativeTo(parent);

		var inputMap = table.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		var actionMap = table.getActionMap();

		inputMap.put(getKeyStroke(VK_ENTER, 0), PLAY_SELECTED_MOVE_ACTION);
		inputMap.put(getKeyStroke(VK_ESCAPE, 0), CLOSE_MOVES_DIALOG_ACTION);

		actionMap.put(PLAY_SELECTED_MOVE_ACTION, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				getSelectedEntry().ifPresent(playedMoveConsumer);
			}
		});

		actionMap.put(CLOSE_MOVES_DIALOG_ACTION, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		});

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {

				if (event.getButton() == BUTTON1 && event.getClickCount() == 2) {

					var rowIndex = table.rowAtPoint(event.getPoint());
					getEntry(rowIndex).ifPresent(playedMoveConsumer);
				}
			}
		});

		addWindowListener(new WindowAdapter() {

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
				onClosed.run();
			}
		});
	}

	/**
	 * Identifies and returns the selected entry.
	 *
	 * @return entry corresponding to the selected row in the table
	 * @since 0.0.6
	 */
	private Optional<SimpleSheetEntry> getSelectedEntry() {
		return getEntry(table.getSelectedRow());
	}

	/**
	 * Identifies and returns the entry corresponding to the specified row index.
	 *
	 * @param rowIndex row index
	 * @return entry corresponding to the specified row index
	 * @since 0.0.6
	 */
	private Optional<SimpleSheetEntry> getEntry(int rowIndex) {

		Optional<SimpleSheetEntry> selectedEntry;

		if (rowIndex == -1) {

			selectedEntry = Optional.empty();

		} else {

			var entryIndex = table.convertRowIndexToModel(rowIndex);
			selectedEntry = Optional.of(moves.entries().get(entryIndex));
		}

		return selectedEntry;
	}
}
