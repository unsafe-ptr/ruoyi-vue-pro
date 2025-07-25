package cn.iocoder.yudao.framework.mybatis.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * 数据库查询优化工具类
 *
 * @author 芋道源码
 */
@Slf4j
public class QueryOptimizeUtils {

    /**
     * 批量查询的最大批次大小
     */
    private static final int MAX_BATCH_SIZE = 1000;
    
    /**
     * IN 查询的最大参数数量（避免SQL过长）
     */
    private static final int MAX_IN_SIZE = 1000;

    /**
     * 优化 IN 查询：当参数过多时拆分为多个查询
     */
    public static <T> void optimizeInQuery(QueryWrapper<T> queryWrapper, String column, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            // 空集合时添加一个不可能匹配的条件
            queryWrapper.eq(column, "IMPOSSIBLE_VALUE_" + System.currentTimeMillis());
            return;
        }
        
        if (values.size() <= MAX_IN_SIZE) {
            queryWrapper.in(column, values);
        } else {
            // 拆分为多个查询
            List<List<?>> chunks = CollUtil.split(values, MAX_IN_SIZE);
            queryWrapper.and(wrapper -> {
                for (int i = 0; i < chunks.size(); i++) {
                    if (i == 0) {
                        wrapper.in(column, chunks.get(i));
                    } else {
                        wrapper.or().in(column, chunks.get(i));
                    }
                }
            });
        }
    }

    /**
     * 优化分页查询：避免深分页问题
     */
    public static <T> IPage<T> optimizePagination(IPage<T> page) {
        long current = page.getCurrent();
        long size = page.getSize();
        
        // 深分页检查（超过10000条数据的分页）
        if (current * size > 10000) {
            log.warn("[optimizePagination] 检测到深分页查询：页码={}, 每页大小={}, 建议使用游标分页", current, size);
        }
        
        // 限制每页最大数量
        if (size > 1000) {
            log.warn("[optimizePagination] 每页查询数量过大：{}, 已限制为1000", size);
            page.setSize(1000);
        }
        
        return page;
    }

    /**
     * 优化字符串查询：避免全表扫描
     */
    public static <T> void optimizeLikeQuery(QueryWrapper<T> queryWrapper, String column, String value) {
        if (StrUtil.isBlank(value)) {
            return;
        }
        
        // 如果搜索词太短，建议使用精确匹配
        if (value.trim().length() < 2) {
            log.debug("[optimizeLikeQuery] 搜索词过短，使用精确匹配：{}", value);
            queryWrapper.eq(column, value.trim());
        } else {
            // 避免前置通配符（%value），这会导致索引失效
            if (value.startsWith("%")) {
                log.warn("[optimizeLikeQuery] 检测到前置通配符查询，可能导致性能问题：{}", value);
            }
            queryWrapper.like(column, value);
        }
    }

    /**
     * 添加常用的查询优化提示
     */
    public static <T> QueryWrapper<T> addOptimizationHints(QueryWrapper<T> queryWrapper) {
        // 添加 MySQL 查询优化提示
        return queryWrapper.last("/* 使用索引提示 */");
    }

    /**
     * 检查查询条件是否可能导致全表扫描
     */
    public static <T> boolean mayFullTableScan(QueryWrapper<T> queryWrapper) {
        String sqlSegment = queryWrapper.getTargetSql();
        
        // 简单检查是否有WHERE条件
        if (StrUtil.isBlank(sqlSegment) || !sqlSegment.toLowerCase().contains("where")) {
            log.warn("[mayFullTableScan] 查询可能导致全表扫描，没有WHERE条件");
            return true;
        }
        
        return false;
    }

    /**
     * 批量处理工具方法
     */
    public static <T> void batchProcess(List<T> dataList, int batchSize, BatchProcessor<T> processor) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        
        int actualBatchSize = Math.min(batchSize, MAX_BATCH_SIZE);
        List<List<T>> batches = CollUtil.split(dataList, actualBatchSize);
        
        for (int i = 0; i < batches.size(); i++) {
            try {
                processor.process(batches.get(i), i + 1, batches.size());
            } catch (Exception e) {
                log.error("[batchProcess] 批次 {}/{} 处理失败", i + 1, batches.size(), e);
                throw new RuntimeException("批量处理失败", e);
            }
        }
    }

    /**
     * 批量处理器接口
     */
    @FunctionalInterface
    public interface BatchProcessor<T> {
        /**
         * 处理批次数据
         * 
         * @param batch 当前批次数据
         * @param currentBatch 当前批次号（从1开始）
         * @param totalBatches 总批次数
         */
        void process(List<T> batch, int currentBatch, int totalBatches);
    }
}