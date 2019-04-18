package com.cypro.ascpay.api.replace.pay;

import com.cypro.ascpay.api.utils.exception.OutOfServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReplacePayServiceMock implements ReplacePayService, Serializable {
    private Logger logger = LoggerFactory.getLogger(ReplacePayServiceMock.class);

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplacePay> query(Map map) throws Exception {
        String errorMsg = "query ";
        String serviceName = "ReplacePayServiceMock.query";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 添加
     * @param replace 参数实体
     * @return
     * @throws Exception
     */
    @Override
    public int insert(ReplacePay replace) throws Exception {
        String errorMsg = "insert ";
        String serviceName = "ReplacePayServiceMock.insert";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 修改
     * @param replace 参数实体
     * @return
     * @throws Exception
     */
    @Override
    public int update(ReplacePay replace) throws Exception {
        String errorMsg = "update ";
        String serviceName = "ReplacePayServiceMock.update";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 删除(物理)
     * @param id 主键id
     * @throws Exception
     */
    @Override
    public void delete(Long id) throws Exception {
        String errorMsg = "delete ";
        String serviceName = "ReplacePayServiceMock.delete";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }
}
