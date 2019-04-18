package com.cypro.reptile.basis;

import com.cypro.reptile.utils.HttpClients;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReptileTest {

    public static void main(String[] args) {
        ReptileTest test = new ReptileTest();
        test.Onerequest();
    }

    public static void Onerequest(){
        Map<String,Object> params = new HashMap<>();
        params.put("searchFlag","1");
        params.put("proxyId","");
        params.put("rfidAreaCode","0731AA");
        params.put("rfidNumber","");
        params.put("vehsStauts","");
        params.put("productType","");
        params.put("mobilePhone","");
        params.put("idName","");
        params.put("idCardNumber","");
        params.put("vehsNumber","");
        params.put("department","");
        params.put("createUserName","");
        params.put("startTime","2017-01-01 00:00:00");
        params.put("endTime","2019-01-24 23:59:59");
        params.put("pageType","0");
        params.put("pageTotal","63704");
        params.put("pageSize","20");
        params.put("pageIndex","1");
        params.put("countIndex","0");

        params.put("SESSION","6688d793-de9e-41d2-b61c-e1952e387b2e");
        params.put("rememberMe","true");
        params.put("loginName","15388903366");
        params.put("loginUserIdCookie","e74dce6dc14049d9851baca1c238faa1");
        params.put("readerNoState","0");
        params.put("route","08420481271f1c14aa109c6a06927ff6");
        //params.put("Cookie","SESSION=6688d793-de9e-41d2-b61c-e1952e387b2e; rememberMe=true; loginName=15388903366; loginUserIdCookie=e74dce6dc14049d9851baca1c238faa1; readerNoState=0; route=08420481271f1c14aa109c6a06927ff6");

        String url = "http://admin.gdrctd.com/web/moto_query.mc?mid=ddcgl_cxysg";
        String charset = "UTF-8";
        String res = HttpClients.sendPost(url,params,charset);

        System.out.println("返回结果："+ res);
    }



}
