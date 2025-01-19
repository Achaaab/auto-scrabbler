package com.github.achaaab.scrabble.model.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.github.achaaab.scrabble.model.core.Tile.BLANK;
import static com.github.achaaab.scrabble.tools.MessageBundle.getMessage;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.toUpperCase;

/**
 * Scrabble tile collection.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public abstract class TileCollection {

	protected final List<Tile> tiles;

	/**
	 * Creates an empty tile collection.
	 *
	 * @since 0.0.0
	 */
	public TileCollection() {
		tiles = new LinkedList<>();
	}

	/**
	 * Clears this collection, removing all tiles contained in it.
	 *
	 * @since 0.0.0
	 */
	public void clear() {
		tiles.clear();
	}

	/**
	 * Adds a tile to this collection.
	 *
	 * @param tile tile to add
	 * @since 0.0.0
	 */
	public void add(Tile tile) {
		tiles.add(tile);
	}

	/**
	 * Adds given tiles to this collection.
	 *
	 * @param tiles tiles to add
	 * @since 0.0.0
	 */
	public void addAll(Collection<Tile> tiles) {
		this.tiles.addAll(tiles);
	}

	/**
	 * Removes given tiles from this collection.
	 *
	 * @param tiles tiles to remove
	 * @since 0.0.0
	 */
	public void removeAll(Collection<Tile> tiles) {
		tiles.forEach(this::remove);
	}

	/**
	 * Removes the given tile from this collection.
	 *
	 * @param tile tile to remove
	 * @since 0.0.0
	 */
	public void remove(Tile tile) {

		tiles.remove(tile.isBlank() ?
				getFirstBlank() :
				tile);
	}

	/**
	 * Returns the backing list of tiles in this collection.
	 *
	 * @return tiles in this collection
	 * @since 0.0.0
	 */
	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Determines if this tile collection is empty.
	 *
	 * @return whether this collection is empty
	 * @since 0.0.0
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	/**
	 * Determines if this collection has a limited capacity and has reached it.
	 *
	 * @return whether this collection of tiles has a maximum capacity that is reached
	 * @since 0.0.0
	 */
	public boolean isFull() {
		return false;
	}

	/**
	 * Returns the number of tiles contained in this collection.
	 *
	 * @return number of tiles in this collection
	 * @since 0.0.0
	 */
	public int size() {
		return tiles.size();
	}

	/**
	 * Picks a tile in this collection, matching the given letter.
	 * <ul>
	 *   <li>If the given letter is a whitespace, returns the first found blank tile.</li>
	 *   <li>If the given letter is a lower case letter, removes the first blank tile then creates and returns
	 *   a new tile with the given letter and a zero value.</li>
	 *   <li>If the given letter is an upper case letter, returns the first tile with the given letter.</li>
	 * </ul>
	 *
	 * @param letter letter to match
	 * @return found or created tile
	 * @throws NoSuchElementException if no tile in this collection matches the given letter
	 * @since 0.0.0
	 */
	public Tile pick(char letter) {

		Tile tile;

		if (letter == BLANK){

			tile = getFirstBlank();
			tiles.remove(tile);

		} else if (isLowerCase(letter)) {

			tiles.remove(getFirstBlank());
			tile = new Tile(toUpperCase(letter), 0);

		} else {

			tile = getFirst(letter);
			tiles.remove(tile);
		}

		return tile;
	}

	/**
	 * Picks tiles from this collection matching the given letters. If at least 1 of the letter was not matched by any
	 * tile, put back already found tiles in this collection and throws an exception.
	 *
	 * @param letters letters to match
	 * @return found tiles, 1 per given letter
	 * @throws NoSuchElementException if at least 1 letter was not matched by any tile
	 * @since 0.0.0
	 */
	public List<Tile> pickAll(char... letters) {

		var tiles = new ArrayList<Tile>();

		try {

			for (var letter : letters) {
				tiles.add(pick(letter));
			}

			return tiles;

		} catch (NoSuchElementException noSuchElementException) {

			addAll(tiles);
			throw noSuchElementException;
		}
	}

	/**
	 * Picks tiles from this collection matching the given letters. If at least 1 of the letter was not matched by any
	 * tile, put back already found tiles in this collection and throws an exception.
	 *
	 * @param letters letters to match
	 * @return found tiles, 1 per given letter
	 * @throws NoSuchElementException if at least 1 letter was not matched by any tile
	 * @since 0.0.0
	 */
	public List<Tile> pickAll(String letters) {
		return pickAll(letters.toCharArray());
	}

	/**
	 * Finds the first blank tile in this collection.
	 *
	 * @return found blank tile
	 * @throws NoSuchElementException if there is no blank tiles in this collection
	 * @since 0.0.0
	 */
	public Tile getFirstBlank() {

		return tiles.stream().
				filter(Tile::isBlank).
				findFirst().
				orElseThrow(() -> new NoSuchElementException(getMessage("no_more_blank")));
	}

	/**
	 * Finds the first tile with the specified letter.
	 *
	 * @param letter letter to match
	 * @return found tile
	 * @throws NoSuchElementException if there is no more tile with the specified letter in this collection
	 * @since 0.0.0
	 */
	public Tile getFirst(char letter) {

		return tiles.stream().
				filter(tile -> tile.letter() == letter).
				findFirst().
				orElseThrow(() -> new NoSuchElementException(getMessage("no_more_letter", letter)));
	}

	/**
	 * Computes the number of tiles with a vowel in this collection.
	 *
	 * @return vowel count
	 * @since 0.0.0
	 */
	public long getVowelCount() {
		return tiles.stream().filter(Tile::isVowel).count();
	}

	/**
	 * Computes the number of tiles with a consonant letter in this collection.
	 *
	 * @return consonant count
	 * @since 0.0.0
	 */
	public long getConsonantCount() {
		return tiles.stream().filter(Tile::isConsonant).count();
	}
}
