const cloud = require('wx-server-sdk')
const notification = require('start')
cloud.init({
	env: cloud.DYNAMIC_CURRENT_ENV
})
const db = cloud.database()

exports.main = async(event, context) => {
	const execTasks = []; // 待执行任务栈
	// 1.查询是否有定时任务。（timeingTask)集合是否有数据。
	let taskRes = await db.collection('memo').where({
		isfinish: false
	}).limit(100).get()
	console.log(taskRes)
	let tasks = taskRes.data;
	// 2.定时任务是否到达触发时间。只触发一次。
	let now = new Date().getTime();
	try {
		for (let i = 0; i < tasks.length; i++) {
			let remindTime = new Date(tasks[i].remindTimeDate).getTime()
			// remindTime = remindTime - 1000 * 60 * 60 * 8
			if (tasks[i].needRemind && !tasks[i].isfinish && remindTime <= now) {
				execTasks.push(tasks[i]); // 存入待执行任务栈
				tasks[i].isfinish = true
				let id = tasks[i]._id
				console.log("要修改id",id)
				delete tasks[i]._id  //删除id
				delete tasks[i]._openid //删除oppid  
				// tasks[i].remindTimeDate = new Date(tasks[i].remindTime)
				// tasks[i].updateTimeDate = new Date(tasks[i].updateTime)
				console.log("修改后的数据",tasks[i])
				 db.collection('memo').doc(id).update(
						{
							data:({
								isfinish:true
							})
					}).then(res=>{
						console.log("修改成功",res)
					}).catch(res=>{
						console.log("修改失败",res)
					})
			}
		}
	} catch (e) {
		console.error("qwe",e)
	}
	// 3.处理待执行任务
	for (let i = 0; i < execTasks.length; i++) {
		let task = execTasks[i];
	    console.log("要处理的定时任务：",task)
		notification.start(task)
	}
}
