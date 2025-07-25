package cn.iocoder.yudao.framework.common.util.performance;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 性能监控工具类
 * 
 * @author 芋道源码
 */
@Slf4j
public class PerformanceMonitorUtils {

    /**
     * 方法执行时间统计
     */
    private static final ConcurrentHashMap<String, MethodPerformance> METHOD_PERFORMANCE_MAP = new ConcurrentHashMap<>();
    
    /**
     * 慢查询阈值（毫秒）
     */
    private static final long SLOW_QUERY_THRESHOLD = 100L;
    
    /**
     * 记录方法执行开始时间
     */
    public static long startTiming() {
        return System.currentTimeMillis();
    }
    
    /**
     * 记录方法执行结束时间并输出性能指标
     */
    public static void endTiming(String methodName, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        recordMethodPerformance(methodName, duration);
        
        if (duration > SLOW_QUERY_THRESHOLD) {
            log.warn("[性能监控] 慢方法检测: {} 执行时间: {}ms", methodName, duration);
        }
    }
    
    /**
     * 记录方法性能数据
     */
    private static void recordMethodPerformance(String methodName, long duration) {
        METHOD_PERFORMANCE_MAP.computeIfAbsent(methodName, k -> new MethodPerformance())
                .record(duration);
    }
    
    /**
     * 获取方法性能统计
     */
    public static MethodPerformance getMethodPerformance(String methodName) {
        return METHOD_PERFORMANCE_MAP.get(methodName);
    }
    
    /**
     * 清理性能统计数据
     */
    public static void clearPerformanceData() {
        METHOD_PERFORMANCE_MAP.clear();
    }
    
    /**
     * 获取系统内存使用情况
     */
    public static MemoryInfo getMemoryInfo() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();
        
        return MemoryInfo.builder()
                .heapUsed(heapMemory.getUsed())
                .heapMax(heapMemory.getMax())
                .heapCommitted(heapMemory.getCommitted())
                .nonHeapUsed(nonHeapMemory.getUsed())
                .nonHeapMax(nonHeapMemory.getMax())
                .nonHeapCommitted(nonHeapMemory.getCommitted())
                .build();
    }
    
    /**
     * 获取线程信息
     */
    public static ThreadInfo getThreadInfo() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        
        return ThreadInfo.builder()
                .threadCount(threadBean.getThreadCount())
                .peakThreadCount(threadBean.getPeakThreadCount())
                .daemonThreadCount(threadBean.getDaemonThreadCount())
                .totalStartedThreadCount(threadBean.getTotalStartedThreadCount())
                .build();
    }
    
    /**
     * 打印性能报告
     */
    public static void printPerformanceReport() {
        log.info("=== 性能监控报告 ===");
        
        // 内存使用情况
        MemoryInfo memoryInfo = getMemoryInfo();
        log.info("内存使用 - 堆内存: {}MB/{}/MB, 非堆内存: {}MB/{}MB", 
                memoryInfo.getHeapUsed() / 1024 / 1024,
                memoryInfo.getHeapMax() / 1024 / 1024,
                memoryInfo.getNonHeapUsed() / 1024 / 1024,
                memoryInfo.getNonHeapMax() / 1024 / 1024);
        
        // 线程使用情况  
        ThreadInfo threadInfo = getThreadInfo();
        log.info("线程使用 - 当前线程数: {}, 峰值线程数: {}, 守护线程数: {}", 
                threadInfo.getThreadCount(),
                threadInfo.getPeakThreadCount(),
                threadInfo.getDaemonThreadCount());
        
        // 方法性能统计
        if (!METHOD_PERFORMANCE_MAP.isEmpty()) {
            log.info("方法性能统计（Top 10）:");
            METHOD_PERFORMANCE_MAP.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue().getAverageTime(), e1.getValue().getAverageTime()))
                    .limit(10)
                    .forEach(entry -> {
                        MethodPerformance perf = entry.getValue();
                        log.info("  {} - 调用次数: {}, 平均耗时: {}ms, 最大耗时: {}ms", 
                                entry.getKey(), perf.getCallCount(), perf.getAverageTime(), perf.getMaxTime());
                    });
        }
        
        log.info("==================");
    }
    
    /**
     * 方法性能统计类
     */
    public static class MethodPerformance {
        private final LongAdder callCount = new LongAdder();
        private final LongAdder totalTime = new LongAdder();
        private volatile long maxTime = 0L;
        private volatile long minTime = Long.MAX_VALUE;
        
        public void record(long duration) {
            callCount.increment();
            totalTime.add(duration);
            
            // 更新最大值
            if (duration > maxTime) {
                maxTime = duration;
            }
            
            // 更新最小值
            if (duration < minTime) {
                minTime = duration;
            }
        }
        
        public long getCallCount() {
            return callCount.sum();
        }
        
        public long getTotalTime() {
            return totalTime.sum();
        }
        
        public long getAverageTime() {
            long count = getCallCount();
            return count > 0 ? getTotalTime() / count : 0;
        }
        
        public long getMaxTime() {
            return maxTime;
        }
        
        public long getMinTime() {
            return minTime == Long.MAX_VALUE ? 0 : minTime;
        }
    }
    
    /**
     * 内存信息类
     */
    @lombok.Builder
    @lombok.Data
    public static class MemoryInfo {
        private long heapUsed;
        private long heapMax;
        private long heapCommitted;
        private long nonHeapUsed;
        private long nonHeapMax;
        private long nonHeapCommitted;
    }
    
    /**
     * 线程信息类
     */
    @lombok.Builder
    @lombok.Data
    public static class ThreadInfo {
        private int threadCount;
        private int peakThreadCount;
        private int daemonThreadCount;
        private long totalStartedThreadCount;
    }
}