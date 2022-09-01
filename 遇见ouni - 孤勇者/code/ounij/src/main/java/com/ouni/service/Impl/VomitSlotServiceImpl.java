package com.ouni.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.domain.Comment;
import com.ouni.domain.User;
import com.ouni.domain.Vo.CommentVo;
import com.ouni.mapper.CommentMapper;
import com.ouni.mapper.UserMapper;
import com.ouni.service.VomitSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VomitSlotServiceImpl implements VomitSlotService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<CommentVo> getComments(int vomitslotId, Page<Comment> commentPage) {

        List<CommentVo> ans = new ArrayList<>();

        List<Comment> records = commentPage.getRecords();

        for (Comment comment : records){
            CommentVo commentVo = new CommentVo(comment.getId(),-1,comment.getCreateTime(),comment.getContent());
            User user = userMapper.selectById(comment.getUserId());
            user.setOpenId("");
            commentVo.setUser(user);

            Long size = commentMapper.selectCount(new QueryWrapper<Comment>().eq("parent_id", comment.getId()));
            commentVo.setSonSize(size.intValue());
            ans.add(commentVo);
        }

        return ans;
    }

    @Override
    public List<CommentVo> getCommentsDetail(Page<Comment> commentPage) {
        List<CommentVo> list = new ArrayList<>();
        for (Comment comment : commentPage.getRecords()){
            CommentVo commentVo = new CommentVo(comment.getId(),comment.getParentId(),comment.getCreateTime(),comment.getContent());
            User user = userMapper.selectById(comment.getUserId());
            user.setOpenId("");
            commentVo.setUser(user);
            User toUser = userMapper.selectById(comment.getToUserId());
            toUser.setOpenId("");
            commentVo.setToUser(toUser);
            list.add(commentVo);
        }
        return list;
    }


}
