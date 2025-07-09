package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileUploadReqVO 测试类
 *
 * @author 芋道源码
 */
class FileUploadReqVOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDirectory() {
        // 测试有效目录
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory("uploads/2024/01");
        
        var violations = validator.validate(vo);
        assertTrue(violations.isEmpty(), "有效目录应该通过验证");
    }

    @Test
    void testNullDirectory() {
        // 测试空目录
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory(null);
        
        var violations = validator.validate(vo);
        assertTrue(violations.isEmpty(), "空目录应该通过验证");
    }

    @Test
    void testEmptyDirectory() {
        // 测试空字符串目录
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory("");
        
        var violations = validator.validate(vo);
        assertTrue(violations.isEmpty(), "空字符串目录应该通过验证");
    }

    @Test
    void testPathTraversalAttack() {
        // 测试路径遍历攻击
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory("../../etc/passwd");
        
        var violations = validator.validate(vo);
        assertFalse(violations.isEmpty(), "路径遍历攻击应该被拒绝");
    }

    @Test
    void testAbsolutePath() {
        // 测试绝对路径
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory("/etc/passwd");
        
        var violations = validator.validate(vo);
        assertFalse(violations.isEmpty(), "绝对路径应该被拒绝");
    }

    @Test
    void testWindowsAbsolutePath() {
        // 测试Windows绝对路径
        FileUploadReqVO vo = new FileUploadReqVO();
        vo.setFile(new MockMultipartFile("test.txt", "test.txt", "text/plain", "test".getBytes()));
        vo.setDirectory("C:\\windows\\system32");
        
        var violations = validator.validate(vo);
        assertFalse(violations.isEmpty(), "Windows绝对路径应该被拒绝");
    }
}