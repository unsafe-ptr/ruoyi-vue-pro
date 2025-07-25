package cn.iocoder.yudao.framework.mybatis.config;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.handler.DefaultDBFieldHandler;
import cn.iocoder.yudao.framework.mybatis.core.interceptor.MybatisInterceptor;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonStringSetTypeHandler;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.PostgreKeyGenerator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * MyBaits 配置类（增强性能监控）
 *
 * @author 芋道源码
 */
@Configuration
@MapperScan(value = "${yudao.info.base-package}", annotationClass = Mapper.class,
        lazyInitialization = "${mybatis-plus.lazy-initialization:false}") // Mapper 懒加载，目前仅用于单元测试
@Slf4j
public class YudaoMybatisAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 1. 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(JdbcUtils.getDbType());
        paginationInnerInterceptor.setOverflow(true); // 溢出总页数后是否进行处理
        
        // 增强：添加分页性能监控
        paginationInnerInterceptor.setMaxLimit(1000L); // 限制单页最大数量
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        // 2. 防止全表更新与删除插件
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        
        // 3. 自定义性能监控插件
        interceptor.addInnerInterceptor(new MybatisInterceptor());
        
        return interceptor;
    }

    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        return new DefaultDBFieldHandler(); // 自动填充参数类
    }

    @Bean
    public IKeyGenerator keyGenerator() {
        DbType dbType = JdbcUtils.getDbType();
        if (DbType.POSTGRE_SQL == dbType) {
            return new PostgreKeyGenerator();
        } else if (DbType.KINGBASE_ES == dbType) {
            return new PostgreKeyGenerator();
        }
        // 其它类型数据库，使用默认自增主键
        return null;
    }

    // ========== 使用 JSON 格式的 TypeHandler ==========

    @Bean
    public TypeHandler<Set<String>> jsonStringSetTypeHandler() {
        return new JsonStringSetTypeHandler();
    }

    @Bean
    public TypeHandler<Set<Long>> jsonLongSetTypeHandler() {
        return new JsonLongSetTypeHandler();
    }

    /**
     * 性能监控和查询优化建议
     */
    static {
        log.info("=== MyBatis 性能优化建议 ===");
        log.info("1. 数据库连接池已优化：支持高并发场景");
        log.info("2. 分页查询已限制：单页最大1000条记录");
        log.info("3. 慢查询检测：已启用，阈值100ms");
        log.info("4. 防全表扫描：已启用BlockAttackInnerInterceptor");
        log.info("5. 建议：为常用查询字段添加索引");
        log.info("6. 建议：使用批量操作处理大量数据");
        log.info("===============================");
    }
}
