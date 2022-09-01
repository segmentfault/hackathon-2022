package com.ouni.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.common.CommonResult;
import com.ouni.common.ResultEnum;
import com.ouni.domain.Comment;
import com.ouni.domain.GoodsPub;
import com.ouni.domain.Vo.CommentVo;
import com.ouni.domain.Vo.VomitSlotVo;
import com.ouni.domain.VomitSlot;
import com.ouni.mapper.CommentMapper;
import com.ouni.mapper.VomitSlotMapper;
import com.ouni.service.VomitSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RequestMapping("/vomitslot")
@RestController
public class VomitSlotController {
    @Autowired
    private VomitSlotMapper vomitSlotMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VomitSlotService vomitSlotService;

    @GetMapping("/getAll")
    public CommonResult<?> getBlogsList(@RequestParam(defaultValue = "1") Integer pageNum){
        Integer pageSize = 10;
        Page<VomitSlot> slotPage = vomitSlotMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<VomitSlot>lambdaQuery().orderByDesc(VomitSlot::getUploadTime));
        ArrayList<VomitSlotVo> vos = new ArrayList<>();
        for (VomitSlot vomitSlot : slotPage.getRecords()){
            VomitSlotVo vo = new VomitSlotVo();
            vo.setVomitSlot(vomitSlot);
            Set likes = redisTemplate.opsForSet().members("blog:" + vomitSlot.getId() + ":likes");
            vo.setLikes(likes.size());
            Integer comments = (Integer)redisTemplate.opsForValue().get("blog:" + vomitSlot.getId() + ":comments"); //blog:id:comments
            vo.setCommentsNum(comments);

            vos.add(vo);
        }
        return new CommonResult(ResultEnum.SUCCESS,vos,slotPage.getTotal());
    }

    @PostMapping("/pub")
    public CommonResult<?> pubVomitBlog(@RequestBody VomitSlot vomitSlot){
        vomitSlot.setUploadTime(new Date());
        System.out.println(vomitSlot);
        try {
            vomitSlotMapper.insert(vomitSlot);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }
        redisTemplate.opsForValue().set("blog:"+vomitSlot.getId()+":views",0);
        redisTemplate.opsForValue().set("blog:"+vomitSlot.getId()+":comments",0);
        return new CommonResult<>(ResultEnum.SUCCESS,vomitSlot);
    }


    /**
     * 查看某个吐槽贴
     * @param vomitslotId
     * @param userId
     * @return
     */
    @GetMapping("/select")
    public CommonResult<?> selectById(@RequestParam("vomitslotId") int vomitslotId,@RequestParam("userId") int userId,@RequestParam(defaultValue = "1") Integer pageNum){

        VomitSlot vomitSlot = vomitSlotMapper.selectById(vomitslotId);

        Long views = redisTemplate.opsForValue().increment("blog:" + vomitslotId + ":views");

        Integer comments = (Integer) redisTemplate.opsForValue().get("blog:"+vomitslotId+":comments");
        Set likes = redisTemplate.opsForSet().members("blog:" + vomitslotId + ":likes");
        VomitSlotVo slotVo = new VomitSlotVo(vomitSlot,views.intValue() ,likes.size(),comments);
        HashMap<String, Object> map = new HashMap<>();

        map.put("VomitSlotVo",slotVo);
        if (likes.contains(userId)){
            map.put("isLike",true);
        }else {
            map.put("isLike",false);
        }

        Integer pageSize = 10;
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getVomitslotId,vomitslotId)
                .eq(Comment::getParentId,-1));

        List<CommentVo> list = vomitSlotService.getComments(vomitslotId, commentPage);

        map.put("comments",list);

        return new CommonResult<>(ResultEnum.SUCCESS,map,commentPage.getTotal());

    }

    @GetMapping("/commentDetail")
    public CommonResult<?> commentDetail(@RequestParam("vomitslotId") int vomitslotId,
                                         @RequestParam(defaultValue = "1")Integer pageNum,@RequestParam("parentId") int parentId){

        Integer pageSize = 10;
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getParentId, parentId)
                .eq(Comment::getVomitslotId, vomitslotId));

        if (commentPage.getRecords() == null){
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }

        List<CommentVo> detail = vomitSlotService.getCommentsDetail(commentPage);
        return new CommonResult<>(ResultEnum.SUCCESS,detail,commentPage.getTotal());
    }


    /**
     * 点赞/取消赞
     * @param vomitslotId
     * @param userId
     * @return
     */
    @GetMapping("/like")
    public CommonResult<?> addLike(@RequestParam("vomitslotId") int vomitslotId,@RequestParam("userId") int userId){
        Set members = redisTemplate.opsForSet().members("blog:" + vomitslotId + ":likes");
        if (members.contains(userId)){
            redisTemplate.opsForSet().remove("blog:" + vomitslotId + ":likes",userId);
        }else {
            redisTemplate.opsForSet().add("blog:" + vomitslotId+ ":likes",userId);
        }
        return new CommonResult<>(ResultEnum.SUCCESS);
    }


    @PostMapping("/comment")
    public CommonResult<?> applyComment(@RequestBody Comment comment){
        comment.setCreateTime(new Date());
        try {
            commentMapper.insert(comment);
        }catch (Exception e){
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }
        redisTemplate.opsForValue().increment("blog:" + comment.getVomitslotId() + ":views");
        return new CommonResult<>(ResultEnum.SUCCESS);
    }




}
