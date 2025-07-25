package cn.iocoder.yudao.module.system.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作台统计数据 Response VO
 *
 * @author 芋道源码
 */
@Schema(description = "管理后台 - 工作台统计数据 Response VO")
@Data
public class DashboardSummaryRespVO {

    @Schema(description = "用户总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userCount;

    @Schema(description = "今日新增用户数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long todayUserCount;

    @Schema(description = "角色总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    private Long roleCount;

    @Schema(description = "部门总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "15")
    private Long deptCount;

    @Schema(description = "菜单总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long menuCount;

    @Schema(description = "今日登录次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Long todayLoginCount;

    @Schema(description = "在线用户数", requiredMode = Schema.RequiredMode.REQUIRED, example = "25")
    private Long onlineUserCount;

    @Schema(description = "系统运行时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "72小时")
    private String systemUptime;

    @Schema(description = "CPU 使用率", requiredMode = Schema.RequiredMode.REQUIRED, example = "65.5")
    private Double cpuUsage;

    @Schema(description = "内存使用率", requiredMode = Schema.RequiredMode.REQUIRED, example = "75.2")
    private Double memoryUsage;

    @Schema(description = "磁盘使用率", requiredMode = Schema.RequiredMode.REQUIRED, example = "45.8")
    private Double diskUsage;

    @Schema(description = "JVM 内存使用率", requiredMode = Schema.RequiredMode.REQUIRED, example = "60.3")
    private Double jvmMemoryUsage;

    @Schema(description = "数据库连接数", requiredMode = Schema.RequiredMode.REQUIRED, example = "8")
    private Integer databaseConnections;

    @Schema(description = "Redis 连接状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "连接正常")
    private String redisStatus;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "今日访问量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1500")
    private Long todayVisitCount;

    @Schema(description = "本周访问量", requiredMode = Schema.RequiredMode.REQUIRED, example = "8500")
    private Long weekVisitCount;

    @Schema(description = "本月访问量", requiredMode = Schema.RequiredMode.REQUIRED, example = "25000")
    private Long monthVisitCount;

}