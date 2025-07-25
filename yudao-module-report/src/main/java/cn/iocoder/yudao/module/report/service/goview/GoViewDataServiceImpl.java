package cn.iocoder.yudao.module.report.service.goview;

import cn.iocoder.yudao.module.report.controller.admin.goview.vo.data.GoViewDataRespVO;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * GoView 数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class GoViewDataServiceImpl implements GoViewDataService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public GoViewDataRespVO getDataBySQL(String sql) {
        // 1. 执行查询
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        // 2. 构建返回结果
        GoViewDataRespVO respVO = new GoViewDataRespVO();
        
        // 2.1 解析元数据
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        String[] columnNames = metaData.getColumnNames();
        respVO.setDimensions(Arrays.asList(columnNames));
        
        // 2.2 解析数据明细 - 优化：使用ArrayList，初始容量设为合理值
        // ArrayList在随机访问和内存使用上都比LinkedList更优
        respVO.setSource(new ArrayList<>(256)); // 初始容量设为256，平衡内存使用和扩容成本
        
        while (sqlRowSet.next()) {
            // 使用预期大小初始化HashMap，减少rehash开销
            Map<String, Object> data = Maps.newHashMapWithExpectedSize(columnNames.length);
            for (String columnName : columnNames) {
                data.put(columnName, sqlRowSet.getObject(columnName));
            }
            respVO.getSource().add(data);
        }
        
        return respVO;
    }
}
