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
 * @ClassName beiduo 快捷api
 * @Date 2019-04-07
 */
@Service
@PropertySource("classpath:application.properties")
public class BDquickPayApi {
    private Logger logger = LoggerFactory.getLogger(BDquickPayApi.class);

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
    String serverip;

    //private String notifyUrl="http://47.111.8.86/katong/baiduo/quick/notify";//快捷支付的异步通知
    private String notifyUrl;//快捷支付的异步通知

    /**
     * 根据外放代号查看渠道商户信息
     * @param jsonObject
     * @throws Exception
     */
    public void getMerInfo(JSONObject jsonObject) throws Exception{
        JSONObject json = commonApi.getmerNoandkey(jsonObject,"3");
        releaId = json.getString("releaId");
        key = json.getString("merKey");
        merchantNo = json.getString("merchantNo");
        passId = json.getString("passId");

        notifyUrl="http://"+serverip+"/katong/baiduo/quick/notify";
    }

    /**
     * 1.商户注册
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject BDquickRegist(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/beiduo/merchant/regist";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("phone",jsonObject.getString("phone"));//结算卡预留手机号
        jsonData.put("settleCard",jsonObject.getString("settleCard"));//结算卡
        jsonData.put("realName",jsonObject.getString("realName"));//姓名
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//联行号
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//单笔手续费
        jsonData.put("bankName",jsonObject.getString("bankName"));//银行名称
        jsonData.put("userId",jsonObject.getString("userId"));//用户ID
        jsonData.put("notifyUrl",jsonObject.getString("notifyUrl"));//通知地址

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
     * 2.商户修改
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject BDquickModify(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/beiduo/merchant/modify";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantCode",jsonObject.getString("merchantCode"));//商户编号
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//结算卡号
        jsonData.put("cardPhone",jsonObject.getString("cardPhone"));//结算手机号
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//联行号
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//单笔手续费
        jsonData.put("bankName",jsonObject.getString("bankName"));//银行名称

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
     * 3.快捷支付
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject BDquickPay(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/beiduo/quick/pay";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantCode",jsonObject.getString("merchantCode"));//商户编号
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("amount",jsonObject.getString("amount"));//金额
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//银行简称
        jsonData.put("unionBankCode",jsonObject.getString("unionBankCode"));//交易卡联行号
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//交易卡号
        jsonData.put("cardPhone",jsonObject.getString("cardPhone"));//交易卡手机号
        jsonData.put("validDate",jsonObject.getString("validDate"));//日期
        jsonData.put("cvv2",jsonObject.getString("cvv2"));//安全码
        jsonData.put("notifyUrl",notifyUrl);//通知地址
        jsonData.put("frontUrl",jsonObject.getString("frontUrl"));//前台显示地址
        jsonData.put("terminalIp",jsonObject.getString("terminalIp"));//终端 IP
        jsonData.put("province",jsonObject.getString("province"));//省
        jsonData.put("city",jsonObject.getString("city"));//市
        jsonData.put("realName",jsonObject.getString("realName"));//真实姓名
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
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
     * 4.订单查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject BDquickPayQuery(JSONObject jsonObject) throws Exception {

        getMerInfo(jsonObject);

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/beiduo/quick/pay/query";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//订单号

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
}
