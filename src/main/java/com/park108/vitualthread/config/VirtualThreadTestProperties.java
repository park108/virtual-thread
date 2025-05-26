package com.park108.vitualthread.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.virtual-thread-test")
public class VirtualThreadTestProperties {

    private boolean usePartitioner;
    private int totalTestSize;
    private int chunkSize;
    private int gridSize;
    private boolean useVirtualThread;

    private int normalThreadPoolMinSize;
    private int normalThreadPoolMaxSize;
    private int normalThreadPoolQueueCapacity;

    public boolean isUsePartitioner() { return usePartitioner; }
    public void setUsePartitioner(boolean usePartitioner) { this.usePartitioner = usePartitioner; }

    public int getTotalTestSize() { return totalTestSize; }
    public void setTotalTestSize(int totalTestSize) { this.totalTestSize = totalTestSize; }

    public int getChunkSize() { return chunkSize; }
    public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }

    public int getGridSize() { return gridSize; }
    public void setGridSize(int gridSize) { this.gridSize = gridSize; }

    public boolean isUseVirtualThread() { return useVirtualThread; }
    public void setUseVirtualThread(boolean useVirtualThread) { this.useVirtualThread = useVirtualThread; }

    public int getNormalThreadPoolMinSize() { return normalThreadPoolMinSize; }
    public void setNormalThreadPoolMinSize(int normalThreadPoolMinSize) {
        this.normalThreadPoolMinSize = normalThreadPoolMinSize;
    }

    public int getNormalThreadPoolMaxSize() { return normalThreadPoolMaxSize; }
    public void setNormalThreadPoolMaxSize(int normalThreadPoolMaxSize) {
        this.normalThreadPoolMaxSize = normalThreadPoolMaxSize;
    }

    public int getNormalThreadPoolQueueCapacity() { return normalThreadPoolQueueCapacity; }
    public void setNormalThreadPoolQueueCapacity(int normalThreadPoolQueueCapacity) {
        this.normalThreadPoolQueueCapacity = normalThreadPoolQueueCapacity;
    }
}
