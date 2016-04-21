package de.htw.ds.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import de.sb.java.TypeMetadata;


public class FileCopyMultiThreaded {
	
	// aus FileCopySingleThreaded kopiert
	static public void main (final String[] args) throws IOException {
		final Path sourcePath = Paths.get(args[0]);
		if (!Files.isReadable(sourcePath)) throw new IllegalArgumentException(sourcePath.toString());

		final Path sinkPath = Paths.get(args[1]);
		if (sinkPath.getParent() != null && !Files.isDirectory(sinkPath.getParent())) throw new IllegalArgumentException(sinkPath.toString());

		// Files.copy(sourcePath, sinkPath, StandardCopyOption.REPLACE_EXISTING);

		try (InputStream fis = Files.newInputStream(sourcePath)) {
			try (OutputStream fos = Files.newOutputStream(sinkPath)) {
				final byte[] buffer = new byte[0x10000];
				for (int bytesRead = fis.read(buffer); bytesRead != -1; bytesRead = fis.read(buffer)) {
					fos.write(buffer, 0, bytesRead);
				}
			}
		}
		
		System.out.println("done.");
	}

	// Part 1
	// neue Klasse Transporter angelegt, da extends Fehlermeldung 
	// erzeugt hat in implements geändert
	static private class Transporter implements Runnable{
		private InputStream inputStream;
		private OutputStream outputStream;
		
		
		// Konstruktor, der Instanzvariablen aufgreift
		public Transporter(InputStream inputStream, OutputStream outputStream){
			
			if (inputStream == null || outputStream == null){
				throw new NullPointerException();
			}

			this.inputStream = inputStream;
			this.outputStream = outputStream;
			}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
}
