package cn.iocoder.yudao.module.crm.util;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.spring.SpringUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionDO;
import cn.iocoder.yudao.module.crm.enums.common.CrmBizTypeEnum;
import cn.iocoder.yudao.module.crm.enums.common.CrmSceneTypeEnum;
import cn.iocoder.yudao.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.system.enums.permission.RoleCodeEnum;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.autoconfigure.MybatisPlusJoinProperties;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.MPJJoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.interfaces.Query;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * CRM 权限 Util
 *
 * @author HUIHUI
 */
public class CrmPermissionUtils {

    /**
     * 下属用户缓存，避免重复查询数据库
     * 缓存时间：5分钟
     */
    private static final Cache<Long, List<AdminUserRespDTO>> SUBORDINATE_USERS_CACHE = 
        CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .maximumSize(1000)
            .build();

    /**
     * 校验用户是否是 CRM 管理员
     *
     * @return 是/否
     */
    public static boolean isCrmAdmin() {
        PermissionApi permissionApi = SpringUtil.getBean(PermissionApi.class);
        return permissionApi.hasAnyRoles(SecurityFrameworkUtils.getLoginUserId(), RoleCodeEnum.CRM_ADMIN.getCode());
    }

    /**
     * 获取下属用户列表（带缓存）
     */
    private static List<AdminUserRespDTO> getSubordinateUsersWithCache(Long userId) {
        return SUBORDINATE_USERS_CACHE.get(userId, () -> {
            AdminUserApi adminUserApi = SpringUtil.getBean(AdminUserApi.class);
            return adminUserApi.getUserListBySubordinate(userId);
        });
    }

    /**
     * 拼接CRM 数据权限的查询条件
     *
     * @param query      连表的 Wrapper
     * @param bizType    CRM 类型
     * @param bizId      CRM 类型对应的编号字段
     * @param userId     当前登录用户编号
     * @param sceneType  场景类型
     */
    public static <T> void appendPermissionCondition(MPJLambdaWrapper<T> query, Integer bizType, MFunction<T, ?> bizId,
                                                                                    Long userId, Integer sceneType) {
        MybatisPlusJoinProperties mybatisPlusJoinProperties = SpringUtil.getBean(MybatisPlusJoinProperties.class);
        final String ownerUserIdField = mybatisPlusJoinProperties.getTableAlias() + ".owner_user_id";
        // 场景一：我负责的数据
        if (CrmSceneTypeEnum.isOwner(sceneType)) {
            query.eq(ownerUserIdField, userId);
        }
        // 场景二：我参与的数据（我有读或写权限，并且不是负责人）
        if (CrmSceneTypeEnum.isInvolved(sceneType)) {
            if (CrmPermissionUtils.isCrmAdmin()) { // 特殊逻辑：如果是超管，直接查询所有，不过滤数据权限
                return;
            }
            query.innerJoin(CrmPermissionDO.class, on -> on.eq(CrmPermissionDO::getBizType, bizType)
                    .eq(CrmPermissionDO::getBizId, bizId)
                    .in(CrmPermissionDO::getLevel, CrmPermissionLevelEnum.READ.getLevel(), CrmPermissionLevelEnum.WRITE.getLevel())
                    .eq(CrmPermissionDO::getUserId,userId));
            query.ne(ownerUserIdField, userId);
        }
        // 场景三：下属负责的数据（下属是负责人）- 优化：使用缓存
        if (CrmSceneTypeEnum.isSubordinate(sceneType)) {
            try {
                List<AdminUserRespDTO> subordinateUsers = getSubordinateUsersWithCache(userId);
                if (CollUtil.isEmpty(subordinateUsers)) {
                    query.eq(ownerUserIdField, -1); // 不返回任何结果
                } else {
                    query.in(ownerUserIdField, convertSet(subordinateUsers, AdminUserRespDTO::getId));
                }
            } catch (Exception e) {
                // 缓存获取失败时的降级处理
                AdminUserApi adminUserApi = SpringUtil.getBean(AdminUserApi.class);
                List<AdminUserRespDTO> subordinateUsers = adminUserApi.getUserListBySubordinate(userId);
                if (CollUtil.isEmpty(subordinateUsers)) {
                    query.eq(ownerUserIdField, -1);
                } else {
                    query.in(ownerUserIdField, convertSet(subordinateUsers, AdminUserRespDTO::getId));
                }
            }
        }
    }

}
