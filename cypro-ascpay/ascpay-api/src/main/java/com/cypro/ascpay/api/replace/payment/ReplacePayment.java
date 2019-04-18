package com.cypro.ascpay.api.replace.payment;

import java.io.Serializable;

public class ReplacePayment implements Serializable {
    /**
     * 编号
     */
    private Long id;
    /**
     * 订单号
     */
    private String pmOrderno;
    /**
     * 扣款金额
     */
    private String pmAmount;
    /**
     * 扣款费率
     */
    private String pmRate;
    /**
     * 扣款到账金额(元)
     */
    private String pmFee;
    /**
     * 用户id
     */
    private String pmUserid;
    /**
     * 扣款异步通知地址
     */
    private String pmNotifyurl;
    /**
     * 扣款订单状态(1:成功; 2:失败; 0:处理中)
     */
    private String pmStates;
    /**
     * 扣款订单描述
     */
    private String pmDesc;
    /**
     * 创建时间
     */
    private String creaTime;
    /**
     * 外放id
     */
    private String releaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPmOrderno() {
        return pmOrderno;
    }

    public void setPmOrderno(String pmOrderno) {
        this.pmOrderno = pmOrderno;
    }

    public String getPmAmount() {
        return pmAmount;
    }

    public void setPmAmount(String pmAmount) {
        this.pmAmount = pmAmount;
    }

    public String getPmRate() {
        return pmRate;
    }

    public void setPmRate(String pmRate) {
        this.pmRate = pmRate;
    }

    public String getPmFee() {
        return pmFee;
    }

    public void setPmFee(String pmFee) {
        this.pmFee = pmFee;
    }

    public String getPmUserid() {
        return pmUserid;
    }

    public void setPmUserid(String pmUserid) {
        this.pmUserid = pmUserid;
    }

    public String getPmNotifyurl() {
        return pmNotifyurl;
    }

    public void setPmNotifyurl(String pmNotifyurl) {
        this.pmNotifyurl = pmNotifyurl;
    }

    public String getPmStates() {
        return pmStates;
    }

    public void setPmStates(String pmStates) {
        this.pmStates = pmStates;
    }

    public String getPmDesc() {
        return pmDesc;
    }

    public void setPmDesc(String pmDesc) {
        this.pmDesc = pmDesc;
    }

    public String getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(String creaTime) {
        this.creaTime = creaTime;
    }

    public String getReleaId() {
        return releaId;
    }

    public void setReleaId(String releaId) {
        this.releaId = releaId;
    }
}
