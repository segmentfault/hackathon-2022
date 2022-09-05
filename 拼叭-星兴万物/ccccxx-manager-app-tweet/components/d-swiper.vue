<template>
  <view class="wrap">
    <swiper
      class="swiper"
      :style="{ height: height * 2 + 'rpx' }"
      :autoplay="autoplay"
      :interval="interval"
      :duration="duration"
      :circular="loop"
      @animationfinish="change"
      :previous-margin="previousMargin + 'rpx'"
      :next-margin="nextMargin + 'rpx'"
    >
      <swiper-item
        v-for="(item, index) in list"
        :key="index"
        @click="$emit('clickItem', item)"
      >
        <view
          v-if="slots"
          class="item"
          :class="[!crown ? '' : current == index ? 'crown-active' : 'crown']"
        >
          <image
            class="item-img"
            :class="[imgShadow ? 'imgShadow' : '']"
            :src="item"
            :style="{ borderRadius: imgRadius + 'px', width: imgWidth }"
            mode=""
          ></image>
        </view>
        <slot :data="item"></slot>
      </swiper-item>
    </swiper>
    <view class="dots flex" :style="{ bottom: bottom * 2 + 'rpx' }" v-if="dots">
      {{ current + 1 + "/" + list.length }}
    </view>
  </view>
</template>

<script>
	export default {
		props: {
			list: {
				type: Array,
				default: () => []
			},
			// 轮播图片key
			imgKey: {
				type: String,
				required: true
			},
			// 高度
			height: {
				type: Number,
				default: 200
			},
			// 图片圆角
			imgRadius: {
				type: Number,
				default: 0
			},
			// 图片阴影
			imgShadow: {
				type: Boolean,
				default: false
			},
			// 前边距
			previousMargin: {
				type: Number,
				default: 0
			},
			// 后边距
			nextMargin: {
				type: Number,
				default: 0
			},
			// 图片宽度
			imgWidth: {
				type: String,
				default: '100%'
			},
			// 是否循环
			loop: {
				type: Boolean,
				default: false
			},
			// 自动播放
			autoplay: {
				type: Boolean,
				default: false
			},
			// 播放时间间隔
			interval: {
				type: Number,
				default: 2000
			},
			// 滑动速度
			duration: {
				type: Number,
				default: 1200
			},
			// 显示指示点
			dots: {
				type: Boolean,
				default: false
			},
			// 轮播点下边距
			bottom: {
				type: Number,
				default: 10
			},
			// 卡片特效
			crown: {
				type: Boolean,
				default: false
			},
			slots: { 
				type: Boolean,
				default: true
			}
		},
		data() {
			return {
				current: 0,
				num:0,
			};
		},
		watch:{
			list:{
				deep:true,
				handler(val){
					console.log(val)
				}
			}
		},
		methods: {
			change(event) {
				let current = event.detail.current
				this.current = current
				this.$emit('change', current)
				this.$emit('change', current,this.list.length)
			}
	
		}
	}
</script>

<style lang="scss" scoped>
.wrap {
  position: relative;

  .crown {
    transform: scale(0.93, 0.85);
  }

  .item {
    height: 100%;
    transition: 1.2s;
    overflow: hidden;
  }

  .item-img {
    width: 100%;
    height: 100%;
  }

  .imgShadow {
    height: calc(100% - 10px);
    margin-bottom: 10px;
    box-shadow: 0 6px 6px rgba(0, 0, 0, 0.15);
  }

  .crown-active {
    transform: scale(1);
  }

  .dots {
    display: flex;
    position: absolute;
    justify-content: flex-end;
    left: 88%;
    transform: translateX(-50%);
    padding: 5rpx 15rpx;
    border-radius: 16rpx;
    color: #fff;
    background: rgba($color: #000000, $alpha: 0.8);
    .dot {
      width: 6rpx;
      height: 6rpx;
      border-radius: 50%;
      background-color: #d6d6d6;
      margin-right: 8rpx;
    }

    .curr-dot {
      height: 6rpx;
      width: 22rpx;
      border-radius: 6rpx;
      background-color: #fff;
    }
  }
}
</style>
