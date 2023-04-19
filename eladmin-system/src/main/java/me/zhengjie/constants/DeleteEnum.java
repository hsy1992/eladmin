package me.zhengjie.constants;

public enum DeleteEnum {
    DELETE(0),
    NOT_DELETE(1);

    private int code;
    DeleteEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
