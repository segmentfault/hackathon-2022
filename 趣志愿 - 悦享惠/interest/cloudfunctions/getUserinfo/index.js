// 云函数入口文件
const cloud = require('wx-server-sdk')
cloud.init()
const db=cloud.database();
//获取用户的openid
exports.main = async(event, context) => {
    const res = db.collection('userInfo').where({
        _openid:event.userInfo.openId
    })

}