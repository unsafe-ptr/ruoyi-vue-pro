package cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 目录路径验证注解
 * 
 * @author 芋道源码
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DirectoryValidator.class)
public @interface ValidDirectory {

    String message() default "目录路径无效，包含非法字符";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}