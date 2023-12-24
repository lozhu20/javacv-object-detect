package com.random.carryyou.constant;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum RoleEnum {

    ROLE_ADMIN("ROLE_ADMIN", "管理员"),

    ROLE_USER("ROLE_USER", "普通用户");

    private final String code;
    private final String description;

    RoleEnum(String code, String description) {
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
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (code.equals(roleEnum.getCode())) {
                return roleEnum.getDescription();
            }
        }
        return null;
    }
}
