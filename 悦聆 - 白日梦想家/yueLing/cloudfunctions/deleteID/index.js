// 云函数入口文件
const cloud = require('wx-server-sdk')
cloud.init({
	env: cloud.DYNAMIC_CURRENT_ENV
})
const db = cloud.database()
// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  db.collection('point').where({
    _id: "b69f67c062cf7de90b0a527d02974f39"
  }).update({
    data:{
      creatID:[],
      homeID:[],
      myID:[]
    }
  
  })
  return {
  }
}