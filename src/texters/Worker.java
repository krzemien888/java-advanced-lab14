package texters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import analizers.CharacterCounter;
import analizers.IAnalizer;
import analizers.MostUsedWord;
import analizers.VowelAndconsonantCompare;
import analizers.VowelHistogram;
import texters.WorkerManager.Cache;


public class Worker extends Thread {
	
	private String directoryPath;
	private Cache cache;
	private boolean work;
	private ArrayList<IAnalizer> analizers;
	
	public Worker(String directoryPath, Cache cache) {
		this.directoryPath = directoryPath;
		this.work = false;
		this.cache = cache;
		this.analizers = new ArrayList<>();
		this.analizers.add(new CharacterCounter());
		this.analizers.add(new MostUsedWord());
		this.analizers.add(new VowelAndconsonantCompare());
		this.analizers.add(new VowelHistogram());
	}
	
	@Override
	public void run() {
		this.work = true;
		while(this.work) {      
			long startTime = System.currentTimeMillis();
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
			long stopTime = System.currentTimeMillis();
		    long readTime = stopTime - startTime;
		    IAnalizer analizer = this.getRandomAnalizer();
		    startTime =  System.currentTimeMillis();
		    String analizerMessage = analizer.getName() + " - " + analizer.apply(fileContent);
		    stopTime =  System.currentTimeMillis();
		    long analizeTime = stopTime - startTime;
		    StringBuilder builder = new StringBuilder();
		    builder.append(this.getName() + "(cache hit: " + cacheHit + "): ");
		    builder.append(randomFile.getName() + " | readTime: " + readTime + "| analizeTime: " + analizeTime + "|");
		    builder.append(analizerMessage);
			message =  builder.toString();
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
	
	private IAnalizer getRandomAnalizer() {
		int choosenAnalizerIndex = ThreadLocalRandom.current().nextInt(0, this.analizers.size());
		return this.analizers.get(choosenAnalizerIndex);
	}
}
