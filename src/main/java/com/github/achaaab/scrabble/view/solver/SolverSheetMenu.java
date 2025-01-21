package com.github.achaaab.scrabble.view.solver;

import com.github.achaaab.scrabble.model.solver.SolverSheet;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static com.github.achaaab.scrabble.tools.ResourceUtilities.loadIcon;
import static com.github.achaaab.scrabble.view.ViewUtilities.isDark;
import static com.github.achaaab.scrabble.view.ViewUtilities.pixels;

/**
 * Context menu for solver sheet.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.2
 */
public class SolverSheetMenu extends JPopupMenu implements MouseListener {

	private static final double ICON_SIZE = pixels(0.50);

	private final SolverSheet model;
	private final JTable view;
	private final JMenuItem remove;

	/**
	 * Creates a context menu for a solver sheet.
	 *
	 * @param model solver sheet model
	 * @param view solver sheet view
	 * @since 1.0.2
	 */
	public SolverSheetMenu(SolverSheet model, JTable view) {

		this.view = view;
		this.model = model;

		var dark = isDark(getBackground());

		var insert = new JMenuItem(getMessage("insert_entry"));
		var clear = new JMenuItem(getMessage("clear_entry"));
		remove = new JMenuItem(getMessage("remove_entry"));

		insert.setIcon(dark ?
				loadIcon("icons/dark/insert_256.png", ICON_SIZE, ICON_SIZE) :
				loadIcon("icons/light/insert_256.png", ICON_SIZE, ICON_SIZE));

		clear.setIcon(dark ?
				loadIcon("icons/dark/clear_256.png", ICON_SIZE, ICON_SIZE) :
				loadIcon("icons/light/clear_256.png", ICON_SIZE, ICON_SIZE));

		remove.setIcon(dark ?
				loadIcon("icons/dark/close_256.png", ICON_SIZE, ICON_SIZE) :
				loadIcon("icons/light/close_256.png", ICON_SIZE, ICON_SIZE));

		add(insert);
		add(clear);
		add(remove);

		insert.addActionListener(this::insert);
		clear.addActionListener(this::clear);
		remove.addActionListener(this::remove);
	}

	/**
	 * Inserts a new empty entry below the selected entry.
	 *
	 * @param event action event
	 * @since 1.0.2
	 */
	private void insert(ActionEvent event) {

		var rowIndex = view.getSelectedRow();

		if (rowIndex != -1) {

			var entryIndex = view.convertRowIndexToModel(rowIndex);
			model.insert(entryIndex);
		}
	}

	/**
	 * Clears the selected entry.
	 *
	 * @param event action event
	 * @since 1.0.2
	 */
	private void clear(ActionEvent event) {

		var rowIndex = view.getSelectedRow();

		if (rowIndex != -1) {

			var entryIndex = view.convertRowIndexToModel(rowIndex);
			model.clear(entryIndex);
		}
	}

	/**
	 * Removes the selected entry.
	 *
	 * @param event action event
	 * @since 1.0.2
	 */
	private void remove(ActionEvent event) {

		var rowIndex = view.getSelectedRow();

		if (rowIndex != -1) {

			var entryIndex = view.convertRowIndexToModel(rowIndex);
			model.remove(entryIndex);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent event) {

		if (event.isPopupTrigger()) {

			selectRowAtPoint(event);
			show(event);
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {

		if (event.isPopupTrigger()) {

			selectRowAtPoint(event);
			show(event);
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {

	}

	@Override
	public void mouseExited(MouseEvent event) {

	}

	/**
	 * Selects the row at the point where the mouse event occurred.
	 *
	 * @param event mouse event
	 * @since 1.0.2
	 */
	private void selectRowAtPoint(MouseEvent event) {

		int row = view.rowAtPoint(event.getPoint());

		if (row != -1) {
			view.setRowSelectionInterval(row, row);
		}
	}

	/**
	 * Shows this menu.
	 *
	 * @param event mouse event
	 * @since 1.0.2
	 */
	private void show(MouseEvent event) {

		remove.setEnabled(model.getRowCount() > 1);

		show(
				event.getComponent(),
				event.getX(),
				event.getY());
	}
}
