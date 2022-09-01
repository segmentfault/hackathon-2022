package com.ouni.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.domain.GoodsPub;
import org.springframework.stereotype.Service;

@Service
public interface GoodService {
    public Page<GoodsPub> getListByName(String name,int pageNum,int pageSize);
}
