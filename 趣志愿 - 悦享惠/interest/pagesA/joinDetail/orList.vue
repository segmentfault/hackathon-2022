<template>
  <view class="box">
    <u-navbar
      back-icon-size="48"
      :border-bottom="false"
      title="组织列表"
      title-color="white"
      :is-back="true"
      back-icon-color="white"
      :background="background"
    >
    </u-navbar>
    <view class="organization">
      <view class="content" v-for="(item, index) in orInfo2" :key="index">
        <view class="left" @click="orDetail(item._id)">
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
          <view class="name" @click="orDetail(item._id)"
            >{{ item.group_name }}
            <u-image
              v-if="item.audit"
              height="43rpx"
              width="136rpx"
              src="/static/img/or-detail/rz.png"
            ></u-image>
          </view>
          <view class="ti" @click="orDetail(item._id)"
            >成立时间：{{ item.createTime }}</view
          >
          <view class="ti" @click="orDetail(item._id)"
            >成员：{{ item.userNumber }}人</view
          >
          <view class="address" @click="mapFun(item)"
            ><text class="t2">地址：{{ item.address }}</text
            ><text class="gps">导航</text></view
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
      orInfo: {},
      orInfo2:[],
      acInfo: {},
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
    await this.getInfo();
  
  },
  methods: {
    async getInfo() {
      db.collection("organization")
        .get()
        .then((res) => {
          this.orInfo = res.data;
          console.log("this.orInfo", res.data);
            this.getUserAc()
        });
    },
      getUserAc(){
      db.collection('userOrganization').where({
        _openid:this.openid
      }).get().then(res=>{
        for(let i=0;i<res.data.length;i++){
          for(let j=0;j<this.orInfo.length;j++){
            if(res.data[i].orID==this.orInfo[j]._id){
              this.orInfo2.push(this.orInfo[j])
            }
          }
        } 
         uni.hideLoading();
      })
    },
    //跳转组织详情
    orDetail(data) {
      uni.navigateTo({
        url: `/pagesB/detail/or-detail?type=true&id=${data}`,
      });
    },
    mapFun(data) {
      uni.openLocation({
        latitude: data.latitude, //纬度
        longitude: data.longitude, //经度
        name: data.addressName,
        address: data.address,
      });
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
