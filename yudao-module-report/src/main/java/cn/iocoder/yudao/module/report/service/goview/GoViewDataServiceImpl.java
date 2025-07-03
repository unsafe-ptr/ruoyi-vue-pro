package cn.iocoder.yudao.module.report.service.goview;

import cn.iocoder.yudao.module.report.controller.admin.goview.vo.data.GoViewDataRespVO;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import cn.hutool.core.util.StrUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * GoView 数据 Service 实现类
 *
 * 补充说明：
 * 1. 目前默认使用 jdbcTemplate 查询项目配置的数据源。如果你想查询其它数据源，可以新建对应数据源的 jdbcTemplate 来实现。
 * 2. 默认数据源是 MySQL 关系数据源，可能数据量比较大的情况下，会比较慢，可以考虑后续使用 Click House 等等。
 *
 * @author 芋道源码
 */
@Service
@Validated
public class GoViewDataServiceImpl implements GoViewDataService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    
    // SQL注入防护：只允许SELECT查询，禁止危险操作
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i).*(DROP|DELETE|UPDATE|INSERT|ALTER|CREATE|TRUNCATE|EXEC|EXECUTE|SCRIPT|UNION|INFORMATION_SCHEMA|SLEEP|BENCHMARK|WAITFOR).*"
    );
    
    // 只允许SELECT查询
    private static final Pattern VALID_SELECT_PATTERN = Pattern.compile(
        "^\\s*SELECT\\s+.*", Pattern.CASE_INSENSITIVE
    );

    @Override
    public GoViewDataRespVO getDataBySQL(String sql) {
        // 参数校验
        if (StrUtil.isBlank(sql)) {
            throw ServiceExceptionUtil.exception(new ErrorCode(400, "SQL语句不能为空"));
        }
        
        // SQL安全校验
        validateSqlSecurity(sql);
        
        // 限制SQL长度，防止过长查询
        if (sql.length() > 2000) {
            throw ServiceExceptionUtil.exception(new ErrorCode(400, "SQL语句长度不能超过2000字符"));
        }
        
        try {
            // 1. 执行查询
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

            // 2. 构建返回结果
            GoViewDataRespVO respVO = new GoViewDataRespVO();
            // 2.1 解析元数据
            SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
            String[] columnNames = metaData.getColumnNames();
            respVO.setDimensions(Arrays.asList(columnNames));
            // 2.2 解析数据明细
            respVO.setSource(new LinkedList<>()); // 由于数据量不确认，使用 LinkedList 虽然内存占用大一点，但是不存在扩容复制的问题
            
            int rowCount = 0;
            final int MAX_ROWS = 10000; // 限制返回行数，防止内存溢出
            
            while (sqlRowSet.next() && rowCount < MAX_ROWS) {
                Map<String, Object> data = Maps.newHashMapWithExpectedSize(columnNames.length);
                for (String columnName : columnNames) {
                    data.put(columnName, sqlRowSet.getObject(columnName));
                }
                respVO.getSource().add(data);
                rowCount++;
            }
            
            return respVO;
        } catch (Exception e) {
            throw ServiceExceptionUtil.exception(new ErrorCode(500, "SQL执行失败：" + e.getMessage()));
        }
    }
    
    /**
     * 验证SQL安全性
     * @param sql SQL语句
     */
    private void validateSqlSecurity(String sql) {
        String normalizedSql = sql.trim().replaceAll("\\s+", " ");
        
        // 1. 检查是否为SELECT语句
        if (!VALID_SELECT_PATTERN.matcher(normalizedSql).matches()) {
            throw ServiceExceptionUtil.exception(new ErrorCode(403, "只允许执行SELECT查询语句"));
        }
        
        // 2. 检查危险操作
        if (SQL_INJECTION_PATTERN.matcher(normalizedSql).matches()) {
            throw ServiceExceptionUtil.exception(new ErrorCode(403, "SQL语句包含不允许的操作"));
        }
        
        // 3. 检查注释和多语句
        if (normalizedSql.contains("--") || normalizedSql.contains("/*") || normalizedSql.contains(";")) {
            throw ServiceExceptionUtil.exception(new ErrorCode(403, "SQL语句不能包含注释或多条语句"));
        }
        
        // 4. 检查系统表访问
        String upperSql = normalizedSql.toUpperCase();
        if (upperSql.contains("INFORMATION_SCHEMA") || 
            upperSql.contains("MYSQL.USER") || 
            upperSql.contains("SYS.") ||
            upperSql.contains("PERFORMANCE_SCHEMA")) {
            throw ServiceExceptionUtil.exception(new ErrorCode(403, "不允许访问系统表"));
        }
    }

}
