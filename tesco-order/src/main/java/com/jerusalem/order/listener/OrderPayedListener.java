package com.jerusalem.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jerusalem.order.config.AlipayTemplate;
import com.jerusalem.order.service.OrdersService;
import com.jerusalem.order.vo.PayAsyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/****
 * @Author: jerusalem
 * @Description: OrderPayedListener
 * 监听并处理支付宝支付成功的异步通知
 * @Date 2020/11/16 18:41
 *****/
@RestController
public class OrderPayedListener {

    @Autowired
    OrdersService ordersService;

    @Autowired
    AlipayTemplate alipayTemplate;

    /****
     * 处理支付成功异步通知回调
     * 给支付宝返回字符串"success"，告诉支付宝已收到通知
     * @return
     */
    @PostMapping("/payed/notify")
    public String handleAplipayed(PayAsyncVo payAsyncVo,HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        //核心操作 -》 验签（验证是否是支付宝发来的数据）
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //解决乱码，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(), alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        if (signVerified){
            //验证成功
//            System.out.println("签名验证成功");
            String result = ordersService.handlePayResult(payAsyncVo);
            return result;
        }else {
//            System.out.println("签名验证失败");
            return "error";
        }
    }
}
