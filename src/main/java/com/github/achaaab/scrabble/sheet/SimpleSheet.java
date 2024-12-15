package com.github.achaaab.scrabble.sheet;

import com.github.achaaab.scrabble.model.Solver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SimpleSheet extends AbstractTableModel {

	private final Solver solver;
	private final boolean editable;
	private final List<SimpleSheetEntry> entries;
	private int total;

	/**
	 * @param solver
	 * @param editable
	 * @since 0.0.0
	 */
	public SimpleSheet(Solver solver, boolean editable) {

		this.solver = solver;
		this.editable = editable;

		entries = new ArrayList<>();
		total = 0;
	}

	/**
	 * @param entry
	 * @since 0.0.0
	 */
	public void add(SimpleSheetEntry entry) {

		entries.add(entry);
		total += entry.getScore();
	}

	/**
	 * Clears this sheet.
	 *
	 * @since 0.0.0
	 */
	public void clear() {

		entries.clear();
		total = 0;
	}

	@Override
	public int getRowCount() {
		return entries.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {

		return switch (column) {

			case 0 -> "Coup";
			case 1 -> "Mot";
			case 2 -> "Référence";
			case 3 -> "Score";

			default -> throw new IllegalArgumentException("unknown column: " + column);
		};
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		var entry = entries.get(rowIndex);

		return switch (columnIndex) {

			case 0 -> rowIndex + 1;
			case 1 -> entry.getWord();
			case 2 -> entry.getKey();
			case 3 -> entry.getScore();

			default -> throw new IllegalArgumentException("unknown column: " + columnIndex);
		};
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		var entry = entries.get(rowIndex);

		switch (columnIndex) {

			case 1 -> entry.setWord((String) value);
			case 2 -> entry.setKey((String) value);

			default -> throw new IllegalArgumentException("unmodifiable column: " + columnIndex);
		};

		solver.replay();

		if (entries.stream().allMatch(SimpleSheetEntry::isComplete)) {
			entries.add(new SimpleSheetEntry());
		}

		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		return editable && switch (columnIndex) {

			case 0, 3 -> false;
			case 1, 2 -> true;

			default -> throw new IllegalArgumentException("unknown column: " + columnIndex);
		};
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public List<SimpleSheetEntry> entries() {
		return entries;
	}
}
