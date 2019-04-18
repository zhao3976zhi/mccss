package com.cypro.ascpay.rest.quick.controller;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.nocard.NocardPay;
import com.cypro.ascpay.api.nocard.NocardPayService;
import com.cypro.ascpay.rest.common.bean.RestResult;
import com.cypro.ascpay.rest.common.bean.ResultEnume;
import com.cypro.ascpay.rest.common.controller.BaseController;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import com.cypro.ascpay.rest.quick.api.XSquickPayApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/xs/quick")
public class XSquickPayController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(XSquickPayController.class);

    @Resource
    private XSquickPayApi xSquickPayApi;

    @Resource(name = "nocardPayService")
    private NocardPayService nocardPayService;
    /**
     * 1.消费申请
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/apply"},method = RequestMethod.POST)
    public RestResult Xsapply(@RequestBody JSONObject jsonObject){
        logger.info("进入XSquickPayController的Xsapply方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //大额费率控制是根据不同银行不同费率控制
            String isValue = getRiskman(jsonObject);
            if(!isValue.isEmpty()){
                return getRestResult(ResultEnume.FAIL,isValue,null);
            }

            //记录消费订单
            //NocardPay nocardPay = JSON.parseObject(jsonObject.toJSONString(), NocardPay.class);

            NocardPay nocardPay = new NocardPay();
            nocardPay.setOrderNo(jsonObject.getString("orderCode"));//订单号
            nocardPay.setAmount(jsonObject.getString("amount"));//订单金额(元)
            nocardPay.setBankName(jsonObject.getString("creditBankName"));//银行名称
            nocardPay.setCardCode(jsonObject.getString("creditCard"));//信用卡卡号
            nocardPay.setCardPhone(jsonObject.getString("creditPhone"));//信用卡预留手机号
            nocardPay.setCvv2(jsonObject.getString("cvv2"));//安全码
            nocardPay.setValid(jsonObject.getString("creditValidDate"));//有效期
            nocardPay.setFee(jsonObject.getString("extraFee"));//固定手续费(元)
            nocardPay.setIdcard(jsonObject.getString("idCard"));//身份证号
            nocardPay.setNotifyurl(jsonObject.getString("notifyUrl"));//异步通知地址
            nocardPay.setUserId(jsonObject.getString("userId"));//用户id
            nocardPay.setRate(jsonObject.getString("rate"));//费率
            nocardPay.setSettleCard(jsonObject.getString("settleCard"));//借记卡卡号
            nocardPay.setSettlePhone(jsonObject.getString("settlePhone"));//借记卡银行预留电话
            nocardPay.setRealName(jsonObject.getString("realName"));//真实姓名


            DecimalFormat df = new DecimalFormat("#.00");
            Double sumFee = Double.valueOf(df.format((jsonObject.getDouble("amount") * jsonObject.getDouble("rate")) + jsonObject.getDouble("extraFee")));//总手续费
            Double selAmt = Double.valueOf(df.format(jsonObject.getDouble("amount") - sumFee));//结算到账金额
            logger.info("总手续费:{}, 结算到账金额:{}",sumFee,selAmt);
            if(jsonObject.getDouble("amount") < sumFee){
                return getRestResult(ResultEnume.FAIL,"订单金额不能小于手续费金额",null);
            }

            nocardPay.setOrderStates("2");//订单状态(1：成功 2：失败 0：处理中)
            nocardPay.setSumFee(sumFee.toString());
            nocardPay.setSelAmt(selAmt.toString());

            //请求上游接口
            JSONObject resJson = xSquickPayApi.XsQuickapply(jsonObject);

            nocardPay.setReleaId(resJson.getString("releaId"));//外放id
            nocardPayService.insert(nocardPay);

            String msg = resJson.getString("msg");
            if(resJson.get("code").equals("000")){//成功
                /**
                 * 返回数据
                 */
                JSONObject data = resJson.getJSONObject("data");
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
     * 2.消费确认
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/confirm"},method = RequestMethod.POST)
    public RestResult XsQuickconfirm(@RequestBody JSONObject jsonObject){
        logger.info("进入XSquickPayController的XsQuickconfirm方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            String orderNo = jsonObject.getString("orderCode");//订单号

            //请求上游接口
            JSONObject resJson = xSquickPayApi.XsQuickconfirm(jsonObject);
            String states = "2";
            if(resJson.get("code").equals("000")) {//成功
                JSONObject datajson = resJson.getJSONObject("data");
                String orderStatus = datajson.getString("orderStatus");
                if(orderStatus.equals("0000")){//交易成功
                    states = "1";
                }else if(orderStatus.equals("4444")){//交易失败
                    states = "2";
                }else if(orderStatus.equals("5555")){//交易失效
                    states = "2";
                }else if(orderStatus.equals("9999")){//交易进行中
                    states = "0";
                }
            }
            Map<String ,String> map = new HashMap<>();
            map.put("orderNo",orderNo);
            List<NocardPay> nocardPayList = nocardPayService.queryList(map);
            if(nocardPayList != null){
                NocardPay nocardPay = new NocardPay();
                nocardPay.setOrderStates(states);
                nocardPay.setOrderDesc(resJson.getString("msg"));
                nocardPay.setId(nocardPayList.get(0).getId());
                //修改订单状态
                nocardPayService.update(nocardPay);
            }

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsQuickconfirm执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 3.消费查询
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = {"/query"},method = RequestMethod.POST)
    public RestResult XsQuickquery(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSquickPayController的XsQuickquery方法,参数为:{}",jsonObject);
        try{
            //验签
            String issign = checkSign(jsonObject);
            if(!issign.isEmpty()){
                return getRestResult(ResultEnume.FAIL,issign,null);
            }

            //请求上游接口
            JSONObject resJson = xSquickPayApi.XsQuickquery(jsonObject);

            return getResult(resJson);
        }catch (Exception e){
            logger.error("XsQuickquery执行出错:{}",e.getMessage());
            e.printStackTrace();
            return getRestResult(ResultEnume.FAIL,"请稍后尝试",null);
        }
    }

    /**
     * 4.消费异步通知
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/notify"} ,method = RequestMethod.POST)
    public String Xsquicknotify(@RequestBody JSONObject jsonObject) throws Exception{
        logger.info("进入XSquickPayController的Xsquicknotify方法,参数jsonObject:{}",jsonObject);
        Map<String,Object> map1 = (Map<String,Object>)jsonObject;
        if(!map1.isEmpty()){
            logger.info("进入XSquickPayController的Xsquicknotify方法,参数map1为:{}",map1);

            //判断订单状态是否处理
            String orderNo = map1.get("orderCode").toString();
            String upOrderCode = map1.get("upOrderCode").toString();//平台订单号
            String resultCode = map1.get("resultCode").toString();//响应码
            String respMsg = map1.get("respMsg").toString();//响应信息
            String settleAmt = map1.get("settleAmt").toString();//实际到账金额

            String states = "2";
            if(resultCode.equals("0000")){//交易成功
                states = "1";//成功
            }else if(resultCode.equals("4444")){//交易失败
                states = "2";//失败
            }else if(resultCode.equals("5555")){//交易失效
                states = "2";//失败
            }else if(resultCode.equals("9999")){//交易进行中
                states = "0";//处理中
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
                    //修改订单状态
                    nocardPayService.update(nocardPay);
                }
            }

            //处理请求下游通知
            String notifyUrl = nocardPayList.get(0).getNotifyurl();//下游通知地址
            logger.info("pay处理请求下游通知notifyUrl:{}",notifyUrl);
            JSONObject json = new JSONObject();
            json.put("orderNo",orderNo);
            json.put("resultCode",resultCode);
            json.put("upOrderCode",upOrderCode);
            json.put("respMsg",respMsg);
            json.put("settleAmt",settleAmt);
            JSONObject reslut = HttpClients.doJsonPost(notifyUrl,json);
            logger.info("外放返回参数reslut:{}",reslut);
            if(reslut != null){
                return reslut.toString();
            }
        }
        return "";
    }

}
