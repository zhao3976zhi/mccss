package com.cypro.reptile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpClients {

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param map
     *            请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param charset
     *             发送和接收的格式
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String,Object> map,String charset){
        StringBuffer sb=new StringBuffer();
        //构建请求参数
        if(map!=null&&map.size()>0){
            for (Map.Entry<String, Object> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        return  sendPost(url,sb.toString(),charset);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset
     *             发送和接收的格式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String charset) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String line;
        StringBuffer sb=new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(60);
            conn.setReadTimeout(60);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
            System.out.println("发送 POST请求出现异常!"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
