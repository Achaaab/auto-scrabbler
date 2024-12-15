package com.github.achaaab.scrabble.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.github.achaaab.scrabble.model.Tile.BLANK;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.toUpperCase;

/**
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
	 * @return tiles in this collection
	 * @since 0.0.0
	 */
	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * @return whether this collection is empty
	 * @since 0.0.0
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	/**
	 * @return whether this collection of tiles has a maximum capacity that is reached
	 * @since 0.0.0
	 */
	public boolean isFull() {
		return false;
	}

	/**
	 * @return number of tiles in this collection
	 * @since 0.0.0
	 */
	public int size() {
		return tiles.size();
	}

	/**
	 * @param letter
	 * @return
	 * @since 0.0.0
	 */
	public Tile pick(char letter) {

		Tile tile;

		if (isLowerCase(letter)) {

			tiles.remove(getFirstBlank());
			tile = new Tile(toUpperCase(letter), 0);

		} else if (letter == BLANK){

			tile = getFirstBlank();
			tiles.remove(tile);

		} else {

			tile = getFirst(letter);
			tiles.remove(tile);
		}

		return tile;
	}

	/**
	 * @param letters
	 * @return
	 * @throws NoSuchElementException
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
	 * @param letters
	 * @return
	 * @since 0.0.0
	 */
	public List<Tile> pickAll(String letters) {
		return pickAll(letters.toCharArray());
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public Tile getFirstBlank() {

		return tiles.stream().
				filter(Tile::isBlank).
				findFirst().
				orElseThrow(() -> new NoSuchElementException("There is no more blank tile in the bag."));
	}

	/**
	 * @param letter
	 * @return
	 * @throws NoSuchElementException if there is no more tile with the specified letter in this collection
	 * @since 0.0.0
	 */
	public Tile getFirst(char letter) {

		return tiles.stream().
				filter(tile -> tile.letter() == letter).
				findFirst().
				orElseThrow(() -> new NoSuchElementException("There is no more " + letter + " tile in the bag."));
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public long getVowelCount() {
		return tiles.stream().filter(Tile::isVowel).count();
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public long getConsonnantCount() {
		return tiles.stream().filter(Tile::isConsonnant).count();
	}
}
