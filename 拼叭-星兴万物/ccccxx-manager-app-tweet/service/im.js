import { service } from '../framework';

export default {
  // 获取群资料
  getGroupData(groupId) {
    return service.post({
      url: '/im/api/auth/im/group/get',
      data: {
        groupId,
      },
    });
  },

  getUserSig(userID) {
    return service.get({
      url: '/im/api/im/msg/getUserSig',
      data: {
        userId: userID,
      },
    });
  },

  // 获取我的关注红点
  checkFriendsRedPoint() {
    return service.get({
      url: '/im/api/auth/im/msg/countFriend',
    });
  },

  // 拉取好友列表
  getFriendsList(data) {
    return service.postJson({
      url: '/im/api/auth/im/friend/page',
      data,
    });
  },

  // 拉取好友列表
  queryFriendsPage(data) {
    return service.postJson({
      url: '/im/api/auth/im/query/friend/page',
      data,
    });
  },

  // 检查好友关系
  checkFriends(myUserId, toUserId) {
    return service.postJson({
      url: '/im/api/auth/im/account/check',
      data: {
        userId: myUserId,
        toUserId,
      },
    });
  },

  // 添加好友
  addFriends(toUserId) {
    return service.postJson({
      url: '/im/api/auth/im/friend/add',
      data: {
        toUserId,
        addSource: 'AddSource_Type_WX',
      },
    });
  },

  // 删除好友
  deleteFriends(toUserId) {
    return service.postJson({
      url: '/im/api/auth/im/friend/delete',
      data: {
        toUserId,
      },
    });
  },

  // 消除关注的红点
  markFriendsRead() {
    return service.postJson({
      url: '/im/api/auth/im/msg/updateFriendIsRead',
    });
  },

  //私聊消息设置已读
  updateRead(sequence, toUserId, fromUserId) {
    return service.postJson({
      url: '/im/api/auth/im/msg/updateRead',
      data: {
        sequence,
        toUserId,
        fromUserId,
      },
    });
  },

  //群聊消息设置已读
  updateGroupRead(userId, groupId) {
    return service.postJson({
      url: '/im/api/auth/im/msg/group/updateRead',
      data: {
        userId,
        groupId,
      },
    });
  },
  //获取好友信息
  getFriendInfo(toUserId) {
    return service.postJson({
      url: '/im/api/auth/im/get/user',
      data: {
        toUserId,
      },
    });
  },
  //用户查询7天前会话列表
  queryUserConversationHis(conversationIdList) {
    return service.postJson({
      url: '/im/api/auth/im/msg/query/user/conversation/his',
      data: {
        conversationIdList,
      },
    });
  },
  //用户查询历史消息列表(一次15条)
  queryUserMessageHis(type, groupId, msgSeq, toUserId) {
    return service.postJson({
      url: '/im/api/auth/im/msg/query/user/message/his',
      data: {
        type,
        groupId,
        msgSeq,
        toUserId,
      },
    });
  },
  //用户消息菜单未读数
  getImMenuCount(userId) {
    return service.get({
      url: '/im/api/im/msg/menu/count',
      data: {
        userId,
      },
    });
  },
  //更新用户未读消息数量
  updateUnreadNum(userId, data) {
    return service.postJson({
      url: '/im/api/auth/im/msg/updateReadNum',
      data: {
        userId: userId,
        list: data,
      },
    });
  },
};
