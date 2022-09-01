package com.ouni.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * goods_pub表
 * @author jml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("goods_pub")
public class GoodsPub {
    @TableId(type = IdType.AUTO)
    @TableField("goods_id")
    private Integer goodsId;
    @TableField("goods_detail")
    private String goodsDetail;
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    @TableField("goods_add")
    private String goodsAdd;
    @TableField("user_id")
    private int userId;
    @TableField("goods_url")
    private String goodsUrl;
    @TableField("upload_time")
    private Date uploadTime;
    @TableField("simple_word")
    private String simpleWord;
}
