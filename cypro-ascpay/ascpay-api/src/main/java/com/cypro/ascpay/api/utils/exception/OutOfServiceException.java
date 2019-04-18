package com.cypro.ascpay.api.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutOfServiceException extends BaseException {
    private static final Logger logger = LoggerFactory.getLogger(OutOfServiceException.class);

    private String serviceName;

    public OutOfServiceException(String errorMsg, String serviceName) {
        super(-1, errorMsg);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public OutOfServiceException(Throwable cause) {
        super(cause);
    }

    public OutOfServiceException(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public OutOfServiceException(Throwable cause, String errorMsg) {
        super(cause, errorMsg);
    }

    public OutOfServiceException(Throwable cause, int errorCode, String errorMsg) {
        super(cause, errorCode, errorMsg);
    }

    public OutOfServiceException(Throwable cause, int errorCode, String errorMsg, String traceId) {
        super(cause, errorCode, errorMsg, traceId);
    }
}