package com.github.achaaab.scrabble.tools;

public class StringUtilities {

	public static String pad(String string, int length, char paddingCharacter, TextHorizontalAlignment alignment) {

		String paddedString;

		var stringLength = string.length();

		if (stringLength < length) {

			var paddingString = Character.toString(paddingCharacter);

			var leftPaddingLength = switch (alignment) {

				case LEFT -> 0;
				case CENTER -> (length - stringLength) / 2;
				case RIGHT -> length - stringLength;
			};

			var rightPaddingLength = length - stringLength - leftPaddingLength;

			paddedString = paddingString.repeat(leftPaddingLength) +
					string +
					paddingString.repeat(rightPaddingLength);

		} else if (stringLength == length) {

			paddedString = string;

		} else {

			paddedString = string.substring(0, length);
		}

		return paddedString;
	}
}
