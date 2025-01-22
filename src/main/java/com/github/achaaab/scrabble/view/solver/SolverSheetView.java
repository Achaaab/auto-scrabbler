package com.github.achaaab.scrabble.view.solver;

import com.github.achaaab.scrabble.model.solver.SolverSheet;
import com.github.achaaab.scrabble.view.WordDocumentFilter;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import java.awt.Dimension;
import java.util.Collections;

import static com.github.achaaab.scrabble.model.solver.SolverSheet.INDEX_COLUMN;
import static com.github.achaaab.scrabble.model.solver.SolverSheet.KEY_COLUMN;
import static com.github.achaaab.scrabble.model.solver.SolverSheet.SCORE_COLUMN;
import static com.github.achaaab.scrabble.model.solver.SolverSheet.TOTAL_COLUMN;
import static com.github.achaaab.scrabble.model.solver.SolverSheet.WORD_COLUMN;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;
import static com.github.achaaab.scrabble.view.ViewUtilities.resizeScrollBars;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * View for a simple sheet.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverSheetView extends JScrollPane {

	private static final double INDEX_COLUMN_WIDTH = 1.50;
	private static final double WORD_COLUMN_WIDTH = 4.00;
	private static final double KEY_COLUMN_WIDTH = 2.50;
	private static final double SCORE_COLUMN_WIDTH = 2.00;
	private static final double TOTAL_COLUMN_WIDTH = 2.00;

	private static final double HEIGHT = 15.00;

	private final SolverSheet model;
	private final JTable table;

	/**
	 * Creates a view for a simple sheet.
	 *
	 * @param model simple sheet
	 * @since 0.0.2
	 */
	public SolverSheetView(SolverSheet model) {

		this.model = model;

		table = new JTable(model);
		table.setRowHeight(pixels(0.50));
		table.setSelectionMode(SINGLE_SELECTION);
		table.putClientProperty("terminateEditOnFocusLost", true);

		var contextMenu = new SolverSheetMenu(model, table);
		table.addMouseListener(contextMenu);

		setBorder(createEmptyBorder());
		resizeScrollBars(this, pixels(0.28), pixels(0.28));
		setViewportView(table);

		var columnModel = table.getColumnModel();

		var indexColumn = columnModel.getColumn(INDEX_COLUMN);
		var wordColumn = columnModel.getColumn(WORD_COLUMN);
		var keyColumn = columnModel.getColumn(KEY_COLUMN);
		var scoreColumn = columnModel.getColumn(SCORE_COLUMN);

		indexColumn.setPreferredWidth(pixels(INDEX_COLUMN_WIDTH));
		wordColumn.setPreferredWidth(pixels(WORD_COLUMN_WIDTH));
		keyColumn.setPreferredWidth(pixels(KEY_COLUMN_WIDTH));
		scoreColumn.setPreferredWidth(pixels(SCORE_COLUMN_WIDTH));

		if (model.isAccumulative()) {

			var totalColumn = columnModel.getColumn(TOTAL_COLUMN);
			totalColumn.setPreferredWidth(pixels(TOTAL_COLUMN_WIDTH));
		}

		var preferredWidth = Collections.
				list(columnModel.getColumns()).
				stream().
				mapToInt(TableColumn::getPreferredWidth).
				sum();

		setPreferredSize(new Dimension(preferredWidth, pixels(HEIGHT)));

		// customize the word edition field with a document filter

		var wordEditionField = new JTextField();
		var wordDocument = wordEditionField.getDocument();

		if (wordDocument instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(WordDocumentFilter.INSTANCE);
		}

		wordColumn.setCellEditor(new DefaultCellEditor(wordEditionField));
	}

	/**
	 * Returns the model of this view.
	 *
	 * @return model of this view
	 * @since 0.0.0
	 */
	public SolverSheet model() {
		return model;
	}

	/**
	 * Returns the inner table.
	 *
	 * @return inner table
	 * @since 0.0.5
	 */
	public JTable table() {
		return table;
	}
}
