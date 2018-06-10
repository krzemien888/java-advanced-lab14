package mbean;

public interface IMyMBeanMXBean {	
    public void setThreadCount(int noOfThreads);
    public int getThreadCount();
    
    public void setCacheSize(int cacheSize);
    public int getCacheSize();
    
    public void setWorkingDirectory(String workingDirectory);
    public String getWorkingDirectory();
    
    public String doConfig();

}
