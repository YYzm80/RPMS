package com.example.common.validator;

import com.example.common.annotation.Xss;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class XssValidator implements ConstraintValidator<Xss, String> {
    /**
     * 使用自带的 basicWithImages 白名单
     */
    private static final Safelist WHITE_LIST = Safelist.relaxed();
    /**
     * 定义输出设置，关闭prettyPrint（prettyPrint=false），目的是避免在清理过程中对代码进行格式化
     * 从而保持输入和输出内容的一致性。
     */
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    /**
     * 验证输入值是否有效，即是否包含潜在的XSS攻击脚本。
     *
     * @param value 输入值，需要进行XSS攻击脚本清理。
     * @param context 上下文对象，提供关于验证环境的信息，如验证失败时的错误消息定制。
     * @return 如果清理后的值与原始值相同，则返回true，表示输入值有效；否则返回false，表示输入值无效。
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 使用Jsoup库对输入值进行清理，以移除潜在的XSS攻击脚本。
        // 使用预定义的白名单和输出设置来确保只保留安全的HTML元素和属性。
        String cleanedValue = null;
        if (value != null) {
            cleanedValue = Jsoup.clean(value, "", WHITE_LIST, OUTPUT_SETTINGS);
        }


        // 比较清理后的值与原始值是否相同，用于判断输入值是否有效。
        if (cleanedValue != null) {
            return cleanedValue.equals(value);
        }
        return true;
    }

}

