package com.cypro.ascpay.provider.replace.entity;

import java.util.Date;

public class ReplaceBankEntity {
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
    private Date creaTime;

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
        this.name = name == null ? null : name.trim();
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

    public Long getReleaId() {
        return releaId;
    }

    public void setReleaId(Long releaId) {
        this.releaId = releaId;
    }

    public Date getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(Date creaTime) {
        this.creaTime = creaTime;
    }
}