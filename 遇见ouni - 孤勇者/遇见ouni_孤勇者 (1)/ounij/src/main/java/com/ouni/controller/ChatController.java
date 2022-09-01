package com.ouni.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.common.ResultEnum;
import com.ouni.common.CommonResult;
import com.ouni.domain.ChatRecord;
import com.ouni.mapper.ChatRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class ChatController {
    @Autowired
    private ChatRecordMapper chatRecordMapper;
    @RequestMapping("/chat_record")
    public CommonResult<?> getRecord(@RequestParam("fromUid") int fromUid, @RequestParam("toUid") int toUid, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        log.info("fromUid:"+ fromUid + ",toUid"+ toUid);
        List<ChatRecord> records = chatRecordMapper.selectList(new QueryWrapper<ChatRecord>().eq("from_user_id", toUid).eq("to_user_id", fromUid).eq("status",0));
        for(ChatRecord record : records){
            record.setStatus(1);
            chatRecordMapper.updateById(record);
            log.info("修改了{}的status",record);
        }
//        Long count = chatRecordMapper.selectCount(new QueryWrapper<>());
//        log.info("count:{}",count);

        Integer pageSize = 15;

        Page<ChatRecord> pageInfo = chatRecordMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<ChatRecord>lambdaQuery()
                .orderByDesc(ChatRecord::getRecordTime)
                .in(ChatRecord::getFromUserId,fromUid,toUid)
                .in(ChatRecord::getToUserId,fromUid,toUid))
                ;
        return new CommonResult<>(ResultEnum.SUCCESS,pageInfo);
    }
}
