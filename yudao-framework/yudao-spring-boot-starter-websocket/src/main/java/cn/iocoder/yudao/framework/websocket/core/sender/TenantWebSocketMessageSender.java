package cn.iocoder.yudao.framework.websocket.core.sender;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import lombok.RequiredArgsConstructor;

/**
 * 租户感知的 WebSocket 消息发送器装饰类
 * 
 * 自动处理租户上下文，确保消息只发送给当前租户或指定租户的用户
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class TenantWebSocketMessageSender implements WebSocketMessageSender {

    private final WebSocketMessageSender delegate;

    @Override
    public void send(Integer userType, Long userId, String messageType, String messageContent) {
        // 如果当前有租户上下文，直接发送；否则跳过（避免跨租户发送）
        if (TenantContextHolder.getTenantId() != null) {
            delegate.send(userType, userId, messageType, messageContent);
        }
    }

    @Override
    public void send(Integer userType, String messageType, String messageContent) {
        // 如果当前有租户上下文，直接发送；否则跳过（避免跨租户发送）
        if (TenantContextHolder.getTenantId() != null) {
            delegate.send(userType, messageType, messageContent);
        }
    }

    @Override
    public void send(String sessionId, String messageType, String messageContent) {
        // Session级别发送不受租户限制
        delegate.send(sessionId, messageType, messageContent);
    }

    /**
     * 发送消息给指定租户的指定用户
     */
    public void sendToTenant(Long tenantId, Integer userType, Long userId, String messageType, String messageContent) {
        TenantUtils.execute(tenantId, () -> {
            delegate.send(userType, userId, messageType, messageContent);
        });
    }

    /**
     * 发送消息给指定租户的指定用户类型
     */
    public void sendToTenant(Long tenantId, Integer userType, String messageType, String messageContent) {
        TenantUtils.execute(tenantId, () -> {
            delegate.send(userType, messageType, messageContent);
        });
    }

    /**
     * 获取原始的委托发送器（用于特殊情况下的直接访问）
     */
    public WebSocketMessageSender getDelegate() {
        return delegate;
    }
}