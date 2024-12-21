package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.sheet.SimpleSheet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import java.util.Collections;

import static java.lang.Math.round;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SimpleSheetView extends JScrollPane {

	private static final float INDEX_COLUMN_WIDTH = 1.50f;
	private static final float WORD_COLUMN_WIDTH = 4.00f;
	private static final float REFERENCE_COLUMN_WIDTH = 2.50f;
	private static final float SCORE_COLUMN_WIDTH = 2.00f;
	private static final float TOTAL_COLUMN_WIDTH = 2.00f;

	private static final float HEIGHT = 15.00f;

	private final SimpleSheet model;

	/**
	 * @param model
	 * @since 0.0.2
	 */
	public SimpleSheetView(SimpleSheet model) {

		this.model = model;

		var table = new JTable(model);
		table.setRowHeight(round(TileView.SIZE * 0.50f));

		setViewportView(table);

		var columnModel = table.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(round(TileView.SIZE * INDEX_COLUMN_WIDTH));
		columnModel.getColumn(1).setPreferredWidth(round(TileView.SIZE * WORD_COLUMN_WIDTH));
		columnModel.getColumn(2).setPreferredWidth(round(TileView.SIZE * REFERENCE_COLUMN_WIDTH));
		columnModel.getColumn(3).setPreferredWidth(round(TileView.SIZE * SCORE_COLUMN_WIDTH));

		if (model.isAccumulative()) {
			columnModel.getColumn(4).setPreferredWidth(round(TileView.SIZE * TOTAL_COLUMN_WIDTH));
		}

		var preferredWidth = Collections.
				list(columnModel.getColumns()).
				stream().
				mapToInt(TableColumn::getPreferredWidth).
				sum();

		setPreferredSize(new Dimension(preferredWidth, round(TileView.SIZE * HEIGHT)));
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public SimpleSheet model() {
		return model;
	}
}
