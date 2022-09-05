<script>
export default {
	globalData: {
      user:{},
			orInfo:{}
	},

	onLaunch: function() {
		console.log('App Launch');

wx.cloud.init({

env: 'qzycloud-1grvormi21122cee',
traceUser: true,

})
	},
	onShow: function() {
		console.log('App Show');
	},
	onHide: function() {
		console.log('App Hide');
	}
};
</script>

<style lang="scss">
	@import './style/index.css';
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";
</style>