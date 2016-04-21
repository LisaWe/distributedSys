package de.htw.ds.sync;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import de.sb.java.Threads;
import de.sb.java.TypeMetadata;
import de.sb.java.io.Streams;


/**
 * Demonstrates child process fork-join using polling, and one of several possible ways of
 * handling {@linkplain InterruptedException}. There are two rules which must always be obeyed when
 * handling such an exception:
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
 */@TypeMetadata(copyright = "2008-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public final class ResyncProcessByPolling {

	/**
	 * Application entry point. The single parameters must be a command line suitable to start a
	 * program/process.
	 * @param args the arguments
	 * @throws IndexOutOfBoundsException if no argument is passed
	 * @throws IOException if there's an I/O related problem
	 * @throws InterruptedException if is operation's thread is interrupted during fork-join
	 */
	static public void main (final String[] args) throws IOException, InterruptedException {
		System.out.println("Starting process... ");
		final Process process = Runtime.getRuntime().exec(args[0]);

		try {
			System.out.println("Connecting process I/O streams with current Java process... ");
			final Callable<?> systemInputTransporter = () -> Streams.copy(System.in, new PrintStream(process.getOutputStream()), 0x10);
			final Callable<?> systemOutputTransporter = () -> Streams.copy(process.getInputStream(), System.out, 0x10);
			final Callable<?> systemErrorTransporter = () -> Streams.copy(process.getErrorStream(), System.err, 0x10);
	
			// System.in transporter must be started as a daemon thread, otherwise read-block prevents termination!
			final Thread systemInputThread = new Thread(Threads.newRunnable(systemInputTransporter));
			systemInputThread.setDaemon(true);
			systemInputThread.start();
			new Thread(Threads.newRunnable(systemOutputTransporter)).start();
			new Thread(Threads.newRunnable(systemErrorTransporter)).start();
	
			System.out.println("Resynchronising process... ");
			final long timestamp = System.currentTimeMillis();
	
			int exitCode;
			while (true) {
				try {
					exitCode = process.exitValue();
					break;
				} catch (final IllegalThreadStateException exception) {
					// MUST try to sleep at least a bit to prevent CPU from running at 100% while polling,
					// starving the system's other tasks for CPU time, and needlessly heating up the CPU!
					Thread.sleep(1);
				}
			}

			System.out.format("Process ended with exit code %s after running %sms.\n", exitCode, System.currentTimeMillis() - timestamp);
		} catch (final Throwable exception) {
			process.destroyForcibly();
			throw exception;   // note use of compiler generated precise rethrow (since Java 7)!
		}
	}
}