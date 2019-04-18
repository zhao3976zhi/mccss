package com.cypro.ascpay.rest.quick.api;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.rest.common.api.CommonApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Base64;

/**
 * @Author ascme
 * @ClassName gongyifu 快捷api
 * @Date 2019-04-08
 */
@Service
@PropertySource("classpath:application.properties")
public class GYFquickPayApi {
    private Logger logger = LoggerFactory.getLogger(GYFquickPayApi.class);

    @Resource
    private CommonApi commonApi;

    /**上游密钥**/
    private String key ;
    /**上游商户号**/
    private String merchantNo;
    /**平台通道id**/
    private String passId;
    /**外放id**/
    private String releaId;
    /**请求ip**/
    private String reqIp="api.channel.gumids.com";//生产

    @Value("${server.ip}")
    private String serverip ;
    //private String bindnotifyUrl="http://47.111.8.86/katong/gongyifu/quick/bindNotify";//绑卡的异步回调

    //private String paynotifyUrl="http://47.111.8.86/katong/gongyifu/quick/payNotify";//快捷支付的异步回调
    private String paynotifyUrl;//快捷支付的异步回调

    //private String settlenotifyUrl="http://47.111.8.86/katong/gongyifu/quick/settleNotify";//结算的异步回调
    private String settlenotifyUrl;//结算的异步回调

    /**
     * 根据外放代号查看渠道商户信息
     * @param jsonObject
     * @throws Exception
     */
    public void getMerInfo(JSONObject jsonObject) throws Exception{
        JSONObject json = commonApi.getmerNoandkey(jsonObject,"3");
        key = json.getString("merKey");
        merchantNo = json.getString("merchantNo");
        passId = json.getString("passId");
        releaId = json.getString("releaId");

        paynotifyUrl ="http://"+serverip+"/katong/gongyifu/quick/payNotify";
        settlenotifyUrl="http://"+serverip+"/katong/gongyifu/quick/settleNotify";
        logger.info("paynotifyUrl的值为：{}，settlenotifyUrl的值为：{}",paynotifyUrl,settlenotifyUrl);
    }

    /**
     * 1.商户注册
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFquickRegist(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/merchant/regist";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantName",jsonObject.getString("merchantName"));//商户名称
        jsonData.put("realName",jsonObject.getString("realName"));//真实姓名
        jsonData.put("mobile",jsonObject.getString("mobile"));//手机号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证
        jsonData.put("merchantAddress",jsonObject.getString("merchantAddress"));//商户地址
        jsonData.put("settleCard",jsonObject.getString("settleCard"));//结算卡号
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//单笔手续费
        jsonData.put("remark",jsonObject.getString("remark"));//备注
        jsonData.put("userId",jsonObject.getString("userId"));//用户ID
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 2.商户信息修改
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFinfoUpdate(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/merchant/info/update";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//商户编号
        jsonData.put("mobile",jsonObject.getString("mobile"));//手机号
        jsonData.put("settleCard",jsonObject.getString("settleCard"));//结算卡号
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 3.商户费率修改
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFrateUpdate(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/merchant/rate/update";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//商户编号
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//手续费
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 4.银联绑卡
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFbindCard(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/bind/card";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderId",jsonObject.getString("orderId"));//订单号
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//商户编号
        jsonData.put("realName",jsonObject.getString("realName"));//姓名
        jsonData.put("mobile",jsonObject.getString("mobile"));//交易卡手机号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证
        jsonData.put("dealCard",jsonObject.getString("dealCard"));//交易卡号
        jsonData.put("notifyUrl",jsonObject.getString("notifyUrl"));//通知地址
        jsonData.put("frontUrl",jsonObject.getString("frontUrl"));//前台跳转地址
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        logger.info("请求参数jsonData：{}",jsonData);

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 5.快捷支付
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFquickPay(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/quick/pay";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//商户编号
        jsonData.put("orderId",jsonObject.getString("orderId"));//订单号
        jsonData.put("realName",jsonObject.getString("realName"));//姓名
        jsonData.put("mobile",jsonObject.getString("mobile"));//交易卡手机号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证
        jsonData.put("dealCard",jsonObject.getString("dealCard"));//交易卡号
        jsonData.put("notifyUrl",paynotifyUrl);//通知地址
        jsonData.put("amount",jsonObject.getString("amount"));//金额
        jsonData.put("goodsName",jsonObject.getString("goodsName"));//商品名称
        jsonData.put("cardType",jsonObject.getString("cardType"));//卡类型 0：借记卡 1：信用卡
        jsonData.put("cvv2",jsonObject.getString("cvv2"));//安全码
        jsonData.put("validDate",jsonObject.getString("validDate"));//有效期
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        jsonObject.put("releaId",releaId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 6.结算
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFsettle(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/settle";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//商户编号
        jsonData.put("orderId",jsonObject.getString("orderId"));//订单号
        jsonData.put("realName",jsonObject.getString("realName"));//姓名
        jsonData.put("mobile",jsonObject.getString("mobile"));//交易卡手机号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证
        jsonData.put("settleCard",jsonObject.getString("settleCard"));//结算卡号
        jsonData.put("notifyUrl",settlenotifyUrl);//通知地址
        jsonData.put("amount",jsonObject.getString("amount"));//金额
        jsonData.put("remark",jsonObject.getString("remark"));//备注
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        jsonObject.put("releaId",releaId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 7.订单查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFqueryOrder(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/query/order";

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderId",jsonObject.getString("orderId"));//订单号
        jsonData.put("type",jsonObject.getString("type"));//0：快捷支付 1：结算
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }

    /**
     * 7.订单查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject GYFqueryBalance(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/gongyifu/query/balance";

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantId",jsonObject.getString("merchantId"));//子商户编号
        logger.info("请求参数jsonData：{}",jsonData);
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return commonApi.requestCommon(jsonObject);
    }
}
