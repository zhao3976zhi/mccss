package com.cypro.ascpay.rest.replace.api;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Base64;

/**
 * @Author ascme
 * @ClassName 请求上游代还控制类(xinsheng大额) API
 * @Date 2019-03-18
 */
@Service
@PropertySource("classpath:application.properties")
public class XSReplaceApi {

    private Logger logger = LoggerFactory.getLogger(XSReplaceApi.class);

    @Resource
    private KFTReplaceApi kftReplaceApi;

    @Resource(name = "replaceReleaService")
    private ReplaceReleaService replaceReleaService;

    /**上游密钥**/
    private String key ;
    /**上游商户号**/
    private String merchantNo;
    /**平台通道id**/
    private String passId;
    /**请求ip**/
    //private String reqIp = "101.132.176.98";//测试
    private String reqIp="api.channel.gumids.com";//生产

    @Value("${server.ip}")
    String serverip;

    //private String notify="http://47.111.8.86/katong/xs/replace/notify";//扣款的异步回调地址
    private String notify;//扣款的异步回调地址

    //private String payNotify="http://47.111.8.86/katong/xs/replace/pay/notify";//付款的异步回调地址
    private String payNotify;//付款的异步回调地址
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

        key = json.getString("merKey");
        merchantNo = json.getString("merchantNo");
        passId = json.getString("passId");

        notify="http://"+serverip+"/katong/xs/replace/notify";
        payNotify="http://"+serverip+"/katong/xs/replace/pay/notify";
    }

    /**
     * 1.签约申请
     * 进行代扣需要先申请签约，签约会返回订单号，然后根据订单号调用签约确认接口确认签约。
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xsapply(JSONObject jsonObject) throws Exception{
        /**
         * 根据外放代号releaNo 获取商户号密钥
         */
        getmerNoandkey(jsonObject,"2");
        /**
         * 公共参数
         * url：请求地址
         * key；商户密钥
         * merchantNo：商户号
         */
        String url = "http://"+reqIp+"/xinsheng/new/agreement/apply";
        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//信用卡卡号
        jsonData.put("realName",jsonObject.getString("realName"));//用户真实姓名
        jsonData.put("validDate",jsonObject.getString("validDate"));//有效期
        jsonData.put("cvv2",jsonObject.getString("cvv2"));//安全码
        jsonData.put("idCard",jsonObject.getString("idCard"));//身份证号
        jsonData.put("phone",jsonObject.getString("phone"));//电话号
        jsonData.put("userId",jsonObject.getString("userId"));//用户ID
        jsonData.put("bankName",jsonObject.getString("bankName"));//银行名称

        /**
         * data:业务参数base64后的字符串
         */
        String data =  Base64.getEncoder().encodeToString(jsonData.toString().getBytes());

        jsonObject.put("url",url);
        jsonObject.put("key",key);
        jsonObject.put("merchantNo",merchantNo);
        jsonObject.put("data",data);
        jsonObject.put("passId",passId);
        return kftReplaceApi.requestCommon(jsonObject);
    }

    /**
     * 2.签约确认
     * 确认签约接口，验证通过后会返回业务协议号和支付协议号
     * @param jsonObject
     * @return
     */
    public JSONObject Xsconfirm(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/agreement/confirm";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("upOrderNo",jsonObject.getString("upOrderNo"));//申请签约返回的平台订单号
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//协议申请返回的短信流水号
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
     * 3.取消签约
     * 取消签订的协议，之后不可用于交易
     * @param jsonObject
     * @return
     */
    public JSONObject Xscannel(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/agreement/cancel";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",jsonObject.getString("orderNo"));//唯一订单号
        jsonData.put("businessCode",jsonObject.getString("businessCode"));//业务协议号
        jsonData.put("payCode",jsonObject.getString("payCode"));//支付协议号
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
     * 4.收款请求
     * 收款接口，交易结果以异步为主
     * @param jsonObject
     * @return
     */
    public JSONObject Xsreceiptapply(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/receipt/apply";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("businessCode",jsonObject.getString("businessCode"));//业务协议号
        jsonData.put("payCode",jsonObject.getString("payCode"));//支付协议号
        jsonData.put("notifyUrl",notify);//通知地址
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        jsonData.put("amount",jsonObject.getString("amount"));//金额
        jsonData.put("province",jsonObject.getString("province"));//省
        jsonData.put("city",jsonObject.getString("city"));//市
        jsonData.put("rate",jsonObject.getString("rate"));//费率

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
     * 5.收款查询
     * 查询收款订单
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xsreceiptquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/receipt/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

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

    /**
     * 6.付款接口
     * 执行还款，需要传收款的平台订单号
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xspay(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/pay";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderCode",jsonObject.getString("orderCode"));//唯一订单号
        jsonData.put("amount",jsonObject.getString("amount"));//金额
        jsonData.put("extraFee",jsonObject.getString("extraFee"));//单笔手续费（从付款金额扣）
        jsonData.put("realName",jsonObject.getString("realName"));//收款人姓名
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//收款人卡号
        jsonData.put("userId",jsonObject.getString("userId"));//用户 ID
        jsonData.put("notifyUrl",payNotify);//通知地址
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
     * 7.付款查询
     *
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xspayquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/pay/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

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

    /**
     * 8 用户余额查询
     * 订单查询接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xsbalancequery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/user/balance/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

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
        return kftReplaceApi.requestCommon(jsonObject);
    }

    /**
     * 9 订单批量查询
     *
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xsquerytime(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/order/query/time";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("beginTime",jsonObject.getString("beginTime"));//开始时间
        jsonData.put("endTime",jsonObject.getString("endTime"));//结束时间
        jsonData.put("type",jsonObject.getString("type"));//订单类型
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
     * 10 商户分润金额查询
     *
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject Xsprofitquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/profit/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("merchantNo",jsonObject.getString("merchantNo"));//商户编号
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
     * 11 卡余额查询 new
     * 订单查询接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public JSONObject XsCardquery(JSONObject jsonObject) throws Exception{
        String url = "http://"+reqIp+"/xinsheng/new/bank/card/balance/query";
        /**
         * 根据外放代号releaNo 获取上游商户号密钥
         */
        getmerNoandkey(jsonObject,"2");

        /**
         * jsonObject:业务参数
         */
        JSONObject jsonData = new JSONObject();
        jsonData.put("cardCode",jsonObject.getString("cardCode"));//卡号

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
