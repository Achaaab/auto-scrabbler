package com.github.achaaab.scrabble.model.duplicate;

import com.github.achaaab.scrabble.model.core.Bag;
import com.github.achaaab.scrabble.model.core.Board;
import com.github.achaaab.scrabble.model.core.Rack;

/**
 * Duplicate involves a single player trying to get as close as possible as the top score on
 * every draw.
 *
 * @param board scrabble board
 * @param rack player's rack
 * @param bag scrabble bag
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public record Duplicate(Board board, Rack rack, Bag bag) {

}
