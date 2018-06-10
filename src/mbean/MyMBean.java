package mbean;

import texters.WorkerManager;

public class MyMBean implements IMyMBeanMXBean {

    private int threadCount;
    private int cacheSize;
    private String workingDirectory;
    private WorkerManager manager;
    
    public MyMBean(WorkerManager manager){
        this.threadCount = manager.getWorkersAmount();
        this.cacheSize = manager.cache.getCacheMaxSize();
        this.manager = manager;
        this.workingDirectory = manager.getWorkingDirectory();
    }
    
    @Override
    public void setThreadCount(int noOfThreads) {
    	if(noOfThreads >= 0) {
    		this.threadCount=noOfThreads;
    		try {
				this.manager.setWorkersNumber(noOfThreads);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }


    @Override
    public int getThreadCount() {
        return this.threadCount;
    }


    @Override
    public void setCacheSize(int cacheSize) {
    	if(cacheSize > 0) {
    		this.manager.cache.setCacheMaxSize(cacheSize);
    		this.cacheSize = cacheSize;    		
    	}
    }


    @Override
    public int getCacheSize() {
        return this.cacheSize;
    }
    
    @Override
    public String doConfig(){
    	StringBuilder builder = new StringBuilder();
    	builder.append("Current cache size: " + this.manager.cache.getSize() + "\n");
    	builder.append("Current cache max size: " + this.manager.cache.getCacheMaxSize() + "\n");
    	builder.append("Current cache hit count: " + this.manager.cache.getCacheHitCount() + "\n");
    	builder.append("Current cache request count: " + this.manager.cache.getCacheRequests() + "\n");
    	float hitRatio = ((float)this.manager.cache.getCacheHitCount()) / ((float)this.manager.cache.getCacheRequests()); 
    	builder.append("Current cache hit ratio: " + hitRatio + "\n");
    	
    	builder.append("Current thread working: " + this.manager.getWorkersAmount() + "\n");
    	builder.append("Current working directory: " + this.manager.getWorkingDirectory() + "\n");
    	return builder.toString();
    }

	@Override
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	@Override
	public String getWorkingDirectory() {	
		return this.workingDirectory;
	}

}
