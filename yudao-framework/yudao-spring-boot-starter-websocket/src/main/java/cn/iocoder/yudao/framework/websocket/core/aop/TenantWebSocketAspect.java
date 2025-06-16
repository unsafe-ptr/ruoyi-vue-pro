package cn.iocoder.yudao.framework.websocket.core.aop;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.framework.websocket.core.annotation.TenantAware;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 租户感知的WebSocket AOP切面
 * 
 * 自动处理带有 {@link TenantAware} 注解的方法的租户上下文
 * 
 * @author 芋道源码
 */
@Aspect
@Slf4j
public class TenantWebSocketAspect {

    @Around("@annotation(cn.iocoder.yudao.framework.websocket.core.annotation.TenantAware) || " +
            "@within(cn.iocoder.yudao.framework.websocket.core.annotation.TenantAware)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
        TenantAware tenantAware = AnnotationUtils.findAnnotation(method, TenantAware.class);
        
        // 如果方法上没有，尝试从类上获取
        if (tenantAware == null) {
            tenantAware = AnnotationUtils.findAnnotation(method.getDeclaringClass(), TenantAware.class);
        }
        
        if (tenantAware == null) {
            return joinPoint.proceed();
        }

        // 尝试从方法参数中获取租户ID
        Long tenantId = extractTenantIdFromArgs(joinPoint, tenantAware.tenantIdParam());
        
        // 如果参数中没有租户ID，尝试从当前上下文获取
        if (tenantId == null) {
            tenantId = TenantContextHolder.getTenantId();
        }

        // 根据配置决定如何处理
        if (tenantId == null) {
            if (tenantAware.required()) {
                throw new IllegalStateException("租户上下文缺失，无法发送WebSocket消息");
            } else {
                log.debug("[TenantWebSocketAspect] 租户上下文缺失，跳过WebSocket消息发送: {}", method.getName());
                return null; // 跳过执行
            }
        }

        // 在指定租户上下文中执行
        return TenantUtils.execute(tenantId, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                }
                throw new RuntimeException(throwable);
            }
        });
    }

    /**
     * 从方法参数中提取租户ID
     */
    private Long extractTenantIdFromArgs(ProceedingJoinPoint joinPoint, String tenantIdParam) {
        Object[] args = joinPoint.getArgs();
        Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (tenantIdParam.equals(parameters[i].getName()) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }
        return null;
    }
}