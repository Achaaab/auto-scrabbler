package com.github.achaaab.scrabble;

import com.github.achaaab.scrabble.model.Bag;
import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.rules.Evaluator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.achaaab.scrabble.model.Dictionary.FRENCH_ODS9;
import static com.github.achaaab.scrabble.rules.Evaluator.getWord;
import static com.github.achaaab.scrabble.model.Tile.getFrenchTiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests of {@link Board}.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
class TestBoard {

	@Test
	void testBeezMe() {

		var board = new Board();
		var evaluator = new Evaluator(board, FRENCH_ODS9);

		var tiles = List.of(
				new Tile('B', 3),
				new Tile('E', 1),
				new Tile('E', 1),
				new Tile('Z', 10));

		board.play(tiles, "4K");

		tiles = List.of(
				new Tile('M', 2));

		var score = evaluator.getScore(tiles, "13C");
		assertEquals(6, score);
	}

	@Test
	void testFullDuplicate() {

		var board = new Board();
		var rack = new Rack();
		var bag = new Bag();

		bag.addAll(getFrenchTiles());

		rack.addAll(bag.pickAll('I', 'U', 'U', 'M', 'S', 'E', 'M'));
		var tiles = rack.pickAll('M', 'U', 'S', 'E', 'U', 'M');
		board.play(tiles, "H7");

		rack.addAll(bag.pickAll('D', 'T', 'C', 'N', 'E', 'V'));
		tiles = rack.pickAll('V', 'I', 'D', 'E', 'N', 'T');
		board.play(tiles, "I6");

		bag.add(rack.pick('C'));
		rack.addAll(bag.pickAll('H', 'O', 'A', 'S', 'G', 'B', 'U'));
		tiles = rack.pickAll('B', 'A', 'G', 'O', 'U', 'S');
		board.play(tiles, "13C");

		bag.add(rack.pick('H'));
		rack.addAll(bag.pickAll('I', 'E', 'O', 'P', 'L', 'S', 'R'));
		tiles = rack.pickAll('P', 'R', 'O', 'I', 'E', 'L', 'S');
		board.play(tiles, "I3");

		rack.addAll(bag.pickAll('E', 'R', 'A', 'A', 'S', 'I', 'L'));
		tiles = rack.pickAll('S', 'A', 'L', 'A', 'I', 'R', 'E');
		board.play(tiles, "11E");

		rack.addAll(bag.pickAll('B', 'U', 'I', 'W', 'N', 'L', 'G'));
		tiles = rack.pickAll('B', 'U', 'N', 'G', 'L', 'W');
		board.play(tiles, "F7");

		bag.add(rack.pick('I'));
		rack.addAll(bag.pickAll('A', 'Y', 'C', 'E', 'F', 'T', 'S'));
		tiles = rack.pickAll('E', 'C', 'S', 'T', 'A', 'Y');
		board.play(tiles, "15D");

		bag.add(rack.pick('F'));
		rack.addAll(bag.pickAll('S', 'O', 'L', 'C', 'I', 'E', 'L'));
		tiles = rack.pickAll('C', 'O', 'L', 'L', 'E', 'I', 'S');
		board.play(tiles, "10A");

		rack.addAll(bag.pickAll('D', 'A', 'U', 'U', 'T', 'R', 'O'));
		tiles = rack.pickAll('T', 'R', 'O', 'U', 'D', 'U');
		board.play(tiles, "A4");

		bag.add(rack.pick('A'));
		rack.addAll(bag.pickAll('T', 'I', 'N', 'E', 'E', 'E', 'R'));
		tiles = rack.pickAll('E', 'R', 'E', 'I', 'N', 'T', 'E');
		board.play(tiles, "B2");

		rack.addAll(bag.pickAll('F', 'A', 'R', 'A', 'I', 'P', 'K'));
		tiles = rack.pickAll('P', 'A', 'R', 'I', 'K', 'A');
		board.play(tiles, "3G");

		bag.add(rack.pick('F'));
		rack.addAll(bag.pickAll('T', 'R', 'A', ' ', 'E', 'E', 'A'));
		tiles = rack.pickAll(' ', 'A', 'T', 'E', 'R', 'A', 'E');
		board.play(tiles, "C4");

		rack.addAll(bag.pickAll('N', 'E', 'I', 'I', 'D', 'E', 'V'));
		tiles = rack.pickAll('I', 'E', 'V', 'I', 'E', 'N');
		board.play(tiles, "L3");

		rack.addAll(bag.pickAll('M', 'O', 'A', 'T', 'E', 'N'));
		tiles = rack.pickAll('A', 'M', 'E', 'N', 'T', 'O');
		board.play(tiles, "14I");

		rack.addAll(bag.pickAll('O', 'E', 'Q', ' ', 'N', 'X'));
		tiles = rack.pickAll(' ', 'N', 'O', 'X');
		board.play(tiles, "12L");

		rack.addAll(bag.pickAll('F', 'F', 'Z', 'J'));
		tiles = rack.pickAll('J', 'Z', 'E');
		board.play(tiles, "5K");
	}

	@Test
	void testPlayHorizontal() {

		var tiles = List.of(
				new Tile('E', 1),
				new Tile('L', 1),
				new Tile('A', 1),
				new Tile('N', 1),
				new Tile('D', 2));

		var board = new Board();
		board.play(tiles, "H8");

		tiles = List.of(
				new Tile('N', 1),
				new Tile('E', 1),
				new Tile('O', 1),
				new Tile('Z', 10),
				new Tile('A', 1),
				new Tile('I', 1),
				new Tile('S', 1));

		board.play(tiles, "8D");

		assertEquals("", getWord(board.getTiles("8C")));
		assertEquals("NEOZELANDAIS", getWord(board.getTiles("8D")));
		assertEquals("NEOZELANDAIS", getWord(board.getTiles("8O")));
	}

	@Test
	void testPlayVertical() {

		var tiles = List.of(
				new Tile('B', 3),
				new Tile('O', 1),
				new Tile('N', 1),
				new Tile('D', 2),
				new Tile('E', 1));

		var board = new Board();
		board.play(tiles, "8D");

		tiles = List.of(
				new Tile('A', 1),
				new Tile('I', 1),
				new Tile('E', 1),
				new Tile('N', 1),
				new Tile('T', 1));

		board.play(tiles, "8J");

		tiles = List.of(
				new Tile('A', 1),
				new Tile('R', 1));

		board.play(tiles, "8C");

		assertEquals("", getWord(board.getTiles("8B")));
		assertEquals("ABONDERAIENT", getWord(board.getTiles("8C")));
		assertEquals("ABONDERAIENT", getWord(board.getTiles("8N")));
		assertEquals("", getWord(board.getTiles("8O")));
	}

	@Test
	void testGetScore() {

		var board = new Board();
		var evaluator = new Evaluator(board, FRENCH_ODS9);

		var tiles = List.of(
				new Tile('T', 1),
				new Tile('U', 1),
				new Tile('Y', 10),
				new Tile('A', 1),
				new Tile('U', 1));

		board.play(tiles, "H8");

		tiles = List.of(
				new Tile('E', 1),
				new Tile('L', 1),
				new Tile('A', 1),
				new Tile('N', 1),
				new Tile('D', 2));

		assertEquals(28, evaluator.getScore(tiles, "G8"));
	}
}
