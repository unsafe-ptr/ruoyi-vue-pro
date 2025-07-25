package cn.iocoder.yudao.module.promotion.service.banner;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerUpdateReqVO;
import cn.iocoder.yudao.module.promotion.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.banner.BannerMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.BANNER_NOT_EXISTS;

/**
 * 首页 banner 实现类
 *
 * @author xia
 */
@Service
@Validated
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public Long createBanner(BannerCreateReqVO createReqVO) {
        // 插入
        BannerDO banner = BannerConvert.INSTANCE.convert(createReqVO);
        bannerMapper.insert(banner);
        // 返回
        return banner.getId();
    }

    @Override
    public void updateBanner(BannerUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateBannerExists(updateReqVO.getId());
        // 更新
        BannerDO updateObj = BannerConvert.INSTANCE.convert(updateReqVO);
        bannerMapper.updateById(updateObj);
    }

    @Override
    public void deleteBanner(Long id) {
        // 校验存在
        this.validateBannerExists(id);
        // 删除
        bannerMapper.deleteById(id);
    }

    private void validateBannerExists(Long id) {
        if (bannerMapper.selectById(id) == null) {
            throw exception(BANNER_NOT_EXISTS);
        }
    }

    @Override
    public BannerDO getBanner(Long id) {
        return bannerMapper.selectById(id);
    }

    @Override
    public PageResult<BannerDO> getBannerPage(BannerPageReqVO pageReqVO) {
        return bannerMapper.selectPage(pageReqVO);
    }

    @Override
    public void addBannerBrowseCount(Long id) {
        // 校验 Banner 是否存在
        validateBannerExists(id);
        // 增加点击次数
        bannerMapper.updateBrowseCount(id);
    }

    @Override
    public List<BannerDO> getBannerListByPosition(Integer position) {
        return bannerMapper.selectBannerListByPosition(position);
    }

    @Override
    public void batchDeleteBanner(List<Long> ids) {
        // 校验存在
        for (Long id : ids) {
            validateBannerExists(id);
        }
        // 批量删除
        bannerMapper.deleteBatchIds(ids);
    }

    @Override
    public void batchUpdateBannerStatus(List<Long> ids, Integer status) {
        // 校验存在
        for (Long id : ids) {
            validateBannerExists(id);
        }
        // 批量更新状态
        bannerMapper.updateBatchStatus(ids, status);
    }

    @Override
    public void updateBannerSort(Long id, Integer sort) {
        // 校验存在
        validateBannerExists(id);
        // 更新排序
        bannerMapper.updateSort(id, sort);
    }

    @Override
    public BannerStatisticsRespVO getBannerStatistics() {
        BannerStatisticsRespVO statistics = new BannerStatisticsRespVO();

        // 基础统计
        statistics.setTotalCount(bannerMapper.selectCount(null));
        statistics.setEnabledCount(bannerMapper.selectCountByStatus(1)); // 启用状态
        statistics.setDisabledCount(bannerMapper.selectCountByStatus(0)); // 禁用状态

        // 时间范围统计
        LocalDateTime todayStart = LocalDateTimeUtils.getToday();
        LocalDateTime weekStart = LocalDateTimeUtils.getWeekStart();
        statistics.setTodayCount(bannerMapper.selectCountByCreateTime(todayStart, null));
        statistics.setWeekCount(bannerMapper.selectCountByCreateTime(weekStart, null));

        // 点击量统计
        statistics.setTotalBrowseCount(bannerMapper.selectTotalBrowseCount());
        statistics.setTodayBrowseCount(bannerMapper.selectBrowseCountByDate(todayStart, null));
        
        // 计算平均点击量
        Long totalCount = statistics.getTotalCount();
        if (totalCount > 0) {
            statistics.setAvgBrowseCount(NumberUtil.div(statistics.getTotalBrowseCount(), totalCount, 2).doubleValue());
        } else {
            statistics.setAvgBrowseCount(0.0);
        }

        // 最大点击量
        statistics.setMaxBrowseCount(bannerMapper.selectMaxBrowseCount());

        // 点击率计算 (假设每个Banner展示1000次)
        Long totalBrowse = statistics.getTotalBrowseCount();
        Long totalShow = totalCount * 1000; // 假设数据
        if (totalShow > 0) {
            statistics.setClickRate(NumberUtil.div(totalBrowse * 100, totalShow, 2).doubleValue());
        } else {
            statistics.setClickRate(0.0);
        }

        statistics.setUpdateTime(LocalDateTime.now());
        return statistics;
    }

    @Override
    public List<BannerDO> getHotBanners(Integer limit) {
        return bannerMapper.selectHotBanners(limit);
    }

}
