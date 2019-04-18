package com.cypro.ascpay.provider.replace.service.impl;

import com.cypro.ascpay.api.replace.pay.ReplacePay;
import com.cypro.ascpay.api.replace.pay.ReplacePayService;
import com.cypro.ascpay.api.replace.payment.ReplacePayment;
import com.cypro.ascpay.api.replace.payment.ReplacePaymentService;
import com.cypro.ascpay.provider.replace.entity.ReplacePayEntity;
import com.cypro.ascpay.provider.replace.entity.ReplacePaymentEntity;
import com.cypro.ascpay.provider.replace.mapper.ReplacePayMapper;
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

@Service("replacePayService")
public class ReplacePayServiceImpl implements Serializable , ReplacePayService {

    private Logger logger = LoggerFactory.getLogger(ReplacePayServiceImpl.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "replacePayMapper")
    private ReplacePayMapper replacePayMapper;

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplacePay> query(Map map) throws Exception {
        logger.info("进入query方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<ReplacePayEntity> replaceEntityList = replacePayMapper.query(map);
                logger.info("replaceEntityList的值为:{}",replaceEntityList);
                List<ReplacePay> replaceList = new LinkedList<>();
                if(replaceEntityList != null){
                    for(ReplacePayEntity entity:replaceEntityList){
                        if(entity != null){
                            ReplacePay replace = new ReplacePay();
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
    public int insert(ReplacePay replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                if(!replace.getPayOrderNo().isEmpty()){//判断订单号是否存在
                    Map<String ,String> map = new HashMap<>();
                    map.put("payOrderNo",replace.getPayOrderNo());
                    List<ReplacePay> list = query(map);

                    if(list.isEmpty()){
                        ReplacePayEntity replaceEntity = new ReplacePayEntity();
                        BeanUtils.copyProperties(replace,replaceEntity);
                        int id = replacePayMapper.insert(replaceEntity);//平台订单号
                        list = query(map);
                        id = Integer.valueOf(list.get(0).getId().toString());
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
    public int update(ReplacePay replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                ReplacePayEntity replaceEntity = new ReplacePayEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = replacePayMapper.update(replaceEntity);//平台订单号
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
                replacePayMapper.delete(id);
            }
        }catch(Exception e){
            logger.error("出错了,Error:{}",e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
