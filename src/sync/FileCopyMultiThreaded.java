package de.htw.ds.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCopyMultiThreaded {

	// aus FileCopySingleThreaded kopiert
	static public void main(final String[] args) throws IOException {

		final Path sourcePath = Paths.get(args[0]);
		if (!Files.isReadable(sourcePath))
			throw new IllegalArgumentException(sourcePath.toString());

		final Path sinkPath = Paths.get(args[1]);
		if (sinkPath.getParent() != null && !Files.isDirectory(sinkPath.getParent()))
			throw new IllegalArgumentException(sinkPath.toString());

		InputStream fis = Files.newInputStream(sourcePath);
		OutputStream fos = Files.newOutputStream(sinkPath);
		// Part 1.3 Pipe
		PipedInputStream inPipe = new PipedInputStream();
		PipedOutputStream outPipe = new PipedOutputStream();
		inPipe.connect(outPipe);

		// Part 1.2 eine Transporterinstanz
		Transporter ti1 = new Transporter(fis, outPipe);
		// ti1.run();
		// Part 1.3 zweite Transporterinstanz
		Transporter ti2 = new Transporter(inPipe, fos);
		Thread thread1 = new Thread(ti1);
		Thread thread2 = new Thread(ti2);

		thread1.start();
		thread2.start();
		System.out.println("done.");
	}

	// Part 1.1
	// neue Klasse Transporter angelegt, da extends Fehlermeldung
	// erzeugt hat, in implements geändert
	static private class Transporter implements Runnable {
		private InputStream inputStream;
		private OutputStream outputStream;

		// Konstruktor, der Instanzvariablen aufgreift
		public Transporter(InputStream inputStream, OutputStream outputStream) {

			if (inputStream == null || outputStream == null) {
				throw new NullPointerException();
			}

			this.inputStream = inputStream;
			this.outputStream = outputStream;
		}

		@Override
		public void run() {

			final byte[] buffer = new byte[0x10000];
			try {
				for (int bytesRead = inputStream.read(buffer); bytesRead != -1; bytesRead = inputStream.read(buffer)) {
					outputStream.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
