package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplaceInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ReplaceInfoMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplaceInfoEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ReplaceInfoEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplaceInfoEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);


}