package com.ouni.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.domain.Comment;
import com.ouni.domain.Vo.CommentVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VomitSlotService {
    List<CommentVo> getComments(int vomitslotId, Page<Comment> commentPage);
    List<CommentVo> getCommentsDetail(Page<Comment> commentPage);
}
