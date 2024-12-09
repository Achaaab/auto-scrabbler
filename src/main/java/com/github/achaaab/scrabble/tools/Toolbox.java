package com.github.achaaab.scrabble.tools;

import java.time.Duration;

import static java.lang.Math.toIntExact;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class Toolbox {

	private static final double NANOSECONDS_PER_SECOND = SECONDS.toNanos(1);

	/**
	 * @param duration
	 * @return
	 * @since 0.0.0
	 */
	public static double toSeconds(Duration duration) {
		return duration.getSeconds() + duration.getNano() / NANOSECONDS_PER_SECOND;
	}

	/**
	 * Causes the currently executing thread to sleep for the specified duration.
	 * Only useful for Java version before 19.
	 *
	 * @param duration sleep duration
	 * @since 0.0.0
	 */
	public static void sleep(Duration duration) {

		var milliseconds = MILLISECONDS.convert(duration);
		var nanoseconds = toIntExact(NANOSECONDS.convert(duration.minusMillis(milliseconds)));

		try {
			Thread.sleep(milliseconds, nanoseconds);
		} catch (InterruptedException interruptedException) {
			currentThread().interrupt();
		}
	}

	/**
	 * Runs an operation in a new thread then returns immediately.
	 *
	 * @param runnable operation to run
	 * @since 0.0.0
	 */
	public static void fork(Runnable runnable) {
		new Thread(runnable).start();
	}
}
