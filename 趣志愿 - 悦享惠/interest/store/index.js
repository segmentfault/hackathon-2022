import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
const store = new Vuex.Store({
	state: {
		// 存放状态
		state:true,
	},
	getters: {
		// state的计算属性
		//用 this.$store.getters.getData()读取
		getData(state){
			return state;
		}  
	},
	mutations: {
		// 更改state中状态的逻辑，同步操作
		//用 this.$store.commit('function_name',payload) 使用，若无参数则不写payload
		change(state){
			state.state=!state.state;
		}
	},
	actions: {
		// 提交mutation，异步操作
	}
})

export default store