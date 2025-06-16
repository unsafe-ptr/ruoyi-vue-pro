package cn.iocoder.yudao.framework.websocket.core.annotation;

import java.lang.annotation.*;

/**
 * 租户感知注解
 * 
 * 用于标记需要进行租户隔离处理的WebSocket消息发送方法
 * 
 * @author 芋道源码
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantAware {

    /**
     * 租户ID参数名
     * 当方法参数中包含租户ID时，指定参数名
     */
    String tenantIdParam() default "tenantId";

    /**
     * 是否强制要求租户上下文
     * 如果为true，当没有租户上下文时会抛出异常；
     * 如果为false，当没有租户上下文时会忽略消息发送
     */
    boolean required() default false;

}