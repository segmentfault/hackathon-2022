package com.ouni.controller;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ouni.common.ResultEnum;
import com.ouni.common.CommonResult;
import com.ouni.domain.GoodsPub;
import com.ouni.domain.Vo.GoodsVo;
import com.ouni.mapper.GoodMapper;
import com.ouni.service.GoodService;
import com.ouni.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/xianzhi")
public class XianzhiController {

    @Autowired
    private QiniuUtil qiniuUtil;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private GoodService goodService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据名字搜索某一种商品
     */
    @GetMapping("/search")
    public CommonResult searchByName(@RequestParam("goodsName") String goodsName, @RequestParam("pageNum") int pageNum){
        Page<GoodsPub> pubPage = goodService.getListByName(goodsName, pageNum, 10);
        return new CommonResult<>(ResultEnum.SUCCESS,pubPage);
    }

    /**
     * 上传文件到七牛云存储
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload/img")
    public CommonResult uploadImgQiniu(@RequestParam(value = "file")  MultipartFile multipartFile) throws IOException {
        FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);
        fileName = fileName.replaceAll(suffixName,"");
        System.out.println("上传的文件名为：" + fileName);
        String path = qiniuUtil.uploadImg(inputStream, fileName+"_"+ UUID.randomUUID()+suffixName);
        return new CommonResult<>(ResultEnum.SUCCESS,path);
    }

    /**
     * 发布商品
     * @param goodsPub
     * @return
     */
    @PostMapping("/pub")
    public CommonResult publishGood(@RequestBody GoodsPub goodsPub){
        goodsPub.setUploadTime(new Date());
        try {
            goodMapper.insert(goodsPub);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult<>(ResultEnum.FAIL_UPLOAD);
        }
        return new CommonResult<>(ResultEnum.SUCCESS,goodsPub);
    }

    /**
     * 修改商品
     * @param goodsPub
     * @return
     */
    @PostMapping("/update")
    public CommonResult updateGood(@RequestBody GoodsPub goodsPub){
        try {
            goodMapper.updateById(goodsPub);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }
        return new CommonResult<>(ResultEnum.SUCCESS,goodsPub);
    }

    @GetMapping("/getMyGoods")
    public CommonResult<?> selectMyGoods(@RequestParam("userId") int userId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        Integer pageSize = 10;
        Page<GoodsPub> MygoodsInfo = goodMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<GoodsPub>lambdaQuery()
                .orderByDesc(GoodsPub::getUploadTime)
                .eq(GoodsPub::getUserId,userId));
        return new CommonResult<>(ResultEnum.SUCCESS,MygoodsInfo);
    }

    /**
     * 删除的自己的某个商品
     * @param goodId
     * @return
     */
    @GetMapping("/del/{goods_id}")
    public CommonResult publishGood(@PathVariable("goods_id") int goodId){
        try {
            goodMapper.deleteById(goodId);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult<>(ResultEnum.UNKNOWN_ERROR);
        }
        return new CommonResult<>(ResultEnum.SUCCESS);
    }

    /**
     * 查看某一个商品
     * @param goodsId
     * @return
     */
    @GetMapping("/select")
    public CommonResult getGoodsInfo(@RequestParam("goodsId") int goodsId){

        GoodsPub good = goodMapper.selectById(goodsId);
        String name = "pageView:"+good.getSimpleWord();

        redisTemplate.opsForValue().setIfAbsent("goods:"+ goodsId + ":views",0); //goods:id:views
        Long pageview = redisTemplate.opsForValue().increment("goods:" + goodsId + ":views");

        redisTemplate.opsForZSet().addIfAbsent("hot",name,0); //热点物品
        redisTemplate.opsForZSet().incrementScore("hot", name, 1);

        GoodsVo goodsVo = new GoodsVo(good, pageview);
        if (good != null){
            return new CommonResult<>(ResultEnum.SUCCESS,goodsVo);
        }else {
            return new CommonResult<>(ResultEnum.NONE_FIND_GOOD);
        }
    }

    /**
     * 分页查看所有商品列表
     * @param pageNum
     * @return
     */
    @GetMapping("/goodsList")
    public CommonResult<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum) {
        Integer pageSize = 10;
        Page<GoodsPub> goodsInfoPage = goodMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<GoodsPub>lambdaQuery().orderByDesc(GoodsPub::getUploadTime));
        return new CommonResult<>(ResultEnum.SUCCESS,goodsInfoPage);
    }


    @GetMapping("/hot")
    public CommonResult<?> getHot(){
        Set hot = redisTemplate.opsForZSet().reverseRange("hot", 0, 2);
        return new CommonResult<>(ResultEnum.SUCCESS,hot);
    }

}
