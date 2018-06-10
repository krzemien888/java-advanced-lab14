package texters;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class WorkerManager {
	private List<Worker> workers;
	private String workingDirectory;
	private int numberOfWorkers;
	private boolean work;
	public Cache cache;
	
	public WorkerManager(int numberOfWorkers, String workingDirectory, int cacheSize) {
		this.workers = new ArrayList<>();
		this.numberOfWorkers = 0;
		this.workingDirectory = workingDirectory;
		this.cache = new Cache(cacheSize);
		try {
			this.setWorkersNumber(numberOfWorkers);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void setWorkersNumber(int workersNumber) throws InterruptedException {
		if(workersNumber < 0) {
			return;
		}

		while(workersNumber != this.workers.size()) {
			
			if(workersNumber > this.workers.size()) {
				workers.add(new Worker(this.workingDirectory, this.cache));
				if(this.work) {
					workers.get(workers.size() - 1).start();
				}
			}
			else if (workersNumber < this.workers.size()) {
				int lastIndex = this.workers.size() - 1;
				workers.get(lastIndex).stopWorking();
				workers.get(lastIndex).join(0);
				workers.remove(lastIndex);
			}
		}
		
		this.numberOfWorkers = workersNumber;
	}
	
	public void startWorkers() { 
		this.work = true;
		for(Worker worker : this.workers) {
			worker.start();
		}
	}
	
	public void stopWorkers() throws InterruptedException {
		this.work = false;
		for(Worker worker : this.workers) {
			worker.stopWorking();
		}
		for(Worker worker : this.workers) {
			worker.join(0);
		}
	}
	
	public int getWorkersAmount() {
		return this.numberOfWorkers;
	}
	
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}
	
	public class Cache {
		private Map<String, String> cacheMap;
		private int cacheMaxSize;
		private int cacheHitCount;
		private int cacheRequests;
		
		public Cache(int cacheMaxSize) {
			this.cacheMap = new HashMap<>();
			this.cacheMaxSize = cacheMaxSize;
			this.setCacheRequests(0);
			this.setCacheHitCount(0);
		}
		
		public synchronized String get(String key) {
			this.setCacheRequests(this.getCacheRequests() + 1);
			String result = this.cacheMap.get(key);
			if(result != null) {
				this.setCacheHitCount(this.getCacheHitCount() + 1);
			}
			return result;
		}
		
		public synchronized void put(String key, String value) {
			if(this.cacheMap.size() == cacheMaxSize) {
				this.removeRandom();
			}
			this.cacheMap.put(key, value);
		}
		
		private void removeRandom() {
			if(!this.cacheMap.isEmpty()) {
				Object[] keyArray = this.cacheMap.keySet().toArray();
				int randomIndex = ThreadLocalRandom.current().nextInt(0, keyArray.length);
				Object randomKey = keyArray[randomIndex];
				this.cacheMap.remove(randomKey);	
			}
		}
		
		public int getCacheRequests() {
			return cacheRequests;
		}

		private void setCacheRequests(int cacheRequests) {
			this.cacheRequests = cacheRequests;
		}

		public int getCacheHitCount() {
			return cacheHitCount;
		}

		private void setCacheHitCount(int cacheHitCount) {
			this.cacheHitCount = cacheHitCount;
		}

		public int getCacheCurrentSize() {
			return this.cacheMap.size();
		}
		
		public int getCacheMaxSize() {
			return this.cacheMaxSize;
		}
		
		public void setCacheMaxSize(int cacheMaxSize) {
			if(cacheMaxSize < this.getCacheCurrentSize()) {
				while(cacheMaxSize < this.getCacheCurrentSize()) {
					this.removeRandom();					
				}
			}
			this.cacheMaxSize = cacheMaxSize;
		}
		
		public int getSize() {
			return this.cacheMap.size();
		}
	}

	public String getFilesInDirectory() {
		File directory = new File(this.workingDirectory);
		File[] files = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".txt");
			}
		});
		
		return Integer.toString(files.length);
	}

}
