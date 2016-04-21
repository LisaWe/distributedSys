package de.htw.ds.sync;

import java.util.stream.IntStream;
import de.sb.java.Reference;
import de.sb.java.TypeMetadata;


/**
 * Demonstrates thread processing and thread resynchronization using parallel streams. Note that
 * these streams lack interruptibility; on the positive side, this removes the need to handle
 * {@linkplain InterruptedException} correctly, which is arguably a nightmare for beginners. On the
 * negative side, this implies that the parent and child threads cannot be interrupted or timed out
 * ...
 */
@TypeMetadata(copyright = "2008-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public final class ResyncThreadByStream {
	@SuppressWarnings("all")


	/**
	 * Application entry point. The arguments must be a child thread count, and the maximum number
	 * of seconds the child threads should take for processing.
	 * @param args the arguments
	 * @throws IndexOutOfBoundsException if less than two arguments are passed
	 * @throws NumberFormatException if any of the given arguments is not an integral number
	 * @throws IllegalArgumentException if any of the arguments is negative
	 * @throws RuntimeException if there is a runtime exception during asynchronous work
	 * @throws ExampleWorkerException if there is a checked exception during asynchronous work
	 */
	static public void main (final String[] args) throws ExampleWorkerException {
		final int childThreadCount = Integer.parseInt(args[0]);
		final int maximumWorkDuration = Integer.parseInt(args[1]);
		resync(childThreadCount, maximumWorkDuration);
	}


	/**
	 * Starts child threads and resynchronizes them, displaying the time it took for the longest
	 * running child to end.
	 * @param childThreadCount the number of child threads
	 * @param maximumWorkDuration the maximum work duration is seconds
	 * @throws IllegalArgumentException if any of the given arguments is negative
	 * @throws Error if there is an error during asynchronous work
	 * @throws RuntimeException if there is a runtime exception during asynchronous work
	 * @throws ExampleWorkerException if there is a checked exception during asynchronous work
	 * @throws IllegalArgumentException if any of the arguments is negative
	 */
	static private void resync (final int childThreadCount, final int maximumWorkDuration) throws ExampleWorkerException {
		if (childThreadCount < 0 | maximumWorkDuration < 0) throw new IllegalArgumentException();
		final long timestamp = System.currentTimeMillis();
		final Reference<Throwable> exceptionReference = new Reference<>();

		System.out.format("Starting %s Java thread(s), implicitly resynchronizing them afterwards ...\n", childThreadCount);
		IntStream.range(0, childThreadCount).parallel().forEach((int index) -> {
			try {
				ExampleWorker.work(maximumWorkDuration);
			} catch (final ExampleWorkerException exception) {
				exceptionReference.put(exception);
			}
		});

		// manual precise rethrow required!
		final Throwable exception = exceptionReference.get();
		if (exception instanceof Error) throw (Error) exception;
		if (exception instanceof RuntimeException) throw (RuntimeException) exception;
		if (exception instanceof ExampleWorkerException) throw (ExampleWorkerException) exception;
		assert exception == null;

		System.out.format("Java thread(s) resynchronized after %sms.\n", System.currentTimeMillis() - timestamp);
	}
}