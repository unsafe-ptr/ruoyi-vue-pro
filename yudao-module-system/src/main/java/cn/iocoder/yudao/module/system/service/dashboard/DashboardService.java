package cn.iocoder.yudao.module.system.service.dashboard;

import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardSummaryRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardTrendRespVO;

import java.util.List;

/**
 * 工作台首页 Service 接口
 *
 * @author 芋道源码
 */
public interface DashboardService {

    /**
     * 获取工作台统计数据
     *
     * @return 统计数据
     */
    DashboardSummaryRespVO getDashboardSummary();

    /**
     * 获取用户趋势数据
     *
     * @param days 天数
     * @return 趋势数据
     */
    List<DashboardTrendRespVO> getUserTrend(Integer days);

    /**
     * 获取登录趋势数据
     *
     * @param days 天数
     * @return 趋势数据
     */
    List<DashboardTrendRespVO> getLoginTrend(Integer days);

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    Object getSystemInfo();

}