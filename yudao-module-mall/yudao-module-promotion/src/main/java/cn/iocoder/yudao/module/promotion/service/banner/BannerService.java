package cn.iocoder.yudao.module.promotion.service.banner;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerStatisticsRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 首页 Banner Service 接口
 *
 * @author xia
 */
public interface BannerService {

    /**
     * 创建 Banner
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBanner(@Valid BannerCreateReqVO createReqVO);

    /**
     * 更新 Banner
     *
     * @param updateReqVO 更新信息
     */
    void updateBanner(@Valid BannerUpdateReqVO updateReqVO);

    /**
     * 删除 Banner
     *
     * @param id 编号
     */
    void deleteBanner(Long id);

    /**
     * 获得 Banner
     *
     * @param id 编号
     * @return Banner
     */
    BannerDO getBanner(Long id);

    /**
     * 获得 Banner 分页
     *
     * @param pageReqVO 分页查询
     * @return Banner分页
     */
    PageResult<BannerDO> getBannerPage(BannerPageReqVO pageReqVO);

    /**
     * 增加 Banner 点击量
     *
     * @param id Banner编号
     */
    void addBannerBrowseCount(Long id);

    /**
     * 获得 Banner 列表
     *
     * @param position 定位
     * @return Banner 列表
     */
    List<BannerDO> getBannerListByPosition(Integer position);

    /**
     * 批量删除 Banner
     *
     * @param ids Banner编号列表
     */
    void batchDeleteBanner(List<Long> ids);

    /**
     * 批量更新 Banner 状态
     *
     * @param ids Banner编号列表
     * @param status 状态
     */
    void batchUpdateBannerStatus(List<Long> ids, Integer status);

    /**
     * 更新 Banner 排序
     *
     * @param id Banner编号
     * @param sort 排序
     */
    void updateBannerSort(Long id, Integer sort);

    /**
     * 获取 Banner 统计数据
     *
     * @return 统计数据
     */
    BannerStatisticsRespVO getBannerStatistics();

    /**
     * 获取热门 Banner 列表
     *
     * @param limit 限制数量
     * @return Banner 列表
     */
    List<BannerDO> getHotBanners(Integer limit);

}
