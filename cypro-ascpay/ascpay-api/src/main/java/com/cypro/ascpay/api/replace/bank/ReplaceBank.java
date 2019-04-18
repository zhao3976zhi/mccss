package com.cypro.ascpay.api.replace.bank;

import java.io.Serializable;

public class ReplaceBank implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 银行名称
     */
    private String name;
    /**
     * 费率
     */
    private String rate;
    /**
     * 单笔手续费
     */
    private String fee;
    /**
     * 外放id
     */
    private Long releaId;
    /**
     * 创建时间
     */
    private String creaTime;

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

    public Long getReleaId() {
        return releaId;
    }

    public void setReleaId(Long releaId) {
        this.releaId = releaId;
    }

    public String getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(String creaTime) {
        this.creaTime = creaTime;
    }
}
