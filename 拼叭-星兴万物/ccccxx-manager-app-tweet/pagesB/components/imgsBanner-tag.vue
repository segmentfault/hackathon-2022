<template name='imgsBanner'>
  <view class="imgsBannerBox">
    <view class="image-mask"></view>
    <swiper
      class="imgsBanner_swiper"
      :current="comCurrentImg"
      @change="changCurrent"
    >
      <swiper-item v-for="(item, index) in imgList" :key="index">
        <image :src="item" mode="aspectFill"></image>
      </swiper-item>
      <swiper-item v-if="imgList.length === 0">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/825cac5b-c92e-4e2e-b43e-dc14e23ae7f6.png"
          mode="aspectFill"
        ></image>
      </swiper-item>
    </swiper>

    <!-- 图片位置 -->
    <view class="imgLength">{{
      comCurrentImg + 1 + "/" + imgList.length
    }}</view>
    <scroll-view
      scroll-x="true"
      class="scrollImgBox"
      :scroll-left="scrollImgList"
      scroll-with-animation
    >
      <view class="scrollImgList">
        <image
          :src="item"
          mode="aspectFill"
          v-for="(item, index) in imgList"
          :key="index"
          :class="comCurrentImg == index ? 'activeImageItem' : ''"
          @click="onClickImg(index)"
          :id="'item' + index"
          @longpress="touchend(item, index)"
        ></image>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  name: "imgsBanner",
  props: {
    imgList: {
      type: Array,
      value: [],
    },
    currentImg: {
      type: Number,
      value: 0,
    },
  },
  data() {
    return {
      comCurrentImg: 0,
      scrollImgList: 0,
      imgLeftList: [],
    };
  },
  created() {
    this.comCurrentImg = this.currentImg;
  },
  mounted() {
    uni.getSystemInfo({
      success: (res) => {
        this.imgList.forEach((i, v) => {
          let info = uni.createSelectorQuery().in(this);
          info
            .select("#item" + v)
            .boundingClientRect((res) => {
              this.imgLeftList.push(res.left);
            })
            .exec();
        });
        this.imgListScroll(this.comCurrentImg);
      },
    });
  },
  methods: {
    // 长按删除照片墙
    touchend(v, i) {
      clearTimeout(setTime);
      let setTime = setTimeout(() => {
        uni.showModal({
          title: "温馨提示",
          content: "是否删除该照片？",
          success: (res) => {
            if (res.confirm) {
              this.imgList.includes(v);
              this.imgList.splice(i, 1);
              setTimeout(() => {
                this.$emit("update:imgList", this.imgList);
                this.$emit("updateUserInfo");
              }, 500);
            }
          },
        });
      }, 500);
    },
    onClickImg(index) {
      this.comCurrentImg = index;
      this.imgListScroll(index);
    },
    changCurrent(e) {
      this.comCurrentImg = e.target.current;
      this.imgListScroll(e.target.current);
    },
    // 图片滑动
    imgListScroll(index) {
      if (index >= 2) {
        this.scrollImgList = this.imgLeftList[index - 2];
      } else {
        this.scrollImgList = 0;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.imgsBannerBox {
  position: relative;
}
.image-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1;
  background: linear-gradient(
    0deg,
    rgba(0, 0, 0, 0) 0%,
    rgba(0, 0, 0, 0.8) 100%
  );
}
.imgLength {
  position: absolute;
  top: 694upx;
  right: 24upx;
  background: rgba(0, 0, 0, 0.34);
  padding: 0 12upx;
  line-height: 32upx;
  font-size: 22upx;
  border-radius: 16upx;
  color: #fff;
}
.imgsBanner_swiper {
  width: 750upx;
  height: 750upx;
  margin-bottom: 24upx;
  swiper-item {
    width: 750upx;
    height: 100%;
    image {
      width: 750upx;
      height: 750upx;
    }
  }
}
.scrollImgBox {
  z-index: 2;
  .scrollImgList {
    white-space: nowrap;
    height: 160upx;
    image {
      width: 160upx;
      height: 160upx;
      margin-right: 24upx;
      display: inline-block;
      border-radius: 8upx;
      box-sizing: border-box;
    }
    image:last-child {
      margin-right: 0;
    }
    // .activeImageItem{
    // 	border: 6upx solid #F57341;
    // }
  }
}
</style>
