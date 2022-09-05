// 云函数入口文件
const cloud = require('wx-server-sdk')
cloud.init({
	env: cloud.DYNAMIC_CURRENT_ENV
})
const db = cloud.database()
// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  let value=event.text;
  //获取埋点数据
  const da = await db.collection('point').where({
    _id: "b69f67c062cf7de90b0a527d02974f39"
  }).get()
  //埋点加一
   da.data[0][value]++; 
   //删除系统自带字段
   delete da.data[0]._id;
   delete da.data[0]._createTime;
   delete da.data[0]._updateTime;
   //更新数据库
   await db.collection('point').doc('b69f67c062cf7de90b0a527d02974f39').update({
    data:da.data[0],
 }).then(res=>{
   console.log("sbsbs",res)
 })
  return {
  }
}