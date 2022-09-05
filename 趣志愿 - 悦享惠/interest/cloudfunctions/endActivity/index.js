// 云函数入口文件
const cloud = require('wx-server-sdk')
cloud.init({
	env: cloud.DYNAMIC_CURRENT_ENV
})
const db = cloud.database()
// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  //查出所有进行中的活动
  let taskRes=await db.collection('activity').where({
    show:true
  }).get()
  let tasks=taskRes.data;
  //获取当前时间
  let now = new Date().getTime();
  for (let i = 0; i < tasks.length; i++) {
    let time=new Date(tasks[i].time).getTime();
    let id= tasks[i]._id;
    if(now>=time){
      db.collection('activity').doc( tasks[i]._id).update({
        data:{
          show:false
        }
      })
    }
  }
  return {
  }
}