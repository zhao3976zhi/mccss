package com.cypro.ascpay.rest.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cypro.ascpay.api.nocard.NocardPay;

import java.lang.reflect.Field;
import java.util.*;

public class JsonAndBeanUtils {
    /**
     * 把数组所有元素ASCII码排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params  需要排序并参与字符拼接的参数组
     *
     * @return 拼接后字符串
     */
    public static String mapToString(Map<String, String> params) {
        if(params.containsKey("sign")){
            params.remove("sign");
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if(!key.equals("sign") && !key.equals("signkey")){
                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }

        return prestr;
    }

    /**
     * 把数组所有元素ASCII码排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param jsonObject 需要排序并参与字符拼接的参数对象
     *
     * @return 字符串
     */
    public static String JsonObjectToString(JSONObject jsonObject){
        Map<String, String> params = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>(){});
        return mapToString(params);
    }

    /**
     * json数据转实体对象
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    public static Object jsonToBean(JSONObject json, Class cls) throws Exception
    {
        Object object = cls.newInstance();
        System.out.println("1---"+object.toString());
        for (String key : json.keySet())
        {
            Field temFiels = cls.getDeclaredField(key);
            System.out.println("2---"+temFiels);
            temFiels.setAccessible(true);
            temFiels.set(object, json.get(key));


            System.out.println("-----"+key);
        }
        return object;
    }

    public static void main(String[] args) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderNo","1");
        jsonObject.put("amount","2");
        NocardPay nocardPay = new NocardPay();
        String jsonStr = jsonObject.toJSONString();
        NocardPay coa = JSON.parseObject(jsonStr, NocardPay.class);


        System.out.println(coa.getAmount());
        /*Map<String ,String > map = new HashMap<>();
        map.put("fsa","21wa");
        map.put("hgsd","sada");
        map.put("bxcvx","fgsda");
        map.put("tsfs","2243");
        String str = mapToString(map);
        JSONObject json = new JSONObject();
        json.put("sign","11");
        json.put("signkey","22");
        json.put("zdadq","33");
        json.put("adaw2","44");
        String str = JsonObjectToString(json);
        System.out.println(str);*/
    }
}
