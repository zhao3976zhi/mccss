package com.cypro.ascpay.api.replace.info;


import java.io.Serializable;

public class ReplaceInfo implements Serializable {
    /**
     * 用户信息id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 身份证
     */
    private String identity;
    /**
     * 卡片有效期
     */
    private String validity;
    /**
     * 卡片安全码
     */
    private String cvn;
    /**
     * 用户id(上游)
     */
    private String userId;
    /**
     * 创建时间
     */
    private String creaTime;
    /**
     * 通道id
     */
    private Long passId;

    private String rate;

    private String fee;

    private String merid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCvn() {
        return cvn;
    }

    public void setCvn(String cvn) {
        this.cvn = cvn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(String creaTime) {
        this.creaTime = creaTime;
    }

    public Long getPassId() {
        return passId;
    }

    public void setPassId(Long passId) {
        this.passId = passId;
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

    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }
}
