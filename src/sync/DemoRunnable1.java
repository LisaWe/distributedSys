package de.htw.ds.sync;

import de.sb.java.TypeMetadata;

/**
 * Demo runnable implementation as explicit class.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public class DemoRunnable1 implements Runnable {

	private final String text;


	/**
	 * Creates a new instance.
	 * @param text a text
	 * @throws NullPointerException if the given text is {@code null}
	 */
	public DemoRunnable1 (final String text) {
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