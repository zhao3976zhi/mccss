package com.cypro.ascpay.api.replace.pay;


import java.util.List;
import java.util.Map;

public interface ReplacePayService {
    /**
     * 查询
     * @param map 参数map
     * @return 返回List集合
     * @throws Exception
     */
    List<ReplacePay> query(Map map) throws Exception;

    /**
     * 添加
     * @param replace 参数实体
     * @return 返回主键id
     * @throws Exception
     */
    int insert(ReplacePay replace) throws Exception;

    /**
     * 修改
     * @param replace 参数实体
     * @return 修改条数
     * @throws Exception
     */
    int update(ReplacePay replace) throws  Exception;

    /**
     * 删除(物理)
     * @param id 主键id
     * @throws Exception
     */
    void delete(Long id) throws Exception;
}
