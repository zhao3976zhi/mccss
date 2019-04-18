package com.cypro.ascpay.rest.quick.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.nocard.NocardPay;
import com.cypro.ascpay.api.nocard.NocardPayService;
import com.cypro.ascpay.api.replace.info.ReplaceInfo;
import com.cypro.ascpay.api.replace.info.ReplaceInfoService;
import com.cypro.ascpay.rest.common.bean.RestResult;
import com.cypro.ascpay.rest.common.bean.ResultEnume;
import com.cypro.ascpay.rest.common.controller.BaseController;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import com.cypro.ascpay.rest.quick.api.BDquickPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping(value = "/baiduo/quick")
public class BDquickPayController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(XSquickPayController.class);

    @Resource
    private BDquickPayApi bDquickPayApi;

    @Resource(name = "nocardPayService")
    private NocardPayService nocardPayService;

    // 用户信息
    @Resource(name = "replaceInfoService")
    private ReplaceInfoService replaceInfoService;
    /**
     * 1.商户注册
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/regist"},method = RequestMethod.POST)
    public RestResult bdregist(@RequestBody JSONObject jsonObject){
        String method = "bdregist";
        logger.info("进入BDquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            //风控（费率）
            String riskStr = getRiskman(jsonObject);
            if(!riskStr.isEmpty()){
                return getRestResult(ResultEnume.FAIL,riskStr,null);
            }
            /**
             * 记录用户信息
             */
            ReplaceInfo replaceInfo = new ReplaceInfo();
            replaceInfo.setName(jsonObject.get("realName").toString());
            replaceInfo.setCardNo(jsonObject.getString("settleCard"));
            replaceInfo.setRate(jsonObject.getString("rate"));
            replaceInfo.setIdentity(jsonObject.getString("idCard"));
            replaceInfo.setFee(jsonObject.getString("extraFee"));
            replaceInfo.setPhone(jsonObject.getString("phone"));
            replaceInfo.setUserId(jsonObject.getString("userId"));
            replaceInfo.setBankName(jsonObject.getString("bankName"));
            //请求上游接口
            JSONObject resJson = bDquickPayApi.BDquickRegist(jsonObject);
            if(resJson.get("code").equals("000")) {//成功

                replaceInfo.setPassId(resJson.getLong("passId"));
                replaceInfo.setMerid(resJson.getJSONObject("data").getString("merchantCode"));//商户的商户编号
                replaceInfoService.insert(replaceInfo);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 2.商户修改
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/modify"},method = RequestMethod.POST)
    public RestResult bdmodify(@RequestBody JSONObject jsonObject){
        String method = "bdmodify";
        logger.info("进入BDquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            //风控（费率）
            String riskStr = getRiskman(jsonObject);
            if(!riskStr.isEmpty()){
                return getRestResult(ResultEnume.FAIL,riskStr,null);
            }
            ReplaceInfo replaceInfo = new ReplaceInfo();
            replaceInfo.setMerid(jsonObject.getString("merchantCode"));
            replaceInfo.setPhone(jsonObject.getString("cardPhone"));
            replaceInfo.setCardNo(jsonObject.getString("cardCode"));
            replaceInfo.setRate(jsonObject.getString("rate"));
            replaceInfo.setFee(jsonObject.getString("extraFee"));
            replaceInfo.setBankName(jsonObject.getString("bankName"));
            //请求上游接口
            JSONObject resJson = bDquickPayApi.BDquickModify(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                replaceInfoService.update(replaceInfo);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 3.快捷支付
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/pay"},method = RequestMethod.POST)
    public RestResult bdpay(@RequestBody JSONObject jsonObject){
        String method = "bdpay";
        logger.info("进入BDquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            NocardPay nocardPay = new NocardPay();
            nocardPay.setOrderNo(jsonObject.getString("orderCode"));//订单号
            nocardPay.setAmount(jsonObject.getString("amount"));//订单金额
            nocardPay.setBankName(jsonObject.getString("bankCode"));//银行名称
            nocardPay.setCardCode(jsonObject.getString("cardCode"));//交易卡号
            nocardPay.setCardPhone(jsonObject.getString("cardPhone"));//交易卡手机号
            nocardPay.setValid(jsonObject.getString("validDate"));//有效期
            nocardPay.setCvv2(jsonObject.getString("cvv2"));//cvv2码
            nocardPay.setNotifyurl(jsonObject.getString("notifyUrl"));//通知地址
            nocardPay.setIdcard(jsonObject.getString("idCard"));//身份证号
            nocardPay.setUserId(jsonObject.getString("userId"));//用户id
            nocardPay.setRealName(jsonObject.getString("realName"));//真实姓名

            String states = "2";//订单状态1 成功，2失败，0处理中
            //请求上游接口
            JSONObject resJson = bDquickPayApi.BDquickPay(jsonObject);

            if(resJson.containsKey("code") && resJson.get("code") != null) {
                if (resJson.get("code").equals("000") || resJson.get("code").equals("0000")) {//成功
                    if (resJson.containsKey("data") && resJson.get("data") != null) {
                        JSONObject data = resJson.getJSONObject("data");
                        if (data.getString("status").equals("1")) {
                            states = "1";
                        } else {
                            states = "2";
                        }
                    }
                }
            }

            nocardPay.setOrderStates(states);
            nocardPay.setOrderDesc(resJson.getString("msg"));
            nocardPay.setReleaId(resJson.getString("releaId"));
            nocardPayService.insert(nocardPay);

            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }
    /**
     * 4.订单查询
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/payquery"},method = RequestMethod.POST)
    public RestResult bdpayquery(@RequestBody JSONObject jsonObject){
        String method = "bdpayquery";
        logger.info("进入BDquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            //请求上游接口
            JSONObject resJson = bDquickPayApi.BDquickPayQuery(jsonObject);
            String msg = resJson.getString("msg");
            if(resJson.get("data") != null){
                JSONObject data = resJson.getJSONObject("data");
                data.remove("profit");
                data.remove("frontUrl");
                data.remove("notifyUrl");
                data.put("companyId",jsonObject.get("merNo"));

                logger.info("返回外放data参数:{}",data);
                return getRestResult(ResultEnume.SUCCESS, msg, data);
            }
            return getRestResult(ResultEnume.FAIL, msg, null);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 5.异步通知
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/notify"},method = RequestMethod.POST)
    public String bdnotify(HttpServletRequest request) throws Exception {
        String method = "bdnotify";

        Map<String ,String[]> maps = request.getParameterMap();
        Set<String> keys = maps.keySet();
        Map<String ,String> map1 = new HashMap<>();
        for(String key : keys){
            String[] value = maps.get(key);
            for(String v : value){
                map1.put(key,v);
            }
        }

        if(!map1.isEmpty()){
            logger.info("进入BDquickPayController的"+method+"方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderNo = map1.get("orderCode").toString();//订单号
            String merchantCode = map1.get("merchantCode").toString();//商户编号
            String status = map1.get("status").toString();//订单状态 1：成功 其他失败
            String respMsg = map1.get("msg").toString();//消息

            String states = "2";
            if(status.equals("1")){//交易成功
                states = "1";//成功
            }else{
                states = "2";//失败
            }

            Map<String ,String> map = new HashMap<>();
            map.put("orderNo",orderNo);
            List<NocardPay> nocardPayList = nocardPayService.queryList(map);
            if(nocardPayList != null){
                if(!nocardPayList.get(0).getOrderStates().equals("1")){//不是成功状态则处理
                    //订单状态处理
                    NocardPay nocardPay = new NocardPay();
                    nocardPay.setOrderStates(states);
                    nocardPay.setOrderDesc(respMsg);
                    nocardPay.setOrderNo(orderNo);
                    nocardPay.setId(nocardPayList.get(0).getId());
                    //修改订单状态
                    nocardPayService.update(nocardPay);
                }
            }

            //处理请求下游通知
            String notifyUrl = nocardPayList.get(0).getNotifyurl();//下游通知地址
            logger.info("pay处理请求下游通知notifyUrl:{}",notifyUrl);
            JSONObject json = new JSONObject();
            json.put("orderNo",orderNo);
            json.put("merchantCode",merchantCode);
            json.put("status",status);
            json.put("msg",respMsg);
            String reslut = HttpClients.doStrPost(notifyUrl,json);
            logger.info("外放返回参数reslut:{}",reslut);
            if(reslut != null){
                return reslut;
            }
        }
        return "";
    }

}
