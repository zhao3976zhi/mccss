package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplacePassEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplacePassMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplacePassEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ReplacePassEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplacePassEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}