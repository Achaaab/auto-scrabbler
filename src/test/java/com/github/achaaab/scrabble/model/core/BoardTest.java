package com.github.achaaab.scrabble.model.core;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static com.github.achaaab.scrabble.model.core.Award.LETTER_DOUBLE;
import static com.github.achaaab.scrabble.model.core.Award.LETTER_TRIPLE;
import static com.github.achaaab.scrabble.model.core.Award.NONE;
import static com.github.achaaab.scrabble.model.core.Award.WORD_DOUBLE;
import static com.github.achaaab.scrabble.model.core.Award.WORD_TRIPLE;
import static com.github.achaaab.scrabble.model.core.Board.SIZE;
import static com.github.achaaab.scrabble.model.core.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.core.Direction.VERTICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of {@link Board}.
 *
 * @author Jonathan Guéhenneux
 * @since 1.0.1
 */
class BoardTest {

	private static final Random RANDOM = new Random();

	@Test
	void getAward() {

		assertEquals(NONE, Board.getAward("A2"));
		assertEquals(LETTER_DOUBLE, Board.getAward("A4"));
		assertEquals(LETTER_TRIPLE, Board.getAward("B6"));
		assertEquals(WORD_DOUBLE, Board.getAward("B2"));
		assertEquals(WORD_TRIPLE, Board.getAward("A1"));
	}

	@Test
	void getSquareKo() {

		var board = new Board();

		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(-1, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(-1, 0));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(-1, SIZE));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(0, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(0, SIZE));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(SIZE, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(SIZE, 0));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getSquare(SIZE, SIZE));
	}

	@RepeatedTest(16)
	void getSquareOk() {

		var board = new Board();

		var column = RANDOM.nextInt(SIZE);
		var row = RANDOM.nextInt(SIZE);

		var square = board.getSquare(column, row);
		assertEquals(column, square.column());
		assertEquals(row, square.row());
	}

	@Test
	void getReferenceKey() {

		var board = new Board();

		var reference = board.getReference("A1");
		var square = board.getSquare(0, 0);
		assertEquals(square, reference.square());
		assertEquals(HORIZONTAL, reference.direction());

		reference = board.getReference("A15");
		square = board.getSquare(14, 0);
		assertEquals(square, reference.square());
		assertEquals(HORIZONTAL, reference.direction());

		reference = board.getReference("8H");
		square = board.getSquare(7, 7);
		assertEquals(square, reference.square());
		assertEquals(VERTICAL, reference.direction());

		assertNull(board.getReference(null));
		assertNull(board.getReference(""));
		assertNull(board.getReference("A16"));
	}

	@RepeatedTest(16)
	void getReference() {

		var board = new Board();

		var square = board.getSquare(RANDOM.nextInt(SIZE), RANDOM.nextInt(SIZE));
		var direction = RANDOM.nextBoolean() ? HORIZONTAL : VERTICAL;

		var reference = board.getReference(square, direction);

		assertEquals(square, reference.square());
		assertEquals(direction, reference.direction());
	}

	@Test
	void playHorizontally() {

		var board = new Board();

		var tile0 = new Tile('F', 4);
		var tile1 = new Tile('O', 1);
		var tile2 = new Tile('O', 1);
		var tiles = List.of(tile0, tile1, tile2);
		var reference = board.getReference("H8");
		board.play(tiles, reference);
		assertEquals(tile0, board.getSquare(7, 7).getTile());
		assertEquals(tile1, board.getSquare(8, 7).getTile());
		assertEquals(tile2, board.getSquare(9, 7).getTile());

		var tile3 = new Tile('B', 3);
		var tile4 = new Tile('A', 1);
		tiles = List.of(tile3, tile4);
		reference = board.getReference("H12");
		board.play(tiles, reference);
		assertEquals(tile3, board.getSquare(11, 7).getTile());
		assertEquals(tile4, board.getSquare(12, 7).getTile());

		var tile5 = new Tile('R', 1);
		tiles = List.of(tile5);
		reference = board.getReference("H7");
		board.play(tiles, reference);
		assertEquals(tile5, board.getSquare(6, 7).getTile());

		var tile6 = new Tile('B', 3);
		tiles = List.of(tile6);
		reference = board.getReference("H11");
		board.play(tiles, reference);
		assertEquals(tile6, board.getSquare(10, 7).getTile());

		var tile7 = new Tile('A', 1);
		var tile8 = new Tile('Z', 10);
		tiles = List.of(tile7, tile8);
		reference = board.getReference("H6");
		board.play(tiles, reference);
		assertEquals(tile7, board.getSquare(5, 7).getTile());
		assertEquals(tile8, board.getSquare(13, 7).getTile());

		var tile9 = new Tile('N', 1);
		tiles = List.of(tile9);
		reference = board.getReference("H6");
		board.play(tiles, reference);
		assertEquals(tile9, board.getSquare(14, 7).getTile());

		var tile10 = new Tile('I', 1);
		var tile11 = new Tile('N', 1);
		var tilesError = List.of(tile10, tile11);
		var referenceError = board.getReference("H5");
		assertThrows(IllegalArgumentException.class, () -> board.play(tilesError, referenceError));
		assertEquals(tile10, board.getSquare(4, 7).getTile());
	}


	@Test
	void playVertically() {

		var board = new Board();

		var tile0 = new Tile('F', 4);
		var tile1 = new Tile('O', 1);
		var tile2 = new Tile('O', 1);
		var tiles = List.of(tile0, tile1, tile2);
		var reference = board.getReference("8H");
		board.play(tiles, reference);
		assertEquals(tile0, board.getSquare(7, 7).getTile());
		assertEquals(tile1, board.getSquare(7, 8).getTile());
		assertEquals(tile2, board.getSquare(7, 9).getTile());

		var tile3 = new Tile('B', 3);
		var tile4 = new Tile('A', 1);
		tiles = List.of(tile3, tile4);
		reference = board.getReference("8L");
		board.play(tiles, reference);
		assertEquals(tile3, board.getSquare(7, 11).getTile());
		assertEquals(tile4, board.getSquare(7, 12).getTile());

		var tile5 = new Tile('R', 1);
		tiles = List.of(tile5);
		reference = board.getReference("8G");
		board.play(tiles, reference);
		assertEquals(tile5, board.getSquare(7, 6).getTile());

		var tile6 = new Tile('B', 3);
		tiles = List.of(tile6);
		reference = board.getReference("8K");
		board.play(tiles, reference);
		assertEquals(tile6, board.getSquare(7, 10).getTile());

		var tile7 = new Tile('A', 1);
		var tile8 = new Tile('Z', 10);
		tiles = List.of(tile7, tile8);
		reference = board.getReference("8F");
		board.play(tiles, reference);
		assertEquals(tile7, board.getSquare(7, 5).getTile());
		assertEquals(tile8, board.getSquare(7, 13).getTile());

		var tile9 = new Tile('N', 1);
		tiles = List.of(tile9);
		reference = board.getReference("8O");
		board.play(tiles, reference);
		assertEquals(tile9, board.getSquare(7, 14).getTile());

		var tile10 = new Tile('I', 1);
		var tile11 = new Tile('N', 1);
		var tilesError = List.of(tile10, tile11);
		var referenceError = board.getReference("8E");
		assertThrows(IllegalArgumentException.class, () -> board.play(tilesError, referenceError));
		assertEquals(tile10, board.getSquare(7, 4).getTile());
	}
}
