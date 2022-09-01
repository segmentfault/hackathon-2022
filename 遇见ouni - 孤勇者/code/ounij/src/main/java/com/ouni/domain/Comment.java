package com.ouni.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Integer id; //评论id

    @TableField("parent_id")
    private Integer parentId; //父评论id

    @TableField("vomitslot_id")
    private Integer vomitslotId; //帖子ID

    @TableField("to_user_id")
    private Integer toUserId; //

    @TableField("create_time")
    private Date createTime; //创建时间

    @TableField("user_id")
    private Integer userId; //用户id

    @TableField("content")
    private String content; //回复内容

}
