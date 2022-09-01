package com.ouni.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoContent {
    private int userId;
    private String userName;
    private String imgUrl;
    private double x; //经度
    private double y; //纬度
    private double distance; //距离
}
