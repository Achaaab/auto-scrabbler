package com.github.achaaab.scrabble.sheet;

import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static com.github.achaaab.scrabble.tools.Alignment.CENTER;
import static java.util.stream.Collectors.joining;

/**
 * Duplicate scoring sheet.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class DuplicateSheet {

	private final List<DuplicateSheetEntry> entries;
	private int total;

	/**
	 * Creates an empty duplicate scoring sheet.
	 *
	 * @since 0.0.0
	 */
	public DuplicateSheet() {

		entries = new ArrayList<>();
		total = 0;
	}

	/**
	 * Adds an entry at the end of this scoring sheet.
	 *
	 * @param entry entry to add
	 * @since 0.0.0
	 */
	public void add(DuplicateSheetEntry entry) {

		entries.add(entry);
		total += entry.score();
	}

	/**
	 * @return sum of each entry score
	 * @since 0.0.0
	 */
	public int total() {
		return total;
	}

	/**
	 * @return number of entries in this scoring sheet
	 * @since 0.0.0
	 */
	public int entryCount() {
		return entries.size();
	}

	/**
	 * Clears this sheet.
	 *
	 * @since 0.0.0
	 */
	public void clear() {

		entries.clear();
		total = 0;
	}

	@Override
	public String toString() {

		var totalString = pad(" " + total + " ", 39, '=', CENTER);

		var entriesString = entries.stream().
				map(DuplicateSheetEntry::toString).
				collect(joining("\n"));

		return totalString + "\n" + entriesString;
	}
}
