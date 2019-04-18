package com.cypro.ascpay.rest.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.replace.bank.ReplaceBank;
import com.cypro.ascpay.api.replace.bank.ReplaceBankService;
import com.cypro.ascpay.api.replace.info.ReplaceInfo;
import com.cypro.ascpay.api.replace.info.ReplaceInfoService;
import com.cypro.ascpay.api.replace.relea.ReplaceRelea;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import com.cypro.ascpay.rest.common.bean.RestResult;
import com.cypro.ascpay.rest.common.bean.ResultEnume;
import com.cypro.ascpay.rest.common.utils.JsonAndBeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ascme
 * @ClassName 封装的公共代码控制类
 * @Date 2019-02-19
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Resource(name = "replaceReleaService")
    private ReplaceReleaService replaceReleaService;

    @Resource(name = "replaceBankService")
    private ReplaceBankService replaceBankService;

    @Resource(name = "replaceInfoService")
    private ReplaceInfoService replaceInfoService;

    /**
     * 公共返回参数格式
     * @param respCode 返回码
     * @param respMsg 返回描述
     * @param data 返回数据
     * @return 返回结果集
     */
    public RestResult getRestResult(String respCode,String respMsg,Object data){
        RestResult result = new RestResult();
        result.setRespCode(respCode);
        result.setRespMsg(respMsg);
        result.setData(data);
        return result;
    }

    /**
     * 平台验签
     * 1.验签规则: 参数按照ASCII码从小到大的顺序进行拼接，待签名字符串格式：signStr="A=a&B=b&C=c&........"
     * 2.签名字符串 sign = MD5( signStr + key)
     * @param jsonObject 待验签参数jsonObject对象
     * @return
     */
    public String checkSign(JSONObject jsonObject) throws Exception{
        try{
            String signStr = JsonAndBeanUtils.JsonObjectToString(jsonObject);
            logger.info("待验签字符串:{}",signStr);
            if(jsonObject.getString("merNo").isEmpty()){
                return "商户编号不能为空";
            }
            //根据代还外放的代号查询相对应的外放密钥
            String merNo = jsonObject.getString("merNo");
            Map<String ,String> map = new HashMap<>();
            map.put("releaNo",merNo);
            List<ReplaceRelea> list = replaceReleaService.query(map);
            logger.info("查询外放list为:{}",list);
            if(!list.isEmpty()){
                String signkey = list.get(0).getReleaKey();
                String sign = DigestUtils.md5Hex(signStr + signkey);
                logger.info("{}外放密钥为:{}, 签名之后为:{}",merNo,signkey,sign);
                if(!sign.equals(jsonObject.getString("sign"))){//签名失败
                    return "签名验证失败!请检查参数是否正确!";
                }
                return "";
            }else{
                return "暂无可用通道";
            }
        }catch (Exception e){
            logger.error("checkSign执行出错:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 公共返回参数处理类
     * @param resJson
     * @return
     */
    public RestResult getResult(JSONObject resJson){
        String msg = resJson.getString("msg");
        if(resJson.containsKey("code") && resJson.get("code") != null) {
            if (resJson.get("code").equals("000") || resJson.get("code").equals("0000")) {//成功
                /**
                 * 返回数据
                 */
                if (!resJson.getString("data").contains("{") || !resJson.getString("data").contains("}")) {
                    logger.info("返回外放data参数:{}", resJson.getString("data"));
                    return getRestResult(ResultEnume.SUCCESS, msg, resJson.getString("data"));
                }else if(resJson.getString("data").contains("<html>")){
                    logger.info("返回外放data参数:{}", resJson.getString("data"));
                    return getRestResult(ResultEnume.SUCCESS, msg, resJson.getString("data"));
                } else {
                    JSONObject data = resJson.getJSONObject("data");
                    data.remove("profit");
                    logger.info("返回外放data参数:{}", data);
                    return getRestResult(ResultEnume.SUCCESS, msg, data);
                }
            }else{
                return getRestResult(ResultEnume.FAIL,msg,null);
            }
        }else{
            return getRestResult(ResultEnume.FAIL,msg,null);
        }
    }

    /**
     * 风控控制(费率，单笔手续费)
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public String getRiskman(JSONObject jsonObject) throws Exception{
        try{
            if(!jsonObject.getString("merNo").isEmpty()){
                Map<String ,String> map = new HashMap<>();
                map.put("releaNo",jsonObject.getString("merNo"));
                List<ReplaceRelea> replaceReleaList = replaceReleaService.query(map);

                if(!replaceReleaList.isEmpty()){
                    if(!replaceReleaList.get(0).getReleaRate().isEmpty()){//费率控制
                        if(jsonObject.containsKey("rate") && jsonObject.get("rate") != null) {
                            logger.info("传入rate:{},平台设置releaRate:{}", jsonObject.getDouble("rate"), replaceReleaList.get(0).getReleaRate());
                            if (jsonObject.getDouble("rate") < Double.valueOf(replaceReleaList.get(0).getReleaRate())) {//判断传入的费率 是否比平台设置的费率要大，小则不通过
                                return "费率不能低于" + replaceReleaList.get(0).getReleaRate();
                            }
                        }
                    }
                    if(!replaceReleaList.get(0).getReleaFee().isEmpty()){//单笔手续费控制
                        if(jsonObject.containsKey("extraFee") && jsonObject.get("extraFee") != null){
                            logger.info("传入extraFee:{},平台设置releaFee:{}",jsonObject.getDouble("extraFee"),replaceReleaList.get(0).getReleaFee());
                            if(jsonObject.getDouble("extraFee") < Double.valueOf(replaceReleaList.get(0).getReleaFee())){//判断传入的单笔手续费 是否比平台设置的单笔手续费要大，小则不通过
                                return "单笔手续费不能低于" + replaceReleaList.get(0).getReleaFee();
                            }
                        }
                    }
                }
            }
            return "";
        }catch (Exception e){
            logger.error("getRiskman执行出错:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据外放代码查询外放密钥
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public List queryRelea(JSONObject jsonObject) throws Exception{
        //根据外放代码查询外放密钥
        String merNo = jsonObject.getString("merNo");
        Map<String ,String> map = new HashMap<>();
        map.put("releaNo",merNo);
        List<ReplaceRelea> list = replaceReleaService.query(map);

        return list;
    }

    /**
     * 根据不同银行控制费率不同（针对新生代还大额）
     * @param jsonObject
     * @throws Exception
     */
    public String  queryReleaBank(JSONObject jsonObject) throws Exception {
        try {
            if (!jsonObject.getString("merNo").isEmpty()) {
                Map<String, String> map = new HashMap<>();
                map.put("releaNo", jsonObject.getString("merNo"));
                List<ReplaceRelea> replaceReleaList = replaceReleaService.query(map);//获取外放信息

                Map<String, String> mapInfo = new HashMap<>();
                mapInfo.put("userId", jsonObject.getString("userId"));
                mapInfo.put("passId", String.valueOf(replaceReleaList.get(0).getPassId()));
                List<ReplaceInfo> replaceInfoList = replaceInfoService.query(mapInfo);//获取用户信息银行

                Map<String, String> mapBank = new HashMap<>();
                mapBank.put("releaId", String.valueOf(replaceReleaList.get(0).getId()));
                mapBank.put("bankName", replaceInfoList.get(0).getBankName());
                List<ReplaceBank> replaceBankList = replaceBankService.query(mapBank);//获取银行费率

                Double rateValue = Double.valueOf(replaceBankList.get(0).getRate());//银行费率

                Double rate = jsonObject.getDouble("rate");//传入费率
                if (rate < rateValue) {//传入费率小于银行费率不通过
                    return "费率不能小于" + rateValue;
                }
            }
            return "";
        } catch (Exception e) {
            logger.error("queryReleaBank执行出错:{}", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据不同银行控制费率不同
     * @param jsonObject
     * @throws Exception
     */
    public String  queryReleaBank1(JSONObject jsonObject) throws Exception {
        try {
            if (!jsonObject.getString("merNo").isEmpty()) {
                Map<String, String> map = new HashMap<>();
                map.put("releaNo", jsonObject.getString("merNo"));
                List<ReplaceRelea> replaceReleaList = replaceReleaService.query(map);//获取外放信息


                Map<String, String> mapBank = new HashMap<>();
                mapBank.put("releaId", String.valueOf(replaceReleaList.get(0).getId()));
                mapBank.put("bankName", jsonObject.getString("creditBankName"));
                List<ReplaceBank> replaceBankList = replaceBankService.query(mapBank);//获取银行费率

                Double rateValue = Double.valueOf(replaceBankList.get(0).getRate());//银行费率

                Double rate = jsonObject.getDouble("rate");//传入费率
                if (rate < rateValue) {//传入费率小于银行费率不通过
                    return "费率不能小于" + rateValue;
                }
            }
            return "";
        } catch (Exception e) {
            logger.error("queryReleaBank执行出错:{}", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
