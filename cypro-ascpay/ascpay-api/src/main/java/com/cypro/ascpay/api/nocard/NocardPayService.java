package com.cypro.ascpay.api.nocard;


import java.util.List;
import java.util.Map;

public interface NocardPayService {
    /**
     * 查询
     * @param map 参数map
     * @return 返回List集合
     * @throws Exception
     */
    List<NocardPay> queryList(Map map) throws Exception;
    /**
     * 添加
     * @param replace 参数实体
     * @return 返回主键id
     * @throws Exception
     */
    int insert(NocardPay replace) throws Exception;

    /**
     * 修改
     * @param replace
     * @return
     * @throws Exception
     */
    int update(NocardPay replace) throws Exception;
}
