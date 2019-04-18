package com.cypro.ascpay.api.replace.relea;

import com.alibaba.fastjson.JSONObject;
import com.cypro.ascpay.api.utils.exception.OutOfServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReplaceReleaServiceMock implements ReplaceReleaService, Serializable {
    private Logger logger = LoggerFactory.getLogger(ReplaceReleaServiceMock.class);

    @Resource(name = "replaceReleaService")
    private ReplaceReleaService replaceReleaService;
    /**
     * 查询
     * @param map 参数map
     * @return
     * @throws Exception
     */
    @Override
    public List<ReplaceRelea> query(Map map) throws Exception {
        String errorMsg = "query ";
        String serviceName = "ReplacePassServiceMock.query";
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
    public int insert(ReplaceRelea replace) throws Exception {
        String errorMsg = "insert ";
        String serviceName = "ReplacePassServiceMock.insert";
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
    public int update(ReplaceRelea replace) throws Exception {
        String errorMsg = "update ";
        String serviceName = "ReplacePassServiceMock.update";
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
        String serviceName = "ReplacePassServiceMock.delete";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }

    /**
     * 根据外放代号查询上游商户号和密钥
     * @param releaNo
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject queryReleaPass(String releaNo,String releaType) throws Exception {
        String errorMsg = "queryReleaPass ";
        String serviceName = "ReplacePassServiceMock.queryReleaPass";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);
    }
}
