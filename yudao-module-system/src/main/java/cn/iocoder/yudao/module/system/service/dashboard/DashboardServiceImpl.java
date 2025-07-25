package cn.iocoder.yudao.module.system.service.dashboard;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.SystemUtil;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.framework.redis.core.RedisKeyRegistry;
import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardSummaryRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dashboard.vo.DashboardTrendRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.loginlog.LoginLogDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.loginlog.LoginLogService;
import cn.iocoder.yudao.module.system.service.menu.MenuService;
import cn.iocoder.yudao.module.system.service.permission.RoleService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台首页 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private RoleService roleService;
    @Resource
    private DeptService deptService;
    @Resource
    private MenuService menuService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public DashboardSummaryRespVO getDashboardSummary() {
        DashboardSummaryRespVO summary = new DashboardSummaryRespVO();

        // 基础统计数据
        summary.setUserCount(adminUserService.getUserCount());
        summary.setRoleCount(roleService.getRoleCount());
        summary.setDeptCount(deptService.getDeptCount());
        summary.setMenuCount(menuService.getMenuCount());

        // 今日数据统计
        LocalDateTime todayStart = LocalDateTimeUtils.getToday();
        summary.setTodayUserCount(adminUserService.getUserCountByCreateTime(todayStart));
        summary.setTodayLoginCount(loginLogService.getLoginCountByTime(todayStart, null));

        // 在线用户数 (通过 Redis Session 统计)
        summary.setOnlineUserCount(getOnlineUserCount());

        // 系统信息
        summary.setSystemUptime(getSystemUptime());
        summary.setCpuUsage(getCpuUsage());
        summary.setMemoryUsage(getMemoryUsage());
        summary.setDiskUsage(getDiskUsage());
        summary.setJvmMemoryUsage(getJvmMemoryUsage());
        summary.setDatabaseConnections(getDatabaseConnections());
        summary.setRedisStatus(getRedisStatus());

        // 访问量统计
        summary.setTodayVisitCount(getVisitCount(todayStart, null));
        summary.setWeekVisitCount(getVisitCount(LocalDateTimeUtils.getWeekStart(), null));
        summary.setMonthVisitCount(getVisitCount(LocalDateTimeUtils.getMonthStart(), null));

        summary.setUpdateTime(LocalDateTime.now());
        return summary;
    }

    @Override
    public List<DashboardTrendRespVO> getUserTrend(Integer days) {
        List<DashboardTrendRespVO> result = new ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DashboardTrendRespVO trend = new DashboardTrendRespVO();
            trend.setDate(date);
            
            LocalDateTime dayStart = LocalDateTimeUtils.getDayStart(date);
            LocalDateTime dayEnd = LocalDateTimeUtils.getDayEnd(date);
            
            Long userCount = adminUserService.getUserCountByCreateTime(dayStart, dayEnd);
            trend.setValue(userCount);
            trend.setLabel("新增用户");
            
            result.add(trend);
        }
        return result;
    }

    @Override
    public List<DashboardTrendRespVO> getLoginTrend(Integer days) {
        List<DashboardTrendRespVO> result = new ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DashboardTrendRespVO trend = new DashboardTrendRespVO();
            trend.setDate(date);
            
            LocalDateTime dayStart = LocalDateTimeUtils.getDayStart(date);
            LocalDateTime dayEnd = LocalDateTimeUtils.getDayEnd(date);
            
            Long loginCount = loginLogService.getLoginCountByTime(dayStart, dayEnd);
            trend.setValue(loginCount);
            trend.setLabel("登录次数");
            
            result.add(trend);
        }
        return result;
    }

    @Override
    public Object getSystemInfo() {
        Map<String, Object> systemInfo = new LinkedHashMap<>();
        
        // 操作系统信息
        systemInfo.put("osName", SystemUtil.getOsInfo().getName());
        systemInfo.put("osArch", SystemUtil.getOsInfo().getArch());
        systemInfo.put("osVersion", SystemUtil.getOsInfo().getVersion());
        
        // Java 信息
        systemInfo.put("javaVersion", SystemUtil.getJavaInfo().getVersion());
        systemInfo.put("javaVendor", SystemUtil.getJavaInfo().getVendor());
        systemInfo.put("javaHome", SystemUtil.getJavaInfo().getHomeDir());
        
        // JVM 信息
        systemInfo.put("jvmName", SystemUtil.getJvmInfo().getName());
        systemInfo.put("jvmVersion", SystemUtil.getJvmInfo().getVersion());
        systemInfo.put("jvmVendor", SystemUtil.getJvmInfo().getVendor());
        
        return systemInfo;
    }

    /**
     * 获取在线用户数
     */
    private Long getOnlineUserCount() {
        try {
            // 通过 Redis 中的 session key 数量来统计在线用户
            return (long) redisTemplate.keys("spring:session:*").size();
        } catch (Exception e) {
            log.warn("获取在线用户数失败", e);
            return 0L;
        }
    }

    /**
     * 获取系统运行时间
     */
    private String getSystemUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        return formatUptime(uptime);
    }

    /**
     * 获取 CPU 使用率
     */
    private Double getCpuUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            return NumberUtil.round(osBean.getProcessCpuLoad() * 100, 2).doubleValue();
        } catch (Exception e) {
            log.warn("获取CPU使用率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取内存使用率
     */
    private Double getMemoryUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
                long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
                long freeMemory = sunOsBean.getFreePhysicalMemorySize();
                long usedMemory = totalMemory - freeMemory;
                return NumberUtil.round((double) usedMemory / totalMemory * 100, 2).doubleValue();
            }
        } catch (Exception e) {
            log.warn("获取内存使用率失败", e);
        }
        return 0.0;
    }

    /**
     * 获取磁盘使用率
     */
    private Double getDiskUsage() {
        try {
            return NumberUtil.round(SystemUtil.getTotalSpace() / (double) SystemUtil.getUsableSpace() * 100, 2).doubleValue();
        } catch (Exception e) {
            log.warn("获取磁盘使用率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取 JVM 内存使用率
     */
    private Double getJvmMemoryUsage() {
        try {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            return NumberUtil.round((double) usedMemory / maxMemory * 100, 2).doubleValue();
        } catch (Exception e) {
            log.warn("获取JVM内存使用率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取数据库连接数
     */
    private Integer getDatabaseConnections() {
        // 这里需要根据实际的数据库连接池实现来获取
        // 暂时返回模拟数据
        return 8;
    }

    /**
     * 获取 Redis 连接状态
     */
    private String getRedisStatus() {
        try {
            redisTemplate.opsForValue().get("test");
            return "连接正常";
        } catch (Exception e) {
            return "连接异常";
        }
    }

    /**
     * 获取访问量
     */
    private Long getVisitCount(LocalDateTime startTime, LocalDateTime endTime) {
        // 这里通过登录日志来模拟访问量，实际项目中可能需要专门的访问统计
        return loginLogService.getLoginCountByTime(startTime, endTime);
    }

    /**
     * 格式化运行时间
     */
    private String formatUptime(long uptime) {
        long days = uptime / (24 * 60 * 60 * 1000);
        long hours = (uptime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (uptime % (60 * 60 * 1000)) / (60 * 1000);
        
        if (days > 0) {
            return String.format("%d天%d小时%d分钟", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%d小时%d分钟", hours, minutes);
        } else {
            return String.format("%d分钟", minutes);
        }
    }

}