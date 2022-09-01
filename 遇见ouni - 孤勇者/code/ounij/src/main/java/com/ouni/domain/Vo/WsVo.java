package com.ouni.domain.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 通信vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsVo {
    private int fromuserId;
    private int touserId;
    private String textMessage;
    private Date recordTime;
    private String imgUrl;
}
