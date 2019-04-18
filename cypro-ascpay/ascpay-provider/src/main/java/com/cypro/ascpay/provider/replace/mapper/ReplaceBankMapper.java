package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplaceBankEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplaceBankMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplaceBankEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(ReplaceBankEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplaceBankEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}