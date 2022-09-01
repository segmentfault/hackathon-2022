package com.ouni.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ouni.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
