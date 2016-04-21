package de.htw.ds.sync;

import de.sb.java.TypeMetadata;

/**
 * Thread demo using Java-8 lambda expression runnable.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public class ThreadDemo4 {

	/**
	 * Application entry point. The runtime arguments must at least
	 * consist of a display text.
	 * @param args the runtime arguments
	 */
	static public void main (final String[] args) {
		final String text = args[0];

		// Lambda expression semantics: (for action-method with these parameters) -> create this result, or null for void 
		final Runnable runnable4 = () -> System.out.println(text);

		final Thread thread = new Thread(runnable4);
		thread.start();		// executes runnable asynchronously
		// thread.run();	// executes runnable synchronously!
	}
}