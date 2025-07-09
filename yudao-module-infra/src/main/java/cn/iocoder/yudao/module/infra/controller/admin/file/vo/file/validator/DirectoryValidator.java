package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.validator;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 目录路径验证器实现
 * 
 * @author 芋道源码
 */
public class DirectoryValidator implements ConstraintValidator<ValidDirectory, String> {

    @Override
    public void initialize(ValidDirectory constraintAnnotation) {
        // 初始化方法，如果需要可以在这里进行一些初始化操作
    }

    @Override
    public boolean isValid(String directory, ConstraintValidatorContext context) {
        // 如果为空，则认为是有效的（可选字段）
        if (StrUtil.isEmpty(directory)) {
            return true;
        }

        // 检查是否包含路径遍历攻击
        return !containsPathTraversal(directory);
    }

    /**
     * 检查路径是否包含路径遍历攻击
     *
     * @param path 路径
     * @return 是否包含路径遍历攻击
     */
    private boolean containsPathTraversal(String path) {
        if (StrUtil.isEmpty(path)) {
            return false;
        }

        // 统一使用正斜杠
        String normalizedPath = path.replace('\\', '/');

        // 检查常见的路径遍历模式
        String[] dangerousPatterns = {
            "..", "..\\", "../", "..%2f", "..%5c", "..%2F", "..%5C",
            "%2e%2e", "%2E%2E", "%2e%2e%2f", "%2E%2E%2F",
            "....//", "....\\\\", "....%2f", "....%5c"
        };

        String lowerPath = normalizedPath.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerPath.contains(pattern)) {
                return true;
            }
        }

        // 检查绝对路径
        if (normalizedPath.startsWith("/") || normalizedPath.matches("^[A-Za-z]:.*")) {
            return true;
        }

        return false;
    }
}