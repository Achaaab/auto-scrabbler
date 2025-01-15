package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.sheet.SimpleSheet;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import java.awt.Dimension;
import java.util.Collections;

import static com.github.achaaab.scrabble.sheet.SimpleSheet.INDEX_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.KEY_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.SCORE_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.TOTAL_COLUMN;
import static com.github.achaaab.scrabble.sheet.SimpleSheet.WORD_COLUMN;
import static com.github.achaaab.scrabble.tools.SwingUtility.resizeScrollBars;
import static java.lang.Math.round;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * View for a simple sheet.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SimpleSheetView extends JScrollPane {

	private static final float INDEX_COLUMN_WIDTH = 1.50f;
	private static final float WORD_COLUMN_WIDTH = 4.00f;
	private static final float KEY_COLUMN_WIDTH = 2.50f;
	private static final float SCORE_COLUMN_WIDTH = 2.00f;
	private static final float TOTAL_COLUMN_WIDTH = 2.00f;

	private static final float HEIGHT = 15.00f;

	private final SimpleSheet model;
	private final JTable table;

	/**
	 * Creates a view for a simple sheet.
	 *
	 * @param model simple sheet
	 * @since 0.0.2
	 */
	public SimpleSheetView(SimpleSheet model) {

		this.model = model;

		table = new JTable(model);
		table.setRowHeight(round(TileView.SIZE * 0.50f));
		table.setSelectionMode(SINGLE_SELECTION);

		resizeScrollBars(this, TileView.SIZE * 0.28f, TileView.SIZE * 0.28f);

		setViewportView(table);

		var columnModel = table.getColumnModel();

		var indexColumn = columnModel.getColumn(INDEX_COLUMN);
		var wordColumn = columnModel.getColumn(WORD_COLUMN);
		var keyColumn = columnModel.getColumn(KEY_COLUMN);
		var scoreColumn = columnModel.getColumn(SCORE_COLUMN);

		indexColumn.setPreferredWidth(round(TileView.SIZE * INDEX_COLUMN_WIDTH));
		wordColumn.setPreferredWidth(round(TileView.SIZE * WORD_COLUMN_WIDTH));
		keyColumn.setPreferredWidth(round(TileView.SIZE * KEY_COLUMN_WIDTH));
		scoreColumn.setPreferredWidth(round(TileView.SIZE * SCORE_COLUMN_WIDTH));

		if (model.isAccumulative()) {

			var totalColumn = columnModel.getColumn(TOTAL_COLUMN);
			totalColumn.setPreferredWidth(round(TileView.SIZE * TOTAL_COLUMN_WIDTH));
		}

		var preferredWidth = Collections.
				list(columnModel.getColumns()).
				stream().
				mapToInt(TableColumn::getPreferredWidth).
				sum();

		setPreferredSize(new Dimension(preferredWidth, round(TileView.SIZE * HEIGHT)));

		// customize the word edition field with a document filter

		var wordEditionField = new JTextField();
		var wordDocument = wordEditionField.getDocument();

		if (wordDocument instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(WordDocumentFilter.INSTANCE);
		}

		wordColumn.setCellEditor(new DefaultCellEditor(wordEditionField));
	}

	/**
	 * @return model of this view
	 * @since 0.0.0
	 */
	public SimpleSheet model() {
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
