package com.cypro.ascpay.provider.replace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.replace.pass.ReplacePass;
import com.cypro.ascpay.api.replace.pass.ReplacePassService;
import com.cypro.ascpay.api.replace.relea.ReplaceRelea;
import com.cypro.ascpay.api.replace.relea.ReplaceReleaService;
import com.cypro.ascpay.provider.replace.entity.ReplaceReleaEntity;
import com.cypro.ascpay.provider.replace.mapper.ReplaceReleaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("replaceReleaService")
public class ReplaceReleaServiceImpl implements Serializable , ReplaceReleaService {

    private Logger logger = LoggerFactory.getLogger(ReplaceReleaServiceImpl.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "replaceReleaMapper")
    private ReplaceReleaMapper replaceReleaMapper;

    @Resource(name = "replacePassService")
    private ReplacePassService replacePassService;

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplaceRelea> query(Map map) throws Exception {
        logger.info("进入query方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<ReplaceReleaEntity> replaceEntityList = replaceReleaMapper.query(map);
                logger.info("replaceEntityList的值为:{}",replaceEntityList);
                List<ReplaceRelea> replaceList = new LinkedList<>();
                if(replaceEntityList != null){
                    for(ReplaceReleaEntity entity:replaceEntityList){
                        if(entity != null){
                            ReplaceRelea replace = new ReplaceRelea();
                            BeanUtils.copyProperties(entity,replace);
                            if(entity.getCreaTime() != null){
                                replace.setCreaTime(sdf.format(entity.getCreaTime()));
                            }
                            replaceList.add(replace);
                        } else{
                            return null;
                        }
                    }
                    logger.info("replaceList的值为:{}",replaceList);
                    return replaceList;
                }else{
                    return null;
                }
            }else{
                logger.error("map不能为空");
                throw new NullPointerException("map不能为空");
            }
        }catch (Exception e){
            logger.error("出错了！Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 添加
     * @param replace 参数实体
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ReplaceRelea replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplaceReleaEntity replaceEntity = new ReplaceReleaEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replaceReleaMapper.insert(replaceEntity);//平台订单号
                logger.info("平台订单号id:{}",id);
                if(id > 0){
                    return id;
                }else{
                    return 0;
                }
            }
            return 0;
        }catch (Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 修改
     * @param replace 参数实体
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ReplaceRelea replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplaceReleaEntity replaceEntity = new ReplaceReleaEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replaceReleaMapper.update(replaceEntity);//平台订单号
                logger.info("平台订单号id:{}",id);
                if(id > 0){
                    return id;
                }else{
                    return 0;
                }
            }
            return 0;
        }catch (Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除
     * @param id 主键id
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) throws Exception {
        logger.info("进入delete方法,接收参数为:{}",id);
        try{
            if(id != null){
                replaceReleaMapper.delete(id);
            }
        }catch(Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据外放代号和类型查询配置的上游通道商户号和密钥
     * @param releaNo
     * @return
     */
    @Override
    public JSONObject queryReleaPass(String releaNo,String releaType) throws Exception{
        logger.info("进入queryReleaPass方法,接收参数为:{}",releaNo);
        try{
            if(!releaNo.isEmpty()){
                //获取上游商户号和密钥
                Map<String,Object> map = new HashMap<>();
                map.put("releaNo",releaNo);
                map.put("releaType",releaType);
                List<ReplaceRelea> replaceReleaList = query(map);
                logger.info("返回replaceReleaList的值为:{}",replaceReleaList);
                if(replaceReleaList.isEmpty()){
                    return null;
                }
                map.put("id",replaceReleaList.get(0).getPassId());
                List<ReplacePass> replacePassList = replacePassService.query(map);
                logger.info("返回replacePassList的值为:{}",replacePassList);
                    if(!replacePassList.isEmpty()){
                        String merchantNo = replacePassList.get(0).getMerNo();//商户号
                        String merkey = replacePassList.get(0).getMerKey();//密钥

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("merchantNo",merchantNo);
                        jsonObject.put("merKey",merkey);
                        jsonObject.put("passRate",replacePassList.get(0).getRate());
                        jsonObject.put("passFee",replacePassList.get(0).getFee());
                        jsonObject.put("passId",replaceReleaList.get(0).getPassId());


                        jsonObject.put("releaRate",replaceReleaList.get(0).getReleaRate());
                        jsonObject.put("releaFee",replaceReleaList.get(0).getReleaFee());
                        jsonObject.put("releaId",replaceReleaList.get(0).getId());
                        return jsonObject;
                    }else{
                    return null;
                }
            }else {
                logger.error("releaNo不能为空");
                throw new NullPointerException("releaNo不能为空");
            }
        }catch (Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
