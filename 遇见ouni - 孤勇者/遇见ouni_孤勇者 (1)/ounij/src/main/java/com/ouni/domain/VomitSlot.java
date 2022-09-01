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
 * vomit_slot表
 * @author jml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("vomit_slot")
public class VomitSlot {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @TableField("content")
    private String content;
    @TableField("upload_time")
    private Date uploadTime;

}
