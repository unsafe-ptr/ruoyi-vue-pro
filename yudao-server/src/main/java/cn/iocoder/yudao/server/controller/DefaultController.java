package cn.iocoder.yudao.server.controller;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;

/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author 芋道源码
 */
@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/admin-api/bpm/**")
    public CommonResult<Boolean> bpm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[工作流模块 yudao-module-bpm - 已禁用][参考 https://doc.iocoder.cn/bpm/ 开启]");
    }

    @RequestMapping("/admin-api/mp/**")
    public CommonResult<Boolean> mp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[微信公众号 yudao-module-mp - 已禁用][参考 https://doc.iocoder.cn/mp/build/ 开启]");
    }

    @RequestMapping(value = { "/admin-api/product/**", // 商品中心
            "/admin-api/trade/**", // 交易中心
            "/admin-api/promotion/**" }) // 营销中心
    public CommonResult<Boolean> mall404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[商城系统 yudao-module-mall - 已禁用][参考 https://doc.iocoder.cn/mall/build/ 开启]");
    }

    @RequestMapping("/admin-api/erp/**")
    public CommonResult<Boolean> erp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[ERP 模块 yudao-module-erp - 已禁用][参考 https://doc.iocoder.cn/erp/build/ 开启]");
    }

    @RequestMapping("/admin-api/crm/**")
    public CommonResult<Boolean> crm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[CRM 模块 yudao-module-crm - 已禁用][参考 https://doc.iocoder.cn/crm/build/ 开启]");
    }

    @RequestMapping(value = { "/admin-api/report/**"})
    public CommonResult<Boolean> report404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[报表模块 yudao-module-report - 已禁用][参考 https://doc.iocoder.cn/report/ 开启]");
    }

    @RequestMapping(value = { "/admin-api/pay/**"})
    public CommonResult<Boolean> pay404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[支付模块 yudao-module-pay - 已禁用][参考 https://doc.iocoder.cn/pay/build/ 开启]");
    }

    @RequestMapping(value = { "/admin-api/ai/**"})
    public CommonResult<Boolean> ai404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[AI 大模型 yudao-module-ai - 已禁用][参考 https://doc.iocoder.cn/ai/build/ 开启]");
    }

    @RequestMapping(value = { "/admin-api/iot/**"})
    public CommonResult<Boolean> iot404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[IoT 物联网 yudao-module-iot - 已禁用][参考 https://doc.iocoder.cn/iot/build/ 开启]");
    }

    /**
     * 测试接口：仅用于系统健康检查
     * 注意：生产环境建议完全移除此接口或加强权限控制
     */
    @RequestMapping(value = { "/test" })
    @PermitAll
    public CommonResult<Boolean> test(HttpServletRequest request) {
        // 安全改进：不再记录敏感的请求信息，只返回基本状态
        String clientIp = ServletUtils.getClientIP(request);
        log.info("Health check request from IP: {}", clientIp);
        
        // 限制响应信息，避免暴露系统内部结构
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("timestamp", System.currentTimeMillis());
        
        return CommonResult.success(true);
    }

}
