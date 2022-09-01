package com.ouni.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ouni.common.ResultEnum;
import com.ouni.common.CommonResult;
import com.ouni.domain.Vo.UserVo;
import com.ouni.domain.User;
import com.ouni.mapper.UserMapper;
import com.ouni.service.UserService;
import com.ouni.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public CommonResult login(@RequestBody User user){
        int u_id;
        log.info(user.getOpenId());
        User u_temp = userService.getUserByOpenId(user.getOpenId());

        if (u_temp == null){
            userMapper.insert(user);
            u_id = userService.getUserByOpenId(user.getOpenId()).getUserId();
        }else {
            userMapper.update(user,new QueryWrapper<User>().eq("user_id",u_temp.getUserId()));
            u_id = u_temp.getUserId();
        }
//        if (u_temp.getOpenId().equals("")){
//            return new commonResult(ResultEnum.FAIL_TOKEN);
//        }
        return new CommonResult<>(ResultEnum.SUCCESS,u_id);
    }

    /**
     *  根据code后台获取openid和session_key
     * @param user
     * @return
     * @throws IOException
     */
    @RequestMapping("/jscode2session")
    public CommonResult jscode2session(@RequestBody UserVo user) throws IOException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx8487398815443c4f&secret=abdd712d2970d7c0df92e278851afb89&js_code=JSCODE&grant_type=authorization_code";
        url=url.replaceAll("JSCODE", user.getCode());
        CloseableHttpResponse response = HttpClients.createDefault().execute(new HttpGet(url));
        String html = EntityUtils.toString(response.getEntity());
        JSONObject obj= JSON.parseObject(html);//将json字符串转换为json对象
        log.info("html --- > {}",html);
        return new CommonResult(ResultEnum.SUCCESS,obj);
    }

    @RequestMapping("/token")
    public CommonResult getToken(@RequestBody UserVo user){
        if (user == null){
            return new CommonResult(ResultEnum.FAIL_TOKEN);
        }
        String token = JWTUtil.getToken(user);
        log.info("token: {}",token);
        return new CommonResult(ResultEnum.SUCCESS,token);
    }

    /**
     * 根据userid 返回头像和昵称
     */
    @RequestMapping("/getUserInfo")
    public CommonResult getUserInfo(@RequestParam("userId") int userId){
        User user = userMapper.selectById(userId);
        if (user != null){
            HashMap<String,String> map = new HashMap<>();
            map.put("nickname",user.getUserName());
            map.put("imgUrl",user.getImgUrl());
            return new CommonResult<>(ResultEnum.SUCCESS,map);
        }
        return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
    }


    /**
     * 签到
     * @return
     */
    @GetMapping("/sign")
    public CommonResult signed(@RequestParam("userId") int userId){
        boolean flag = userService.doSign(userId);
        if (flag){
            return new CommonResult<>(ResultEnum.SUCCESS);
        }else {
            return new CommonResult(ResultEnum.FAIL_SIGN);
        }
    }

    @GetMapping("/signCount")
    public CommonResult signCount(@RequestParam("userId") int userId){
        int count = userService.signContinueCount(userId);
        int[] arr = userService.signCountArr(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("continuityCount",count);
        map.put("days",arr);
        return new CommonResult(ResultEnum.SUCCESS,map);
    }


}
