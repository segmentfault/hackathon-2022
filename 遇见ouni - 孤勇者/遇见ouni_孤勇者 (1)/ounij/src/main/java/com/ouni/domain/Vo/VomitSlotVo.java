package com.ouni.domain.Vo;

import com.ouni.domain.VomitSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VomitSlotVo {
    private VomitSlot vomitSlot;
    private Integer views; //浏览量
    private Integer likes;//点赞数
    private Integer commentsNum;//评论数
}
