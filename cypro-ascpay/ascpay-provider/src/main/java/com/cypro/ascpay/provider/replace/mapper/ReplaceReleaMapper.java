package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplaceReleaEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplaceReleaMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplaceReleaEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ReplaceReleaEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplaceReleaEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

}