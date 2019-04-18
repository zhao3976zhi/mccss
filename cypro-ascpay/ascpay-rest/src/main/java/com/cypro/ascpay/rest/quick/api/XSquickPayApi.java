package com.cypro.ascpay.rest.quick.api;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.rest.replace.api.KFTReplaceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Base64;

/**
 * @Author ascme
 * @ClassName 请求上游快捷控制类(xinsheng大额) API
 * @Date 2019-03-27
 */
@Service
@PropertySource("classpath:application.properties")
public class XSquickPayApi {
    private Logger logger = LoggerFactory.getLogger(XSquickPayApi.class);

    @Resource
    private KFTReplaceApi kftReplaceApi;

    /**上游密钥**/
    private String key ;
    /**上游商户号**/
    private String merchantNo;
    /**平台通道id**/
    private String passId;
    /**外放id**/
    private String releaId;

    /**请求ip**/
    //private String reqIp = "101.132.176.98";//测试
    private String reqIp="api.channel.gumids.com";//生产

    @Value("${server.ip}")
    String serverip;
    //private String notify="http://47.111.8.86/katong/xs/quick/notify";//扣款的异步回调地址
    private String notify;//扣款的异步回调地址

    /**
     * 公共 根据外放代号releaNo 获取商户号密钥
     * @param jsonObject
     * @param releaType  外放代还通道类型 1小额，2大额
     * @throws Exception
     */
    public void getmerNoandkey(JSONObject jsonObject,String releaType) throws Exception{
        /**
         *根据外放代号releaNo 获取商户号密钥
         */
        String releaNo = jsonObject.getString("merNo");
        JSONObject json = kftReplaceApi.getMernoAndkey(releaNo, releaType);

        releaId = json.getString("releaId");
        key = json.getString("merKey");
        merchantNo = json.getString("merchantNo");
        passId = json.getString("passId");

        notify="http://"+serverip+"/katong/xs/quick/notify";
    }

    /**
     * 1.消费申请
     * 请求消费，会返回平台订单号并发送验证码
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject XsQuickapply(JSONObject jsonObject) throws Exception {
        /**
         * 根据外放代号releaNo 获取商户号密钥
         */
        getmerNoandkey(jsonObject,"3");
        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/xinsheng/new/quickPay/apply";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("creditCard",jsonObject.getString("creditCard"));//信用卡卡号
        jsonData.put("amount",jsonObject.getString("amount"));//订单金额
        jsonData.put("realName",jsonObject.getString("realName"));//用户真实姓名
        jsonData.put("creditValidDate",jsonObject.getString("creditValidDate"));//信用卡有效期
        jsonData.put("cvv2",jsonObject.getString("cvv2"));//安全码
        jsonData.put("creditPhone",jsonObject.getString("creditPhone"));//信用卡银行预留电话
        jsonData.put("creditBankName",jsonObject.getString("creditBankName"));//信用卡所在银行名称
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
        jsonData.put("settleCard",jsonObject.getString("settleCard"));//借记卡卡号
        jsonData.put("settlePhone",jsonObject.getString("settlePhone"));//借记卡银行预留电话
        jsonData.put("notifyUrl",notify);//通知地址
        jsonData.put("userId",jsonObject.getString("userId"));//用户ID
        jsonData.put("province",jsonObject.getString("province"));//省
        jsonData.put("city",jsonObject.getString("city"));//市
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//额外手续费

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
        return kftReplaceApi.requestCommon(jsonObject);
    }

    /**
     * 2.消费确认
     * 发送短信验证码到接口确认消费，交易结果会异步通知
     * @param jsonObject
     * @return
     */
    public JSONObject XsQuickconfirm(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/quickPay/confirm";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"3");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("upOrderCode",jsonObject.getString("upOrderCode"));//申请签约返回的平台订单号
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//协议申请返回的短信流水号
        jsonData.put("smsCode",jsonObject.getString("smsCode"));//短信验证码
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return kftReplaceApi.requestCommon(jsonObject);
    }

    /**
     * 3.消费查询
     * 取消签订的协议，之后不可用于交易
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject XsQuickquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/quickPay/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"3");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return kftReplaceApi.requestCommon(jsonObject);
    }
}
