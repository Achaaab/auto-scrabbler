package com.github.achaaab.scrabble.view;

import com.github.achaaab.scrabble.model.Duplicate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.Box.createVerticalGlue;
import static javax.swing.BoxLayout.PAGE_AXIS;
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

		var southPanel = new Box(Y_AXIS);
		southPanel.setAlignmentX(CENTER_ALIGNMENT);
		southPanel.add(createVerticalGlue());
		southPanel.add(rack);
		southPanel.add(createVerticalGlue());
		southPanel.setMaximumSize(RackView.SIZE);
		southPanel.setMinimumSize(RackView.SIZE);

		add(board, CENTER);
		add(southPanel, SOUTH);
	}
}
