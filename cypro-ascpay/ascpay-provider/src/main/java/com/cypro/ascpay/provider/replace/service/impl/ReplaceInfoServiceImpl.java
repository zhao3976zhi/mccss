package com.cypro.ascpay.provider.replace.service.impl;

import com.cypro.ascpay.api.replace.info.ReplaceInfo;
import com.cypro.ascpay.api.replace.info.ReplaceInfoService;
import com.cypro.ascpay.provider.replace.entity.ReplaceInfoEntity;
import com.cypro.ascpay.provider.replace.mapper.ReplaceInfoMapper;
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

@Service("replaceInfoService")
public class ReplaceInfoServiceImpl implements Serializable, ReplaceInfoService {
    private Logger logger = LoggerFactory.getLogger(ReplaceInfoServiceImpl.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "replaceInfoMapper")
    private ReplaceInfoMapper replaceInfoMapper;

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplaceInfo> query(Map map) throws Exception {
        logger.info("进入query方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<ReplaceInfoEntity> replaceInfoEntityList = replaceInfoMapper.query(map);
                logger.info("replaceInfoEntityList的值为:{}",replaceInfoEntityList);
                List<ReplaceInfo> replaceInfoList = new LinkedList<>();
                if(replaceInfoEntityList != null){
                    for(ReplaceInfoEntity entity:replaceInfoEntityList){
                        if(entity != null){
                            ReplaceInfo replaceInfo = new ReplaceInfo();
                            BeanUtils.copyProperties(entity,replaceInfo);
                            if(entity.getCreaTime() != null){
                                replaceInfo.setCreaTime(sdf.format(entity.getCreaTime()));
                            }
                            replaceInfoList.add(replaceInfo);
                        } else{
                            return null;
                        }
                    }
                    logger.info("replaceInfoList的值为:{}",replaceInfoList);
                    return replaceInfoList;
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
    public int insert(ReplaceInfo replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                if(replace.getUserId() != null){//判断用户id和银行卡是否存在
                    Map<String ,String> map = new HashMap<>();
                    map.put("userId",replace.getUserId());
                    map.put("cardNo",replace.getCardNo().toString());
                    map.put("passId",replace.getPassId().toString());
                    List<ReplaceInfo> replaceInfoList =  query(map);

                    if(replaceInfoList.isEmpty()){
                        ReplaceInfoEntity replaceEntity = new ReplaceInfoEntity();
                        BeanUtils.copyProperties(replace,replaceEntity);
                        int id = replaceInfoMapper.insert(replaceEntity);//平台订单号
                        logger.info("平台订单号id:{}",id);
                        if(id > 0){
                            return id;
                        }else{
                            return 0;
                        }
                    }
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
    public int update(ReplaceInfo replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplaceInfoEntity replaceEntity = new ReplaceInfoEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replaceInfoMapper.update(replaceEntity);//平台订单号
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
                replaceInfoMapper.delete(id);
            }
        }catch(Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


}
