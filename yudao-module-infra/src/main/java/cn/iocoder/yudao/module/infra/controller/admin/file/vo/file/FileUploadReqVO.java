package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "管理后台 - 上传文件 Request VO")
@Data
public class FileUploadReqVO {

    @Schema(description = "文件附件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件附件不能为空")
    private MultipartFile file;

    @Schema(description = "文件目录", example = "XXX/YYY")
    private String directory;

    @AssertTrue(message = "目录路径无效，包含非法字符")
    public boolean isDirectoryValid() {
        if (StrUtil.isEmpty(directory)) {
            return true; // 空值认为是有效的
        }
        
        // 统一使用正斜杠
        String normalizedPath = directory.replace('\\', '/');
        
        // 检查绝对路径
        if (normalizedPath.startsWith("/") || normalizedPath.matches("^[A-Za-z]:.*")) {
            return false;
        }
        
        // 检查路径遍历攻击
        String[] dangerousPatterns = {
            "..", "..\\", "../", "..%2f", "..%5c", "..%2F", "..%5C",
            "%2e%2e", "%2E%2E", "%2e%2e%2f", "%2E%2E%2F",
            "....//", "....\\\\", "....%2f", "....%5c"
        };
        
        String lowerPath = normalizedPath.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerPath.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
}
