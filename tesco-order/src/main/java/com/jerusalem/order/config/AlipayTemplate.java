package com.jerusalem.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.jerusalem.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/****
 * @Author: jerusalem
 * @Description: AlipayTemplate
 * 支付宝支付相关配置
 * @Date 2020/11/15 21:20
 *****/
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id;
    // 商户私钥（PKCS8格式RSA2私钥）
    private  String merchant_private_key;
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key;
    // 服务器异步通知页面路径（需http://格式的完整路径，不能加?id=123类自定义参数，必须外网可以正常访问）
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url;
    // 页面跳转同步通知页面路径（需http://格式的完整路径，不能加?id=123类自定义参数，必须外网可以正常访问）
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url;
    // 签名方式
    private  String sign_type;
    // 字符编码格式
    private  String charset;
    // 支付宝网关
    private  String gatewayUrl;
    //订单超时时间
    private String timeout;


    public String pay(PayVo vo) throws AlipayApiException {

        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求，设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                //收单功能（订单超时，不允许再支付）
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        String result = alipayClient.pageExecute(alipayRequest).getBody();
//        System.out.println("支付宝的响应："+result);
        return result;
    }
}