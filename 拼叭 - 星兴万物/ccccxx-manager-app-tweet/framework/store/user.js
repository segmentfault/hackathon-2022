import UserService from '../../service/user.js'
const user = {
	state: {
		myUserId: 0,
		myUserInfo: null,
		myUserTicket: '',
		redpoint: 0,
		identityType: 0, // 0 普通身份 1赏金猎人 2合伙人
	},
	mutations: {
		setMyUserTicket(state, ticket) {
			state.myUserTicket = ticket
		},
		setMyUserId(state, id) {
			state.myUserId = id
		},
		setMyUserInfo(state, userInfo) {
			state.myUserInfo = userInfo
		},
		setRedpoint(state, redpoint) {
			state.redpoint = redpoint
		},
		setIdentityType(state) {
			// 获取身份
			UserService.getIdentity().then(res => {
				state.identityType = res
				console.log(state.identityType)
			})

		}
	}
}

export default user
