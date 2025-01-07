package com.github.achaaab.scrabble.rules;

import com.github.achaaab.scrabble.model.Board;
import com.github.achaaab.scrabble.model.Dictionary;
import com.github.achaaab.scrabble.model.Direction;
import com.github.achaaab.scrabble.model.Rack;
import com.github.achaaab.scrabble.model.Square;
import com.github.achaaab.scrabble.model.Tile;
import com.github.achaaab.scrabble.model.TileCollection;
import com.github.achaaab.scrabble.model.Trie;

import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.model.Dictionary.LETTER_COUNT;
import static com.github.achaaab.scrabble.model.Direction.HORIZONTAL;
import static com.github.achaaab.scrabble.model.Direction.VERTICAL;
import static com.github.achaaab.scrabble.model.Tile.blank;
import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static java.lang.Character.toLowerCase;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Evaluator {

	public static final int SCRABBLE_LENGTH = 7;
	public static final int SCRABBLE_REWARD = 50;

	private final Board board;
	private final Dictionary dictionary;

	private int[] letterCounts;
	private Tile[] tileSamples;
	private List<Tile> tiles;
	private StringBuilder word;
	private List<Move> moves;
	private Direction direction;
	private Square anchorSquare;
	private Square startSquare;
	private Tile currentTile;

	/**
	 * Creates an evaluator.
	 *
	 * @param board board on which to evaluate moves
	 * @param dictionary dictionary to use
	 * @since 0.0.0
	 */
	public Evaluator(Board board, Dictionary dictionary) {

		this.board = board;
		this.dictionary = dictionary;
	}

	/**
	 * Creates a move from specified parameters.
	 *
	 * @param square starting square
	 * @param direction layout direction
	 * @param word word to play
	 * @param tileCollection tile collection from which to pick tiles to complete the word on the board
	 * @return created move
	 * @throws IllegalArgumentException if the given word do not fit at the given square and direction
	 * @since 0.0.2
	 */
	public Move getMove(Square square, Direction direction, String word, TileCollection tileCollection) {

		this.direction = direction;

		startSquare = square;
		tiles = new ArrayList<>();

		var letters = word.toCharArray();
		var reference = board.getReference(startSquare, direction);

		for (var letter : letters) {

			if (square == null) {
				throw new IllegalArgumentException(getMessage("word_not_fitting", word, reference));
			} else if (square.isEmpty()) {
				tiles.add(tileCollection.pick(letter));
			}

			square = square.getNext(direction);
		}

		return new Move(
				reference,
				word,
				new ArrayList<>(tiles),
				getScore());
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public int getScore() {

		var score = 0;
		var square = startSquare;
		var wordScore = 0;
		var wordMultiplier = 1;

		for (var tile : tiles) {

			while (square.hasTile()) {

				var squareTile = square.getTile();

				square = square.getNext(direction);
				wordScore += squareTile.value();
			}

			var award = square.award();

			var letterMultiplier = switch (award) {

				case LETTER_DOUBLE -> 2;
				case LETTER_TRIPLE -> 3;
				default -> 1;
			};

			switch (award) {

				case WORD_DOUBLE -> wordMultiplier *= 2;
				case WORD_TRIPLE -> wordMultiplier *= 3;
			}

			wordScore += letterMultiplier * tile.value();

			var prefixTiles = board.getPreviousTiles(square, direction.across());
			var suffixTiles = board.getNextTiles(square, direction.across());

			if (!prefixTiles.isEmpty() || !suffixTiles.isEmpty()) {

				var acrossWordMultiplier = switch (award) {

					case WORD_DOUBLE -> 2;
					case WORD_TRIPLE -> 3;
					default -> 1;
				};

				var crossWordScore = acrossWordMultiplier * (
						prefixTiles.stream().mapToInt(Tile::value).sum() +
								letterMultiplier * tile.value() +
								suffixTiles.stream().mapToInt(Tile::value).sum());

				score += crossWordScore;
			}

			square = square.getNext(direction);
		}

		while (square != null && square.hasTile()) {

			wordScore += square.getTile().value();
			square = square.getNext(direction);
		}

		wordScore *= wordMultiplier;
		score += wordScore;

		if (tiles.size() == SCRABBLE_LENGTH) {
			score += SCRABBLE_REWARD;
		}

		return score;
	}

	/**
	 * @param rack
	 * @return
	 * @since 0.0.0
	 */
	public List<Move> getMoves(Rack rack) {

		var trie = dictionary.getTrie();
		letterCounts = rack.getLetterCounts();
		tileSamples = rack.getTileSamples();
		tiles = new ArrayList<>();
		word = new StringBuilder();
		moves = new ArrayList<>();

		for (var square : board) {

			if (square.isAnchor()) {

				anchorSquare = square;

				direction = HORIZONTAL;

				if (square.hasPreviousTile(direction)) {
					prefixFromBoard(trie);
				} else {
					prefixFromRack(trie, anchorSquare);
				}

				direction = VERTICAL;

				if (square.hasPreviousTile(direction)) {
					prefixFromBoard(trie);
				} else {
					prefixFromRack(trie, anchorSquare);
				}
			}
		}

		return moves;
	}

	/**
	 * @param trie
	 * @since 0.0.0
	 */
	private void prefixFromBoard(Trie trie) {

		var square = anchorSquare;

		while (square.hasPreviousTile(direction)) {
			square = square.getPrevious(direction);
		}

		startSquare = square;

		while (square != anchorSquare) {

			var tile = square.getTile();
			var letter = tile.letter();

			trie = trie.getChild(letter);
			word.append(letter);

			square = square.getNext(direction);
		}

		suffixFromRack(trie, square);

		word.delete(0, word.length());
	}

	/**
	 * @param trie
	 * @param square
	 * @since 0.0.0
	 */
	private void prefixFromRack(Trie trie, Square square) {

		startSquare = square;
		suffixFromRack(trie, anchorSquare);

		square = square.getPrevious(direction);

		if (square != null && square.isEmpty() && !square.isAnchor()) {

			var children = trie.getChildren();

			for (var index = 0; index < LETTER_COUNT; index++) {

				var child = children[index];

				if (child != null) {

					var letter = (char) ('A' + index);

					if (letterCounts[index] > 0) {
						prefixFromRack(child, square, tileSamples[index]);
					}

					if (letterCounts[LETTER_COUNT] > 0) {
						prefixFromRack(child, square, blank(letter));
					}
				}
			}
		}
	}

	/**
	 * @param trie
	 * @param square
	 * @param tile
	 * @since 0.0.0
	 */
	private void prefixFromRack(Trie trie, Square square, Tile tile) {

		currentTile = tile;

		var value = tile.value();

		var letter = value == 0 ?
				toLowerCase(tile.letter()) :
				tile.letter();

		var index = value == 0 ?
				LETTER_COUNT :
				letter - 'A';

		letterCounts[index]--;
		tiles.addLast(tile);
		word.append(letter);

		prefixFromRack(trie, square);

		word.deleteCharAt(word.length() - 1);
		tiles.removeLast();
		letterCounts[index]++;
	}

	/**
	 * Suffixes the current word with the current letter from the board.
	 *
	 * @param trie
	 * @param square
	 * @since 0.0.0
	 */
	private void suffixFromBoard(Trie trie, Square square) {

		var tile = square.getTile();
		var child = trie.getChild(tile);

		if (child != null) {

			word.append(tile.letter());

			if (square.hasNext(direction)) {

				square = square.getNext(direction);

				if (square.isEmpty()) {

					checkCurrentWord(child);
					suffixFromRack(child, square);

				} else {

					suffixFromBoard(child, square);
				}

			} else {

				checkCurrentWord(child);
			}

			word.deleteCharAt(word.length() - 1);
		}
	}

	/**
	 * Suffixes the word with a letter from the rack.
	 *
	 * @param trie
	 * @param square
	 * @since 0.0.0
	 */
	private void suffixFromRack(Trie trie, Square square) {

		var children = trie.getChildren();

		for (var index = 0; index < LETTER_COUNT; index++) {

			var child = children[index];

			if (child != null) {

				var letter = (char) ('A' + index);

				if (letterCounts[index] > 0) {
					suffixFromRack(child, square, tileSamples[index]);
				}

				if (letterCounts[LETTER_COUNT] > 0) {
					suffixFromRack(child, square, blank(letter));
				}
			}
		}
	}

	/**
	 * @param trie
	 * @param square
	 * @param tile
	 * @since 0.0.0
	 */
	private void suffixFromRack(Trie trie, Square square, Tile tile) {

		currentTile = tile;

		var value = tile.value();

		var letter = value == 0 ?
				toLowerCase(tile.letter()) :
				tile.letter();

		var index = value == 0 ?
				LETTER_COUNT :
				letter - 'A';

		if (checkAcrossWord(square)) {

			letterCounts[index]--;
			tiles.addLast(currentTile);
			word.append(letter);

			if (square.hasNext(direction)) {

				square = square.getNext(direction);

				if (square.isEmpty()) {

					checkCurrentWord(trie);
					suffixFromRack(trie, square);

				} else {

					suffixFromBoard(trie, square);
				}

			} else {

				checkCurrentWord(trie);
			}

			letterCounts[index]++;
			tiles.removeLast();
			word.deleteCharAt(word.length() - 1);
		}
	}

	/**
	 * Adds the score of a formed across word.
	 *
	 * @param square
	 * @return {@code false} if there is an invalid across word, {@code true} otherwise
	 * @since 0.0.0
	 */
	private boolean checkAcrossWord(Square square) {

		var valid = true;

		var acrossDirection = direction.across();

		if (square.hasPreviousTile(acrossDirection) ||
				square.hasNextTile(acrossDirection)) {

			var previousTiles = board.getPreviousTiles(square, acrossDirection);

			var acrossTrie = dictionary.getTrie();

			acrossTrie = acrossTrie.getChild(previousTiles);
			acrossTrie = acrossTrie.getChild(currentTile);

			if (acrossTrie == null) {

				valid = false;

			} else {

				var nextTiles = board.getNextTiles(square, acrossDirection);
				var nextTileIterator = nextTiles.iterator();

				while (valid && nextTileIterator.hasNext()) {

					var nextTile = nextTileIterator.next();

					acrossTrie = acrossTrie.getChild(nextTile);

					if (acrossTrie == null) {
						valid = false;
					}
				}

				valid = valid && acrossTrie.isWord();
			}
		}

		return valid;
	}

	/**
	 * Checks if the current trie is terminal. If it is, adds the current move.
	 *
	 * @param trie
	 * @since 0.0.0
	 */
	private void checkCurrentWord(Trie trie) {

		if (trie.isWord()) {

			var move = new Move(
					board.getReference(startSquare, direction),
					word.toString(),
					new ArrayList<>(tiles),
					getScore());

			moves.add(move);
		}
	}
}
