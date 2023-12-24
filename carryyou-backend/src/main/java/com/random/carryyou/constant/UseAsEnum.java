package com.random.carryyou.constant;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum UseAsEnum {

    PATH1("01", "品种检测"),

    PATH2("02", "成熟度检测"),

    OTHER("03", "不支持的分类");

    private final String code;
    private final String description;

    UseAsEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 将编码转换为对应的描述
     *
     * @param code 编码
     * @return 对应的描述
     */
    public static String fromCode(@NonNull String code) {
        for (UseAsEnum useAsEnum : UseAsEnum.values()) {
            if (code.equals(useAsEnum.getCode())) {
                return useAsEnum.getDescription();
            }
        }
        return null;
    }

}
