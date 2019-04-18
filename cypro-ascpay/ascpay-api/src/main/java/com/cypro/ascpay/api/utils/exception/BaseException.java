package com.cypro.ascpay.api.utils.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final int UNKNOWN_ERROR_CODE = 0;
    public static final int OUT_OF_SERVICE_CODE = -1;
    public static final String UNKNOWN_ERROR_MSG = "";
    private Throwable cause;
    private int errorCode;
    private String traceId;

    public BaseException(String errorMsg) {
        this((Throwable)null, errorMsg);
    }

    public BaseException(Throwable cause) {
        this(cause, "");
    }

    public BaseException(int errorCode, String errorMsg) {
        this((Throwable)null, errorCode, errorMsg);
    }

    public BaseException(Throwable cause, String errorMsg) {
        this(cause, 0, errorMsg);
    }

    public BaseException(Throwable cause, int errorCode, String errorMsg) {
        this(cause, errorCode, errorMsg, (String)null);
    }

    public BaseException(Throwable cause, int errorCode, String errorMsg, String traceId) {
        super(errorMsg);
        this.cause = cause;
        this.errorCode = errorCode;
        this.traceId = traceId;
    }

    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream ps) {
        if (null == this.getCause()) {
            super.printStackTrace(ps);
        } else {
            ps.println(this);
            this.getCause().printStackTrace(ps);
        }

    }

    public void printStackTrace(PrintWriter pw) {
        if (null == this.getCause()) {
            super.printStackTrace(pw);
        } else {
            pw.println(this);
            this.getCause().printStackTrace(pw);
        }

    }

    public Throwable getCause() {
        return this.cause == this ? null : this.cause;
    }

    public String getMessage() {
        return this.getCause() == null ? super.getMessage() : super.getMessage() + this.getCause().getMessage();
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getTraceId() {
        return this.traceId;
    }
}