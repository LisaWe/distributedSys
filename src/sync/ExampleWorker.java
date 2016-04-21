package de.htw.ds.sync;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import de.sb.java.TypeMetadata;


/**
 * Example worker facade.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public final class ExampleWorker {

	/**
	 * Prevents instantiation
	 */
	private ExampleWorker () {}


	/**
	 * Performs a piece of example work (by sleeping) that may require up to the given maximum
	 * duration to complete. Returns the duration it actually took to complete.
	 * @param maximumDuration the maximum duration is seconds
	 * @return the "work" duration in seconds
	 * @throws ExampleWorkerException if there is a "work" related problem
	 */
	public static int work (final long maximumDuration) throws ExampleWorkerException {
		try {
			final int maximumDelay = (int) TimeUnit.SECONDS.toMillis(maximumDuration);
			final int actualDelay = ThreadLocalRandom.current().nextInt(maximumDelay);
			Thread.sleep(actualDelay);
			return actualDelay;
		} catch (final Exception exception) {
			throw new ExampleWorkerException(exception.getMessage(), exception.getCause());
		}
	}
}