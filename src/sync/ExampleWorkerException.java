package de.htw.ds.sync;

import de.sb.java.TypeMetadata;


/**
 * This example exception is thrown by the example worker to indicate work related problems.
 */
@TypeMetadata(copyright = "2013-2015 Sascha Baumeister, all rights reserved", version = "0.3.0", authors = "Sascha Baumeister")
public class ExampleWorkerException extends Exception {
	static private final long serialVersionUID = 1L;


	/**
	 * Creates a new instance with neither message nor cause.
	 */
	public ExampleWorkerException () {}


	/**
	 * Creates a new instance with the given message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public ExampleWorkerException (final String message, final Throwable cause) {
		super(message, cause);
	}


	/**
	 * Creates a new instance with the given message.
	 * @param message the message
	 */
	public ExampleWorkerException (final String message) {
		super(message);
	}


	/**
	 * Creates a new instance with the given cause.
	 * @param cause the cause
	 */
	public ExampleWorkerException (final Throwable cause) {
		super(cause);
	}
}