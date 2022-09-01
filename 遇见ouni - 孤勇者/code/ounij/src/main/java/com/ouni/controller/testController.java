package com.ouni.controller;

import com.ouni.common.ResultEnum;
import com.ouni.common.CommonResult;
import com.ouni.domain.User;
import com.ouni.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class testController {
    @Autowired
    private UserService userService;

    @RequestMapping("/tes")
    public CommonResult<?> test(){
        User s = userService.getUserByOpenId("oSdjC5YXIG1qkUo5Xp0UlfKBZC4s");
        System.out.println(s);
        return new CommonResult<>(ResultEnum.SUCCESS);
    }


}
