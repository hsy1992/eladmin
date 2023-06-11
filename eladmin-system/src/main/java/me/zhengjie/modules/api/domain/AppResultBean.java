package me.zhengjie.modules.api.domain;

import lombok.Data;

@Data
public class AppResultBean<T> {
    private T data;
    private int code;
    private String msg;

    public AppResultBean(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> AppResultBean<T> ok(T result) {
        return ok(result, "成功");
    }

    public static <T> AppResultBean<T> ok(T result, String message) {
        return new AppResultBean<T>(result, 200, message);
    }


    public static <T> AppResultBean<T> fail(T result) {
        return fail(result, "失败");
    }

    public static <T> AppResultBean<T> fail(T result, String message) {
        return new AppResultBean<T>(result, 200, message);
    }

}
