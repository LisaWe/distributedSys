package de.htw.ds.sync;

import de.sb.java.TypeMetadata;

/**
 * Thread demo using named inner runnable class.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public class ThreadDemo2 {

	/**
	 * Application entry point. The runtime arguments must at least
	 * consist of a display text.
	 * @param args the runtime arguments
	 */
	static public void main (final String[] args) {
		final String text = args[0];

		final Runnable runnable2 = new DemoRunnable2(text);

		final Thread thread = new Thread(runnable2);
		thread.start();		// executes runnable asynchronously
		// thread.run();	// executes runnable synchronously!
	}


	/**
	 * Demo runnable implementation as static inner class.
	 */
	static private class DemoRunnable2 implements Runnable {

		private final String text;


		/**
		 * Creates a new instance.
		 * @param text a text
		 * @throws NullPointerException if the given text is {@code null}
		 */
		public DemoRunnable2(final String text) {
	
			if (text == null) throw new NullPointerException();

			this.text = text;
		}


		/**
		 * {@inheritDoc}
		 */
		public void run() {
			System.out.println(this.text);
		}
	}
}