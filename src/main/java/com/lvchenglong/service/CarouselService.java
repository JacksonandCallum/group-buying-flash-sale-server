package com.lvchenglong.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvchenglong.entity.Carousel;
import com.lvchenglong.mapper.CarouselMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselService {
    @Resource
    private CarouselMapper carouselMapper;


    public void add(Carousel carousel) {
        carouselMapper.insert(carousel);
    }


    public void delete(Integer id) {
        carouselMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public void updateById(Carousel carousel) {
        carouselMapper.updateById(carousel);
    }

    public Carousel selectById(Integer id) {
        Carousel carousel = carouselMapper.selectById(id);
        return carousel;
    }

    public List<Carousel> selectAll(Carousel carousel) {
        List<Carousel> list = carouselMapper.selectAll(carousel);
        return list;
    }


    public PageInfo<Carousel> selectPage(Carousel carousel, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Carousel> list = carouselMapper.selectAll(carousel);
        return PageInfo.of(list);
    }


}
