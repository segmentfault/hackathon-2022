<template>
  <view class="">
    <view
			v-if="obj.supplierInfo"
      class="supplier-info p40"
    >
			<view class=" flex flex-between align-items-start">
				<image :src="obj.supplierInfo.cover" class="cover"></image>
				<view class="flex-1 pl30">
				  <view class="fs36 bold color-fff oneOmit" @tap.stop="jump(obj.supplierInfo.supplierId)">{{
				    obj.supplierInfo.companyName
				  }}</view>
				  <view class="flex flex-middle mt10 fs24">
				    {{ obj.luckType === 1 ? '截止时间' : '开奖时间' }}:
				    {{
				      $utils.dayjs(obj.endTime).format('YYYY-MM-DD HH:mm')
				    }}
				  </view>
					<view class="canyu">
					  <view class="number" v-if="obj.luckType === 1">{{ obj.joinUserNum }}/{{ obj.userNum }}人参与</view>
					  <view class="number" v-else>{{ obj.joinUserNum }}人参与</view>
					  <view class="draw">
					    {{ obj.luckType === 1 ? '即抽即中' : '报名抽奖' }}
					  </view>
					</view>
					<view class="call-btn mt10" @tap="callSupplier"></view>
				  <!-- <view class="flex flex-middle address mt20">
				    <image
				      src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/7654ca94-35a9-4812-9df6-67812f6e628e.png"
				    ></image>
				    <view class="fs24 color-disable ml10 oneOmit">{{
				      obj.supplierInfo.companyAddress
				    }}</view>
				  </view> -->
				  <!-- <view class="fs24 color-tip mt20 flex flex-between flex-middle">
				    <view
				      >¥<text class="fs32 mr10">{{ obj.supplierInfo.originalPrice }}</text
				      >门市价</view
				    >
				    <text class="look-btn fs24">查看详情</text>
				  </view> -->
				</view>
      </view>
			<view class="">
				<view class="flex flex-between prize-list mt10">
					<view class="prize-item mt10" v-for="(e, i) in obj.prizeList" :key="e.prizeName">
					  <view class="cell mt">
					    <view class="left">{{ e.prizeName }}</view>
					    <view class="right">x{{ e.prizeNum }}</view>
					  </view>
					</view>
				</view>
				<view class="state mt30 color-white">
					<view class="fs32">抽奖说明</view>
					<view class="flex flex-between align-items-start fs24 mt15">
						<view class="desc flex-1">
							{{obj.desc}}
						</view>
						<view class="look-more" @tap.stop="showStates">查看更多</view>
					</view>
				</view>
			</view>
    </view>

<!--    <view class="mb30 mt30 fw600 text-center" v-if="obj.state == 1"
      >{{ obj.luckType == 2 ? '开奖' : '截止' }}时间：{{
        $utils.dayjs(obj.endTime).format('YYYY-MM-DD HH:mm')
      }}</view
    > -->

    <view
      class="flex flex-center flex-middle p30 game-status mt30"
      v-if="obj.state == 2"
    >
      <view class="text-center game-over fs36 color-fff">已结束</view>
    </view>

    <view
      class="flex flex-center flex-middle p30 game-status mt30"
      v-if="obj.state == 1"
    >
			<view class="" v-if="!obj.isLuck">
				<view class="" v-if="obj.isHelp && !obj.userIsHelp && shareId && shareId != userId">
					<view class="text-center game-ing fs36 color-fff" @tap="help"
					  >给TA 助力</view
					>
				</view>
				<view class="" v-else>
					<view class="" v-if="obj.luckType == 1">
						<view class="text-center game-ing fs36 color-fff" @tap="drawHandle"
							>点击抽奖</view
						>
					</view>
					<view class="" v-if="obj.luckType == 2">
						<view class="text-center game-ing fs36 color-fff" @tap="drawHandle"
							>点击报名参与</view
						>
					</view>
				</view>
			</view>
			
			<view class="" v-if="obj.isLuck">
				<view class="" v-if="obj.isHelp">
					<view class="" v-if="!obj.userIsHelp && shareId && shareId != userId">
						<view class="text-center game-ing fs36 color-fff" @tap="help"
						  >给TA 助力</view
						>
					</view>
					<view class="" v-else>
						<button id="btn1" type="default" open-type="share" hover-class="none" class="text-center game-ing color-fff">
							<view class="">
								<view class="fs36">邀请好友</view>
								<view class="fs26">提高中奖成功率</view>
							</view>
						</button>
					</view>
				</view>
				
			  <view class="" v-if="!obj.isHelp && obj.luckType == 2">
			    <view class="text-center game-ing fs36 color-fff">已报名待开奖</view>
<!-- 					<view class="text-center game-ing fs36 color-fff">
						<view class=""></view>
					</view> -->
			  </view>
			</view>
    </view>

		<view
			class="mt30 mb30"
			v-if="(obj.state == 2 && obj.luckType == 2) || obj.luckType == 1"
		>
			<view class="" v-if="obj.isWin">
				<view class="fs36 color-tip text-center">恭喜，您抽中以下奖品</view>
				<gift
					:list="[obj.prizeItem.couponTemplate]"
					giftType="2"
					v-if="obj.prizeItem"
				></gift>
			</view>
			<view class="fs36 color-tip text-center" v-if="!obj.isWin && obj.isLuck">抱歉，未抽到奖！</view>
		</view>
		
		<view class="help-list mt20" v-if="obj.isHelp && obj.isLuck">
			<view class="fs28">给你助力的好友</view>
			<view class="help-item flex flex-between flex-middle mt40">
				<image
				  v-for="item in helpList"
				  :src="item.head"
				  class="mr10"
				  :key="item.head"
				></image>
				<view class="more-icon fs36" v-for="items in (4 - helpList.length)" :key="items">?</view>
				<view class="more-icon fs24" @tap="moreHelp">更多</view>
			</view>
		</view>

    <view
      class="p30 mt60 text-center participation-user"
    >
      <navigator
        :url="getUrl"
        hover-class="none"
        class="fs26"
        @tap="$eventRecord(214)"
      >
        <text class="color-tip mr10" v-if="obj.state == 1">活动进行中</text
        >已有{{ total }}人参与,查看>
      </navigator>
      <view class="flex flex-center mt20">
        <image
          v-for="item in participationList"
          :src="item.head"
          class="mr10"
          :key="item.userId"
        ></image>
        <text v-if="total > 6">...</text>
      </view>
    </view>

    <view v-if="!userType" class="flex flex-between flex-middle go-home mb40">
      <view class="head flex">
        <image
          src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/4aef8a6c-8245-4192-9b25-cadaf4d0a23f.png"
          class="rel"
        ></image>
      </view>
      <view class="ml30 pr80 flex-1">
				<view class="fs30">加入“赏金猎人” </view>
				<view class="fs24">一个分享，日常成果，轻松躺赚，月入过万！</view>
			</view>
      <image
        @tap="goBounty"
        src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/3b6238c5-cd66-429e-87dc-84fb0fe65350.png"
        class="look-btn mr30"
      ></image>
    </view>

    <view class="shop-special mt20" v-if="false">
      <view class="flex flex-center fs36 mb40">商家专场</view>
      <view
        class="item mt30"
        v-for="item in shopList"
        :key="item.goodsId"
        @tap="goDetail(item.goodsId)"
      >
        <view class="flex flex-baseline flex-between">
          <view class="color-tip fs32 left">¥{{ item.discountPrice }}</view>
          <view class="fs26 oneOmit right">{{
            item.goodsList[0].goodsName
          }}</view>
        </view>
        <view class="flex flex-baseline flex-between mt10">
          <view class="fs24 color-999 left"
            >门市价 ¥{{ item.originalPrice }}</view
          >
          <view class="right flex flex-middle flex-between">
            <view class="fs24 color-666"
              >剩余{{ item.goodsList[0].goodsPriceItem.stockNum }}</view
            >
            <text class="share-icon fs22"
              >分享赚{{ item.goodsList[0].goodsPriceItem.shareRebate }}</text
            >
          </view>
        </view>
      </view>
      <view class="fs24 color-666 more mt30" @tap="goSuperill">查看更多></view>
    </view>

    <view class="gift-list p30">
      <view class="flex flex-between flex-middle fs28">
        <view class="flex flex-middle">
          <image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/b031e00d-a003-4d74-b62e-1ce54e86c950.png"
            class="gift-icon mr20"
          ></image>
          <text>抽奖的奖品</text>
        </view>
        <view class="draw-state color-666" @tap="showStates"
          >抽奖说明</view
        >
      </view>
      <gift :list="conponList" v-if="conponList" giftType="1"></gift>
    </view>

    <view class="foot fixed flex flex-between flex-middle">
      <view
        class="bg-black color-fff"
        @tap="jumpDrawList"
        v-if="total > 0 || obj.state == 2"
        >获奖名单</view
      >
      <button
				id="btn2"
        class="bg-black color-fff"
        open-type="share"
        v-if="obj.state == 1"
        @tap="$eventRecord(215)"
      >
        分享好友
      </button>
    </view>

    <view class="shadow" v-if="showState">
      <view class="shadow-body p30">
        <view class="text-center pb30"
          ><image
            src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/4824c3d4-3ce0-4e6e-8dd6-d2f43682779f.png"
            class="help-icon"
          ></image
        ></view>
        <view class="fs36 fw600 text-center">说明</view>
        <view class="fs28 color-999 mt30">
          {{ obj.desc || '暂无说明' }}
        </view>
        <view class="fs38 fw600 text-center close" @tap="showState = false"
          >我知道了</view
        >
      </view>
    </view>
    <view class="shadow" v-if="isLogin">
      <view class="shadow-body">
        <view class="title fs36 fw600">温馨提示</view>
        <view class="content fs28 color-999">您还未登录,现在去登录?</view>
        <view class="confirm" @tap="confirm">确定</view>
      </view>
    </view>
  </view>
</template>

<script>
import gameServe from '../../../service/game';
import Gift from '../../components/gift';
import app from '../../../App.vue';
import ProductService from '../../../service/product';

export default {
  components: { Gift },
  data() {
    return {
      id: '',
			shareId:"",
      obj: {},
      participationList: [],
      total: 0,
      showState: false,
      templateIds: '',
      conponList: [],
      isLoading: false,
      isLogin: false,
      isAgain: false,
      shopList: [],
			prizeList:[],
			userId: uni.getStorageSync('userInfo').userId,
			helpList:[]
    };
  },
  onLoad(option) {
    const seen = wx.getLaunchOptionsSync();
    this.id = parseInt(option.id);
		this.shareId = parseInt(option.shareId);
		if (seen.scene == 1007 || seen.scene == 1008) {
		  this.$eventRecord(218);
		}
		this.init();
		this.getParticipationUser();
  },
  onShareAppMessage(e) {
		if(e.from == 'button' && e.target.id === 'btn1'){
			return {
			  title: `帮我助个力，给你白嫖剧本杀/密逃/酒吧免单`,
			  path: `/pagesB/luckDraw/drawDetail/drawDetail?id=${this.id}&shareId=${this.userId}`,
			  imageUrl:
			    'https://image-1306191496.cos.ap-nanjing.myqcloud.com/c2f6213c-a6ca-4238-acca-a99c624b53c0.png',
			};
		}else{
			return {
			  title: `【${this.obj.supplierInfo.companyName}】福利大放送，抽奖送免单!`,
			  path: `/pagesB/luckDraw/drawDetail/drawDetail?id=${this.id}`,
			  imageUrl:
			    'https://image-1306191496.cos.ap-nanjing.myqcloud.com/a0501d7f-ff7e-44e9-a333-5b5d4a7b4302.png',
			};
		}
  },
  computed:{
    getUrl(){
      return this.obj.isHelp?`/pagesB/powerList/powerList?id=${this.id}`:`/pagesB/luckDraw/drawUser/drawUser?id=${this.id}`
    },
    userType(){
      return this.$store.state.user.identityType;
    }
  },
  mounted(){
		if (uni.getStorageSync('userInfo')){
			this.$store.commit('setIdentityType')
		}
  },
  methods: {
		callSupplier(){
			uni.makePhoneCall({
			  phoneNumber: String(this.obj.supplierInfo.companyPhone)
			});
		},
		goBounty(){
			uni.navigateTo({
				url:"/pagesB/commodity/bounty"
			})
		},
		moreHelp(){
			uni.navigateTo({
				url:`/pagesB/powerList/powerList?id=${this.id}`
			})
		},
    goSuperill() {
      uni.navigateTo({
        url: `/pagesB/commodity/index?supplierId=${this.obj.supplierId}`,
      });
    },
    goDetail(id) {
      uni.navigateTo({
        url: `/pagesB/commodity/sInfo?id=${id}`,
      });
    },
    goHome() {
      uni.redirectTo({
        url: '/pages/index/index',
      });
    },
    confirm() {
      this.$authorization();
      this.isLogin = false;
    },
    showStates() {
      this.$eventRecord(216);
      this.showState = true;
    },
    jump(id) {
      this.$eventRecord(217);
      uni.navigateTo({
        url: `/pagesA/businessDetail/index?supplierId=${id}`,
      });
    },
    init() {
      gameServe.getDrawDetail({ id: this.id,shareUserId:this.shareId }).then((res) => {
        const arr = [];
        res.prizeList.forEach((item) => {
          arr.push(item.prizeId);
        });
        this.obj = res;
        this.templateIds = arr.join(',')
        this.isLoading = false;
        this.getConpon();
        this.getSeckillList();
				if(res.isHelp){
					this.getMeHelpUser()
				}
      });
    },
		help(){
			if(!this.isLoading){
				this.isLoading = true
				gameServe.assistance({luckDrawId:this.id,shareUserId:this.shareId,helpType:2}).then(res => {
					if(!res){
						this.isLoading = false;
						this.init();
						this.$toast("助力成功");
					}
				}).catch(err => this.isLoading = false)
			}
		},
		getMeHelpUser(){
			gameServe.helpMeUser({id:this.id,pageNum:1,pageSize:4}).then(res => {
				this.helpList = res.list
			})
		},
    async getSeckillList() {
      const params = {
        supplierId: this.obj.supplierId,
        pageNum: 1,
        pageSize: 2,
      };
      if (app.globalData.latitude) {
        params.lat = app.globalData.latitude;
        params.lng = app.globalData.longitude;
      } else {
        const data = await app.methods.getLocation();
				const latitude = data.latitude || 28.22778
				const longitude = data.longitude || 112.93886
				app.globalData.latitude = latitude;
				app.globalData.longitude = longitude;
        params.lat = latitude;
        params.lng = longitude;
      }
      ProductService.seckillList(params).then((res) => {
        this.shopList = res.list;
      });
    },
    drawHandle() {
      this.$eventRecord(213);
      if (!uni.getStorageSync('userInfo').head) {
        this.$toast('请先授权手机号和头像!');
        setTimeout(() => {
          uni.navigateTo({
            url: `/pagesA/user/login?isOnload=${true}`,
          });
        },1500);
        return;
      }
      const that = this;
      if (!that.isLoading) {
        uni.requestSubscribeMessage({
          tmplIds: ['pEu7pUIxm_L54D8UmRf-kA-h_pV5xB7wK2o-C7L_264'],
          complete(res) {
            that.isLoading = true;
            uni.showLoading();
            gameServe.draw({ id: that.id }).then((data) => {
              uni.hideLoading();
              that.init();
              that.getParticipationUser();
            });
          },
        });
      }
    },
    getParticipationUser() {
      gameServe
        .getDrawUser({ pageNum: 1, pageSize: 6, id: this.id })
        .then((res) => {
          this.total = res.total;
          this.participationList = res.list;
        });
    },
    getConpon() {
      gameServe.getconponList({ templateIds: this.templateIds }).then((res) => {
        this.conponList = res;
      });
    },
    jumpDrawList() {
      uni.navigateTo({
        url: `/pagesB/luckDraw/drawUser/drawUser?navId=2&id=${this.id}`,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.supplier-info {
  background: #21252a;
	color:#fff;
	align-items: flex-start;
  .cover {
    width: 200upx;
    height: 200upx;
    border-radius: 12upx;
  }
  // .marks {
  //   padding: 4upx 12upx;
  //   border-radius: 20upx;
  //   background: #fbedec;
  //   color: #e92f29;
  // }
  // .address {
  //   image {
  //     width: 28upx;
  //     height: 28upx;
  //   }
  //   view {
  //     width: 400upx;
  //   }
  // }
  .call-btn {
    padding: 6upx 24upx;
    border-radius: 30upx;
    background: url(https://image-1306191496.cos.ap-nanjing.myqcloud.com/effc74c2-64dc-4d68-b3da-7a4d6c562a93.png)
                no-repeat center;
    background-size: 100% 100%;
    width:164upx;
    height: 52upx;
    color: #21252a;
		display: inline-block;
  }
	.canyu {
	  display: flex;
	  margin-top: 18upx;
	  margin-left: 8upx;
	  .number {
	    font-size: 24upx;
	    line-height: 38upx;
	  }
	  .draw {
	    margin-left: 12upx;
	    width: 130upx;
	    height: 40upx;
	    border-radius: 4px;
	    text-align: center;
	    border: 2upx solid #db2a15;
	    font-size: 24upx;
	    color: #db2a15;
	    line-height: 38upx;
	  }
	}
	.prize-list{
		flex-wrap: wrap;
		.prize-item {
			width: 48%;
		  .cell {
		    width: 100%;
		    background: url(https://image-1306191496.cos.ap-nanjing.myqcloud.com/0d5a75f1-061d-48f4-9c4e-c579204491a4.png)
		      no-repeat center;
		    background-size: 100% 100%;
		    border-radius: 16upx;
		    font-size: 28upx;
		    font-weight: 500;
		    color: #ff4f3a;
		    line-height: 36upx;
		    padding: 12upx 0;
		    display: flex;
		    justify-content: space-between;
		    align-items: flex-end;
				.left{
					width: 73%;
					padding-left:40upx;
				}
				.right{
					width: 27%;
					text-align: center;
				}
		  }
		  .mt {
		    margin-bottom: 12upx;
		  }
		}
	}
	.state{
		.desc{
			max-height:68upx;
			padding-right: 40upx;
			overflow: hidden;
		}
		.look-more{
			padding:6upx 16upx;
			border-radius: 30upx;
			border:1px solid #fff;
		}
	}
}
.help-list{
	padding:0 70upx;
	text-align: center;
	image,.more-icon{
		width: 100upx;
		height:100upx;
		border-radius: 50%;
	}
	.more-icon{
		background: #E8E8E8;
		text-align: center;
		line-height: 100upx;
		color:#666;
	}
}
.game-status {
  min-height: 200upx;
}
.game-over,
.game-ing {
  height: 260upx;
  width: 260upx;
  background: #999;
  border-radius: 50%;
  padding: 40upx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}
// .game{
//   margin-top:60upx !important;
//   height: 108upx !important;
//   width: 316upx !important;
//   border-radius: 25px !important;
// }
.game-ing {
  background: #e92f29;
}
button.game-ing{
	color:#fff;
	line-height: 1.5;
}
.win-title {
  text-align: center;
  line-height: 200upx;
}
.participation-user {
  image {
    width: 60upx;
    height: 60upx;
    border-radius: 50%;
  }
}
.gift-list {
  padding-bottom: 140upx;
  .gift-icon {
    width: 60upx;
    height: 60upx;
  }
  .draw-state {
    padding: 6upx 16upx;
    border-radius: 30upx;
		background: #ddd;
  }
}
.foot {
  bottom: 0;
  left: 0;
  right: 0;
  height: 120upx;
  background: #fff;
  view,
  button {
    height: 90upx;
    line-height: 90upx;
    text-align: center;
    flex: 1;
    margin: 0 20upx;
    border-radius: 60upx;
    border: 1px solid #000;
    padding: 0;
    font-size: 36upx;
  }
}
.shadow {
  text-align: center;
  .title {
    line-height: 100upx;
  }
  .content {
    margin-top: 40upx;
    margin-bottom: 50upx;
  }
  .confirm {
    line-height: 80upx;
    border-top: 1px solid #ccc;
  }
}
.go-home {
  width: calc(100% - 40upx);
  margin: 30upx auto;
  background: linear-gradient(to right, #ffadad, #ffecec);
  height: 150upx;
  border-radius: 80upx;
  padding: 0 20upx;
  color: #812216;
  .head {
    image {
      width: 112upx;
      height: 112upx;
      border-radius: 50%;
    }
    .ml-10 {
      margin-left: -10upx;
    }
  }
	.pr80{
		padding-right:80upx;
	}
  .look-btn {
    width: 170upx;
    height: 64upx;
  }
}
.shop-special {
  width: calc(100% - 40upx);
  margin: 30upx auto;
  background: #fff;
  border-radius: 16upx;
  padding: 40upx 40upx 1px 40upx;
  box-shadow: 0 0 32upx 6upx #eee;
  .left {
    width: 25%;
    text-align: center;
  }
  .right {
    width: 65%;
  }
  .share-icon {
    color: #ff4f3a;
    border: 1px solid #ff4f3a;
    padding: 2upx 10upx;
    border-radius: 4upx;
  }
  .more {
    width: 170upx;
    height: 40upx;
    border-radius: 20upx;
    background: #eee;
    margin: 20upx auto;
    text-align: center;
    line-height: 40upx;
  }
}
</style>
