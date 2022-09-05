<template>
  <view style="background-color: #f4f5fa; height: 100vh">
    <u-navbar
      :is-back="false"
      title="志趣"
      title-color="white"
      :background="background"
      :border-bottom="false"
    >
      <view class="slot-wrap u-p-l-32">
        <u-image
          @click="goOrhome()"
          width="166rpx"
          height="60rpx"
          src="/static/img/switch.png"
        ></u-image>
      </view>
    </u-navbar>
    <view class="top">
      <view class="userMsg">
        <view class="u-m-l-16 msg">
          <u-image
            class="u-m-l-32"
            border-radius="90"
            width="128rpx"
            height="128rpx"
            :src=userInfo.user_img
          >
          </u-image>
          <view class="u-m-l-16">
            <view class="name u-flex"> {{userInfo.user_name}} </view>
            <view class="id"> 志愿者ID：{{userInfo._openid}} </view>
          </view>
        </view>
        <view class="set">设置</view>
      </view>
      <view class="service">
        <view class="card">
          <view class="t1 u-m-l-20 u-m-t-12">服务时长</view>
          <view class="bottom u-m-l-20 u-m-t-16">
            <view class="left" style="color: #eb626a">
              <view class="num">
                <view class="n">{{userInfo.serviceTime?userInfo.serviceTime:0}}</view>
                <view class="tx">小时</view>
              </view>
              <view class="EN u-m-t-5">hours of service</view>
            </view>
            <view class="right">
              <img class="img" src="/static/img/my/aix.png" alt="" />
            </view>
          </view>
        </view>
        <view class="card">
          <view class="t1 u-m-l-20 u-m-t-12">加入组织</view>
          <view class="bottom u-m-l-20 u-m-t-16">
            <view class="left" style="color: #ff9a48">
              <view class="num">
                <view class="n">{{userOr}}</view>
                <view class="tx">个</view>
              </view>
              <view class="EN u-m-t-5">Join Us</view>
            </view>
            <view class="right">
              <img class="img" src="/static/img/my/zhuzi.png" alt="" />
            </view>
          </view>
        </view>
        <view class="card">
          <view class="t1 u-m-l-20 u-m-t-12">参加项目</view>
          <view class="bottom u-m-l-20 u-m-t-16">
            <view class="left" style="color: #0076ff">
              <view class="num">
                <view class="n">{{userAc}}</view>
                <view class="tx">个</view>
              </view>
              <view class="EN u-m-t-5">project meeting</view>
            </view>
            <view class="right">
              <img class="img" src="/static/img/my/xiangmu.png" alt="" />
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="mid">
      <view class="head">
        <view class="left">志愿榜单</view>
        <view class="right" @click="goList()"
          >查看全部
          <u-icon name="arrow-right" color="#6C6D6F" size="28"></u-icon>
        </view>
      </view>
      <view class="list">
        <view class="msg" v-for="(item,index) in userList" :key="index">
          <view class="userMsg">
            <view class="medal">
              <img class="img" :src="rankImg[index]" alt="" />
            </view>
            <view class="userImg">
              <img class="Uimg" :src="item.user_img" alt="" />
            </view>
            <view class="name">{{itme.user_name}}</view>
          </view>
          <view class="num">影响力{{item.serviceTime}}</view>
        </view>
      </view>
    </view>
    <view class="achievement">
      <view class="a-top">成就徽章</view>
      <view class="show">
        <view class="s-content"  v-for="(item,index) in achievement" :key="index">
          <u-image 
            @click="goAch(item._id)"
            :src="item.img"
            width="120rpx"
            height="120rpx"
          ></u-image>
          <view class="u-m-t-16">{{item.badge_name}}</view>
        </view>
      </view>
    </view>
  	<u-skeleton :loading="loading" :animation="true" bgColor="#FFF"></u-skeleton>
  </view>
</template>

<script>
const db = wx.cloud.database();
const _ = db.command
export default {
  data() {
    return {
      bgColor: "#001f3f",
      background: {
        backgroundColor: "#4575F6 ",
      },
      userLoginFlag: "",
      userInfo: {},
      userAc:'',
      userOr:'',
      openid:'',
      loading: true,
      achievement:{},
      userList:{},
      rankImg:['/static/img/my/1.png','/static/img/my/2.png','/static/img/my/3.png']
    };
  },
   async onLoad() {
    this.getuserInfo();
    console.log("dsfdfasdfs", uni.getStorageSync("login"));
    //获取用户信息
    wx.getStorage({
      key: "login",
      success(res) {
        console.log("dsad", res.data);
      },
    });
   this.getAchievement();
  },
  onShow(){
    this.getuserList()
     setTimeout(() => {
				this.loading = false;
			}, 1000)
     this.getUserAc();
  },
  methods: {
    async getuserInfo() {
      this.openid = wx.getStorageSync("openid");
      const re=await db.collection("userInfo")
        .where({
          _openid:this.openid ,
        }).get()
		this.userInfo=re.data[0];
    var disLength = this.userInfo._openid.length;
		this.userInfo._openid=re.data[0]._openid.substring(disLength-5,disLength);
      console.log("sdfsdf", this.userInfo);
    },
    //获取用户参与组织，项目数量
    getUserAc(){
      db.collection('userOrganization').where({
        _openid:this.openid
      }).get().then(res=>{
        console.log("组织",res)
        this.userOr=res.data.length
      })
       db.collection('userActivity').where({
        _openid:this.openid
      }).get().then(res=>{
        console.log("活动",res)
        this.userAc=res.data.length
      })
    },
    getAchievement(){
      db.collection('badge').get().then(res=>{
        console.log("徽章",res);
        this.achievement=res.data;
      })
    },
    goAch(data) {
      let a = data;
      uni.navigateTo({
        url: `/pagesA/achievement/achievement?id=${a}`,
      });
    },
    goList() {
      uni.navigateTo({
        url: "../../../pagesA/list/list",
      });
    },
    onTabItemTap: function (item) {
     if (!uni.getStorageSync("login")) {
        uni.navigateTo({
          url: "/pagesA/sign/sign?id=1",
        });
        }
    }, 
    //获取志愿榜单
    getuserList(){
      db.collection('userInfo').orderBy("serviceTime","desc").limit(3).get().then(res=>{
        console.log("结果",res)
        this.userList=res.data
      })
    },
    goOrhome() {
       if (!uni.getStorageSync("Ologin")) {
        uni.navigateTo({
          url: "/pagesA/sign/sign?id=2",
        }); } else{
           uni.navigateTo({
        url: "../../../pagesB/or-home/or-home",
         });
        }
    
    },
    Login() {
      if (!uni.getStorageSync("login")) {
        uni.navigateTo({
          url: "/pagesA/sign/sign?id=1",
        });
      }
    },
  },
};
</script>
<style scoped lang="scss">
.top {
  width: 100%;
  height: 470rpx;
  background-color: #4575f6;
  display: flex;
  flex-direction: column;

  .text {
    text-align: center;
    margin-top: 70rpx;
    font-size: 36rpx;
    font-weight: 600;
    color: #ffffff;
  }

  .userMsg {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 54rpx;

    .msg {
      display: flex;
    }

    .name {
      font-size: 36rpx;
      font-weight: 600;
      color: #ffffff;
    }

    .id {
      padding-top: 8rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #ffffff;
    }

    .set {
      text-align: center;
      line-height: 52rpx;
      border-radius: 20rpx 0 0 20rpx;
      width: 124rpx;
      height: 52rpx;
      background: rgba(255, 255, 255, 0.4);
      opacity: 1;
      filter: blur(undefinedpx);
    }
  }
}

.service {
  display: flex;
  margin-left: 24rpx;
  margin-top: 48rpx;

  .card {
    margin-left: 8rpx;
    width: 224rpx;
    height: 160rpx;
    border-radius: 16rpx;
    background-color: white;

    .t1 {
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
    }

    .bottom {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;

      .num {
        display: flex;
        align-items: flex-end;

        .n {
          font-size: 32rpx;
        }

        .tx {
          padding-left: 5rpx;
          font-size: 20rpx;
        }
      }

      .EN {
        font-size: 16rpx;
      }
    }

    .right {
      margin-right: 16rpx;

      .img {
        width: 48rpx;
        height: 48rpx;
      }
    }
  }
}

.mid {
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  height: 334rpx;
  margin: 32rpx;
  border-radius: 16rpx;
  .head {
    display: flex;
    justify-content: space-between;
    margin-left: 24rpx;
    margin-right: 24rpx;
    margin-top: 32rpx;

    .left {
      font-size: 32rpx;
      font-weight: 600;
      color: #333333;
    }

    .right {
      font-size: 28rpx;
      font-weight: 400;
      color: #6c6d6f;
    }
  }

  .list {
    display: flex;
    flex-direction: column;

    .msg {
      display: flex;
      justify-content: space-between;
      margin: 24rpx 32rpx 0 24rpx;
    }

    .userMsg {
      display: flex;

      .img {
        width: 36rpx;
        height: 36rpx;
      }

      .name {
        margin-left: 16rpx;
        font-size: 28rpx;
        font-weight: 600;
        color: #333333;
        line-height: 42rpx;
      }

      .Uimg {
        margin-left: 16rpx;
        border-radius: 50%;
        width: 42rpx;
        height: 42rpx;
      }
    }

    .num {
      font-size: 24rpx;
      font-weight: 600;
      color: #333333;
      opacity: 0.7;
    }
  }
}
.posi {
  position: absolute;
  right: -30rpx;
}
.achievement {
  height: 300rpx;
  margin-left: 32rpx;
  margin-right: 32rpx;
  background: #ffffff;
  border-radius: 16rpx;
  padding-top: 8rpx;
  .a-top {
    margin-left: 24rpx;
    margin-top: 18rpx;
    font-size: 32rpx;
    font-weight: 600;
    color: #333333;
  }

  .s-content {
    position: relative;
    margin-top: 24rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .show {
    display: flex;
    justify-content: space-around;

    .img {
      width: 120rpx;
      height: 120rpx;
    }
  }
}
</style>
