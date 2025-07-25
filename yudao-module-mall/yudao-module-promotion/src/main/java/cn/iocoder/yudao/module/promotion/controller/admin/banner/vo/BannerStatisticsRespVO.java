package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Banner 统计数据 Response VO
 *
 * @author 芋道源码
 */
@Schema(description = "管理后台 - Banner 统计数据 Response VO")
@Data
public class BannerStatisticsRespVO {

    @Schema(description = "Banner 总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Long totalCount;

    @Schema(description = "启用的 Banner 数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
    private Long enabledCount;

    @Schema(description = "禁用的 Banner 数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    private Long disabledCount;

    @Schema(description = "今日新增 Banner 数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long todayCount;

    @Schema(description = "本周新增 Banner 数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "15")
    private Long weekCount;

    @Schema(description = "总点击量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000")
    private Long totalBrowseCount;

    @Schema(description = "今日点击量", requiredMode = Schema.RequiredMode.REQUIRED, example = "500")
    private Long todayBrowseCount;

    @Schema(description = "平均点击量", requiredMode = Schema.RequiredMode.REQUIRED, example = "200.5")
    private Double avgBrowseCount;

    @Schema(description = "最热门 Banner 点击量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000")
    private Long maxBrowseCount;

    @Schema(description = "点击率", requiredMode = Schema.RequiredMode.REQUIRED, example = "15.5")
    private Double clickRate;

    @Schema(description = "最后更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}