package com.github.achaaab.scrabble.sheet;

import com.github.achaaab.scrabble.rules.Solver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple scrabble sheet without drawn letters indication.
 *
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
	 * Creates a simple sheet.
	 *
	 * @param solver solver associated to this sheet
	 * @param editable whether this sheet is editable
	 * @param accumulative Whether this sheet is accumulative. An accumulative sheet has a cumulative total column.
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
	 * @return Whether this sheet is accumulative. An accumulative sheet has a cumulative total column.
	 * @since 0.0.2
	 */
	public boolean isAccumulative() {
		return accumulative;
	}

	/**
	 * Adds an exception listener to this sheet.
	 * Any exception fired by an action on this sheet should signal it to the listeners.
	 *
	 * @param exceptionListener exception listener to add
	 * @since 0.0.0
	 */
	public void addExceptionListener(ExceptionListener exceptionListener) {
		exceptionListeners.add(exceptionListener);
	}

	/**
	 * Fires a signal: exception occurred. Calls the method {@link ExceptionListener#exceptionOccurred(Exception)} on
	 * all the registered listeners.
	 *
	 * @param exception exception to signal
	 * @since 0.0.0
	 */
	private void fireExceptionOccurred(Exception exception) {
		exceptionListeners.forEach(exceptionListener -> exceptionListener.exceptionOccurred(exception));
	}

	/**
	 * Adds an entry to this sheet.
	 *
	 * @param entry entry to add
	 * @since 0.0.0
	 */
	public void add(SimpleSheetEntry entry) {
		entries.add(entry);
	}

	/**
	 * Sets the specified entry as the last entry in this sheet then adds en empty entry to allow user input.
	 *
	 * @param entry entry to set as last entry
	 * @since 0.0.5
	 */
	public void setLast(SimpleSheetEntry entry) {

		var lastIndex = entries.size() - 1;
		entries.set(lastIndex, entry);
		add(new SimpleSheetEntry());
		fireTableDataChanged();
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
	 * Gets the total score at the specified row (for accumulative sheets).
	 *
	 * @param rowIndex row index
	 * @return total score at the specified row
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
				add(new SimpleSheetEntry());
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
