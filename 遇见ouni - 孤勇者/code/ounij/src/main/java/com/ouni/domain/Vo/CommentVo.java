package com.ouni.domain.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ouni.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentVo {

    private Integer id; //评论id

    private Integer parentId; //父评论id



    private String toUserName;

    private Date createTime; //创建时间

    private User user; //用户id
    private User toUser; //to评论id

    private String content; //回复内容

    public CommentVo(Integer id, Integer parentId,  Date createTime, String content) {
        this.id = id;
        this.parentId = parentId;
        this.createTime = createTime;
        this.content = content;
    }

//    private List<CommentVo> sons;

    private Integer sonSize;



}
