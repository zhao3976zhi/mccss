package com.cypro.ascpay.rest.replace.controller;

import com.alibaba.fastjson.JSON;
import com.cypro.ascpay.api.replace.info.ReplaceInfo;
import com.cypro.ascpay.api.replace.info.ReplaceInfoService;
import com.cypro.ascpay.api.replace.pay.ReplacePay;
import com.cypro.ascpay.api.replace.pay.ReplacePayService;
import com.cypro.ascpay.api.replace.payment.ReplacePayment;
import com.cypro.ascpay.api.replace.payment.ReplacePaymentService;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import com.cypro.ascpay.rest.common.bean.RestResult;
import com.cypro.ascpay.rest.common.bean.ResultEnume;
import com.cypro.ascpay.rest.common.controller.BaseController;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import com.cypro.ascpay.rest.replace.api.KFTReplaceApi;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author ascme
 * @ClassName 请求上游代还控制类(KFT)快付通
 * @Date 2019-02-19
 */
@RestController
@RequestMapping(value = "/replace")
public class KFTReplaceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(KFTReplaceController.class);

    //代还用户信息
    @Resource(name = "replaceInfoService")
    private ReplaceInfoService replaceInfoService;
    //代还扣款交易
    @Resource(name = "replacePaymentService")
    private ReplacePaymentService replacePaymentService;
    //代还支付交易
    @Resource(name="replacePayService")
    private ReplacePayService replacePayService;
    //代还外放信息
    @Resource(name="replaceReleaService")
    private ReplaceReleaService replaceReleaService;
    /**
     * 上游代还api接口
     */
    @Resource
    private KFTReplaceApi kftReplaceApi;

    /**
     * 1.快捷代扣协议申请
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/apply"},method = RequestMethod.POST)
    public RestResult KFTApply(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTApply方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTApplyapi(jsonObject);
            String msg = resJson.get("msg").toString();
            if(resJson.get("code").equals("000")){//成功
                /**
                 * 记录用户信息
                 */
                ReplaceInfo replaceInfo = new ReplaceInfo();
                replaceInfo.setName(jsonObject.get("name").toString());
                replaceInfo.setCardNo(jsonObject.getString("cardCode"));
                replaceInfo.setCvn(jsonObject.getString("cvv2"));
                replaceInfo.setIdentity(jsonObject.getString("idCard"));
                replaceInfo.setValidity(jsonObject.getString("validDate"));
                replaceInfo.setPhone(jsonObject.getString("phone"));
                replaceInfo.setUserId(jsonObject.getString("userId"));
                replaceInfo.setPassId(resJson.getLong("passId"));
                replaceInfoService.insert(replaceInfo);

                /**
                 * 返回数据
                 */
                String data = resJson.get("data").toString();
                JSONObject datajson = JSON.parseObject(data);
                return getRestResult(ResultEnume.SUCCESS,msg,datajson);
            } else{
                return getRestResult(ResultEnume.FAIL,msg,null);
            }
        }catch (Exception e){
            logger.error("KFTApply执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 2.快捷协议代扣确认
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/confirm"},method = RequestMethod.POST)
    public RestResult KFTConfirm(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTConfirm方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTconfirm(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTConfirm执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 3.取消信用卡代扣协议
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/cannel"},method = RequestMethod.POST)
    public RestResult KFTCannel(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTCannel方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTcannel(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTCannel执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 4.协议查询接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/query"},method = RequestMethod.POST)
    public RestResult KFTQuery(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTQuery方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTquery(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTQuery执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 5.三要素认证
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/valid/three"},method = RequestMethod.POST)
    public RestResult KFTValidThree(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTValidThree方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTthree(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTValidThree执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 6.协议扣款接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/payment"},method = RequestMethod.POST)
    public RestResult KFTPayment(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTPayment方法,参数为:{}",jsonObject);
        try{
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

            // 添加协议扣款记录
            DecimalFormat df = new DecimalFormat("#.00");//保留小数点后两位
            Double amount = jsonObject.getDouble("amount");//扣款金额
            Double rate = jsonObject.getDouble("rate");//扣款费率
            Double shouxf = Double.valueOf(df.format(amount*rate));//手续费
            Double pmfee = Double.valueOf(df.format(amount - shouxf)); //扣款到账金额;
            logger.info("扣款金额：{}，手续费：{}，扣款到账金额：{}",amount,shouxf,pmfee);

            JSONObject jsonRelea = replaceReleaService.queryReleaPass(jsonObject.getString("merNo"),"1");
            Double releaRate = jsonRelea.getDouble("releaRate");//外放
            Double passRate = jsonRelea.getDouble("passRate");//平台
            //计算分润金额 ： 订单金额 * （费率差）
            Double releaProfit = Double.valueOf(df.format(amount * (rate - releaRate)));//外放分润(金额 *(扣款费率-平台设置费率))
            Double ptProfit = Double.valueOf(df.format(amount * (releaRate - passRate)));//平台所得分润
            logger.info("传入费率：{}；平台外放费率：{}；上游通道费率：{}",rate,releaRate,passRate);
            logger.info("平台分润：{}；外放分润：{}",ptProfit,releaProfit);

            ReplacePayment replacePayment = new ReplacePayment();
            replacePayment.setPmOrderno(jsonObject.getString("orderNo"));//订单号
            replacePayment.setPmAmount(String.valueOf(amount));//扣款金额
            replacePayment.setPmRate(String.valueOf(rate));//扣款费率
            replacePayment.setPmFee(String.valueOf(pmfee));//扣款到账金额;
            replacePayment.setPmNotifyurl(jsonObject.getString("notifyUrl"));//扣款异步通知地址
            replacePayment.setPmUserid(jsonObject.getString("userId"));//用户id
            replacePayment.setPmStates("2");//扣款订单状态(1:成功; 2:失败; 0:处理中)
            replacePayment.setReleaId(jsonRelea.getString("releaId"));
            Long id = replacePaymentService.insert(replacePayment);//返回新增记录id

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTpayment(jsonObject);
            ReplacePayment replacePaymentupd = new ReplacePayment();
            if(resJson.get("code").equals("000")){//成功
                //请求成功之后改变订单状态
                JSONObject datajson = resJson.getJSONObject("data");
                replacePaymentupd.setPmStates(datajson.getString("orderStatus"));
            }
            replacePaymentupd.setId(id);
            replacePaymentupd.setPmDesc(resJson.getString("msg"));
            replacePaymentService.update(replacePaymentupd);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTPayment执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 7.单笔支付接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/pay"},method = RequestMethod.POST)
    public RestResult KFTPay(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTPay方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //风控（额外单笔手续费）
            String riskStr = getRiskman(jsonObject);
            if(!riskStr.isEmpty()){
                return getRestResult(ResultEnume.FAIL,riskStr,null);
            }
            //添加支付记录
            DecimalFormat df = new DecimalFormat("#.00");
            Double amount = jsonObject.getDouble("amount");//支付金额(元)
            Double extraFee = jsonObject.getDouble("extraFee");//手续费(元)---单笔手续费
            if(amount<=extraFee){
                return getRestResult(ResultEnume.FAIL,"付款金额不能小于等于手续费金额",null);
            }
            Double factAmt =  amount  -  extraFee ;//到账金额(元)
            factAmt = Double.valueOf(df.format(factAmt));
            logger.info("支付金额：{}，手续费：{}，支付到账金额：{}",amount,extraFee,factAmt);

            JSONObject jsonRelea = replaceReleaService.queryReleaPass(jsonObject.getString("merNo"),"1");
            Double releaFee = jsonRelea.getDouble("releaFee");//外放
            Double passFee = jsonRelea.getDouble("passFee");//平台
            //计算分润金额 ： 订单金额 * （单笔手续费差）
            Double releaProfit = Double.valueOf(df.format(extraFee - releaFee));//外放分润(扣款单笔手续费-平台设置单笔手续费)
            Double ptProfit =Double.valueOf(df.format(releaFee - passFee));//平台所得分润
            logger.info("平台分润：{}；外放分润：{}",ptProfit,releaProfit);

            ReplacePay replacePay = new ReplacePay();
            replacePay.setPayOrderNo(jsonObject.getString("orderNo"));//唯一订单号
            replacePay.setPayTranAmt(amount.toString());//支付金额(元)
            replacePay.setPayRoutAmt(extraFee.toString());//手续费(元)---单笔手续费
            replacePay.setPayFactAmt(String.valueOf(factAmt));//到账金额(元)
            replacePay.setPayName(jsonObject.getString("name"));//真实姓名
            replacePay.setPayIdcard(jsonObject.getString("idCard"));//身份证号
            replacePay.setPayCardcode(jsonObject.getString("cardCode"));//银行卡号
            replacePay.setPayPhone(jsonObject.getString("phone"));//电话号
            replacePay.setUserId(jsonObject.getString("userId"));//用户 ID
            replacePay.setPayNotifyurl(jsonObject.getString("notifyUrl"));//异步通知地址
            replacePay.setPayOrderStates("2");//支付订单状态(1：成功 2：失败 0：处理中)
            replacePay.setReleaId(jsonRelea.getString("releaId"));//外放id
            int id = replacePayService.insert(replacePay);

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTpay(jsonObject);
            ReplacePay replacePayupd = new ReplacePay();
            if(resJson.get("code").equals("000")) {//成功
                //请求成功之后改变订单状态
                JSONObject datajson = resJson.getJSONObject("data");
                replacePayupd.setPayOrderStates(datajson.getString("orderStatus"));
            }
            replacePayupd.setId(Long.valueOf(id));
            replacePayupd.setPayOrderDesc(resJson.getString("msg"));
            replacePayService.update(replacePayupd);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTPay执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 8.订单查询接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/order/query"},method = RequestMethod.POST)
    public RestResult KFTOrderquery(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTOrderquery方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTorderquery(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                String msg = resJson.get("msg").toString();
                JSONObject data = resJson.getJSONObject("data");

                JSONObject dataJson = new JSONObject();
                dataJson.put("orderCode",data.getString("orderCode"));
                dataJson.put("amount",data.getString("amount"));
                dataJson.put("orderStatus",data.getString("orderStatus"));
                dataJson.put("createTime",data.getString("createTime"));
                return getRestResult(ResultEnume.SUCCESS,msg,dataJson);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTOrderquery执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 9.协议扣款 / 单笔支付 回调通知
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/notify"},method = RequestMethod.POST)
    //public RestResult KFTNotify(HttpServletRequest request) throws Exception {
    public RestResult KFTNotify(@RequestBody JSONObject jsonObject) throws Exception {
        logger.info("进入KFTReplaceController的KFTNotify方法,参数jsonObject:{}",jsonObject);
        Map<String,Object> map1 = (Map<String,Object>)jsonObject;
        //Map<String ,String[]> map = request.getParameterMap();
        if(!map1.isEmpty()){
            /*Set<String> keys = map.keySet();
            Map<String ,String> map1 = new HashMap<>();
            for(String key : keys){
                String[] value = map.get(key);
                for(String v : value){
                    map1.put(key,v);
                }
            }*/
            logger.info("进入KFTReplaceController的KFTNotify方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderNo = map1.get("orderNo").toString();
            String orderStatus = map1.get("orderStatus").toString();//1 成功 -1 失败
            String errorCode = map1.get("errorCode").toString();
            String errorMsg = map1.get("errorMsg").toString();

            String states = "";
            if(orderStatus.equals("1")){
                states = "1";
            }else if(orderStatus.equals("-1")){
                states = "2";
            }

            //扣款订单处理
            Map<String ,String> mapPayment = new HashMap<>();
            mapPayment.put("pmOrderno",orderNo);
            List<ReplacePayment> replacePaymentList = replacePaymentService.query(mapPayment);
            if(!replacePaymentList.isEmpty()){
                String pmStates = replacePaymentList.get(0).getPmStates();
                logger.info("平台订单状态pmStates:{}",pmStates);
                if(!pmStates.equals("1")){//平台成功
                    ReplacePayment replacePaymentupd = new ReplacePayment();
                    replacePaymentupd.setId(replacePaymentList.get(0).getId());
                    replacePaymentupd.setPmStates(states);
                    if(map1.get("errorMsg") !=null){//描述不为空
                        replacePaymentupd.setPmDesc(map1.get("errorMsg").toString());
                    }
                    replacePaymentService.update(replacePaymentupd);
                }
                //处理请求下游通知
                String notifyUrl = replacePaymentList.get(0).getPmNotifyurl();//下游通知地址
                logger.info("pm处理请求下游通知notifyUrl:{}",notifyUrl);
                JSONObject json = new JSONObject();
                json.put("orderNo",orderNo);
                json.put("orderStatus",orderStatus);
                json.put("errorCode",errorCode);
                json.put("errorMsg",errorMsg);
                HttpClients.doJsonPost(notifyUrl,json);
                return getRestResult(ResultEnume.SUCCESS,null,null);
            }

            //支付订单处理
            Map<String ,String> mapPay = new HashMap<>();
            mapPay.put("payOrderNo",orderNo);
            List<ReplacePay> replacePayList = replacePayService.query(mapPay);
            if(!replacePayList.isEmpty()){
                String payStates = replacePayList.get(0).getPayOrderStates();
                logger.info("平台订单状态payStates:{}",payStates);
                if(!payStates.equals("1")){//平台成功
                    ReplacePay replacePayupd = new ReplacePay();
                    replacePayupd.setId(replacePayList.get(0).getId());
                    replacePayupd.setPayOrderStates(states);
                    if(map1.get("errorMsg") !=null){
                        replacePayupd.setPayOrderDesc(map1.get("errorMsg").toString());
                    }
                    replacePayService.update(replacePayupd);
                }
                //处理请求下游通知
                String notifyUrl = replacePayList.get(0).getPayNotifyurl();//下游通知地址
                logger.info("pay处理请求下游通知notifyUrl:{}",notifyUrl);
                JSONObject json = new JSONObject();
                json.put("orderNo",orderNo);
                json.put("orderStatus",orderStatus);
                json.put("errorCode",errorCode);
                json.put("errorMsg",errorMsg);
                HttpClients.doJsonPost(notifyUrl,json);
                return getRestResult(ResultEnume.SUCCESS,null,null);
            }
        }
        return getRestResult(ResultEnume.FAIL,null,null);
    }

    /**
     * 10.用户余额查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/user/balance"},method = RequestMethod.POST)
    public RestResult KFTBalance(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTBalance方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTuserbalance(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                String msg = resJson.get("msg").toString();
                JSONObject data = resJson.getJSONObject("data");

                JSONObject dataJson = new JSONObject();
                dataJson.put("userId",data.getString("userId"));
                dataJson.put("balance",data.getString("balance"));
                dataJson.put("createTime",data.getString("createTime"));
                dataJson.put("companyId",jsonObject.getString("merNo"));//返回外放代号

                return getRestResult(ResultEnume.SUCCESS,msg,dataJson);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTBalance执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 11.商户分润查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/company/balance"},method = RequestMethod.POST)
    public RestResult KFTComBalance(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入KFTReplaceController的KFTComBalance方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = kftReplaceApi.KFTcombalance(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                //返回余额 和 分润，需要进行分润处理

            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error("KFTComBalance执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }
}
