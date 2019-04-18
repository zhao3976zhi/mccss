package com.cypro.ascpay.api.basis;

import com.cypro.ascpay.api.utils.exception.OutOfServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BaseServiceMock<T> implements BaseService<T>, Serializable {
    private Logger logger = LoggerFactory.getLogger(BaseServiceMock.class);

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    public List<T> query(Map map) throws Exception {
        String errorMsg = "query ";
        String serviceName = "BaseServiceMock.query";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }



    /**
     * 添加
     * @param t 参数实体
     * @return
     * @throws Exception
     */
    public int insert(T t) throws Exception {
        String errorMsg = "insert ";
        String serviceName = "BaseServiceMock.insert";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 修改
     * @param t 参数实体
     * @return
     * @throws Exception
     */
    public int update(T t) throws Exception {
        String errorMsg = "update ";
        String serviceName = "BaseServiceMock.update";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 删除(物理)
     * @param id 主键id
     * @throws Exception
     */
    public void delete(Long id) throws Exception {
        String errorMsg = "delete ";
        String serviceName = "BaseServiceMock.delete";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }
}
