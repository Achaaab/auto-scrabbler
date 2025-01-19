package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.duplicate.Duplicate;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.BoxLayout.Y_AXIS;

public class DuplicateView extends JPanel {

	private final Duplicate model;

	private final BoardView board;
	private final RackView rack;

	public DuplicateView(Duplicate model) {

		this.model = model;

		board = new BoardView(model.board());
		rack = new RackView(model.rack());

		setLayout(new BorderLayout());

		var rackPanel = new Box(Y_AXIS);
		rackPanel.setAlignmentX(CENTER_ALIGNMENT);
		rackPanel.add(rack);

		add(board, CENTER);
		add(rackPanel, SOUTH);
	}
}
