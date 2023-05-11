package me.zhengjie.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 产品code 生成工具
 */
public class ProductCodeUtil {

    private static String sdf = "yyyyMMddHHmmss";

    public static synchronized String getCode() {
        return DateUtil.date().toString(sdf) + RandomUtil.randomNumbers(3);
    }
}
