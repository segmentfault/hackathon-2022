import GameService from '@/service/game.js';
import IMService from '@/service/im.js';
import QQMapWX from '@/static/qqmap/qqmap-wx-jssdk.min.js';
export default {
  methods: {
    // 销售关系
    getBindRelation(options) {
      if (options.scene) {
        const shareUserId = options.scene.split('shareUserId')[1];
        gameService.bindRelation({ parentId: shareUserId, parentType: 1 });
      }
    },
    // 获取小红点
    geRedpoint(allConversation = [], hisConversation = []) {
      let res = 0;
      allConversation.forEach((v) => {
        if (v.type !== '@TIM#SYSTEM' && v.type !== 'GROUP' && !v.isFriend) {
          res += v.unreadCount;
        }
      });
      hisConversation.forEach((v) => {
        if (
          this.isShowConversation(v, allConversation) &&
          v.type !== '@TIM#SYSTEM' &&
          v.type !== 'GROUP' &&
          !v.isFriend &&
          v.unreadCount
        ) {
          res += v.unreadCount;
        }
      });
      this.$store.commit('setRedpoint', res);
    },
    //二次校验历史数据是否存在IM自带会话中
    isShowConversation(conversation, allConversation) {
      let isShow = true;
      for (let item of allConversation) {
        if (conversation.conversationID === item.conversationID) {
          isShow = false;
          break;
        }
      }
      return isShow;
    },
    // im 初始化之后获取的历史未读消息
    queryHisConversation(allConversation) {
      let conversationIDList = [];
      for (let conversation of allConversation) {
        conversationIDList.push(conversation.conversationID);
      }
      IMService.queryUserConversationHis(conversationIDList).then((res) => {
        this.geRedpoint(allConversation, res)
      });
    },
    // 分享拉新
    getshareInfo(options) {
      // 分享拉新
      if (options.shareUserId) {
        this.shareUserId = options.shareUserId;
      }
      if (this.shareUserId && this.myUserId) {
        this.$authorization();
        GameService.bindRelation({
          parentId: this.shareUserId,
          parentType: 1,
        }).then(() => {
          this.shareUserId = '';
        });
      }
    },
    // 服务端消息
    getTrackingID() {
      const pages = getCurrentPages();
      const url = pages[pages.length - 1].$page.fullPath;
      const trackingID = this.$getParam(url, 'trackingID');
      if (trackingID) this.$eventRecord(trackingID);
    },
    async totalUnreadCount(noReadMsg = 0) {
      noReadMsg = await IMService.getImMenuCount(this.myUserId);
      this.$store.commit('setRedpoint', noReadMsg);
    },
    // 获取用户当前经纬度
    getLocation() {
      uni.getLocation({
        success: (res) => {
          let qqmapsdk = new QQMapWX({
            key: '3Z3BZ-74NL4-UD2UR-DFAWK-BK6C3-7XB7B',
          });
          // 逆地址解析
          qqmapsdk.reverseGeocoder({
            location: {
              latitude: res.latitude,
              longitude: res.longitude,
            },
            success: (res) => {
              // 判断是否在长沙
              if (res.result.ad_info.city !== '长沙市') {
                uni.redirectTo({
                  url: '/pageB/out-eervice/out-eervice',
                  success: (res) => {},
                });
              }
            },
          });
        },
        fail: (err) => {
          if (err.errMsg === 'getLocation:fail auth deny') {
            uni.showModal({
              title: '是否授权定位',
              content: '请确认授权，否则无法使用小程序',
              success: function (tip) {
                if (tip.confirm) {
                  uni.openSetting({
                    success: (res) => {
                      if (res.authSetting['scope.userLocation'] === false) {
                        uni.redirectTo({
                          url: '/pageB/out-eervice/out-eervice',
                          success: (res) => {},
                          fail: (res) => {},
                        });
                      } else {
                        uni.getLocation({
                          success: (res) => {
                            let qqmapsdk = new QQMapWX({
                              key: '3Z3BZ-74NL4-UD2UR-DFAWK-BK6C3-7XB7B',
                            });
                            // 逆地址解析
                            qqmapsdk.reverseGeocoder({
                              location: {
                                latitude: res.latitude,
                                longitude: res.longitude,
                              },
                              success: (res) => {
                                // 判断是否在长沙
                                if (res.result.ad_info.city !== '长沙市') {
                                  uni.redirectTo({
                                    url: '/pageB/out-eervice/out-eervice',
                                    success: (res) => {},
                                    fail: (res) => {},
                                  });
                                }
                              },
                            });
                          },
                        });
                      }
                    },
                    fail: (res) => {
                      res;
                    },
                  });
                } else {
                  uni.redirectTo({
                    url: '/pageB/out-eervice/out-eervice',
                    success: (res) => {},
                    fail: (res) => {},
                  });
                }
              },
            });
          }
        },
      });
    },
  },
};
