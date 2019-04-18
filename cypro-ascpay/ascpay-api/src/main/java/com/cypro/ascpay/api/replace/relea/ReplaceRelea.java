package com.cypro.ascpay.api.replace.relea;

import java.io.Serializable;

/**
 * @Author ascme
 * @ClassName  代还外放表
 * @Date 2019-03-05
 */
public class ReplaceRelea implements Serializable {
    /**
     * 编号
     */
    private Long id;
    /**
     * 外放名称
     */
    private String releaName;
    /**
     * 外放代号
     */
    private String releaNo;
    /**
     * 外放费率(%)
     */
    private String releaRate;
    /**
     * 外放单笔手续费(元)
     */
    private String releaFee;
    /**
     * 外放密钥
     */
    private String releaKey;
    /**
     * 通道id
     */
    private Long passId;
    /**
     * 状态(1：可用,0：禁用)
     */
    private Long releaStates;
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

    public String getReleaName() {
        return releaName;
    }

    public void setReleaName(String releaName) {
        this.releaName = releaName;
    }

    public String getReleaNo() {
        return releaNo;
    }

    public void setReleaNo(String releaNo) {
        this.releaNo = releaNo;
    }

    public String getReleaRate() {
        return releaRate;
    }

    public void setReleaRate(String releaRate) {
        this.releaRate = releaRate;
    }

    public String getReleaFee() {
        return releaFee;
    }

    public void setReleaFee(String releaFee) {
        this.releaFee = releaFee;
    }

    public String getReleaKey() {
        return releaKey;
    }

    public void setReleaKey(String releaKey) {
        this.releaKey = releaKey;
    }

    public Long getPassId() {
        return passId;
    }

    public void setPassId(Long passId) {
        this.passId = passId;
    }

    public Long getReleaStates() {
        return releaStates;
    }

    public void setReleaStates(Long releaStates) {
        this.releaStates = releaStates;
    }

    public String getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(String creaTime) {
        this.creaTime = creaTime;
    }
}
