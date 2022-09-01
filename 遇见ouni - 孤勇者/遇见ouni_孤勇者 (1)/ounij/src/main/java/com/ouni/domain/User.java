package com.ouni.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * user表
 * @author jml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    private Integer userId;
    @TableField("open_id")
    private String openId;
    @TableField("user_name")
    private String userName;
    @TableField("img_url")
    private String imgUrl;
}
