package mbean;

public interface IMyMBeanMXBean {	
    public void setThreadCount(int noOfThreads);
    public int getThreadCount();
    
    public void setCacheSize(int cacheSize);
    public int getCacheSize();
    
    public String getWorkingDirectory();
    public String getCacheHitRatio();
    
    public String doInfo();

}
