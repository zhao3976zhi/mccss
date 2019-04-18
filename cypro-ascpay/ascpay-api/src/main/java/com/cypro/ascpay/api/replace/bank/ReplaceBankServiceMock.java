package com.cypro.ascpay.api.replace.bank;

import com.cypro.ascpay.api.utils.exception.OutOfServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReplaceBankServiceMock implements ReplaceBankService, Serializable {
    private Logger logger = LoggerFactory.getLogger(ReplaceBankServiceMock.class);

    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplaceBank> query(Map map) throws Exception {
        String errorMsg = "query ";
        String serviceName = "ReplaceBankServiceMock.query";
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
    public int insert(ReplaceBank replace) throws Exception {
        String errorMsg = "insert ";
        String serviceName = "ReplaceBankServiceMock.insert";
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
    public int update(ReplaceBank replace) throws Exception {
        String errorMsg = "update ";
        String serviceName = "ReplaceBankServiceMock.update";
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
        String serviceName = "ReplaceBankServiceMock.delete";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }
}
