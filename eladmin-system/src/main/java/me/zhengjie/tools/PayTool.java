package me.zhengjie.tools;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import me.zhengjie.modules.api.domain.AppOrderResponse;
import org.slf4j.Logger;

import java.math.BigDecimal;

/**
 * 支付
 */
public class PayTool {

    public static String appid = "wx1b9c92940ccdc573";

    /**
     * 商户号
     */
    public static String merchantId = "1648045897";

    /**
     * 商户API私钥路径
     */
    public static String privateKey = ResourceUtil.getResourceObj("apiclient_key.pem").readUtf8Str();
    /**
     * 商户证书序列号
     */
    public static String merchantSerialNumber = "6DBF60C54FB8CA3E2A36C3BD07A1AF242DF0C279";

    public static String apiV3key = "nr8MHZMwtVTANSRQNOD69ScIEC6C5P6b";

    public static String wechatPayCertificatePath = ResourceUtil.getResource("apiclient_cert.pem").getPath();

    public static JsapiServiceExtension service;

    private Config config =
            new RSAAutoCertificateConfig.Builder()
                    .merchantId(merchantId)
                    // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                    .privateKey(privateKey)
//                    .privateKeyFromPath(privateKeyPath)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3key)
                    .build();
    private static final PayTool instance = new PayTool();

    private PayTool() {
        // 初始化服务
        service =
                new JsapiServiceExtension.Builder()
                        .config(config)
                        .signType("RSA") // 不填默认为RSA
                        .build();
    }

    public static PayTool getInstance() {
        return instance;
    }

    /**
     * 关闭订单
     */
    public static void closeOrder(String id) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(merchantId);
        request.setOutTradeNo(id);
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        service.closeOrder(request);
    }

    /**
     * JSAPI支付下单，并返回JSAPI调起支付数据
     */
    public static PrepayWithRequestPaymentResponse prepayWithRequestPayment(AppOrderResponse orderResponse, OrderResult orderResult) {
        try {
            Transaction transaction = queryOrderByOutTradeNo(orderResponse.getWxId());
            closeOrder(orderResponse.getWxId());
        } catch (Exception e) {
            // 订单不存在
        }
        PrepayRequest request = buildRequest(orderResponse);
        orderResult.onResult(request.getOutTradeNo());
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.prepayWithRequestPayment(request);
    }

    /**
     * 微信支付订单号查询订单
     */
    public static Transaction queryOrderById() {

        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.queryOrderById(request);
    }

    /**
     * 商户订单号查询订单
     */
    public static Transaction queryOrderByOutTradeNo(String id) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(id);
        request.setMchid(merchantId);
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.queryOrderByOutTradeNo(request);
    }


    public Transaction parse(RequestParam requestParam) {
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser((NotificationConfig) config);
        // 以支付通知回调为例，验签、解密并转换成 Transaction
        Transaction transaction = parser.parse(requestParam, Transaction.class);
        return transaction;
    }

    private static PrepayRequest buildRequest(AppOrderResponse orderResponse) {
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal((int) (new BigDecimal(orderResponse.getTotalPrice()).doubleValue() * 100));
        prepayRequest.setAmount(amount);
        prepayRequest.setMchid(merchantId);
        prepayRequest.setOutTradeNo(UUID.fastUUID().toString(true));
        prepayRequest.setAppid(appid);
        prepayRequest.setDescription(orderResponse.getShopName() + "的商品订单");
        Payer payer = new Payer();
        payer.setOpenid("oRmge5Tvuo9acOppzlHw42zAJVDM");
        prepayRequest.setPayer(payer);
        prepayRequest.setNotifyUrl("https://www.siyutechnology.cn/app/result/");
        return prepayRequest;
    }

    public interface OrderResult {
        void onResult(String wxId);
    }

}
