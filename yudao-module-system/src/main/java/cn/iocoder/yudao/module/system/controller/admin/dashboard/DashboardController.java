package cn.iocoder.yudao.module.system.controller.admin.dashboard;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardSummaryRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardTrendRespVO;
import cn.iocoder.yudao.module.system.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 工作台首页
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - 工作台首页")
@RestController
@RequestMapping("/system/dashboard")
@Validated
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "获取工作台统计数据")
    @PreAuthorize("@ss.hasPermission('system:dashboard:query')")
    public CommonResult<DashboardSummaryRespVO> getDashboardSummary() {
        DashboardSummaryRespVO summary = dashboardService.getDashboardSummary();
        return success(summary);
    }

    @GetMapping("/user-trend")
    @Operation(summary = "获取用户趋势数据")
    @PreAuthorize("@ss.hasPermission('system:dashboard:query')")
    public CommonResult<List<DashboardTrendRespVO>> getUserTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<DashboardTrendRespVO> trend = dashboardService.getUserTrend(days);
        return success(trend);
    }

    @GetMapping("/login-trend")
    @Operation(summary = "获取登录趋势数据")
    @PreAuthorize("@ss.hasPermission('system:dashboard:query')")
    public CommonResult<List<DashboardTrendRespVO>> getLoginTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<DashboardTrendRespVO> trend = dashboardService.getLoginTrend(days);
        return success(trend);
    }

    @GetMapping("/system-info")
    @Operation(summary = "获取系统信息")
    @PreAuthorize("@ss.hasPermission('system:dashboard:query')")
    public CommonResult<Object> getSystemInfo() {
        Object systemInfo = dashboardService.getSystemInfo();
        return success(systemInfo);
    }
}