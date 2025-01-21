package com.github.achaaab.scrabble.model.solver;

import com.github.achaaab.scrabble.tools.ExceptionListener;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;

/**
 * Scrabble sheet for solver mode.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverSheet extends AbstractTableModel {

	public static final int INDEX_COLUMN = 0;
	public static final int WORD_COLUMN = 1;
	public static final int KEY_COLUMN = 2;
	public static final int SCORE_COLUMN = 3;
	public static final int TOTAL_COLUMN = 4;

	private final boolean editable;
	private final boolean accumulative;
	private final List<SolverSheetEntry> entries;
	private final List<ExceptionListener> exceptionListeners;

	/**
	 * Creates a simple sheet.
	 *
	 * @param editable whether this sheet is editable
	 * @param accumulative Whether this sheet is accumulative. An accumulative sheet has a cumulative total column.
	 * @since 1.0.2
	 */
	public SolverSheet(boolean editable, boolean accumulative) {

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
	public void add(SolverSheetEntry entry) {
		entries.add(entry);
	}

	/**
	 * Sets the specified entry as the last entry in this sheet then adds en empty entry to allow user input.
	 *
	 * @param entry entry to set as last entry
	 * @since 0.0.5
	 */
	public void setLast(SolverSheetEntry entry) {

		entries.getLast().copy(entry);
		add(new SolverSheetEntry());
		fireTableDataChanged();
	}

	/**
	 * Inserts a new empty entry at the specified index.
	 *
	 * @param index index at which the specified element is to be inserted
	 * @since 1.0.2
	 */
	public void insert(int index) {

		var entry = new SolverSheetEntry();
		entries.add(index, entry);
		fireTableDataChanged();
	}

	/**
	 * Clears the entry at the specified index.
	 *
	 * @param index index of the entry to clear
	 * @since 1.0.2
	 */
	public void clear(int index) {

		entries.get(index).clear();
		fireTableDataChanged();
	}

	/**
	 * Removes the entry at the specified index.
	 *
	 * @param index index of the entry to remove
	 * @since 1.0.2
	 */
	public void remove(int index) {

		entries.remove(index);
		fireTableDataChanged();
	}

	/**
	 * Clears this sheet, removing all its entries.
	 *
	 * @since 0.0.0
	 */
	public void clear() {

		entries.clear();
		fireTableDataChanged();
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

			case INDEX_COLUMN -> getMessage("move");
			case WORD_COLUMN -> getMessage("word");
			case KEY_COLUMN -> getMessage("reference");
			case SCORE_COLUMN -> getMessage("score");
			case TOTAL_COLUMN -> getMessage("sum");

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
				mapToInt(SolverSheetEntry::getScore).
				sum();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		var entry = entries.get(rowIndex);

		return switch (columnIndex) {

			case INDEX_COLUMN -> rowIndex + 1;
			case WORD_COLUMN -> entry.getWord();
			case KEY_COLUMN -> entry.getKey();
			case SCORE_COLUMN -> entry.getScore();
			case TOTAL_COLUMN -> getTotal(rowIndex);

			default -> throw new IllegalArgumentException("unknown column: " + columnIndex);
		};
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		var entry = entries.get(rowIndex);

		switch (columnIndex) {

			case WORD_COLUMN -> entry.setWord((String) value);
			case KEY_COLUMN -> entry.setKey((String) value);

			case INDEX_COLUMN, SCORE_COLUMN, TOTAL_COLUMN ->
					throw new IllegalArgumentException("unmodifiable column: " + columnIndex);

			default -> throw new IllegalArgumentException("unknown column: " + columnIndex);
		};

		try {

			if (entries.stream().allMatch(SolverSheetEntry::isComplete)) {
				add(new SolverSheetEntry());
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

			case INDEX_COLUMN, SCORE_COLUMN, TOTAL_COLUMN -> false;
			case WORD_COLUMN, KEY_COLUMN -> true;

			default -> throw new IllegalArgumentException("unknown column: " + columnIndex);
		};
	}

	/**
	 * @return entries in this sheet
	 * @since 0.0.0
	 */
	public List<SolverSheetEntry> entries() {
		return entries;
	}
}
