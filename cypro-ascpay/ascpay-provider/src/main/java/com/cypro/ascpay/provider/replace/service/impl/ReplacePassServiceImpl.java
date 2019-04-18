package com.cypro.ascpay.provider.replace.service.impl;

import com.cypro.ascpay.api.replace.pass.ReplacePass;
import com.cypro.ascpay.api.replace.pass.ReplacePassService;
import com.cypro.ascpay.provider.replace.entity.ReplacePassEntity;
import com.cypro.ascpay.provider.replace.mapper.ReplacePassMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("replacePassService")
public class ReplacePassServiceImpl implements Serializable , ReplacePassService {

    private Logger logger = LoggerFactory.getLogger(ReplacePassServiceImpl.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "replacePassMapper")
    private ReplacePassMapper replacePassMapper;

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplacePass> query(Map map) throws Exception {
        logger.info("进入query方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<ReplacePassEntity> replaceEntityList = replacePassMapper.query(map);
                logger.info("replaceEntityList的值为:{}",replaceEntityList);
                List<ReplacePass> replaceList = new LinkedList<>();
                if(replaceEntityList != null){
                    for(ReplacePassEntity entity:replaceEntityList){
                        if(entity != null){
                            ReplacePass replace = new ReplacePass();
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
    public int insert(ReplacePass replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplacePassEntity replaceEntity = new ReplacePassEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replacePassMapper.insert(replaceEntity);//平台订单号
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
    public int update(ReplacePass replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplacePassEntity replaceEntity = new ReplacePassEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replacePassMapper.update(replaceEntity);//平台订单号
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
                replacePassMapper.delete(id);
            }
        }catch(Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
