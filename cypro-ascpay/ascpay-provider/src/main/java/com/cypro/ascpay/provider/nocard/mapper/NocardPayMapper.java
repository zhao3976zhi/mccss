package com.cypro.ascpay.provider.nocard.mapper;

import com.cypro.ascpay.provider.basis.mapper.BaseMapper;
import com.cypro.ascpay.provider.nocard.entity.NocardPayEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NocardPayMapper extends BaseMapper<NocardPayEntity> {
    /**
     * 查询
     * @param map
     * @return
     */
    List<NocardPayEntity> query(Map map);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(NocardPayEntity record);

    /**
     * 修改
     * @param record
     * @return
     */
    int update(NocardPayEntity record);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

}