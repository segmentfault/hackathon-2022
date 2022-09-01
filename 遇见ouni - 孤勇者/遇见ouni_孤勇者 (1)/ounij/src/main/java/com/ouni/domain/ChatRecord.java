package com.ouni.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * chat_record表
 * @author jml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("chat_record")
public class ChatRecord {
    @TableId(type = IdType.AUTO)
    @TableField("records_id")
    private Integer recordsId;
    @TableField("from_user_id")
    private int fromUserId;
    @TableField("to_user_id")
    private int toUserId;
    @TableField("record_time")
    private Date recordTime;
    @TableField("message")
    private String message;
    @TableField("img_url")
    private String imgUrl;
    @TableField("status")
    private int status = 0;

    public ChatRecord(int fromUserId, int toUserId, Date recordTime, String message, String imgUrl) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.recordTime = recordTime;
        this.message = message;
        this.imgUrl = imgUrl;
    }
}
