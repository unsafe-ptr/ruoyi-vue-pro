package cn.iocoder.yudao.module.infra.framework.file.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.*;

/**
 * 路径验证工具类
 * 
 * @author 芋道源码
 */
public class PathValidationUtils {

    /**
     * 验证并清理目录路径，防止路径遍历攻击
     *
     * @param directory 原始目录路径
     * @return 清理后的目录路径
     */
    public static String validateAndCleanDirectory(String directory) {
        if (StrUtil.isEmpty(directory)) {
            return null;
        }

        // 1. 检查是否包含路径遍历攻击的字符序列
        String normalizedPath = directory.replace('\\', '/'); // 统一使用正斜杠
        if (containsPathTraversal(normalizedPath)) {
            throw ServiceExceptionUtil.exception(FILE_PATH_INVALID);
        }

        // 2. 清理路径，移除多余的斜杠和点
        String cleanedPath = cleanPath(normalizedPath);
        
        // 3. 确保路径不以斜杠开头或结尾
        if (cleanedPath.startsWith("/")) {
            cleanedPath = cleanedPath.substring(1);
        }
        if (cleanedPath.endsWith("/")) {
            cleanedPath = cleanedPath.substring(0, cleanedPath.length() - 1);
        }

        return cleanedPath.isEmpty() ? null : cleanedPath;
    }

    /**
     * 检查路径是否包含路径遍历攻击
     *
     * @param path 路径
     * @return 是否包含路径遍历攻击
     */
    private static boolean containsPathTraversal(String path) {
        if (StrUtil.isEmpty(path)) {
            return false;
        }

        // 检查常见的路径遍历模式
        String[] dangerousPatterns = {
            "..", "..\\", "../", "..%2f", "..%5c", "..%2F", "..%5C",
            "%2e%2e", "%2E%2E", "%2e%2e%2f", "%2E%2E%2F",
            "....//", "....\\\\", "....%2f", "....%5c"
        };

        String lowerPath = path.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerPath.contains(pattern)) {
                return true;
            }
        }

        // 检查绝对路径
        if (path.startsWith("/") || path.matches("^[A-Za-z]:.*")) {
            return true;
        }

        return false;
    }

    /**
     * 清理路径，移除多余的斜杠和点
     *
     * @param path 原始路径
     * @return 清理后的路径
     */
    private static String cleanPath(String path) {
        if (StrUtil.isEmpty(path)) {
            return path;
        }

        // 移除多余的斜杠
        path = path.replaceAll("/+", "/");
        
        // 移除开头的斜杠
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        // 移除结尾的斜杠
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
}