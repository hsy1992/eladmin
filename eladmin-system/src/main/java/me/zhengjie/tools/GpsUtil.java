package me.zhengjie.tools;

public class GpsUtil {

    ///   <summary>
    /// 计算两点GPS坐标的距离
    ///   </summary>
    ///   <param name="n1"> 第一点的纬度坐标 </param>
    ///   <param name="e1"> 第一点的经度坐标 </param>
    ///   <param name="n2"> 第二点的纬度坐标 </param>
    ///   <param name="e2"> 第二点的经度坐标 </param>
    ///   <returns></returns>
    public static double Distance(double n1, double e1, double n2, double e2) {
        double jl_jd = 102834.74258026089786013677476285;
        double jl_wd = 111712.69150641055729984301412873;
        double b = Math.abs((e1 - e2) * jl_jd);
        double a = Math.abs((n1 - n2) * jl_wd);
        return Math.sqrt((a * a + b * b));

    }

}
