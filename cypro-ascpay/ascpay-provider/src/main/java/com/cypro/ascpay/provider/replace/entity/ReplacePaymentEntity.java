package com.cypro.ascpay.provider.replace.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author ascme
 * @ClassName 代还扣款交易流水
 * @Date 2019-03-07
 */
public class ReplacePaymentEntity implements Serializable {
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
    private Date creaTime;
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
        this.pmOrderno = pmOrderno == null ? null : pmOrderno.trim();
    }

    public String getPmAmount() {
        return pmAmount;
    }

    public void setPmAmount(String pmAmount) {
        this.pmAmount = pmAmount == null ? null : pmAmount.trim();
    }

    public String getPmRate() {
        return pmRate;
    }

    public void setPmRate(String pmRate) {
        this.pmRate = pmRate == null ? null : pmRate.trim();
    }

    public String getPmFee() {
        return pmFee;
    }

    public void setPmFee(String pmFee) {
        this.pmFee = pmFee == null ? null : pmFee.trim();
    }

    public String getPmUserid() {
        return pmUserid;
    }

    public void setPmUserid(String pmUserid) {
        this.pmUserid = pmUserid == null ? null : pmUserid.trim();
    }

    public String getPmNotifyurl() {
        return pmNotifyurl;
    }

    public void setPmNotifyurl(String pmNotifyurl) {
        this.pmNotifyurl = pmNotifyurl == null ? null : pmNotifyurl.trim();
    }

    public String getPmStates() {
        return pmStates;
    }

    public void setPmStates(String pmStates) {
        this.pmStates = pmStates == null ? null : pmStates.trim();
    }

    public String getPmDesc() {
        return pmDesc;
    }

    public void setPmDesc(String pmDesc) {
        this.pmDesc = pmDesc;
    }

    public Date getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(Date creaTime) {
        this.creaTime = creaTime;
    }

    public void setReleaId(String releaId) {
        this.releaId = releaId;
    }

    public String getReleaId() {
        return releaId;
    }
}