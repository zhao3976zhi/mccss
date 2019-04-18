package com.cypro.ascpay.rest.replace.controller;

import com.alibaba.fastjson.JSONObject;
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
import com.cypro.ascpay.rest.replace.api.XSReplaceApi;
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
 * @ClassName xinsheng(新生)代还 大额
 * @Date 2019-03-18
 */
@RestController
@RequestMapping(value = "/xs/replace")
public class XSReplaceController extends BaseController {

    private Logger logger  = LoggerFactory.getLogger(XSReplaceController.class);
    //代还用户信息
    @Resource(name = "replaceInfoService")
    private ReplaceInfoService replaceInfoService;

    //代还扣款交易
    @Resource(name = "replacePaymentService")
    private ReplacePaymentService replacePaymentService;

    //代还外放信息
    @Resource(name="replaceReleaService")
    private ReplaceReleaService replaceReleaService;

    //代还支付交易
    @Resource(name="replacePayService")
    private ReplacePayService replacePayService;
    /**
     * 上游代还api接口
     */
    @Resource
    private XSReplaceApi xSReplaceApi;

    /**
     * 1.签约申请
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/apply"},method = RequestMethod.POST)
    public RestResult Xsapply(@RequestBody JSONObject jsonObject){
        logger.info("进入XSReplaceController的Xsapply方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xsapply(jsonObject);
            String msg = resJson.getString("msg");
            if(resJson.get("code").equals("000")){//成功
                /**
                 * 记录用户信息
                 */
                ReplaceInfo replaceInfo = new ReplaceInfo();
                replaceInfo.setName(jsonObject.getString("realName"));
                replaceInfo.setCardNo(jsonObject.getString("cardCode"));
                replaceInfo.setBankName(jsonObject.getString("bankName"));
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
                String data = resJson.getString("data");
                logger.info("返回外放data参数:{}",data);
                return getRestResult(ResultEnume.SUCCESS,msg,data);
            } else{
                return getRestResult(ResultEnume.FAIL,msg,null);
            }
        }catch (Exception e){
            logger.error("Xsapply执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 2.签约确认
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/confirm"},method = RequestMethod.POST)
    public RestResult Xsconfirm(@RequestBody JSONObject jsonObject){
        logger.info("进入XSReplaceController的Xsconfirm方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xsconfirm(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("Xsconfirm执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 3.取消签约
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/cannel"},method = RequestMethod.POST)
    public RestResult XsCannel(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XsCannel方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xscannel(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsCannel执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 4.收款请求
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/payment"},method = RequestMethod.POST)
    public RestResult XsPayment(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XsPayment方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //大额费率控制是根据不同银行不同费率控制
            /*String isValue = queryReleaBank(jsonObject);
            if(!isValue.isEmpty()){
                return getRestResult(ResultEnume.FAIL,isValue,null);
            }*/
            //风控（费率）
            String riskStr = getRiskman(jsonObject);
            if(!riskStr.isEmpty()){
                return getRestResult(ResultEnume.FAIL,riskStr,null);
            }

            // 添加协议扣款记录
            DecimalFormat df = new DecimalFormat("#.00");
            Double amount = jsonObject.getDouble("amount");//扣款金额
            Double rate = jsonObject.getDouble("rate");//扣款费率
            Double shouxf = Double.valueOf(df.format(amount*rate));//手续费
            Double pmfee = Double.valueOf(df.format(amount - shouxf)); //扣款到账金额;
            logger.info("扣款金额：{}，手续费：{}，扣款到账金额：{}",amount,shouxf,pmfee);

            JSONObject jsonRelea = replaceReleaService.queryReleaPass(jsonObject.getString("merNo"),"2");
            /*Double releaRate = jsonRelea.getDouble("releaRate");//外放
            Double passRate = jsonRelea.getDouble("passRate");//平台
            //计算分润金额 ： 订单金额 * （费率差）
            Double releaProfit = Double.valueOf(df.format(amount * (rate - releaRate)));//外放分润(金额 *(扣款费率-平台设置费率))
            Double ptProfit = Double.valueOf(df.format(amount * (releaRate - passRate)));//平台所得分润
            logger.info("传入费率：{}；平台外放费率：{}；上游通道费率：{}",rate,releaRate,passRate);
            logger.info("平台分润：{}；外放分润：{}",ptProfit,releaProfit);*/

            ReplacePayment replacePayment = new ReplacePayment();
            replacePayment.setPmOrderno(jsonObject.getString("orderCode"));//订单号
            replacePayment.setPmAmount(String.valueOf(amount));//扣款金额
            replacePayment.setPmRate(String.valueOf(rate));//扣款费率
            replacePayment.setPmFee(String.valueOf(pmfee));//扣款到账金额;
            replacePayment.setPmNotifyurl(jsonObject.getString("notifyUrl"));//扣款异步通知地址
            replacePayment.setPmUserid(jsonObject.getString("userId"));//用户id
            replacePayment.setPmStates("2");//扣款订单状态(1:成功; 2:失败; 0:处理中)
            replacePayment.setReleaId(jsonRelea.getString("releaId"));
            Long id = replacePaymentService.insert(replacePayment);//返回新增记录id

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xsreceiptapply(jsonObject);
            ReplacePayment replacePaymentupd = new ReplacePayment();
            if(resJson.get("code").equals("000")){//成功
                //请求成功之后改变订单状态
                JSONObject datajson = resJson.getJSONObject("data");
                String resultCode = datajson.getString("resultCode");//0000 9999 4444
                String states="2";
                if(resultCode.equals("0000")){//0000 成功 4444 失败 9999 处理中
                    states="1";
                }else if(resultCode.equals("9999")){//处理中
                    states="0";
                }else if(resultCode.equals("4444")){//失败
                    states="2";
                }
                replacePaymentupd.setPmStates(states);//扣款订单状态
            }
            replacePaymentupd.setId(id);
            replacePaymentupd.setPmDesc(resJson.getString("msg"));
            replacePaymentService.update(replacePaymentupd);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsPayment执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 4.1 收款请求的异步通知
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/notify"} ,method = RequestMethod.POST)
    public String Xsnotify(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的Xsnotify方法,参数jsonObject:{}",jsonObject);
        Map<String,Object> map1 = (Map<String,Object>)jsonObject;
        if(!map1.isEmpty()){
            logger.info("进入XSReplaceController的Xsnotify方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderCode = map1.get("orderCode").toString();//收款请求订单号
            String resultCode = map1.get("resultCode").toString();//处理结果
            String errorCode = map1.get("errorCode").toString();//错误码
            String errorMsg = map1.get("errorMsg").toString();//错误信息
            String upOrderCode = map1.get("upOrderCode").toString();//平台订单号
            String amount =  map1.get("amount").toString();//金额

            String states = "2";
            if(resultCode.equals("0000")){//0000 成功 4444 失败 9999 处理中
                states = "1";//0000 成功
            }else if(resultCode.equals("4444")){
                states = "2";//4444 失败
            }else if(resultCode.equals("9999")){
                states = "0";//9999 处理中
            }

            //扣款订单处理
            Map<String ,String> mapPayment = new HashMap<>();
            mapPayment.put("pmOrderno",orderCode);
            List<ReplacePayment> replacePaymentList = replacePaymentService.query(mapPayment);
            if(!replacePaymentList.isEmpty()){
                String pmStates = replacePaymentList.get(0).getPmStates();
                logger.info("平台订单状态pmStates:{}",pmStates);
                if(!pmStates.equals("1")){//平台成功
                    ReplacePayment replacePaymentupd = new ReplacePayment();
                    replacePaymentupd.setId(replacePaymentList.get(0).getId());
                    replacePaymentupd.setPmStates(states);
                    if(map1.get("errorMsg") !=null){
                        replacePaymentupd.setPmDesc(map1.get("errorMsg").toString());
                    }
                    replacePaymentService.update(replacePaymentupd);
                }
                //处理请求下游通知
                String notifyUrl = replacePaymentList.get(0).getPmNotifyurl();//下游通知地址
                logger.info("pm处理请求下游通知notifyUrl:{}",notifyUrl);
                JSONObject json = new JSONObject();
                json.put("orderCode",orderCode);
                json.put("resultCode",resultCode);
                json.put("errorCode",errorCode);
                json.put("errorMsg",errorMsg);
                json.put("upOrderCode",upOrderCode);
                json.put("amount",amount);
                String res = HttpClients.doStrPost(notifyUrl,json);
                logger.info("外放返回参数reslut:{}",res);
                if(res != null){
                    return res.toString();
                }
                return "";
            }
        }
        return "";
    }

    /**
     * 5.收款查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/receipt/query"},method = RequestMethod.POST)
    public RestResult XsReceiptquery(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XsReceiptquery方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xsreceiptquery(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsReceiptquery执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 6.付款接口
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/pay"},method = RequestMethod.POST)
    public RestResult XSPay(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XSPay方法,参数为:{}",jsonObject);
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

            JSONObject jsonRelea = replaceReleaService.queryReleaPass(jsonObject.getString("merNo"),"2");//大额
            Double releaFee = jsonRelea.getDouble("releaFee");//外放
            Double passFee = jsonRelea.getDouble("passFee");//平台
            //计算分润金额 ： 订单金额 * （单笔手续费差）
            Double releaProfit = Double.valueOf(df.format(extraFee - releaFee));//外放分润(扣款单笔手续费-平台设置单笔手续费)
            Double ptProfit =Double.valueOf(df.format(releaFee - passFee));//平台所得分润
            logger.info("平台分润：{}；外放分润：{}",ptProfit,releaProfit);

            ReplacePay replacePay = new ReplacePay();
            replacePay.setPayOrderNo(jsonObject.getString("orderCode"));//唯一订单号
            replacePay.setPayTranAmt(amount.toString());//支付金额(元)
            replacePay.setPayRoutAmt(extraFee.toString());//手续费(元)---单笔手续费
            replacePay.setPayFactAmt(String.valueOf(factAmt));//到账金额(元)
            replacePay.setPayName(jsonObject.getString("realName"));//真实姓名
            replacePay.setPayIdcard(jsonObject.getString("idCard"));//身份证号
            replacePay.setPayCardcode(jsonObject.getString("cardCode"));//银行卡号
            replacePay.setPayPhone(jsonObject.getString("phone"));//电话号
            replacePay.setUserId(jsonObject.getString("userId"));//用户 ID
            replacePay.setPayNotifyurl(jsonObject.getString("notifyUrl"));//异步通知地址
            replacePay.setPayOrderStates("2");//支付订单状态(1：成功 2：失败 0：处理中)
            replacePay.setReleaId(jsonRelea.getString("releaId"));//外放id
            int id = replacePayService.insert(replacePay);

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xspay(jsonObject);
            ReplacePay replacePayupd = new ReplacePay();
            if(resJson.get("code").equals("000")) {//成功
                //请求成功之后改变订单状态
                JSONObject datajson = resJson.getJSONObject("data");
                String resultCode = datajson.getString("resultCode");
                String states="2";
                if(resultCode.equals("0000")){//0000 成功 4444 失败 9999 处理中
                    states="1";
                }else if(resultCode.equals("9999")){//处理中
                    states="0";
                }else if(resultCode.equals("4444")){//失败
                    states="2";
                }
                replacePayupd.setPayOrderStates(states);
            }
            replacePayupd.setId(Long.valueOf(id));
            replacePayupd.setPayOrderDesc(resJson.getString("msg"));
            replacePayService.update(replacePayupd);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XSPay执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 6.1 付款请求的异步通知
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/pay/notify"} ,method = RequestMethod.POST)
    public String Xspaynotify(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的Xspaynotify方法,参数jsonObject:{}",jsonObject);
        Map<String,Object> map1 = (Map<String,Object>)jsonObject;
        if(!map1.isEmpty()){
            logger.info("进入XSReplaceController的KFTNotify方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderCode = map1.get("orderCode").toString();//收款请求订单号
            String resultCode = map1.get("resultCode").toString();//处理结果
            String errorCode = map1.get("errorCode").toString();//错误码
            String errorMsg = map1.get("errorMsg").toString();//错误信息
            String upOrderCode = map1.get("upOrderCode").toString();//平台订单号
            String amount =  map1.get("amount").toString();//金额

            String states = "2";
            if(resultCode.equals("0000")){//0000 成功 4444 失败 9999 处理中
                states = "1";//0000 成功
            }else if(resultCode.equals("4444")){
                states = "2";//4444 失败
            }else if(resultCode.equals("9999")){
                states = "0";//9999 处理中
            }

            //支付订单处理
            Map<String ,String> mapPay = new HashMap<>();
            mapPay.put("payOrderNo",orderCode);
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
                json.put("orderNo",orderCode);
                json.put("resultCode",resultCode);
                json.put("errorCode",errorCode);
                json.put("errorMsg",errorMsg);
                json.put("amount",amount);
                json.put("upOrderCode",upOrderCode);
                String reslut = HttpClients.doStrPost(notifyUrl,json);
                logger.info("外放返回参数reslut:{}",reslut);
                if(reslut != null){
                    return reslut.toString();
                }
                return "";
            }
        }
        return "";
    }

    /**
     * 7.付款查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/pay/query"},method = RequestMethod.POST)
    public RestResult XsPayquery(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XsPayquery方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xspayquery(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsPayquery执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 8.用户余额查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    /*@ResponseBody
    @RequestMapping(value = {"/user/balance"},method = RequestMethod.POST)
    public RestResult XSuserBalance(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XSuserBalance方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.Xsbalancequery(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                String msg = resJson.get("msg").toString();
                JSONObject data = resJson.getJSONObject("data");

                JSONObject dataJson = new JSONObject();
                dataJson.put("userId",data.getString("userId"));
                dataJson.put("balance",data.getString("balance"));
                dataJson.put("createTime",data.getString("createTime"));
                dataJson.put("companyId",jsonObject.getString("merNo"));//返回外放代号
                logger.info("返回外放参数dataJson:{}",dataJson);
                return getRestResult(ResultEnume.SUCCESS,msg,dataJson);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error("XSuserBalance执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }*/

    /**
     * 9.卡余额查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/user/balance"},method = RequestMethod.POST)
    public RestResult XSCardBalance(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSReplaceController的XSCardBalance方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSReplaceApi.XsCardquery(jsonObject);
            if(resJson.get("code").equals("000")) {//成功
                String msg = resJson.get("msg").toString();
                JSONObject data = resJson.getJSONObject("data");
                data.put("companyId",jsonObject.getString("merNo"));//返回外放代号
                logger.info("返回外放参数dataJson:{}",data);
                return getRestResult(ResultEnume.SUCCESS,msg,data);
            }
            return getResult(resJson);
        }catch (Exception e){
            logger.error("XSCardBalance执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }
}
