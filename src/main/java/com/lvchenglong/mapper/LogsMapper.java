package com.lvchenglong.mapper;

import com.lvchenglong.entity.Logs;

import java.util.List;

public interface LogsMapper {
    void insert(Logs logs);

    void deleteById(Integer id);

    void updateById(Logs logs);

    List<Logs> selectAll(Logs logs);

    Logs selectById(Integer id);
}
