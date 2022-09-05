<template>
  <view class="page-bg" v-if="detail">
    <comHead changeColor :isHome="isHome">{{
      detail.mine ? "我创建的局" : "组局详情"
    }}</comHead>

    <view class="activity-image">
			<swiper :indicator-dots="false" :autoplay="false" :style="{height:imgHeight + 'px'}">
				<swiper-item v-for="(item,index) in detail.covers" :key="index">
					<image :src="item" mode="widthFix" style="width:100%" @load="imgLoad"></image>
				</swiper-item>
			</swiper>
    </view>
		
		<view class="main">
			<view class="activity-card">
				<view
					class="activityLabel"
					v-if="detail.activityId === 3"
				>
					<!-- <image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/36bdea81-61dc-4b07-8b0b-cad588ae9e32.png"></image>
					【活动局】五一商圈剧本全场30元起 > -->
				</view>
				<view
					class="activityLabel getCoupon"
					v-else
				>
					<!-- 【领券】全场合作商家立省65元  > -->
				</view>
				<view class="activity-label">
					<text class="activity-card-label">{{ detail.typeName }}</text>
					<text class="activity-card-label" v-if="detail.mine">我是局主</text>
				</view>
				<view class="bar-card-info">
					<!-- 当前组局状态 -->
					<view class="slices">
						<image
							src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/1e82635e-3b61-4bdf-9ce0-0ffbfcf06fd2.png"
							v-if="detail.state === '1'"
						></image>
						<image
							src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/172d97ec-8fdd-47a2-8fd6-ea6f962c5be1.png"
							v-else-if="detail.state === '2'"
						></image>
						<image
							src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/31d95971-a0a5-43e8-8823-3dc13c295ff0.png"
							v-else-if="detail.state === '3'"
						></image>
					</view>
					<view class="place">
						<view class="place-name">{{ detail.title }}</view>
					</view>
					<view class="date clearfix">
						<view class="date-info"
							>{{ $formatGameTime(detail.gameStartTime) }} 开始</view
						>
						<button plain open-type="share" class="share-btn">
							<image
								class="share-icon"
								src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-share.png"
							></image>
						</button>
					</view>
					<view class="adress" @click="openLocation(1)" v-if="!detail.supplier || !detail.transcript">
						<image
							class="map-icon"
							src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
						></image>
						{{ detail.address }}
					</view>
					<view class="type">
						<view class="wayInfo">
							{{detail.payType === 1 ? "局主请客" : $constants.PAY_TYPES.find((v) => v.value === detail.payType).name + ` | ¥${detail.price}/人`}}
							<text class="incomingWay">{{ joinType[detail.joinType - 1].name }}</text>
							<view class="v2">{{$constants.COMPLETE_TYPES.find((v) => v.value === detail.complete).name}}</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="merchant" v-if="detail.supplier && detail.transcript">
				<view class="merchant-info">
					<view class="info">
						<view class="merchant-name" @tap="jumpMerchantDetail(detail.supplier.supplierId)">
							<text>{{detail.supplier.companyName}}</text>
							<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png" class="arrow"></image>
						</view>
						<view class="adress" @tap="openLocation(2)">
							<image
								class="map-icon"
								src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/card-place-icon.png"
							></image>
							{{ detail.supplier.companyAddress }}
						</view>
					</view>
					<image src="https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/call-icon.png" class="call" @click.stop="call(detail.supplier.companyPhone)"></image>
				</view>
				<view class="play" @tap="jumpDetail">
					<image :src="detail.transcript.pic"></image>
					<view class="play-info">
						<view class="play-name">{{detail.transcript.name}}</view>
						<view class="mark">
							<text v-if="detail.transcript.type">{{detail.transcript.type == 1 ? '盒装' : detail.transcript.type == 2 ? '城限' : detail.transcript.type == 3 ? '独家' : '实景'}} | </text>
							<text v-if="detail.transcript.man">{{detail.transcript.man}}男{{detail.transcript.woman}}女 | </text>
							<text v-if="detail.transcript.people_set">{{detail.transcript.people_set}}</text>
							<text v-if="detail.transcript.people">{{detail.transcript.people}}人 | </text>
							<text v-if="detail.transcript.duration">{{detail.transcript.duration}}h | </text>
							<text v-if="detail.transcript.difficulty">{{detail.transcript.difficulty}}</text>
						</view>
						<view class="play-price" v-if="detail.price">
							<text>¥{{detail.price}}/</text>
							<text class="unit">人</text>
							<text class="price-state">门市价</text>
						</view>
						<view :class="[{'mt10':!detail.price},'play-intro']">{{detail.transcript.brief}}</view>
					</view>
				</view>
			</view>

			<view class="user-wrap" v-if="detail.mine">
				<view class="user-view">
					<view class="user-title">局管理</view>
					<view class="user-list" @tap="jumpAdminpage">
						<view
							class="user-item items"
							:class="[member.sex !== 1 ? 'girl' : 'boy']"
							v-show="idx < 3"
							v-for="(member, idx) in detail.members.filter((v) => v.state !== 3)"
							:key="idx"
						>
							<avatar :avatarUrl="member.head"></avatar>
						</view>
						<view class="user-item more-item" v-if="joinMembers.length > 3">
							+{{ joinMembers.length - 3 }}
							<text class="dot"></text>
						</view
						>
					</view>
					<image src="https://image-1306191496.cos.ap-nanjing.myqcloud.com/917de11a-2b57-48a4-868e-e8e4783bd850.png" class="more"></image>
				</view>
			</view>

			<view class="tab-bg">
				<view class="tab-bg-l">
					<view class="v1">已入局 {{ joinMembers.length }} 人</view>
					<view class="v2"
						>期待
						{{
							detail.minPeople == detail.maxPeople
								? detail.minPeople
								: detail.minPeople + "~" + detail.maxPeople
						}}
						人
					</view>
				</view>
			</view>

			<!-- 已入局人员列表 -->
			<game-player-item
				v-for="(member, i) in joinMembers"
				:key="i"
				:member="member"
        :detail="detail"
				:showContact="detail.mine && member.userId !== myUserId"
			></game-player-item>
		</view>
    <!-- 底部操作 -->
    <!-- 报名未通过或者被踢，就不能再申请了 -->
    <view class="error-tip" v-if="myMemberInfo.state === 4">
      抱歉，审核未通过，可申请重新加入</view
    >
    <view class="error-tip" v-if="myMemberInfo.state === 5">
      抱歉，您被群主踢出，可申请重新加入</view
    >
    <view
      class="btm-bg error-tip"
      v-if="detail.complete == 3 || detail.complete == 4"
    >
      本局报名已结束，看看别的局呗
    </view>
    <view class="btm-bg" v-else>
      <view class="flex-c">
        <!-- 自己是群主才能管理 -->
        <button class="yq-btn" @click="dialogShow('退出')" v-if="showQuBtn()">
          退出
        </button>
        <view class="gl-btn" v-if="detail.mine" @click="updateGroup">编辑</view>
        <button class="yq-btn" @click="goGroupChat" v-if="detail.mine">
          群聊
        </button>
        <button
          class="yq-btn"
          open-type="share"
          v-else
          @click="this.$eventRecord(58)"
        >
          邀请
        </button>

        <!-- 最后一个按钮 - 如果我是群主 -->
        <template v-if="detail.mine">
          <button class="yq-btn black-c" @click="dialogShow('完成组局')">
            完成组局
          </button>
        </template>
        <!-- 我不是群主 -->
        <template v-else>
          <!-- 没有申请过、自己退出的，可以再次报名 -->
          <view
            v-if="!myMemberInfo || myMemberInfo.state === 3"
            class="yq-btn black-c"
            @click="dialogShow('我要报名')"
          >
            我要报名</view
          >
          <view
            v-else-if="myMemberInfo.state === 1"
            class="yq-btn black-c"
            @click="goGroupChat"
            >进入群聊</view
          >
          <view v-else-if="myMemberInfo.state === 2" class="yq-btn white-c"
            >已报名，待群主审核</view
          >
          <view
            v-else-if="myMemberInfo.state === 4 || myMemberInfo.state === 5"
            class="yq-btn white-c"
            @click="dialogShow('我要报名')"
            >重新报名</view
          >
        </template>
      </view>
    </view>
    <Dialog
      @confirm="confirm"
      v-if="dialog"
      :dislog="dialog"
      :text="text"
      :title="title"
      :bool="bool"
      @close="close"
    >
    </Dialog>
    <!-- 入局成功的弹窗 -->
    <game-created
      class="game-created"
      v-show="createdDialog"
      :percent="percent"
      :subscription="subscription"
      :head="head"
      :title="gameCreatedTtile"
    >
      <view>
        <view class="btn-box2">
          <view @click="getGameDetail" class="btn1">知道了</view>
          <view @click="jumpUserEdit" class="btn2">快速完善</view>
        </view>
      </view>
    </game-created>
    <view
      v-show="createdDialog"
      @click="createdDialog = false"
      class="shadow"
    ></view>
    <!-- 输入密码框 -->
    <view
      v-if="gamePasswordDialog"
      class="shadow"
      @click="gamePasswordDialog = false"
    ></view>
    <gamePassword
      v-if="gamePasswordDialog"
      class="gamePassword"
      :detail="detail"
      @changePassword="changePassword"
      :isPasswoedBox.sync="isPasswoedBox"
    ></gamePassword>
		
		<share-two
		  v-if="shareTwoDialog"
		  :codeUrl="codeUrl"
			:bgImgUrl="bgImgUrl"
		  @cancel="shareTwoDialog = false"
		/>
		
		<canvas canvas-id="cardCover" class="cardCanvas"></canvas>
  </view>
</template>

<script>
import gamePassword from "./game-password.vue";
import gameCreated from "../components/game-created.vue";
import { config } from "@/framework";
import comHead from "../../components/com-head.vue";
import gameService from "../../service/game.js";
import GamePlayerItem from "../components/game-player-item.vue";
import Dialog from "../components/Dialog.vue";
import IM from "../../pages/im/lib";
import UserService from "@/service/user.js";
import shareTwo from "../components/Share-two.vue";
import basic from "../../service/basic";
import { cosUploadFiles } from "../cos/utils";
export default {
  components: {
    comHead,
    GamePlayerItem,
    Dialog,
    gameCreated,
    gamePassword,
		shareTwo
  },
  events: {
    memberChanged: "getGameDetail", // 成员有变动的时候，刷新页面数据
  },
  data() {
    return {
			imgHeight:0,
			codeUrl: "",
			bgImgUrl:"https://image-1306191496.cos.ap-nanjing.myqcloud.com/9bde5da3-140e-4a01-b305-86e170860780.png",
			isShare:false,
			shareTwoDialog:false,
      bool: false,
      isPasswoedBox: false,
      detail: null,
      me: false,
      dialog: false, //  是否显示dialog组件
      text: "", // 根据用户操作，提示对应的文字
      title: "",
      msg: "", // 用户正在进行的操作
      // 展示入局成功弹窗
      createdDialog: false,
      subscription: "",
      head: "",
      gamePasswordDialog: false,
      percent: 0,
      gameCreatedTtile: "",
      // 入局方式
      joinType: [
        {
          name: "申请入局",
        },
        {
          name: "直接入局",
        },
        {
          name: "密码入局",
        },
      ],
      scene: "",
      activityId: "", // 活动局
			shareImg:'',//商家信息
			isHome:false
    };
  },
  onLoad(o) {
    this.$eventRecord(205)
		if (o.gameId) {
			this.gameId = o.gameId || o.scene; // 兼容订阅消息
		}
		if (o.scene) {
			this.gameId = o.scene.replace("gameId", "")
			this.isHome = true
		}
		if(o.isHome){
			this.isHome = o.isHome
		}
		if(!uni.getStorageSync('userInfo').head){
			uni.navigateTo({
				url:`/pagesA/user/login?isOnload=${true}`
			})
		}else{
			// 服务端消息
			const pages = getCurrentPages();
			const url = pages[pages.length - 1].$page.fullPath;
			const trackingID = this.$getParam(url, "trackingID");
			if (trackingID) this.$eventRecord(trackingID);
			
			this.getGameDetail();
			let res = wx.getLaunchOptionsSync();
			this.scene = String(res.scene);
			if(o.shareUserId && this.myUserId){
				gameService.bindRelation({parentId:o.shareUserId,parentType:1})
			}
		}
  },
  onShow() {
    // 通过分享直接进入页面 会存在无userid的情况
    if (!this.myUserId) {
    }
    let res = wx.getLaunchOptionsSync();
    res.scene = String(res.scene);
    this.$nextTick(() => {
      setTimeout(() => {
        if (res.scene === "1043") {
          // 埋点
          this.$eventRecord(89);
        } else if (res.scene === "1157") {
          // 埋点
          this.$eventRecord(82);
        } else {
          // 埋点
          this.$eventRecord(71);
        }
      }, 3000);
    });
  },

  onPullDownRefresh() {
    this.getGameDetail()
      .then(() => {
        uni.stopPullDownRefresh();
      })
      .catch(() => {
        uni.stopPullDownRefresh();
      });
  },
  onShareAppMessage() {
    this.$eventRecord(196)
		uni.showLoading()
		const promise = new Promise(resolve => {
			setTimeout(() => {
				uni.hideLoading()
				resolve({
					imageUrl:this.shareImg,
					title: this.detail.title,
					path: `/pagesA/game/detail?gameId=${this.detail.gameId}&shareUserId=${this.myUserId}&isHome=1`,
				})
			}, 1000)
		})
    return {
      title: this.detail.title,
      path: `/pagesA/game/detail?gameId=${this.detail.gameId}&shareUserId=${this.myUserId}&isHome=1`,
      imageUrl: this.detail.covers[0],
			promise
    };
  },
  onPageScroll(e) {
    let { scrollTop = 0 } = e;
    uni.$emit("onPageScroll", scrollTop);
  },

  methods: {
		imgLoad(e){
			const sysInfo = uni.getSystemInfoSync()
			this.imgHeight = e.detail.height * (sysInfo.screenWidth/ e.detail.width)
		},
		jumpAdminpage(){
			const isAuth = this.$authorization()
			if(!isAuth) return
			uni.navigateTo({
				url:`/pagesA/game/player?gameId=${this.gameId}&min=${this.detail.minPeople}&max=${this.detail.maxPeople}`
			})
		},
		jumpDetail(){
			const isAuth = this.$authorization()
			if(!isAuth) return
      this.$eventRecord(199)
			uni.navigateTo({
				url:`/pagesA/businessDetail/sInfo?typeId=${this.detail.typeId}&activityId=${this.detail.activityId}&id=${this.detail.transcript.id}`
			})
		},
		async createCardImg(){
			const that = this
			let d = await uni.downloadFile({url:this.detail.covers[0]})
			const gbImg = d[1].tempFilePath
			const bgimgInfo = await uni.getImageInfo({src:gbImg})
			const imgInfo = bgimgInfo[1]
			let sh = 200 * (imgInfo.width / 500)
			let sy = (imgInfo.height - sh) / 2
			const ctx = uni.createCanvasContext('cardCover')
			const price = this.detail.payType === 1 ? "局主请客" : this.$constants.PAY_TYPES.find((v) => v.value === this.detail.payType).name + ` | ¥${this.detail.price}/人`
			ctx.setFillStyle('#fff')
			ctx.fillRect(0,0,500,400)
			ctx.drawImage(gbImg,0,sy,imgInfo.width,sh,0,0,500,200)
			ctx.font = "normal bold 18px sans-serif"
			ctx.fillStyle = '#000'
			ctx.fillText(this.detail.title,10,230)
			ctx.setFillStyle('#000')
			ctx.fillRect(440,215,40,20)
			ctx.font = "normal 14px sans-serif"
			ctx.fillStyle = "#fff"
			ctx.fillText('分享',445,230)
			ctx.fillStyle = '#000'
			ctx.fillText(this.$formatGameTime(this.detail.gameStartTime) + '开始',10,255)
			ctx.fillText(price,10,275)
			ctx.fillText(this.detail.address,10,295)
			const len = this.detail.members.length < 5 ? this.detail.members.length : 5
			for(let i = 0;i < len;i++){
				ctx.save()
				ctx.beginPath()
				let d = await uni.downloadFile({url:this.detail.members[i].head})
				const head = d[1].tempFilePath
				await ctx.arc(((i + 1) * 10) + i * 80 + 40,350,40,0,Math.PI * 2)
				await ctx.clip()
				await ctx.drawImage(head,((i + 1) * 10) + i * 80,310,80,80)
				await ctx.restore()
			}
			// ctx.draw()
			this.savaNotice(ctx)
		},
		//保存画布图片
		savaNotice(ctx){
		  const that = this
			const timeId = setTimeout(() => {
				//生成图片
				uni.canvasToTempFilePath({
					canvasId: 'cardCover',
					fileType: 'png',
					quality: 1,
					success(res) {
						console.log(res.tempFilePath)
						that.shareImg = res.tempFilePath
					},
					fail(){
						uni.hideLoading()
					}
				})
			}, 1000)
			ctx.draw(true, timeId)
		},
		getCodeUrl() {
		  let parmas = {
		    scene: `gameId${this.gameId}`,
		    isHyaline: false,
		    width: 300,
		    autoColor:false,
		    lineColor: { r: "0", g: "0", b: "0" },
		    page: "pagesA/game/detail",
		  };
		  basic.getMiniProgramQrcodeApi(parmas).then((res) => {
		    this.codeUrl = "data:image/png;base64," + res;
				this.shareTwoDialog = true
		  });
		},
    goDiscount(e) {
			if(e == 1){
				// 埋点
				this.$eventRecord(195);
        const { members } = this.detail
        const isFlag = members.some(item => item.userId === this.myUserId)
        if(isFlag) { 
          uni.navigateTo({
            url: `/pagesB/inviteActivity/discountCenter?activityId=${this.detail.activityId}&gameId=${this.detail.gameId}`,
          });
        } else { 
          uni.navigateTo({
            url: `/pagesA/barActivity/discountCenter?activityId=${this.detail.activityId}&gameId=${this.detail.gameId}`,
          });
        }
			}else{
				uni.navigateTo({
				  url: "/pagesA/vouchers/buy",
				});
			}
    },
		jumpMerchantDetail(id){
			const isAuth = this.$authorization()
			if(!isAuth) return
      this.$eventRecord(197)
			if(id){
				uni.navigateTo({
				  url: "/pagesB/commodity/index?supplierId=" + id,
				})
			}
		},
		call(phone){
      this.$eventRecord(198)
			uni.makePhoneCall({
			  phoneNumber: phone,
			});
		},
    // dialog点击确认的回调
    confirm() {
      if (this.msg === "我要报名") {
        this.joinGame();
      } else if (this.msg === "完成组局") {
        this.finishGame();
      } else if (this.msg === "退出") {
        this.quit();
      }
    },
    updateGroup() {
			const isAuth = this.$authorization()
			if(!isAuth) return
      uni.navigateTo({
        url: "/pagesA/create/index?gameId=" + this.detail.gameId,
      });
      // 埋点
      this.$eventRecord(49);
    },
    // 输入密码
    changePassword(val) {
      if (val.length === 4) {
        this.joinGame(val);
      }
    },
    // 跳转到完善用户资料
    jumpUserEdit() {
      uni.redirectTo({
        url: "/pagesA/user/edit",
      });
    },
    // 是否展示退出按钮
    showQuBtn() {
      let info = this.detail.members.find((n) => n.userId === this.myUserId);
      if (info && info.state === 1) {
        return true;
      }
      return false;
    },
    // 退出组局
    async quit() {
      try {
        // 如果是群主并且是最后以一个人，退出则提示组局解散
        if (this.detail.members.length === 1 && this.detail.mine) {
          this.$toast("已解散", "success");
        } else {
          this.$toast("退出成功", "success");
        }
        await gameService.quitGroupBureau(this.gameId);
        this.dialog = false;
        // 退出后将用户状态改回成3
        let info = this.detail.members.find((n) => n.userId === this.myUserId);
        info.state = 3;
        this.getGameDetail();
      } catch (err) {
        this.dialog = false;
      }
    },
    async dialogShow(value) {
      const isAuth = this.$authorization()
      if(!isAuth) return
      if (value === "我要报名") {
        this.$eventRecord(253)
        if (this.detail.joinType === 3) {
          this.gamePasswordDialog = true;

          return;
        } else {
          this.text = "是否加入该组局？";
          this.title = "温馨提示";
          // 埋点
          this.$eventRecord(59);
        }
      } else if (value === "完成组局") {
        this.text = "请确认组员是否都已到达现场";
        this.title = "温馨提示";
        // 埋点
        this.$eventRecord(51);
      } else {
        // 如果是群主并且是最后以一个人，退出则提示组局解散
        if (this.detail.members.length === 1 && this.detail.mine) {
          this.title = "温馨提示";
          this.text = "退出后局将解散，确定退出吗？";
        } else {
          this.title = "温馨提示";
          this.text = "确定退出该组局吗？";
        }
      }
      this.dialog = true;
      this.msg = value;
    },
    // 关闭dialog面板
    close(bool) {
      this.dialog = bool;
    },
    openLocation(i) {
      // 打开地图显示位置
			uni.openLocation({
			  latitude: Number(this.detail.lat),
			  longitude: Number(this.detail.lng),
			  name: this.detail.address,
			});
    },
    getGameDetail() {
      this.createdDialog = false;
      this.gamePasswordDialog = false;
      return gameService.gameDetail(this.gameId,uni.getStorageSync('userInfo').userId).then((res) => {
        //  (res)
        // 群主默认第一
        res.members.forEach((v, i) => {
          if (v.identity === 1) {
            //  (v)
            res.members.splice(i, 1);
            res.members.unshift(v);
          }
          if (res.members[0].userId === this.myUserId) {
            res.mine = true;
          }
          // 头像处理
          if (v.head && v.head.startsWith("https://image-1306191496")) {
            v.head += "?imageView2/1/w/112/h/112/q/80/webp";
          }
        });
        // 追加当前组局状态
        if (res.minPeople - 1 === res.members.length) {
          res.status = "1"; // 还差一人
        } else if (res.minPeople <= res.members.length) {
          res.status = "2"; // 达到最低人数
        } else if (res.maxPeople === res.members.length) {
          res.status = "3"; //  达到最大人数
        }
        res.cover = this.$pictureUrl(res.cover, 750, 294);
        if (res.activityId) {
          this.$eventRecord(119);
        }
				if(res.typeName == '密室'){
					res.transcript = res.secretEscape
				}else{
					res.transcript = res.script
				}
				
        this.detail = res
				console.log(this.detail)
				this.createCardImg()
      });
    },
    goGroupChat() {
			const isAuth = this.$authorization()
			if(!isAuth) return
      // 打开群聊
      IM.chatInGroup(this.detail.groupId, this.detail.gameStartTime);
      // 埋点
      this.$eventRecord(50);
    },
    joinGame(password) {
      //(this.gameId);
      // 申请入局
      gameService
        .joinGame({
          gameId: this.gameId,
          password: password,
          userId: this.myUserId,
        })
        .then((res) => {
          this.gamePasswordDialog = false;
          // 获取用户信息
          this.dialog = false;
          UserService.queryUserInfo().then((res) => {
            // 如果用户没有关注公众号 或者用户资料不足90% 展示引导页

            if (res.subscription !== 1 || res.rate < 90) {
              (this.subscription = res.subscription), (this.head = res.head);
              this.percent = res.rate;
              this.gameCreatedTtile =
                this.detail.joinType === 2 || this.detail.joinType === 3
                  ? "加入成功"
                  : "已报名，待局主大人审核";
              setTimeout(() => {
                this.createdDialog = true;
              }, 100);
            } else {
              let msg = {
                2: "加入成功",
                1: "已提交申请",
              }[this.detail.joinType];
              this.$toast(msg, "success");
              this.getGameDetail();
            }
          });
        })
        .catch((err) => {
          this.close();
        });
    },
    viewOthers() {
      // 看看别的局，直接跳首页好了
      uni.reLaunch({
        url: "/pages/index/index",
      });
    },
    goPlayerList() {
      // 跳成员管理
      if (!this.detail.mine) return;
      uni.navigateTo({
        url: `/pagesA/game/player?gameId=${this.gameId}&min=${
          this.detail.minPeople
        }&max=${this.detail.maxPeople}&members=${JSON.stringify(
          this.detail.members
        )}`,
      });
      // 埋点
      this.$eventRecord(46);
    },
    // 完成组局
    finishGame() {
      this.dialog = false;
      if (this.detail.minPeople > this.detail.members.length) {
        this.$toast(
          `人数少于${this.detail.minPeople}人无法结束,如需关闭请退出该局`
        );
      } else {
        gameService.finish(this.gameId).then(() => {
          this.$toast("组局已完成", "success");
          this.getGameDetail();
          if (this.detail.activityId === 2) {
            this.dialog = true;
            this.title = "温馨提示";
            this.bool = true;
            this.text = "组局成功，恭喜您获得一张5折优惠券";
          }
        });
      }
    },
  },
  computed: {
    joinMembers() {
      // 已经加入组局的人员列表
      if (!this.detail) return [];
      return this.detail.members.filter((v) => v.state === 1);
    },
    myMemberInfo() {
      // 我在局聊中的人员信息，没有申请的话返回null
      if (!this.detail) return null;
      let userId = uni.getStorageSync("userInfo").userId;
      return this.detail.members.find((v) => v.userId === userId);
    },
  },
};
</script>

<style scoped lang="scss">
.boy {
  border: 4upx solid #0076ff;
}

.girl {
  border: 4upx solid #d765d4;
}

.flex-s {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-c {
  display: flex;
  align-items: center;
}

.page-bg {
  position: relative;
  // min-height: 100vh;
  width: 100%;
  background-color: #fff;
  // padding-bottom: calc(146upx + env(safe-area-inset-bottom));
  .activity-image {
    height: 100%;
    width: 100%;
  }
	.main{
		position: absolute;
		top:500upx;
		background: #fff;
		border-radius: 32rpx 32rpx 0rpx 0rpx;
		width:100%;
		padding-bottom:100upx;
	}
  .activity-card {
    position: relative;
    z-index: 2;
    width: 100%;
    background:linear-gradient(180deg, rgba(255, 255, 255, 0.86) 0%, #ffffff 61%, #ffffff 100%);
		margin-top:-160upx;
    border-radius: 32upx 32upx 0upx 0upx;
    z-index: 10;
		.activityLabel {
			position: relative;
		  width: 80%;
		  height: 50upx;
		  // background: linear-gradient(270deg, #FFFFFF 0%, #FFEDED 100%);
			background: transparent;
		  border-radius: 10upx;
		  text-align: center;
		  font-family: PingFangSC-Medium, PingFang SC;
		  color: #ee5050;
		  display: flex;
		  align-items: center;
		  padding-left:110upx;
			left:60upx;
			top: -90upx;
			image{
				position: absolute;
				left:-30upx;
				top:-10upx;
				width: 130upx;
				height:48upx;
			}
		}
		.getCoupon{
			left:30upx;
			width: 60%;
			padding-left:0;
		}
    .active-img {
      position: absolute;
      top: 0;
      right: 0;
      image {
        width: 112upx;
        height: 112upx;
      }
    }
    .activity-label {
      width: 100%;
      height: 50upx;
      position: absolute;
      top: -15upx;
      .activity-card-label {
        background: #333333;
        border-radius: 26upx;
        border: 4upx solid #ffffff;
        font-size: 24upx;
        font-weight: 500;
        padding: 4upx 22upx;
        color: #ffffff;
        margin-left: 16upx;
        &:first-child {
          margin-left: 35upx;
        }
      }
    }

    .bar-card-info {
      margin: 0px 36upx;
      width: calc(100% - 72upx);
      // background: #fff;
			padding-top:1px;
      .slices {
        width: 112upx;
        height: 49upx;
        position: absolute;
        right: 16upx;
        top: 5upx;
        image {
          width: 140upx;
          height: 100%;
        }
      }
      .place {
        // padding-top: 32upx;
        // width: calc(100% - 140upx);
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 36upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
				// margin-top: 30upx;
      }
      .share-btn {
        width: 36upx;
        height: 36upx;
        float: right;
        .share-icon {
          width: 36upx;
          height: 36upx;
        }
      }
      .date {
        margin-top: 6upx;
        font-size: 32upx;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #000000;
        line-height: 44upx;
        .date-info {
          width: calc(100% -48upx);
          float: left;
        }
      }

      .type {
        margin-top: 24upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        display: flex;
        line-height: 26upx;
        align-items: center;
        justify-content: space-between;
				.wayInfo{
					display: flex;
					align-items: center;
					position: relative;
					width: 100%;
					.incomingWay{
						font-size:24upx;
						color:#999;
						width: 140upx;
						text-align: center;
						margin-left: 30upx;
						height:40upx;
						line-height: 40upx;
						border-radius: 20px;
						background: #F0F0F0;
					}
					.v2{
						position: absolute;
						top:10upx;
						right:0;
					}
				}
				
        .v2 {
          font-size: 32upx;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #c0b6b6;
        }
      }

      .adress {
        padding: 0 16upx;
        display: inline-block;
        margin-top: 25upx;
        background: #ececec;
        border-radius: 18upx;
        font-size: 26upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;

        .map-icon {
          height: 24upx;
          width: 24upx;
          margin-top: 4upx;
          margin-right: 6upx;
        }

        color: #000000;
      }
    }
  }
	.merchant{
		width: calc(100% - 84upx);
		margin-top: 20upx;
		margin-left: 42upx;
		border-radius: 24upx;
		background: #F5F5F5;
		padding:24upx 20upx;
		&-info{
			display: flex;
			align-items: flex-start;
			.info{
				flex:1;
				.merchant-name{
					font-weight: bold;
					display: flex;
					align-items: center;
					.arrow{
						width: 15upx;
						max-height: 25upx;
						margin-left: 10upx;
					}
				}
				
				.adress {
				  padding: 6upx 16upx;
				  display: inline-block;
				  margin-top: 25upx;
				  background: #ececec;
				  border-radius: 18upx;
				  font-size: 22upx;
				  font-family: PingFangSC-Regular, PingFang SC;
				  font-weight: 400;
					display: flex;
					align-items: center;
					width: fit-content;
				  .map-icon {
				    height: 24upx;
				    width: 24upx;
				    margin-right: 6upx;
				  }
				
				  color: #000000;
				}
			}
			.call{
				width: 50upx;
				height: 50upx;
			}
		}
		.play{
			display: flex;
			margin-top: 30upx;
			image{
				width: 130upx;
				height: 190upx;
				border-radius: 20upx;
			}
			&-info{
				flex:1;
				padding-left:20upx;
				font-size:22upx;
				color:#333;
				.play-name{
					font-size:32upx;
					color:#000;
					font-weight: Semibold;
				}
				.mark{
					text{
						margin-right: 5px;
					}
				}
				.play-price{
					margin: 10upx 0;
					font-size:32upx;
					color:#FF4F3A;
					.unit{
						font-size:20upx;
					}
					.price-state{
						font-size: 22upx;
						color:#999;
						margin-left: 6upx;
					}
				}
				.play-intro{
					color:#999;
					display: -webkit-box;
					-webkit-box-orient: vertical;    
					-webkit-line-clamp: 2;    
					overflow: hidden;
				}
			}
		}
	}
	.user-wrap{
		padding-left:42upx;
		padding-right:42upx;
		margin-top: 20upx;
		.user-view{
			padding:0 30upx;
			border-radius: 20upx;
			background: #F5F5F5;
			height:100upx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			.user-title{
				width: 150upx;
				font-size:32upx;
				font-weight:600;
				color:#000;
			}
			.user-list {
				display: flex;
				align-items: center;
				flex:1;
				justify-content: flex-end;
				padding-right:20upx;
				.user-item {
					position: relative;
					height: 80upx;
					width: 80upx;
					border-radius: 50%;
				}
				.items{
					margin-right:-50upx;
				}
				.mar-l {
					left: -30upx;
				}
			}
	
			.more-item {
				display: flex;
				align-items: center;
				justify-content: center;
				background-color: #fff !important;
				font-size: 24upx;
				font-family: PingFangSC-Medium, PingFang SC;
				font-weight: bold;
				color: #000;
				border:2px solid #D765D4;
				position: relative;
				.dot{
					position: absolute;
					display: inline-block;
					width: 30upx;
					min-height: 30upx;
					border-radius: 50%;
					background: #FF5050;
					top: -8upx;
					right:-8upx;
				}
			}
			.more{
				width: 26upx;
				height:40upx;
			}
		}
	}

  .tab-bg {
    margin-top: 70upx;
    margin-left: 42upx;
    margin-right: 42upx;
    border-radius: 32upx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;

    &-l {
      height: 36upx;
      display: flex;
      border-left: 8upx solid #333333;
      align-items: center;
      padding-left: 16upx;
      border-radius: 5upx;

      .v1 {
        font-size: 32upx;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #000000;
      }

      .v2 {
        font-size: 30upx;
        margin-left: 28upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #999999;
        &:last-child {
					margin-left: 40upx;
          font-family: PingFangSC-Semibold, PingFang SC;
        }
      }
    }

    &-r {
      border-radius: 46upx;
      border: 1px solid #999999;
      font-size: 26upx;
      line-height: 26upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #333333;
      padding: 18upx 32upx;
    }
  }

  .error-tip {
    position: fixed;
    text-align: center;
    padding: 22upx 0px!important;
    left: 0px;
    bottom: 160upx;
    width: 100%;
    background: linear-gradient(270deg, #ffe9e6 0%, #ffcec8 100%);
    font-size: 32upx;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #333333;
    z-index: 99;
  }

  .btm-bg {
    position: fixed;
    padding: 0 12upx;
    bottom: 0;
    left: 0;
    right: 0;
    height: calc(96upx + env(safe-area-inset-bottom));
    width: 100%;
    background-color: #fff;
    z-index: 99;
		.flex-c{
			height:100%;
		}
    .gl-btn,
    .yq-btn {
			padding:0;
      flex: 1;
      background: #ffffff;
      border-radius: 46upx;
      border: 2upx solid #333333;
      font-size: 30upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #000000;
      margin: 0 9upx;
      align-items: center;
      line-height: 76upx;
      text-align: center;
    }

    .succe-btn {
      border: 2upx solid #333333;
      line-height: 76upx;
      position: relative;
      text-align: center;
      width: 50%;
      padding: 0upx 40upx;
      border-radius: 46upx;
      font-size: 40upx;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;

      .tip {
        position: absolute;
        padding: 2upx 0upx;
        z-index: 111;
        width: 300upx;
        top: -50upx;
        left: 0px;
        background: #ffd4d4;
        border-radius: 20upx;
        font-size: 24upx;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #333333;

        .tip-trangle-bottom {
          position: absolute;
          bottom: -12upx;
          left: calc(100% / 2);
          width: 0;
          height: 0;
          border-left: 7px solid transparent;
          border-right: 7px solid transparent;
          border-top: 7px solid #ffd4d4;
        }
      }
    }

    .black-c {
      background-color: #000000;
      color: #ffffff;
    }

    .white-c {
      color: #333333;
      border: 2upx solid #333333;
    }
  }
}
.shadow {
  position: fixed;
  z-index: 99998;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
}
.game-created {
  position: fixed;
  z-index: 99999;
  top: 200upx;
  left: 50%;
  transform: translateX(-50%);
}
.gamePassword {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 99999;
}
.btn-box1 {
  margin-top: 60upx;
  display: flex;
  justify-content: center;
  .btn1 {
    box-sizing: border-box;
    font-size: 32upx;
    width: 280upx;
    line-height: 80upx;
    text-align: center;
    color: #333;
    border: 2upx solid #333;
    border-radius: 40upx;
  }
}
.btn-box2 {
  width: calc(100% - 40upx);
  margin-left: 20upx;
  margin-top: 100upx;
  display: flex;
  justify-content: space-between;
  .btn1 {
    box-sizing: border-box;
    font-size: 32upx;
    width: 280upx;
    line-height: 80upx;
    text-align: center;
    color: #333;
    border: 2upx solid #333;
    border-radius: 40upx;
    width: 260upx;
    border-radius: 54upx;
    line-height: 108upx;
  }
  .btn2 {
    box-sizing: border-box;
    font-size: 32upx;
    width: 280upx;
    line-height: 80upx;
    text-align: center;
    color: #333;
    border: 2upx solid #333;
    border-radius: 40upx;
    width: 260upx;
    border-radius: 54upx;
    line-height: 108upx;
    background: #333;
    color: #fff;
  }
}
.cardCanvas{
	width: 500px;
	height:400px;
	position: absolute;
	left:-1000px;
	bottom:0;
}
.mt20{
	margin-top: 20upx;
}
</style>
