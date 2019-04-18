package com.cypro.ascpay.api.basis;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    List<T> query(Map map) throws Exception;

    int insert(T t) throws Exception;

    int update(T t) throws  Exception;

    void delete(Long id) throws Exception;

}
