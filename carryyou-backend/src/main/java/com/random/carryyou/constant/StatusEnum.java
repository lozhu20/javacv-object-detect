package com.random.carryyou.constant;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum StatusEnum {

    NORMAL("01", "正常"),

    SUSPEND("99", "注销"),

    DELETE("98", "删除");

    private final String code;
    private final String description;

    StatusEnum(String code, String description) {
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
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (code.equals(statusEnum.getCode())) {
                return statusEnum.getDescription();
            }
        }
        return null;
    }

}
