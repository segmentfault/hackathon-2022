package com.ouni.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.domain.GoodsPub;
import com.ouni.mapper.GoodMapper;
import com.ouni.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;

    @Override
    public Page<GoodsPub> getListByName(String name,int pageNum,int pageSize) {

        Page<GoodsPub> MygoodsInfo = goodMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<GoodsPub>lambdaQuery()
                .orderByDesc(GoodsPub::getUploadTime)
                .like(GoodsPub::getGoodsDetail,"%" + name + "%"));
        return MygoodsInfo;
    }
}
