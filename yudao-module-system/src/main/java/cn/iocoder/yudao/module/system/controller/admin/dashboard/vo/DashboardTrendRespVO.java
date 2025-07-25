package cn.iocoder.yudao.module.system.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 工作台趋势数据 Response VO
 *
 * @author 芋道源码
 */
@Schema(description = "管理后台 - 工作台趋势数据 Response VO")
@Data
public class DashboardTrendRespVO {

    @Schema(description = "日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01")
    private LocalDate date;

    @Schema(description = "数值", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long value;

    @Schema(description = "标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "新增用户")
    private String label;

}