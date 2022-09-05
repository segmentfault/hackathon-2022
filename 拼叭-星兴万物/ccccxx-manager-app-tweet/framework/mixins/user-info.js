import {
	mapState,
	mapGetters
} from 'vuex'

module.exports = {
	computed: {
		myUserId() {
			return uni.getStorageSync("userInfo").userId
		},
		myUserInfo() {
			return uni.getStorageSync("userInfo")
		},
		...mapState({
			myUserTicket: state => state.user.myUserTicket
		})
	}
}
