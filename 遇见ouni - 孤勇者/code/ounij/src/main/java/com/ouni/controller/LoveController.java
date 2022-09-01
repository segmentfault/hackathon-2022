package com.ouni.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ouni.common.CommonResult;
import com.ouni.common.ResultEnum;
import com.ouni.domain.GeoContent;
import com.ouni.domain.User;
import com.ouni.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/love")
public class LoveController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;
    private static final String GEO_KEY = "DISTANCE";

    /**
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
    @GetMapping("/getNear")
    public CommonResult<?> getNearInfo(
            @RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude,
            @RequestParam("userId") String userId){

        redisTemplate.opsForGeo().add(GEO_KEY,new Point(longitude, latitude),userId);
        Distance disFiveKmFive = new Distance(Integer.MAX_VALUE, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs argsFiveKmFive = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(Integer.MAX_VALUE);
        GeoResults<RedisGeoCommands.GeoLocation<String>> resultsFiveKmFive = redisTemplate.opsForGeo().radius(GEO_KEY,userId,disFiveKmFive,argsFiveKmFive);
        List<GeoContent> geoContents = new ArrayList<>();

        for (int i = 0 ; i < resultsFiveKmFive.getContent().size(); i++){
            GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult = resultsFiveKmFive.getContent().get(i);
            int id = Integer.parseInt(geoResult.getContent().getName());
            User user = userMapper.selectById(id);
            String name = null;
            String imgUrl = null;
            if (user != null){
                name = user.getUserName();
                imgUrl = user.getImgUrl();
            }
            double x = geoResult.getContent().getPoint().getX();
            double y = geoResult.getContent().getPoint().getY();
            double distance = geoResult.getDistance().getValue();
            geoContents.add(new GeoContent(id,name,imgUrl,x,y,distance));
        }
        redisTemplate.opsForValue().set(longitude + ":" + latitude, JSON.toJSONString(geoContents));
        return  new CommonResult<>(ResultEnum.SUCCESS,geoContents);
    }

    @GetMapping("/jsGetResult")
    public CommonResult<?> jsGetResult(
            @RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude){
        System.out.println(longitude+":"+latitude);
        Object resultsFiveKmFive =  redisTemplate.opsForValue().get(longitude + ":" + latitude);
        System.out.println(resultsFiveKmFive);
        if (resultsFiveKmFive != null){
            return  new CommonResult<>(ResultEnum.SUCCESS,JSONObject.parseArray((String) resultsFiveKmFive,GeoContent.class));
        }else {
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }
    }


    @GetMapping("/addGeo")
    public CommonResult<?> addGeo(
            @RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude,
            @RequestParam("userId") String userId){
        redisTemplate.opsForGeo().add(GEO_KEY,new Point(longitude, latitude),userId);
        return  new CommonResult<>(ResultEnum.SUCCESS,null);
    }


}
