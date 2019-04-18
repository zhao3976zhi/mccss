package com.cypro.ascpay.provider.nocard.service.impl;

import com.cypro.ascpay.api.nocard.NocardPay;
import com.cypro.ascpay.api.nocard.NocardPayService;
import com.cypro.ascpay.provider.basis.service.impl.BaseServiceImpl;
import com.cypro.ascpay.provider.nocard.entity.NocardPayEntity;
import com.cypro.ascpay.provider.nocard.mapper.NocardPayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("nocardPayService")
public class NocardPayServiceImpl extends BaseServiceImpl<NocardPayEntity> implements NocardPayService  {
    private static Logger logger = LoggerFactory.getLogger(NocardPayServiceImpl.class);

    @Resource(name = "nocardPayMapper")
    private NocardPayMapper nocardPayMapper;

    @Resource(name = "nocardPayMapper")
    public void setSqlMapper (NocardPayMapper nocardPayMapper)
    {
        super.setBaseMapper (nocardPayMapper);
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<NocardPay> queryList(Map map) throws Exception {
        logger.info("进入queryList方法,接收参数为:{}",map);
        try{
            if(!map.isEmpty()){
                List<NocardPayEntity> replaceEntityList = nocardPayMapper.query(map);
                logger.info("replaceEntityList的值为:{}",replaceEntityList);
                List<NocardPay> replaceList = new LinkedList<>();
                if(replaceEntityList != null){
                    for(NocardPayEntity entity:replaceEntityList){
                        if(entity != null){
                            NocardPay replace = new NocardPay();
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
    public int insert(NocardPay replace) throws Exception {
        logger.info("进入insert方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                NocardPayEntity replaceEntity = new NocardPayEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = nocardPayMapper.insert(replaceEntity);//平台订单号
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
    public int update(NocardPay replace) throws Exception {
        logger.info("进入update方法,接收参数为:{}",replace);
        try{
            if(replace != null){
                NocardPayEntity replaceEntity = new NocardPayEntity();
                BeanUtils.copyProperties(replace,replaceEntity);
                int id = nocardPayMapper.update(replaceEntity);//平台订单号
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
}
