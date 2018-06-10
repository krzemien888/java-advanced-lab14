package texters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import texters.WorkerManager.Cache;


public class Worker extends Thread {
	
	private String directoryPath;
	private Cache cache;
	private boolean work;
	
	public Worker(String directoryPath, Cache cache) {
		this.directoryPath = directoryPath;
		this.work = false;
		this.cache = cache;
	}
	
	@Override
	public void run() {
		this.work = true;
		while(this.work) {
			File[] textFiles = this.getFiles();
			File randomFile = this.getRandomFile(textFiles);
			String fileContent = this.cache.get(randomFile.getAbsolutePath());
			String message = null;
			boolean cacheHit = fileContent != null;
			if(fileContent == null) {
				try {
					fileContent = this.readContent(randomFile);
					this.cache.put(randomFile.getAbsolutePath(), fileContent);
				} catch (IOException e) {
					message = this.getName() + " has problems with " + randomFile;
					e.printStackTrace();
				}
			}
			message = this.getName() + " choose " + randomFile + " file char count: " + fileContent.length() + " (cache hit: " + cacheHit + ")";
			System.out.println(message);
		}
	}
	
	private String readContent(File randomFile) throws IOException {
		return Files.lines(randomFile.toPath()).collect(Collectors.joining("\n"));
	}

	private File[] getFiles() {
		File directory = new File(directoryPath);
		return directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".txt");
			}
		});
	}
	
	private File getRandomFile(File[] files) {
		int choosenFileIndex = ThreadLocalRandom.current().nextInt(0, files.length);
		return files[choosenFileIndex];
	}
	
	public void stopWorking() {
		this.work = false;
	}
}
