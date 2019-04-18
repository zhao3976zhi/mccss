package com.cypro.ascpay.api.nocard;

import com.cypro.ascpay.api.basis.BaseServiceMock;
import com.cypro.ascpay.api.utils.exception.OutOfServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class NocardPayServiceMock extends BaseServiceMock<NocardPay> implements NocardPayService, Serializable {
    private Logger logger = LoggerFactory.getLogger(NocardPayServiceMock.class);
    @Override
    public List<NocardPay> queryList(Map map) throws Exception {
        String errorMsg = "queryList ";
        String serviceName = "NocardPayServiceMock.queryList";
        logger.error("Rpc: serviceName = {};msg={} ", serviceName, errorMsg);
        throw new OutOfServiceException(errorMsg, serviceName);

    }
}
