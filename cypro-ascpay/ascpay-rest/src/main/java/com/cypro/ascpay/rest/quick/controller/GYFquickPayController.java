package com.cypro.ascpay.rest.quick.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.nocard.NocardPay;
import com.cypro.ascpay.api.nocard.NocardPayService;
import com.cypro.ascpay.api.replace.info.ReplaceInfo;
import com.cypro.ascpay.api.replace.info.ReplaceInfoService;
import com.cypro.ascpay.api.replace.pay.ReplacePay;
import com.cypro.ascpay.api.replace.pay.ReplacePayService;
import com.cypro.ascpay.rest.common.bean.RestResult;
import com.cypro.ascpay.rest.common.bean.ResultEnume;
import com.cypro.ascpay.rest.common.controller.BaseController;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import com.cypro.ascpay.rest.quick.api.GYFquickPayApi;
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
@RequestMapping(value = "/gongyifu/quick")
public class GYFquickPayController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(GYFquickPayController.class);

    @Resource
    private GYFquickPayApi gyFquickPayApi;
    @Resource(name = "nocardPayService")
    private NocardPayService nocardPayService;
    @Resource(name = "replacePayService")
    private ReplacePayService replacePayService;
    //用户信息
    @Resource(name = "replaceInfoService")
    private ReplaceInfoService replaceInfoService;
    /**
     * 1.商户注册
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/regist"},method = RequestMethod.POST)
    public RestResult gyfregist(@RequestBody JSONObject jsonObject){
        String method = "gyfregist";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
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
            replaceInfo.setPhone(jsonObject.getString("mobile"));
            replaceInfo.setUserId(jsonObject.getString("userId"));

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFquickRegist(jsonObject);
            if(resJson.get("code").equals("0000")) {//成功
                replaceInfo.setPassId(resJson.getLong("passId"));
                replaceInfo.setMerid(resJson.getJSONObject("data").getString("merchantId"));//商户的商户编号
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
     * 2.商户信息修改
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/infoUpdate"},method = RequestMethod.POST)
    public RestResult gyfInfoUpdate(@RequestBody JSONObject jsonObject){
        String method = "gyfInfoUpdate";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            ReplaceInfo replaceInfo = new ReplaceInfo();
            replaceInfo.setPhone(jsonObject.getString("mobile"));
            replaceInfo.setMerid(jsonObject.getString("merchantId"));
            replaceInfo.setCardNo(jsonObject.getString("settleCard"));
            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFinfoUpdate(jsonObject);
            if(resJson.get("code").equals("0000")) {//成功
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
     * 3.商户费率修改
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/rateUpdate"},method = RequestMethod.POST)
    public RestResult gyfrateUpdate(@RequestBody JSONObject jsonObject){
        String method = "gyfrateUpdate";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
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
            replaceInfo.setRate(jsonObject.getString("rate"));
            replaceInfo.setMerid(jsonObject.getString("merchantId"));
            replaceInfo.setFee(jsonObject.getString("extraFee"));
            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFrateUpdate(jsonObject);
            if(resJson.get("code").equals("0000")) {//成功
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
     * 4.银联绑卡
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/bindCard"},method = RequestMethod.POST)
    public RestResult gyfBindCard(@RequestBody JSONObject jsonObject){
        String method = "gyfBindCard";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFbindCard(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }
    /**
     * 5.快捷支付
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/pay"},method = RequestMethod.POST)
    public RestResult gyfPay(@RequestBody JSONObject jsonObject){
        String method = "gyfPay";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            NocardPay nocardPay = new NocardPay();
            nocardPay.setOrderNo(jsonObject.getString("orderId"));//订单号
            nocardPay.setAmount(jsonObject.getString("amount"));//订单金额
            nocardPay.setCardCode(jsonObject.getString("dealCard"));//交易卡号
            nocardPay.setCardPhone(jsonObject.getString("mobile"));//交易卡手机号
            nocardPay.setValid(jsonObject.getString("validDate"));//有效期
            nocardPay.setCvv2(jsonObject.getString("cvv2"));//cvv2码
            nocardPay.setNotifyurl(jsonObject.getString("notifyUrl"));//通知地址
            nocardPay.setIdcard(jsonObject.getString("idCard"));//身份证号
            nocardPay.setUserId(jsonObject.getString("userId"));//用户id
            nocardPay.setRealName(jsonObject.getString("realName"));//真实姓名

            String states = "2";//订单状态1 成功，2失败，0处理中

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFquickPay(jsonObject);
            if(resJson.containsKey("code") && resJson.get("code") != null) {
                if (resJson.get("code").equals("000") || resJson.get("code").equals("0000")) {//成功
                    if (resJson.containsKey("data") && resJson.get("data") != null) {
                        JSONObject data = resJson.getJSONObject("data");
                        if (data.getString("code").equals("0100")) {
                            states = "0";
                        } else if (data.getString("code").equals("02")) {
                            states = "1";
                        } else if (data.getString("code").equals("03")) {
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
     * 6.结算
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/settle"},method = RequestMethod.POST)
    public RestResult gfysettle(@RequestBody JSONObject jsonObject){
        String method = "gfysettle";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }
            ReplacePay replacePay = new ReplacePay();
            replacePay.setPayOrderNo(jsonObject.getString("orderId"));//唯一订单号
            replacePay.setPayTranAmt(jsonObject.getString("amount"));//支付金额(元)
            replacePay.setPayName(jsonObject.getString("realName"));//真实姓名
            replacePay.setPayIdcard(jsonObject.getString("idCard"));//身份证号
            replacePay.setPayCardcode(jsonObject.getString("settleCard"));//银行卡号
            replacePay.setPayPhone(jsonObject.getString("mobile"));//电话号
            replacePay.setUserId(jsonObject.getString("userId"));//用户 ID
            replacePay.setPayNotifyurl(jsonObject.getString("notifyUrl"));//异步通知地址


            String states = "2";//订单状态1 成功，2失败，0处理中

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFsettle(jsonObject);
            if(resJson.containsKey("code") && resJson.get("code") != null) {
                if (resJson.get("code").equals("000") || resJson.get("code").equals("0000")) {//成功
                    if (resJson.containsKey("data") && resJson.get("data") != null) {
                        JSONObject data = resJson.getJSONObject("data");
                        if (data.getString("code").equals("0100")) {
                            states = "0";
                        } else if (data.getString("code").equals("02")) {
                            states = "1";
                        } else if (data.getString("code").equals("03")) {
                            states = "2";
                        }
                    }
                }
            }

            replacePay.setPayOrderStates(states);
            replacePay.setPayOrderDesc(resJson.getString("msg"));
            replacePay.setReleaId(resJson.getString("releaId"));
            replacePayService.insert(replacePay);

            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }


    /**
     * 7.订单查询
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/queryOrder"},method = RequestMethod.POST)
    public RestResult gyfQueryorder(@RequestBody JSONObject jsonObject){
        String method = "gyfQueryorder";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFqueryOrder(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 8.商户余额查询
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBalance"},method = RequestMethod.POST)
    public RestResult gyfQueryBalance(@RequestBody JSONObject jsonObject){
        String method = "gyfQueryBalance";
        logger.info("进入GYFquickPayController的"+method+"方法,参数为:{}",jsonObject);
        try {
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = gyFquickPayApi.GYFqueryBalance(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error(method+"执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /*
     * 8.银联绑卡的异步通知
     * @param jsonObject
     * @return
     *
    @ResponseBody
    @RequestMapping(value = {"/bindNotify"},method = RequestMethod.POST)
    public RestResult gyfbindNotify(@RequestBody JSONObject jsonObject){

        return null;
    }*/

    /**
     * 9.快捷支付的异步通知
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/payNotify"},method = RequestMethod.POST)
    public String gyfpayNotify(HttpServletRequest request) throws Exception {
        String method = "gyfpayNotify";

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
            logger.info("进入GYFquickPayController的"+method+"方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderNo = map1.get("orderId").toString();//订单号
            String respCode = map1.get("respCode").toString();//订单码
            String status = map1.get("orderStatus").toString();//订单状态 02 成功 03 失败 其他处理中
            String respMsg = map1.get("respMsg").toString();//订单信息

            String states = "2";
            if(status.equals("0100")){
                states="0";
            }else if(status.equals("02")){
                states="1";
            }else if(status.equals("03")){
                states="2";
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
            json.put("orderId",orderNo);
            json.put("respCode",respCode);
            json.put("orderStatus",status);
            json.put("respMsg",respMsg);
            String reslut = HttpClients.doStrPost(notifyUrl,json);
            logger.info("外放返回参数reslut:{}",reslut);
            if(reslut != null){
                return reslut;
            }
        }
        return "";
    }

    /**
     * 10.结算的异步通知
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/settleNotify"},method = RequestMethod.POST)
    public String gyfsettleNotify(HttpServletRequest request) throws Exception {
        String method = "gyfsettleNotify";

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
            logger.info("进入GYFquickPayController的"+method+"方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderNo = map1.get("orderId").toString();//订单号
            String respCode = map1.get("respCode").toString();//订单码
            String status = map1.get("orderStatus").toString();//订单状态 02 成功 03 失败 其他处理中
            String respMsg = map1.get("respMsg").toString();//订单信息

            String states = "2";
            if(status.equals("0100")){
                states="0";
            }else if(status.equals("02")){
                states="1";
            }else if(status.equals("03")){
                states="2";
            }

            //支付订单处理
            Map<String ,String> mapPay = new HashMap<>();
            mapPay.put("payOrderNo",orderNo);
            List<ReplacePay> replacePayList = replacePayService.query(mapPay);
            if(!replacePayList.isEmpty()) {
                String payStates = replacePayList.get(0).getPayOrderStates();
                logger.info("平台订单状态payStates:{}", payStates);
                if (!payStates.equals("1")) {//平台成功
                    ReplacePay replacePayupd = new ReplacePay();
                    replacePayupd.setId(replacePayList.get(0).getId());
                    replacePayupd.setPayOrderStates(states);
                    if (map1.get("respMsg") != null) {
                        replacePayupd.setPayOrderDesc(map1.get("respMsg").toString());
                    }
                    replacePayService.update(replacePayupd);
                }
            }

            //处理请求下游通知
            String notifyUrl = replacePayList.get(0).getPayNotifyurl();//下游通知地址
            logger.info("pay处理请求下游通知notifyUrl:{}",notifyUrl);
            JSONObject json = new JSONObject();
            json.put("orderId",orderNo);
            json.put("respCode",respCode);
            json.put("orderStatus",status);
            json.put("respMsg",respMsg);
            String result = HttpClients.doStrPost(notifyUrl,json);
            logger.info("外放返回参数result:{}",result);
            if(result != null){
                return result;
            }
        }
        return "";
    }


}
