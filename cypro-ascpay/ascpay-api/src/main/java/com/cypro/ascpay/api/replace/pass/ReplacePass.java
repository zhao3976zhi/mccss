package com.cypro.ascpay.api.replace.pass;


import java.io.Serializable;

public class ReplacePass implements Serializable {
    /**
     * 通道id
     */
    private Long id;
    /**
     * 通道名称
     */
    private String name;
    /**
     * 通道费率
     */
    private String rate;
    /**
     * 额外单笔手续费(元)
     */
    private String fee;
    /**
     * 创建时间
     */
    private String creaTime;
    /**
     * 商户号
     */
    private String merNo;
    /**
     * 商户密钥
     */
    private String merKey;
    /**
     * 商户状态
     */
    private Long states;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setCreaTime(String creaTime) {
        this.creaTime = creaTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreaTime() {
        return creaTime;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getMerKey() {
        return merKey;
    }

    public void setMerKey(String merKey) {
        this.merKey = merKey;
    }

    public Long getStates() {
        return states;
    }

    public void setStates(Long states) {
        this.states = states;
    }
}
