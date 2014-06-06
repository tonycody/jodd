// Copyright (c) 2003-2014, Jodd Team (jodd.org). All Rights Reserved.

package jodd.lagarto;

import jodd.util.CharUtil;

import java.nio.CharBuffer;

/**
 * Scanner over a char buffer.
 */
public class CharScanner {

	protected char[] input;
	protected int ndx = 0;
	protected int total;

	/**
	 * Initializes scanner.
	 */
	protected void initialize(char[] input) {
		this.input = input;
		this.ndx = -1;
		this.total = input.length;
	}

	// ---------------------------------------------------------------- find

	/**
	 * Finds a character in some range and returns its index.
	 * Returns <code>-1</code> if character is not found.
	 */
	protected final int find(char target, int from, int end) {
		int index = from;

		while (index < end) {
			char c = input[index];

			if (c == target) {
				break;
			}
			index++;
		}

		if (index == end) {
			return -1;
		}

		return index;
	}

	/**
	 * Finds character buffer in some range and returns its index.
	 * Returns <code>-1</code> if character is not found.
	 */
	protected final int find(char[] target, int from, int end) {
		int index = from;

		while (index < end) {
			if (match(target, index)) {
				break;
			}
			index++;
		}

		if (index == end) {
			return -1;
		}

		return index;
	}


	// ---------------------------------------------------------------- match

	/**
	 * Matches char buffer with content on given location.
	 */
	protected final boolean match(char[] target, int ndx) {
		if (ndx + target.length >= total) {
			return false;
		}

		int j = ndx;

		for (int i = 0; i < target.length; i++, j++) {
			if (input[j] != target[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Matches char buffer with content at current location.
	 */
	public final boolean match(char[] target) {
		return match(target, ndx);
	}

	/**
	 * todo ovaj method pretrvara char u upper case i radi match sa targetom koji je vec u uppercase zbog performansi
	 */
	public final boolean matchCaseInsensitiveWithUpper(char[] uppercaseTarget) {
		if (ndx + uppercaseTarget.length > total) {
			return false;
		}

		int j = ndx;

		for (int i = 0; i < uppercaseTarget.length; i++, j++) {
			char c = CharUtil.toUpperAscii(input[j]);

			if (c != uppercaseTarget[i]) {
				return false;
			}
		}

		return true;
	}

	// ---------------------------------------------------------------- char sequences

	/**
	 * Creates char sub-sequence from the input.
	 */
	protected final CharSequence charSequence(int from, int to) {
		int len = to - from;
		if (len == 0) {
			return EMPTY_CHAR_BUFFER;
		}
		return CharBuffer.wrap(input, from, len);
	}

	protected static CharBuffer EMPTY_CHAR_BUFFER = CharBuffer.wrap(new char[0]);

	/**
	 * Creates substring from the input.
	 */
	protected final String substring(int from, int to) {
		return new String(input, from, to - from);
	}

	// ---------------------------------------------------------------- position

	private int lastOffset = -1;
	private int lastLine;
	private int lastLastNewLineOffset;

	/**
	 * Returns <code>true</code> if EOF.
	 */
	protected final boolean isEOF() {
		return ndx >= total;
	}

	/**
	 * Returns current {@link jodd.lagarto.CharScanner.Position}.
	 */
	protected Position position() {
		return position(ndx);
	}

	/**
	 * Calculates {@link Position current position}: offset, line and column.
	 */
	protected Position position(int position) {
		int line;
		int offset;
		int lastNewLineOffset;

		if (position > lastOffset) {
			line = 1;
			offset = 0;
			lastNewLineOffset = 0;
		} else {
			line = lastLine;
			offset = lastOffset;
			lastNewLineOffset = lastLastNewLineOffset;
		}

		while (offset < position) {
			char c = input[offset];

			if (c == '\n') {
				line++;
				lastNewLineOffset = offset + 1;
			}

			offset++;
		}

		lastOffset = offset;
		lastLine = line;
		lastLastNewLineOffset = lastNewLineOffset;

		return new Position(position, line, position - lastNewLineOffset + 1);
	}

	/**
	 * Current position.
	 */
	public static class Position {

		private final int offset;
		private final int line;
		private final int column;

		public Position(int offset, int line, int column) {
			this.offset = offset;
			this.line = line;
			this.column = column;
		}

		public Position(int offset) {
			this.offset = offset;
			this.line = -1;
			this.column = -1;
		}

		public String toString() {
			if (offset == -1) {
				return "[" + line + ':' + column + ']';
			}
			if (line == -1) {
				return "[@" + offset + ']';
			}
			return "[" + line + ':' + column + " @" + offset + ']';
		}
	}

}