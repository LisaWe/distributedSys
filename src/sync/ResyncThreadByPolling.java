package de.htw.ds.sync;

import java.util.HashSet;
import java.util.Set;
import de.sb.java.Reference;
import de.sb.java.Threads;
import de.sb.java.TypeMetadata;


/**
 * Demonstrates child thread fork-join using polling, and one of several possible ways of handling
 * {@linkplain InterruptedException}. There are two rules which must always be obeyed when handling
 * such an exception:
 * <ul>
 * <li>it must be dealt with immediately, at least partially - simply deferring the handling to the
 * method caller is never a good idea, as the latter lacks access to the child threads created!</li>
 * <li>The resulting code must never break the before-after relationship between the parent thread
 * and it's child threads!</li>
 * </ul>
 * Classical ways to handle this type of exception are:
 * <ul>
 * <li>interrupt the child threads (requires them to be designed to be interruptible), optionally
 * rethrowing the exception within the parent thread (implies the whole operation is designed to be
 * interruptible)</li>
 * <li>ignore the interruption, optionally re-setting the parent thread's interrupt flag once the
 * child threads have ended naturally</li>
 * <li>kill the process (safe "kill" option as resources will be freed automatically)</li>
 * <li>kill the parent thread (must close resources, the child threads continue until they've run
 * their course)</li>
 * </ul>
 */
@TypeMetadata(copyright = "2008-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public final class ResyncThreadByPolling {

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
		final Set<Thread> threads = new HashSet<>();

		System.out.format("Starting %s Java thread(s)...\n", childThreadCount);
		for (int index = 0; index < childThreadCount; ++index) {
			final Runnable runnable = () -> {
				try {
					ExampleWorker.work(maximumWorkDuration);
				} catch (final Throwable exception) {
					exceptionReference.put(exception);
				}
			};

			final Thread thread = new Thread(runnable);
			threads.add(thread);
			thread.start();
		}

		System.out.println("Resynchronising Java thread(s)... ");
		Threads.pollUninterruptibly(() -> {
			for (Thread thread : threads) {
				if (thread.isAlive()) return false;
			}
			return true;
		});

		// perform manual precise rethrow if required!
		final Throwable exception = exceptionReference.get();
		if (exception instanceof Error) throw (Error) exception;
		if (exception instanceof RuntimeException) throw (RuntimeException) exception;
		if (exception instanceof ExampleWorkerException) throw (ExampleWorkerException) exception;
		assert exception == null;

		System.out.format("Java thread(s) resynchronized after %sms.\n", System.currentTimeMillis() - timestamp);
	}
}