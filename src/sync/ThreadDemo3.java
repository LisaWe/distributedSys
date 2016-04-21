package de.htw.ds.sync;

import de.sb.java.TypeMetadata;

/**
 * Thread demo using anonymous inner runnable class.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public class ThreadDemo3 {

	/**
	 * Application entry point. The runtime arguments must at least
	 * consist of a display text.
	 * @param args the runtime arguments
	 */
	static public void main (final String[] args) {
		final String text = args[0];

		final Runnable runnable3 = new Runnable() {
			public void run() {
				System.out.println(text);
			}
		};

		final Thread thread = new Thread(runnable3);
		thread.start();		// executes runnable asynchronously
		// thread.run();	// executes runnable synchronously!
	}
}