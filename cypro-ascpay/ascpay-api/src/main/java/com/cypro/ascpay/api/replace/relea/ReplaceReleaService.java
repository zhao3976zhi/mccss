package com.cypro.ascpay.api.replace.relea;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ReplaceReleaService {
    /**
     * 查询
     * @param map 参数map
     * @return 返回List集合
     * @throws Exception
     */
    List<ReplaceRelea> query(Map map) throws Exception;

    /**
     * 添加
     * @param replace 参数实体
     * @return 返回主键id
     * @throws Exception
     */
    int insert(ReplaceRelea replace) throws Exception;

    /**
     * 修改
     * @param replace 参数实体
     * @return 修改条数
     * @throws Exception
     */
    int update(ReplaceRelea replace) throws  Exception;

    /**
     * 删除(物理)
     * @param id 主键id
     * @throws Exception
     */
    void delete(Long id) throws Exception;

    /**
     * 根据外放代号查询上游商户号和密钥
     * @param releaNo
     * @return
     * @throws Exception
     */
    JSONObject queryReleaPass(String releaNo,String releaType) throws  Exception;
}
