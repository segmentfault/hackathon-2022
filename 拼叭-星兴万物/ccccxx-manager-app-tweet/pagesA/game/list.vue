<template>
	<view class="page-game-list">
		<view class="card-item" v-for="item,i in joinList" :key="i">
			<barCard :bar="item"></barCard>
		</view>
	</view>
</template>

<script>
	import barCard from "../../components/barCard.vue"
	import homeService from '../../service/home.js'
	export default {
		components: {
			barCard
		},
		data() {
			return {
				pageNum: 1,
				joinList: [],
				finish: false
			};
		},
		onLoad() {
			this.getGameList()
		},
		onReachBottom() {
			this.getGameList()
		},
		onShow(){
			// 埋点
          this.$eventRecord(73)
		},
		methods: {
			getGameList() {
				if (this.finish) return
				homeService.myJoin({
					pageNum: this.pageNum,
					pageSize: 10
				}).then(res => {
					this.joinList = this.joinList.concat(res)
					this.pageNum++
					if (res.length < 10) {
						this.finish = true
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.page-game-list {
		padding: 0upx 42upx 40upx 42upx;
	}
</style>
