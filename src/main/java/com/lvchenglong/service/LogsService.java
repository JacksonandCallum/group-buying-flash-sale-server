package com.lvchenglong.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.entity.Logs;
import com.lvchenglong.mapper.LogsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsService {
    @Resource
    private LogsMapper logsMapper;

    public void add(Logs logs) {
        logsMapper.insert(logs);
    }

    public void delete(Integer id) {
        logsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Logs logs) {
        logsMapper.updateById(logs);
    }

    public Logs selectById(Integer id) {
        Logs logs = logsMapper.selectById(id);
        return logs;
    }

    public List<Logs> selectAll(Logs logs) {
        List<Logs> list = logsMapper.selectAll(logs);
        return list;
    }


    public PageInfo<Logs> selectPage(Logs logs, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Logs> list = logsMapper.selectAll(logs);
        return PageInfo.of(list);
    }

}
