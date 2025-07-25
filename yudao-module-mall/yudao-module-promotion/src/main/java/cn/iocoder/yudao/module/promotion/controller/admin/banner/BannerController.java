package cn.iocoder.yudao.module.promotion.controller.admin.banner;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerStatisticsRespVO;
import cn.iocoder.yudao.module.promotion.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.promotion.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - Banner 管理")
@RestController
@RequestMapping("/promotion/banner")
@Validated
public class BannerController {

    @Resource
    private BannerService bannerService;

    @PostMapping("/create")
    @Operation(summary = "创建 Banner")
    @PreAuthorize("@ss.hasPermission('promotion:banner:create')")
    public CommonResult<Long> createBanner(@Valid @RequestBody BannerCreateReqVO createReqVO) {
        return success(bannerService.createBanner(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 Banner")
    @PreAuthorize("@ss.hasPermission('promotion:banner:update')")
    public CommonResult<Boolean> updateBanner(@Valid @RequestBody BannerUpdateReqVO updateReqVO) {
        bannerService.updateBanner(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 Banner")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:banner:delete')")
    public CommonResult<Boolean> deleteBanner(@RequestParam("id") Long id) {
        bannerService.deleteBanner(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 Banner")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:banner:query')")
    public CommonResult<BannerRespVO> getBanner(@RequestParam("id") Long id) {
        BannerDO banner = bannerService.getBanner(id);
        return success(BannerConvert.INSTANCE.convert(banner));
    }

    @GetMapping("/page")
    @Operation(summary = "获得 Banner 分页")
    @PreAuthorize("@ss.hasPermission('promotion:banner:query')")
    public CommonResult<PageResult<BannerRespVO>> getBannerPage(@Valid BannerPageReqVO pageVO) {
        PageResult<BannerDO> pageResult = bannerService.getBannerPage(pageVO);
        return success(BannerConvert.INSTANCE.convertPage(pageResult));
    }

    // 新增的优化功能

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除 Banner")
    @PreAuthorize("@ss.hasPermission('promotion:banner:delete')")
    public CommonResult<Boolean> batchDeleteBanner(@RequestBody List<Long> ids) {
        bannerService.batchDeleteBanner(ids);
        return success(true);
    }

    @PostMapping("/batch-update-status")
    @Operation(summary = "批量更新 Banner 状态")
    @PreAuthorize("@ss.hasPermission('promotion:banner:update')")
    public CommonResult<Boolean> batchUpdateStatus(@RequestParam("ids") List<Long> ids, 
                                                  @RequestParam("status") Integer status) {
        bannerService.batchUpdateBannerStatus(ids, status);
        return success(true);
    }

    @PostMapping("/update-sort")
    @Operation(summary = "更新 Banner 排序")
    @PreAuthorize("@ss.hasPermission('promotion:banner:update')")
    public CommonResult<Boolean> updateBannerSort(@RequestParam("id") Long id, 
                                                  @RequestParam("sort") Integer sort) {
        bannerService.updateBannerSort(id, sort);
        return success(true);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取 Banner 统计数据")
    @PreAuthorize("@ss.hasPermission('promotion:banner:query')")
    public CommonResult<BannerStatisticsRespVO> getBannerStatistics() {
        BannerStatisticsRespVO statistics = bannerService.getBannerStatistics();
        return success(statistics);
    }

    @GetMapping("/hot-banners")
    @Operation(summary = "获取热门 Banner 列表")
    @PreAuthorize("@ss.hasPermission('promotion:banner:query')")
    public CommonResult<List<BannerRespVO>> getHotBanners(@RequestParam(defaultValue = "10") Integer limit) {
        List<BannerDO> hotBanners = bannerService.getHotBanners(limit);
        return success(BannerConvert.INSTANCE.convertList(hotBanners));
    }

}
