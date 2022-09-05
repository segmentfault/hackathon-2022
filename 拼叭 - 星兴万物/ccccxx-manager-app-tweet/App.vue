<script>
	import update from 'framework/utils/update.js';
	import login from './service/login.js'

	export default {
		globalData: {
			addressCallback: null,
			latitude: null,
			longitude: null,
		},
		onLaunch() {
			// 检测是否需要更新
			update();
			if (!uni.getStorageSync('userInfo').userId) {
				login.defLogin()
			}
		},
		methods: {
			getLocation() {
				return new Promise((res, rej) => {
					
					uni.getLocation({
						success(data) {
							res(data)
						},
						fail(err) {
							res(err)
						}
					});
				});
			},
		},
	};
</script>

<style lang="scss">
	@import 'uview-ui/index.scss';
	@import './style/index.css';
	@import '/static/css/comm.css';

	.empty-tip {
		margin-top: 39upx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 26upx;
		font-family: PingFangSC-Light, PingFang SC;
		font-weight: 300;
		color: #999 !important;

		.empty-icon {
			height: 64upx;
			width: 64upx;
			margin-right: 18upx;
			color: #fff !important;
		}
	}
</style>
