package com.cypro.ascpay.provider.replace.service.impl;

import com.cypro.ascpay.api.replace.payment.ReplacePayment;
import com.cypro.ascpay.api.replace.payment.ReplacePaymentService;
import com.cypro.ascpay.provider.replace.entity.ReplacePaymentEntity;
import com.cypro.ascpay.provider.replace.mapper.ReplacePaymentMapper;
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

@Service("replacePaymentService")
public class ReplacePaymentServiceImpl implements Serializable , ReplacePaymentService {

    private Logger logger = LoggerFactory.getLogger(ReplacePaymentServiceImpl.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "replacePaymentMapper")
    private ReplacePaymentMapper replacePaymentMapper;

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplacePayment> query(Map map) throws Exception {
        logger.info("进入query方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<ReplacePaymentEntity> replaceEntityList = replacePaymentMapper.query(map);
                logger.info("replaceEntityList的值为:{}",replaceEntityList);
                List<ReplacePayment> replaceList = new LinkedList<>();
                if(replaceEntityList != null){
                    for(ReplacePaymentEntity entity:replaceEntityList){
                        if(entity != null){
                            ReplacePayment replace = new ReplacePayment();
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
    public Long insert(ReplacePayment replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                if(!replace.getPmOrderno().isEmpty()){//判断订单号是否存在
                    Map<String ,String> map = new HashMap<>();
                    map.put("pmOrderno",replace.getPmOrderno());
                    List<ReplacePayment> list = query(map);

                    if(list.isEmpty()){
                        ReplacePaymentEntity replaceEntity = new ReplacePaymentEntity();
                        BeanUtils.copyProperties(replace,replaceEntity);
                        Long id = replacePaymentMapper.insert(replaceEntity);//平台订单号
                        list = query(map);
                        id = list.get(0).getId();
                        logger.info("平台订单号id:{}",id);
                        if(id > 0){
                            return id;
                        }else{
                            return 0L;
                        }
                    }
                }
            }
            return 0L;
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
    public int update(ReplacePayment replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplacePaymentEntity replaceEntity = new ReplacePaymentEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replacePaymentMapper.update(replaceEntity);//平台订单号
                logger.info("update成功数量:{}",id);
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
                replacePaymentMapper.delete(id);
            }
        }catch(Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
