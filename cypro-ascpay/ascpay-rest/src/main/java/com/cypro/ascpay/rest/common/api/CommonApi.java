package com.cypro.ascpay.rest.common.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import com.cypro.ascpay.rest.common.utils.HttpClients;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ascme
 * @ClassName 公共 API 方法
 * @Date 2019-04-07
 */
@Service
public class CommonApi {
    private Logger logger = LoggerFactory.getLogger(CommonApi.class);

    /**
     * 服务器ip
     */
    public String server_ip="47.92.252.143";


    @Resource(name = "replaceReleaService")
    private ReplaceReleaService replaceReleaService;
    /**
     * 1.获取平台外放密钥
     * 2.获取上游渠道商户号和密钥(merchantNo,merKey)
     * @param releaNo 外放代号
     * @return
     * @throws Exception
     */
    public JSONObject getMernoAndkey(String releaNo, String releaType) throws Exception{
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
    public JSONObject getmerNoandkey(JSONObject jsonObject,String releaType) throws Exception{
        /**
         *根据外放代号releaNo 获取商户号密钥
         */
        String releaNo = jsonObject.getString("merNo");
        JSONObject json = getMernoAndkey(releaNo, releaType);

        return json;
    }


    /**
     * 公共请求方式
     * @param jsonObject
     * @return
     */
    public JSONObject requestCommon(JSONObject jsonObject) {
        String merchantNo = jsonObject.get("merchantNo").toString();
        String url = jsonObject.get("url").toString();
        String key = jsonObject.get("key").toString();

        String data = "";
        if (jsonObject.containsKey("data")) {
            data = jsonObject.get("data").toString();
        }

        /**
         * sign:签名
         */
        String sign = null;
        if (!data.isEmpty()) {
            sign = DigestUtils.md5Hex(data + key);
        } else {
            sign = DigestUtils.md5Hex(key);
        }

        Map<String, String> map = new HashMap<>();
        map.put("merchantNo", merchantNo);
        if (!data.isEmpty()) {
            map.put("data", data);
        }
        map.put("sign", sign);
        logger.info("请求参数jsonObject为:{}", jsonObject);
        logger.info("请求地址为:{}", url);
        logger.info("请求上游参数map为:{}", map);
        String res = HttpClients.sendPost(url, map);
        logger.info("上游返回参数为res:{}", res);
        JSONObject json = JSON.parseObject(res);
        if (jsonObject.containsKey("passId")) {
            json.put("passId", jsonObject.getString("passId"));
        }
        if (jsonObject.containsKey("releaId")) {
            json.put("releaId", jsonObject.getString("releaId"));
        }
        return json;
    }
}
