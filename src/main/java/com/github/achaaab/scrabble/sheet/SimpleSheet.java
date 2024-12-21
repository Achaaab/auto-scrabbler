package com.github.achaaab.scrabble.sheet;

import com.github.achaaab.scrabble.rules.Solver;

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
	private final boolean accumulative;
	private final List<SimpleSheetEntry> entries;
	private final List<ExceptionListener> exceptionListeners;

	/**
	 * @param solver
	 * @param editable
	 * @param accumulative
	 * @since 0.0.2
	 */
	public SimpleSheet(Solver solver, boolean editable, boolean accumulative) {

		this.solver = solver;
		this.editable = editable;
		this.accumulative = accumulative;

		entries = new ArrayList<>();
		exceptionListeners = new ArrayList<>();
	}

	/**
	 * @return
	 * @since 0.0.2
	 */
	public boolean isAccumulative() {
		return accumulative;
	}

	/**
	 * @param exceptionListener
	 * @since 0.0.0
	 */
	public void addExceptionListener(ExceptionListener exceptionListener) {
		exceptionListeners.add(exceptionListener);
	}

	/**
	 * @param exception
	 * @since 0.0.0
	 */
	private void fireExceptionOccurred(Exception exception) {
		exceptionListeners.forEach(exceptionListener -> exceptionListener.exceptionOccurred(exception));
	}

	/**
	 * @param entry
	 * @since 0.0.0
	 */
	public void add(SimpleSheetEntry entry) {
		entries.add(entry);
	}

	/**
	 * Clears this sheet.
	 *
	 * @since 0.0.0
	 */
	public void clear() {
		entries.clear();
	}

	@Override
	public int getRowCount() {
		return entries.size();
	}

	@Override
	public int getColumnCount() {
		return accumulative ? 5 : 4;
	}

	@Override
	public String getColumnName(int column) {

		return switch (column) {

			case 0 -> "Coup";
			case 1 -> "Mot";
			case 2 -> "Référence";
			case 3 -> "Score";
			case 4 -> "Total";

			default -> throw new IllegalArgumentException("unknown column: " + column);
		};
	}

	/**
	 * @param rowIndex
	 * @return
	 * @since 0.0.2
	 */
	private int getTotal(int rowIndex) {

		return entries.
				subList(0, rowIndex + 1).
				stream().
				mapToInt(SimpleSheetEntry::getScore).
				sum();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		var entry = entries.get(rowIndex);

		return switch (columnIndex) {

			case 0 -> rowIndex + 1;
			case 1 -> entry.getWord();
			case 2 -> entry.getKey();
			case 3 -> entry.getScore();
			case 4 -> getTotal(rowIndex);

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

		try {

			solver.replay();

			if (entries.stream().allMatch(SimpleSheetEntry::isComplete)) {
				entries.add(new SimpleSheetEntry());
			}

		} catch (Exception exception) {

			fireExceptionOccurred(exception);

		} finally {

			fireTableDataChanged();
		}
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
	 * @return entries in this sheet
	 * @since 0.0.0
	 */
	public List<SimpleSheetEntry> entries() {
		return entries;
	}
}
