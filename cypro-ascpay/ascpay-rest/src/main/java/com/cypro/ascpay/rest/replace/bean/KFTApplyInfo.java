package com.cypro.ascpay.rest.replace.bean;

/**
 * @Author ascme
 * @ClassName  上游(KFT)代还用户信息实体类
 * @Date 2019-02-19
 */
public class KFTApplyInfo {
    /**
     * 唯一订单号
     */
    private String orderNo;
    /**
     * 用户真实姓名
     */
    private String name;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 银行卡号
     */
    private String cardCode;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 有效期
     */
    private String validDate;
    /**
     * 安全码
     */
    private String cvv2;
    /**
     * 用户ID
     */
    private String userId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
