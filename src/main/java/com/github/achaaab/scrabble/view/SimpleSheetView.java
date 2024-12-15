package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.sheet.SimpleSheet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;

import static java.lang.Math.round;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SimpleSheetView extends JScrollPane {

	private final SimpleSheet model;
	private final JTable table;

	public SimpleSheetView(SimpleSheet model) {

		this.model = model;

		table = new JTable(model);
		table.setRowHeight(round(TileView.SIZE * 0.50f));

		setViewportView(table);

		var columnModel = table.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(round(TileView.SIZE * 1.50f));
		columnModel.getColumn(1).setPreferredWidth(round(TileView.SIZE * 4.00f));
		columnModel.getColumn(2).setPreferredWidth(round(TileView.SIZE * 2.50f));
		columnModel.getColumn(3).setPreferredWidth(round(TileView.SIZE * 2.00f));

		setPreferredSize(new Dimension(round(TileView.SIZE * 10.00f), round(TileView.SIZE * 15.00f)));
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public SimpleSheet model() {
		return model;
	}
}
