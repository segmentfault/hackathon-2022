<template>
  <view class="box">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="活动列表"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view>
      <view
        class="content"
        @click="goAcdetail(item._id)"
        v-for="(item, index) in orInfo2"
        :key="index"
      >
        <view class="left">
          <u-image
            height="180rpx"
            border-radius="8rpx"
            width="180rpx"
            :src="item.image"
            alt=""
          >
          </u-image>
        </view>
        <view class="right">
          <view class="name"
            >{{ item.name }}
            <u-image
              class="u-m-l-8"
              height="40rpx"
              width="88rpx"
              :src="
                item.show
                  ? '/static/img/or-detail/start.png'
                  : '/static/img/or-detail/end.png'
              "
            ></u-image>
          </view>
          <view class="ti">活动类型：{{ item.type }}</view>
          <view class="ti">开始时间：{{ item.time }}</view>
          <view class="address"
            ><text class="t2"
              >计划招募：{{ item.userNumber }} / {{ item.allNumber }}</text
            ></view
          >
        </view>
      </view>
    </view>
  </view>
</template>

<script>
const db = wx.cloud.database();
export default {
  data() {
    return {
      acInfo: {},
        orInfo2:[],
      background: {
        backgroundColor: "#4575F6 ",
      },
       openid:'',
    };
  },
  async onLoad(option) {
    uni.showLoading({
		  	title: '加载中',
			mask:true
	  });
     this.openid = wx.getStorageSync("openid");
    this.getInfo();
  },
  methods: {
    async getInfo() {
      db.collection("activity")
        .get()
        .then((res) => {
          this.acInfo = res.data;
          console.log("this.acInfo", this.acInfo);
          this.getUserAc();
        });
    },

    goAcdetail(data) {
      uni.navigateTo({
        url: `/pagesB/detail/ac-detail?acid=${data}`,
      });
    },
     getUserAc(){
       db.collection('userActivity').where({
        _openid:this.openid
      }).get().then(res=>{
        console.log(res)
        for(let i=0;i<res.data.length;i++){
          for(let j=0;j<this.acInfo.length;j++){
            if(res.data[i].activityID==this.acInfo[j]._id){
                this.orInfo2.push(this.acInfo[j])
            }
          }
        } 
         uni.hideLoading();
      })
    },
   
   
  },
};
</script>

<style lang="scss">
.box {
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
  padding: 36rpx 32rpx 0 32rpx;
  height: 100vh;
}

.choose {
  padding-left: 212rpx;
  display: flex;
  align-items: center;
  width: 100wh;
  /* 如果您想让slot内容占满整个导航栏的宽度 */
  flex: 1;

  text {
    font-size: 32rpx;
    font-weight: 600;
    color: #ffffff;
    opacity: 0.6;
  }

  .active {
    font-size: 36rpx;
    opacity: 1 !important;
  }
}
.search {
  display: flex;
  position: relative;
  margin-bottom: 15rpx;
  .searchBtn {
    z-index: 9990;
    position: absolute;
    width: 104rpx;
    height: 60rpx;
    top: 10rpx;
    right: 10rpx;
    line-height: 60rpx;
    text-align: center;
    color: white;

    background: #4575f6;
    border-radius: 124px 124px 124px 124px;
  }
}
.content {
  display: flex;
  margin-top: 24rpx;
  padding: 24rpx 0 24rpx 24rpx;
  background-color: #ffffff;
  border-radius: 24px;

  .right {
    display: flex;
    flex-direction: column;
    margin-left: 16rpx;

    .name {
      display: flex;
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.8;
    }

    .ti {
      margin-top: 8rpx;
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.4;
    }

    .address {
      margin-top: 8rpx;

      .t2 {
        font-size: 24rpx;
        font-weight: 600;
        color: #333333;
        opacity: 0.4;
      }

      .gps {
        padding-left: 8rpx;
        font-size: 24rpx;
        font-weight: 600;
        color: #4575f6;
      }
    }
  }
}

.newData {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 24rpx;
  margin-bottom: 18rpx;
  padding-left: 27rpx;
  padding-bottom: 18upx;

  .date {
    text-align: center;
    border: 1rpx solid #4575f6;
    border-radius: 24rpx;
    font-size: 24rpx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    width: 96rpx;
    height: 124rpx;
    margin-right: 10rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .week {
      color: #4575f6;
    }

    .day {
      color: #4575f6;
    }
  }

  .checked {
    background-color: #4575f6;

    .checked_text {
      color: #fff;
    }
  }
}
</style>
