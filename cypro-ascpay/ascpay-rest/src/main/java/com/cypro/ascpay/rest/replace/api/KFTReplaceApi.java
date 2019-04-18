package com.cypro.ascpay.rest.replace.api;

import com.alibaba.fastjson.JSON;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import com.cypro.ascpay.rest.common.api.CommonApi;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import org.apache.commons.codec.digest.DigestUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ascme
 * @ClassName 请求上游代还控制类(KFT小额) API
 * @Date 2019-02-19
 */
@Service
@PropertySource("classpath:application.properties")
public class KFTReplaceApi  {

    private Logger logger = LoggerFactory.getLogger(KFTReplaceApi.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Resource(name = "replaceReleaService")
    private ReplaceReleaService replaceReleaService;
    /**上游密钥**/
    private String key ;
    /**上游商户号**/
    private String merchantNo;
    /**平台通道id**/
    private String passId;

    /**请求ip**/
    //private String reqIp="101.132.176.98";//测试地址
    //private String reqIp="101.132.45.75";//正式地址
    private String reqIp="api.channel.gumids.com";//正式生产域名地址

    @Value("${server.ip}")
    String serverip;
    /**扣款和支付异步通知(请求上游)**/
    //private String notifyUrl="http://47.111.8.86/katong/replace/notify";
    private String notifyUrl;

    /**
     * 1.获取平台外放密钥
     * 2.获取上游渠道商户号和密钥(merchantNo,merKey)
     * @param releaNo 外放代号
     * @return
     * @throws Exception
     */
    public JSONObject getMernoAndkey(String releaNo,String releaType) throws Exception{
        try{
            if(!releaNo.isEmpty()){
                JSONObject json = replaceReleaService.queryReleaPass(releaNo, releaType);
                return json;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("获取商户号和密钥出错 :{}",e.getMessage());
            throw e;
        }
    }

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
        JSONObject json = getMernoAndkey(releaNo, releaType);

        key = json.getString("merKey");
        merchantNo = json.getString("merchantNo");
        passId = json.getString("passId");

        notifyUrl="http://"+serverip+"/katong/replace/notify";
    }

    /**
     * 1.快捷代扣协议申请
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject KFTApplyapi(JSONObject jsonObject) throws Exception{
        /**
         * 根据外放代号releaNo 获取商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/kuaifutong/new/agreement/apply";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//唯一订单号
        jsonData.put("name",jsonObject.getString("name"));//用户真实姓名
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//银行编码
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//银行卡号
        jsonData.put("phone",jsonObject.getString("phone"));//电话号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
        jsonData.put("validDate",jsonObject.getString("validDate"));//有效期
        jsonData.put("cvv2",jsonObject.getString("cvv2"));//安全码
        jsonData.put("userId",jsonObject.getString("userId"));//用户ID
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return requestCommon(jsonObject);
    }

    /**
     * 2.快捷协议代扣确认
     * @param jsonObject
     * @return
     */
    public JSONObject KFTconfirm(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/agreement/confirm";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("smsSeq",jsonObject.getString("smsSeq"));//协议申请返回的短信流水号
        jsonData.put("authCode",jsonObject.getString("authCode"));//短信验证码
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 3.取消信用卡代扣协议 取消签订的协议，之后不可用于交易
     * @param jsonObject
     * @return
     */
    public JSONObject KFTcannel(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/agreement/cannel";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("agreementCode",jsonObject.getString("agreementCode"));//协议号
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 4.用于查询代扣协议详细信息
     * @param jsonObject
     * @return
     */
    public JSONObject KFTquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/agreement/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("agreementCode",jsonObject.getString("agreementCode"));//协议号
        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 5.三要素认证
     * @param jsonObject
     * @return
     */
    public JSONObject KFTthree(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/bankcard/valid/three";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("realName",jsonObject.getString("realName"));//姓名
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//银行编号
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//银行卡号
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 6.协议扣款接口  用户支付至平台账户
     * @param jsonObject
     * @return
     */
    public JSONObject KFTpayment(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/agreement/payment";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//唯一订单号，保证唯一
        jsonData.put("agreementNo",jsonObject.getString("agreementNo"));//协议号
        jsonData.put("amount",jsonObject.getString("amount"));//扣款金额（包含手续费）
        jsonData.put("rate",jsonObject.getString("rate"));//费率
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        if(jsonObject.containsKey("province")){
            jsonData.put("province",jsonObject.getString("province"));//省份
        }
        if(jsonObject.containsKey("city")){
            jsonData.put("city",jsonObject.getString("city"));//城市
        }
        if(jsonObject.containsKey("cityCode")){
            jsonData.put("cityCode",jsonObject.getString("cityCode"));//城市编码
        }
        if(jsonObject.containsKey("businessType")){
            jsonData.put("businessType",jsonObject.getString("businessType"));//行业类别
        }
        jsonData.put("notifyUrl",notifyUrl);//异步通知地址
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//代扣卡号
        jsonData.put("terminalIp",jsonObject.getString("terminalIp"));//终端用户 IP

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 7.单笔支付接口  付款至同人指定银行卡
     * @param jsonObject
     * @return
     */
    public JSONObject KFTpay(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/pay";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//唯一订单号，保证唯一
        jsonData.put("amount",jsonObject.getString("amount"));//扣款金额（包含手续费）
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//额外手续费
        jsonData.put("name",jsonObject.getString("name"));//真实姓名
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
        jsonData.put("bankCode",jsonObject.getString("bankCode"));//银行编号
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//银行卡号
        jsonData.put("phone",jsonObject.getString("phone"));//电话号
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        jsonData.put("notifyUrl",notifyUrl);//异步通知地址

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 8.订单查询接口
     * @param jsonObject
     * @return
     */
    public JSONObject KFTorderquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/order/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//唯一订单号，保证唯一
        jsonData.put("type",jsonObject.getString("type"));//类型  0：收款订单 1：支付订单


        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 9.用户余额查询 订单查询接口
     * @param jsonObject
     * @return
     */
    public JSONObject KFTuserbalance(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/user/balance";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID


        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 10.商户分润查询
     * @param jsonObject
     * @return
     */
    public JSONObject KFTcombalance(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/kuaifutong/new/company/balance";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"1");

        /**
         * jsonObject:业务参数( 该接口无业务参数)
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantNo",merchantNo);//用户 ID

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        return requestCommon(jsonObject);
    }

    /**
     * 公共请求方式
     * @param jsonObject
     * @return
     */
    public JSONObject requestCommon(JSONObject jsonObject){
        String merchantNo = jsonObject.get("merchantNo").toString();
        String url = jsonObject.get("url").toString();
        String key = jsonObject.get("key").toString();

        String data = "";
        if(jsonObject.containsKey("data")){
            data = jsonObject.get("data").toString();
        }


        /**
         * sign:签名
         */
        String sign = null;
        if(!data.isEmpty()){
            sign = DigestUtils.md5Hex(data + key);
        }else{
            sign = DigestUtils.md5Hex(key);
        }

        Map<String ,String> map  = new HashMap<>();
        map.put("merchantNo",merchantNo);
        if(!data.isEmpty()){
            map.put("data",data);
        }
        map.put("sign",sign);
        logger.info("请求参数jsonObject为:{}",jsonObject);
        logger.info("请求地址为:{}",url);
        logger.info("请求上游参数map为:{}",map);
        String res = HttpClients.sendPost(url,map);
        logger.info("上游返回参数为res:{}",res);
        JSONObject json = JSON.parseObject(res);
        if(jsonObject.containsKey("releaId")){
            json.put("releaId",jsonObject.getString("releaId"));
        }
        if(jsonObject.containsKey("passId")){
            json.put("passId",jsonObject.getString("passId"));
        }
        return json;
    }
}
