package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Solver;
import com.github.achaaab.scrabble.tools.SwingUtility;
import com.github.achaaab.scrabble.tools.Toolbox;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.BorderLayout;

import static com.github.achaaab.scrabble.tools.SwingUtility.showException;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showOptionDialog;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SolverView extends Box {

	private final Solver model;

	private final BoardView board;
	private final RackView rack;
	private final SimpleSheetView sheet;
	private final JTextField letters;
	private final JButton solve;

	/**
	 * @param model
	 * @since 0.0.0
	 */
	public SolverView(Solver model) {

		super(X_AXIS);

		this.model = model;

		board = new BoardView(model.board());
		rack = new RackView(model.rack());
		sheet = new SimpleSheetView(model.sheet());
		letters = new JTextField();
		solve = new JButton("Résoudre");

		var rackPanel = new Box(Y_AXIS);
		rackPanel.setAlignmentX(CENTER_ALIGNMENT);
		rackPanel.add(rack);

		var drawPanel = new JPanel();
		drawPanel.setLayout(new BorderLayout());
		drawPanel.add(letters, CENTER);
		drawPanel.add(solve, EAST);
		drawPanel.setBorder(createTitledBorder("Tirage"));

		var leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(board, CENTER);
		leftPanel.add(rackPanel, SOUTH);

		var rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(sheet, CENTER);
		rightPanel.add(drawPanel, SOUTH);

		add(leftPanel);
		add(rightPanel);

		sheet.model().addTableModelListener(event -> {

			leftPanel.repaint();
			letters.setText("");
		});

		sheet.model().addExceptionListener(SwingUtility::showException);

		var lettersDocument = letters.getDocument();

		if (lettersDocument instanceof AbstractDocument abstractDocument) {
			abstractDocument.setDocumentFilter(new DocumentFilter() {

				@Override
				public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
					super.insertString(fb, offset, string.toUpperCase().replaceAll("[^A-Z ]+", ""), attr);
				}

				@Override
				public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
					super.replace(fb, offset, length, text.toUpperCase().replaceAll("[^A-Z ]+", ""), attrs);
				}
			});
		}

		letters.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent event) {
				updateLetters();
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				updateLetters();
			}

			@Override
			public void changedUpdate(DocumentEvent event) {

			}
		});

		solve.addActionListener(event -> {

			var bestMovesSheet = model.solve();
			var options = new String[] { "OK" };

			showOptionDialog(this, new SimpleSheetView(bestMovesSheet), "Meilleurs coups",
					DEFAULT_OPTION, PLAIN_MESSAGE, null, options, options[0]);
		});
	}

	/**
	 * @since 0.0.0
	 */
	private void updateLetters() {

		try {
			model.change(letters.getText());
		} catch (Exception exception) {
			showException(exception);
		} finally {
			rack.repaint();
		}
	}
}
