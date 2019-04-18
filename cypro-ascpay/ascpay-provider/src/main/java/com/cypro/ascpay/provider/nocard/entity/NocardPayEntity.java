package com.cypro.ascpay.provider.nocard.entity;

import java.util.Date;

public class NocardPayEntity {
    private Long id;

    private String orderNo;

    private String amount;

    private String rate;

    private String fee;

    private String sumFee;

    private String selAmt;

    private String cardCode;

    private String realName;

    private String valid;

    private String cvv2;

    private String cardPhone;

    private String bankName;

    private String idcard;

    private String settleCard;

    private String settlePhone;

    private String notifyurl;

    private String userId;

    private String orderStates;

    private String orderDesc;

    private Date creaTime;

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
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee == null ? null : fee.trim();
    }

    public String getSumFee() {
        return sumFee;
    }

    public void setSumFee(String sumFee) {
        this.sumFee = sumFee == null ? null : sumFee.trim();
    }

    public String getSelAmt() {
        return selAmt;
    }

    public void setSelAmt(String selAmt) {
        this.selAmt = selAmt == null ? null : selAmt.trim();
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode == null ? null : cardCode.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2 == null ? null : cvv2.trim();
    }

    public String getCardPhone() {
        return cardPhone;
    }

    public void setCardPhone(String cardPhone) {
        this.cardPhone = cardPhone == null ? null : cardPhone.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getSettleCard() {
        return settleCard;
    }

    public void setSettleCard(String settleCard) {
        this.settleCard = settleCard == null ? null : settleCard.trim();
    }

    public String getSettlePhone() {
        return settlePhone;
    }

    public void setSettlePhone(String settlePhone) {
        this.settlePhone = settlePhone == null ? null : settlePhone.trim();
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl == null ? null : notifyurl.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getOrderStates() {
        return orderStates;
    }

    public void setOrderStates(String orderStates) {
        this.orderStates = orderStates == null ? null : orderStates.trim();
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc == null ? null : orderDesc.trim();
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