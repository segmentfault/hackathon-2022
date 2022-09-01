package com.ouni.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ouni.domain.User;
import com.ouni.mapper.UserMapper;
import com.ouni.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByOpenId(String openId) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("open_id", openId));
    }

    @Override
    public boolean doSign(int userId) {
        //  获取日期信息
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyy:MM"));
        // 拼接key
        String key = "sign:" + userId + ":" +keySuffix;
        // 获取今天是本月的第几天 , 注意这里是从1 到 31 ,而redis的值是从0 到 30 , 所以 , 这里要减一
        int dayOfMonth = now.getDayOfMonth();
        dayOfMonth = 15;
        System.out.println(dayOfMonth);
        if (redisTemplate.opsForValue().getBit(key,dayOfMonth-1)){
            return false; //表示已经签到
        }
        // 写入Redis . SETBIT key offset 1 , 这里使用boolean值是为了节省空间 , 所以这里使用true
        redisTemplate.opsForValue().setBit(key,dayOfMonth - 1,true);
        return true;
    }

    @Override
    public int signContinueCount(int userId) {
        // 获取日期信息
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyy:MM"));
        // 拼接key
        String key = "sign:" + userId + ":" +keySuffix;
        // 获取今天是本月的第几天 , 注意这里是从1 到 31 ,而我们的redis的值是从0 到 30 , 所以 , 这里要减一
        int dayOfMonth = now.getDayOfMonth();
        // 获取本月截止今天为止的签到记录 , 返回的是一个十进制数字
        List<Long> list = redisTemplate.opsForValue().bitField(key,BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (list == null || list.isEmpty()) {
            // 没有签到结果
            return 0;
        }
        Long num = list.get(0);
        if (num == null || num == 0){
            return 0;
        }
        int count = 0;
        // 循环遍历
        while (true) {
            // 让这个数字与1做与运算 , 得到数字的最后一个bit位,判断这个bit位是否是0
            if ((num & 1) == 0) {
                // 为零说明未签到 , 结束
                break;
            }else{
                // 不为零 , 说明已签到 , 计数器 +1
                count++;
            }
            // 把数字右移一位 , 继续下一个bit位
            num >>>= 1;
        }
        return count;
    }

    @Override
    public int[] signCountArr(int userId) {
        int[] arr =  new int[31];
        // 获取日期信息
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern("yyyy:MM"));
        // 拼接key
        String key = "sign:" + userId + ":" +keySuffix;
        // 获取今天是本月的第几天 , 注意这里是从1 到 31 ,而我们的redis的值是从0 到 30 , 所以 , 这里要减一
        int dayOfMonth = now.getDayOfMonth();
        // 获取本月截止今天为止的签到记录 , 返回的是一个十进制数字
        List<Long> list = redisTemplate.opsForValue().bitField(key,BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (list == null || list.isEmpty()) {
            // 没有签到结果
            return arr;
        }
        Long num = list.get(0);
        if (num == null || num == 0){
            return arr;
        }
        int count = dayOfMonth-1;

        // 循环遍历
        System.out.println("num" + num);
        while (count >= 0) {
            // 让这个数字与1做与运算 , 得到数字的最后一个bit位,判断这个bit位是否是0
            if ((num & 1) == 0) {

            }else{
                // 不为零 , 说明已签到
                arr[count] = 1;
            }
            num >>>= 1;
            count--;
        }
        return arr;
    }


}
