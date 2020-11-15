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
@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2016092200568607";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSgX/nTQ0lD+S8ObaM5LGZ1hiz18GXnNpqPLhJCym4xOpn35FNPHrPkDGEoMKrZ5LJeA4cZulckD8AtpvBCpeyIkrj/i1WVmSg10hVX67MlVets4UecCHZv2hKAN0/iId76kozdqrd7Csp/YgXPquN9Np0NFotggTrmiBANk+vcpTF9SCGrDq/isOoCvClfbvVJjApfLLOel3yECe5K/SZ8puiWILVm1NxEXAqJ8z0ipPZVGrXsT6Bo0pEyCPcEL0SqaC9WT0zdWQzdUknCzZV9W2wKjEXBJG9hqxay5kPaKm9leBatSkDAaDxH/N5g36HRfY7BmklwRZsp17lHinxAgMBAAECggEAfnnfck35WBKFc90a9D0F+Xlzr+ZGEV3uzKIIsb46UXFlrzC5HoVkvEWOCiJCjHiIpvbGr8xED43TZgk/IwLC/JxQLM0kVJGWo6fWoSVOIP2YSLNe620APBvaq3BdkFiMJfSYBB+g2J7mkIR39SE8Nvu3j3QWmYzSNJbE2spINnwTzNBL1OPaB5h3hSjyI07KaUcOjhTBF0EZl83NlBDsxmQvy0NmuOIWAcIXXvGoIbwkA774J3LhwL+VS4W2FpQj4FlxvDlPu24GeNWN7oO66T3Jp9bweO120ObhuKwZQosDGkJq0975zVSJX5QtUWHMM/QDPO8Pk24n2AoPcACQcQKBgQDS6kqD+sK8dDBpkmxYopA1gJJATnur0RHFZJb5webOhnEZnePhB1hhhGvKFcrdY2hcYeQiUZkHMsnWItNUe9E9ccp4++m6KKG0iV/BQda7zx1zMTTZUMvSbO282Q31YnQu7Yz6BSk4f/U5Qbu61AK53Tv1ejSAgQhXt1Pwq8KD7QKBgQCx0pkqW4+53tY2o4iPqFGjKYI2yk5bAH5etmOvW51OZ4Slsq/aUJKBVG6fOpRVKkiXulHhrp5csZH0/C7kaj4Hy7TjgUKSWvwlv7i7jgN0dq/bhVJz82y+N9pENWvy5J0I8Kt67XH+6JDEGWjlV58auifMRSx5mRJNn5pM6qrFlQKBgFyZWm/JV1fv1xVyoLjlXlTvBsbO7kMH/jpgqFwtAk1n/x3VEShJ1kayIbTOjotWSopMvCFJG9tqM+0cyxWLatkELXWifAIsNpqRuYWah1FbZD2fu+kxLNtM0a+YyCUUvZeg2cUnIOraWupxbp9e13eMpvdmWMiWXfhM18CRWEwdAoGAUwT0l076EhgUQJwm1JML0jY94eCfpmLbnNJgRe1qysEPr+B1s2IslA7cOqC5we0kyRmmwsuoibQpZYwbRG7JmRAk2pZtgzDRSbpxv7a0rDoBLmbXMOU0Hraqw2+Bf3v2SMc79/9FWnIvrC4EyBYZZPwGOpsNAZRSdEUQX9qrceUCgYB99OOtFFt1ixzyTCyUj3Fuiw7BsPhdI3nuMSoNTPIDNpzRBp/KFXyv/FNJ2CjTAsX3OR3D6KmEYihqUfrYeb0P5zoybcQLMxbXxK+ec6F2o6U2iqFIq0MKwHUqsb9X3pj4qE0ZHbFgRtIHnL2/QGV5PFJdmIZIBKZcvB8fW6ztDA==";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyQQceVUChTJGtF/a8SXufhSxDTKporieTq9NO7yDZSpDlAX1zVPT/nf0KWAlxq1TYappWMIYtyrOABhJyn6flNP6vuSBiM5lYsepHvYrtRHqlFiJruEkiaCgEZBKL5aCfBHYj0oqgQn9MpNV/PEH4cBYAVaiI4+VX8CBUQfeEGjgN6OkpLULZ3X0JUkmSnVvCNJ1m3PD68IIlbOfEZXJUKCqmZhzprGR5VWswjxA+g87cMwvijL4gdkSy/daG62Bz5vApcmmMkuX1k1fMWP4ajZCASVw8HD+MSLRhd8We9F97gd8CW0TavzbdR+mTS5H4yEgO8F9HRAsbkhV9yu0yQIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url;

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
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
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);
        return result;
    }
}
