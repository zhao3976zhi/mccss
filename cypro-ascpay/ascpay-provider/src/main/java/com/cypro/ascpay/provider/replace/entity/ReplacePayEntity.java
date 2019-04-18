package com.cypro.ascpay.provider.replace.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author ascme
 * @ClassName 代还支付流水实现类
 * @Date 2019-03-07
 */
public class ReplacePayEntity implements Serializable {
    /**
     * 编号
     */
    private Long id;
    /**
     * 支付订单号
     */
    private String payOrderNo;
    /**
     * 支付金额(元)
     */
    private String payTranAmt;
    /**
     * 手续费(元)
     */
    private String payRoutAmt;
    /**
     * 到账金额(元)
     */
    private String payFactAmt;
    /**
     * 支付订单状态(1：成功 2：失败 0：处理中)
     */
    private String payOrderStates;
    /**
     * 支付订单描述
     */
    private String payOrderDesc;
    /**
     * 支付对象姓名
     */
    private String payName;
    /**
     * 支付对象身份证号
     */
    private String payIdcard;
    /**
     * 支付对象银行卡号
     */
    private String payCardcode;
    /**
     * 支付对象预留手机号
     */
    private String payPhone;
    /**
     * 支付异步通知地址
     */
    private String payNotifyurl;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 交易时间
     */
    private Date creaTime;
    /**
     * 外放代号
     */
    private String releaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayTranAmt() {
        return payTranAmt;
    }

    public void setPayTranAmt(String payTranAmt) {
        this.payTranAmt = payTranAmt;
    }

    public String getPayRoutAmt() {
        return payRoutAmt;
    }

    public void setPayRoutAmt(String payRoutAmt) {
        this.payRoutAmt = payRoutAmt;
    }

    public String getPayFactAmt() {
        return payFactAmt;
    }

    public void setPayFactAmt(String payFactAmt) {
        this.payFactAmt = payFactAmt;
    }

    public String getPayOrderStates() {
        return payOrderStates;
    }

    public void setPayOrderStates(String payOrderStates) {
        this.payOrderStates = payOrderStates == null ? null : payOrderStates.trim();
    }

    public String getPayOrderDesc() {
        return payOrderDesc;
    }

    public void setPayOrderDesc(String payOrderDesc) {
        this.payOrderDesc = payOrderDesc == null ? null : payOrderDesc.trim();
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName == null ? null : payName.trim();
    }

    public String getPayIdcard() {
        return payIdcard;
    }

    public void setPayIdcard(String payIdcard) {
        this.payIdcard = payIdcard == null ? null : payIdcard.trim();
    }

    public String getPayCardcode() {
        return payCardcode;
    }

    public void setPayCardcode(String payCardcode) {
        this.payCardcode = payCardcode == null ? null : payCardcode.trim();
    }

    public String getPayPhone() {
        return payPhone;
    }

    public void setPayPhone(String payPhone) {
        this.payPhone = payPhone == null ? null : payPhone.trim();
    }

    public String getPayNotifyurl() {
        return payNotifyurl;
    }

    public void setPayNotifyurl(String payNotifyurl) {
        this.payNotifyurl = payNotifyurl == null ? null : payNotifyurl.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(Date creaTime) {
        this.creaTime = creaTime;
    }

    public String getReleaId() {
        return releaId;
    }

    public void setReleaId(String releaId) {
        this.releaId = releaId;
    }
}