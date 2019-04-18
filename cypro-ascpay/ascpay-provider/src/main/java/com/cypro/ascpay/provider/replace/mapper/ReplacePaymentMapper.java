package com.cypro.ascpay.provider.replace.mapper;

import com.cypro.ascpay.provider.replace.entity.ReplacePaymentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReplacePaymentMapper {
    /**
     * 查询
     * @param map
     * @return
     */
    List<ReplacePaymentEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    Long insert(ReplacePaymentEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(ReplacePaymentEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}