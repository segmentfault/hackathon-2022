package com.ouni.domain.Vo;

import com.ouni.domain.GoodsPub;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo {
    private GoodsPub goodsPub;
    private double pageView;
}
