package com.cypro.ascpay.api.nocard;

import java.io.Serializable;

public class NocardPay implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单金额(元)
     */
    private String amount;
    /**
     * 费率
     */
    private String rate;
    /**
     * 固定手续费(元)
     */
    private String fee;
    /**
     * 总手续费(元)
     */
    private String sumFee;
    /**
     * 到账金额(元)
     */
    private String selAmt;
    /**
     * 信用卡卡号
     */
    private String cardCode;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 有效期
     */
    private String valid;
    /**
     * 安全码
     */
    private String cvv2;
    /**
     * 信用卡预留手机号
     */
    private String cardPhone;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 身份证号
     */
    private String idcard;
    /**
     * 借记卡卡号
     */
    private String settleCard;
    /**
     * 借记卡银行预留电话
     */
    private String settlePhone;
    /**
     * 通知地址
     */
    private String notifyurl;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 订单状态(1：成功 2：失败 0：处理中)
     */
    private String orderStates;
    /**
     * 订单描述
     */
    private String orderDesc;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSumFee() {
        return sumFee;
    }

    public void setSumFee(String sumFee) {
        this.sumFee = sumFee;
    }

    public String getSelAmt() {
        return selAmt;
    }

    public void setSelAmt(String selAmt) {
        this.selAmt = selAmt;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getCardPhone() {
        return cardPhone;
    }

    public void setCardPhone(String cardPhone) {
        this.cardPhone = cardPhone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getSettleCard() {
        return settleCard;
    }

    public void setSettleCard(String settleCard) {
        this.settleCard = settleCard;
    }

    public String getSettlePhone() {
        return settlePhone;
    }

    public void setSettlePhone(String settlePhone) {
        this.settlePhone = settlePhone;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderStates() {
        return orderStates;
    }

    public void setOrderStates(String orderStates) {
        this.orderStates = orderStates;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
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
