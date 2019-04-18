package com.cypro.ascpay.api.replace.pass;

import java.util.List;
import java.util.Map;

public interface ReplacePassService {
    /**
     * 查询
     * @param map 参数map
     * @return 返回List集合
     * @throws Exception
     */
    List<ReplacePass> query(Map map) throws Exception;

    /**
     * 添加
     * @param replace 参数实体
     * @return 返回主键id
     * @throws Exception
     */
    int insert(ReplacePass replace) throws Exception;

    /**
     * 修改
     * @param replace 参数实体
     * @return 修改条数
     * @throws Exception
     */
    int update(ReplacePass replace) throws  Exception;

    /**
     * 删除(物理)
     * @param id 主键id
     * @throws Exception
     */
    void delete(Long id) throws Exception;
}
