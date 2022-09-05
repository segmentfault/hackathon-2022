<template>
  <view class="">
    <view class="nav flex flex-center flex-middle fixed fs32 text-center bold">
      <view class="nav-item" @tap="toggleNav(1)">
        <view class="">抽奖用户</view>
        <view :class="[{ active: navId == 1 }, 'line']"></view>
      </view>
      <view class="nav-item" @tap="toggleNav(2)">
        <view class="">获奖名单</view>
        <view :class="[{ active: navId == 2 }, 'line']"></view>
      </view>
    </view>

    <view class="list p30">
      <view class="" v-show="navId == 1">
        <view
          class="item p30 flex fs32 mt20"
          v-for="item in list"
          :key="item.id"
        >
          <image :src="item.head"></image>
          <view class="flex-1 pl20">
            <view class="bold">{{ item.nickName }}</view>
            <view class="fs26 color-999 mt10">{{
              $utils.dayjs(item.createTime).format('YYYY-MM-DD HH:mm:ss')
            }}</view>
          </view>
        </view>
      </view>
      <view class="" v-show="navId == 2">
        <view v-for="item in list" :key="item.id" class="">
          <view
            class=""
            v-for="items in item.winList"
            :key="items.userId"
            class="item p30 flex fs32 mt20"
          >
            <image :src="items.head"></image>
            <view class="flex-1 pl20">
              <view class="bold">{{ items.nickName }}
                 <view class="type fs20" v-if="item.couponTemplate" :class="{'type-active':item.couponTemplate.couponType==2}">{{discountType(item.couponTemplate)}}</view>
              </view>
              <view class="fs30 mt10">{{ item.prizeName }}</view>
              <view class="fs26 color-999 mt10">{{
                $utils.dayjs(items.createTime).format('YYYY-MM-DD HH:mm:ss')
              }}</view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import gameServe from '../../../service/game';

export default {
  data() {
    return {
      id: '',
      navId: 1,
      pageNum: 1,
      pageSize: 10,
      list: [],
      isLoading: false,
    };
  },
  onLoad(option) {
    this.id = option.id;
    if (option.navId) {
      this.navId = option.navId;
    }
    this.init();
  },
  onReachBottom() {
    if (this.list.length < this.total && this.navId == 1) {
      this.pageNum++;
      this.init();
    }
  },
  computed:{
      discountType(){
        return function(value){
          if(value.couponType==2){
          return '减'+value.discount;
          }
          switch(value.discount){
            case 0:return "免单";break;
            case 5:return "半价";break;
            default :return value.discount+'折';
          }
        }
      }
  },
  methods: {
    init() {
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        id: this.id,
      };
      const req =
        this.navId == 1
          ? gameServe.getDrawUser(params)
          : gameServe.getPrizeUser({ id: this.id });
      req
        .then((res) => {
          this.isLoading = false;
          if (this.navId == 1) {
            this.total = res.total;
            this.list = this.list.concat(res.list);
          } else {
            this.list = this.list.concat(res);
            console.log( this.list)
          }
        })
        .catch((err) => (this.isLoading = false));
    },
    toggleNav(id) {
      if (!this.isLoading) {
        this.pageNum = 1;
        this.isLoading = true;
        this.navId = id;
        this.list = [];
        this.init();
      }
    },
  },
};
</script>

<style lang="scss">
.nav {
  top: 0;
  left: 0;
  right: 0;
  height: 120upx;
  background: #fff;
  box-shadow: 0 0 12upx 0 #ccc;
  .nav-item {
    width: 150upx;
    margin: 0 40upx;
    .line {
      width: 80upx;
      height: 10upx;
      background: transparent;
      margin: 10upx auto;
    }
    .active {
      background: linear-gradient(to right, #ff4f3a, #cd6cff);
    }
  }
}
.bold{
  display: flex;
  align-items: center;
   .type{
     margin-left: 8upx;
     padding:5upx 6upx; 
     color:white;
     background: #E9722A;
     border-radius: 8upx;
   }
}
.type-active{
   background:#E72F32 !important;
}
.list {
  margin-top: 100upx;
  .item {
    border-radius: 20upx;
    background: #fbf7f7;
    image {
      width: 100upx;
      height: 100upx;
      border-radius: 50%;
    }
  }
}
</style>
