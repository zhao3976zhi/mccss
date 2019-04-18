package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplacePayEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplacePayMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplacePayEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ReplacePayEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplacePayEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}