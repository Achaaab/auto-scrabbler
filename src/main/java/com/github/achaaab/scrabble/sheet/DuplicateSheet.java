package com.github.achaaab.scrabble.sheet;

import java.util.ArrayList;
import java.util.List;

import static com.github.achaaab.scrabble.tools.StringUtilities.pad;
import static com.github.achaaab.scrabble.tools.TextHorizontalAlignment.CENTER;
import static java.util.stream.Collectors.joining;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class DuplicateSheet {

	private final List<DuplicateSheetEntry> entries;
	private int total;

	/**
	 * @since 0.0.0
	 */
	public DuplicateSheet() {

		entries = new ArrayList<>();
		total = 0;
	}

	/**
	 * @param entry
	 * @since 0.0.0
	 */
	public void add(DuplicateSheetEntry entry) {

		entries.add(entry);
		total += entry.score();
	}

	/**
	 * @return
	 * @since 0.0.0
	 */
	public int total() {
		return total;
	}

	/**
	 * @return
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
