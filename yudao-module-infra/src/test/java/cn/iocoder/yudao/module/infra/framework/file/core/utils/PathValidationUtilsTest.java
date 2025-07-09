package cn.iocoder.yudao.module.infra.framework.file.core.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 路径验证工具类测试
 *
 * @author 芋道源码
 */
class PathValidationUtilsTest {

    @Test
    void testValidateAndCleanDirectory_NullInput() {
        String result = PathValidationUtils.validateAndCleanDirectory(null);
        assertNull(result);
    }

    @Test
    void testValidateAndCleanDirectory_EmptyInput() {
        String result = PathValidationUtils.validateAndCleanDirectory("");
        assertNull(result);
    }

    @Test
    void testValidateAndCleanDirectory_ValidDirectory() {
        String result = PathValidationUtils.validateAndCleanDirectory("test/directory");
        assertEquals("test/directory", result);
    }

    @Test
    void testValidateAndCleanDirectory_WithTrailingSlash() {
        String result = PathValidationUtils.validateAndCleanDirectory("test/directory/");
        assertEquals("test/directory", result);
    }

    @Test
    void testValidateAndCleanDirectory_WithLeadingSlash() {
        String result = PathValidationUtils.validateAndCleanDirectory("/test/directory");
        assertEquals("test/directory", result);
    }

    @Test
    void testValidateAndCleanDirectory_WithMultipleSlashes() {
        String result = PathValidationUtils.validateAndCleanDirectory("test///directory");
        assertEquals("test/directory", result);
    }

    @Test
    void testValidateAndCleanDirectory_PathTraversalAttack() {
        // 测试路径遍历攻击
        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("../../etc/passwd");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("..\\..\\windows\\system32");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("....//etc/passwd");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("....\\\\windows\\system32");
        });
    }

    @Test
    void testValidateAndCleanDirectory_UrlEncodedPathTraversal() {
        // 测试URL编码的路径遍历攻击
        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("..%2f..%2fetc%2fpasswd");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("..%5c..%5cwindows%5csystem32");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("%2e%2e%2f%2e%2e%2fetc%2fpasswd");
        });
    }

    @Test
    void testValidateAndCleanDirectory_AbsolutePath() {
        // 测试绝对路径
        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("/etc/passwd");
        });

        assertThrows(ServiceException.class, () -> {
            PathValidationUtils.validateAndCleanDirectory("C:\\windows\\system32");
        });
    }

    @Test
    void testValidateAndCleanDirectory_ValidComplexPath() {
        String result = PathValidationUtils.validateAndCleanDirectory("uploads/2024/01/images");
        assertEquals("uploads/2024/01/images", result);
    }

    @Test
    void testValidateAndCleanDirectory_WithDots() {
        // 测试包含点的路径（但不是路径遍历）
        String result = PathValidationUtils.validateAndCleanDirectory("my.file.txt");
        assertEquals("my.file.txt", result);
    }
}