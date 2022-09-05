<template>
	<view class="card-active merchan-sites">
		<view class="card-tabs" v-if="tabs.length">
			<view class="name">类型</view>
			<view class="card-tab" v-for="(item, index) in tabs" :key="index" :class="{ active: typeId === item.id }"
				@click="handlerTab(item, index)">{{ item.label }}</view>
		</view>
		<slot></slot>
		<view class="card-box-center" v-if='cardList.length!=0'>
			<u-card :show-head="false" class="card-box-center-wrapper" 
				v-for="(item, index) in cardList" :key="index">
				<view slot="body" @click="$emit('handlerDetail', item)" class="card-item">
					<image @click.stop="handlerChange(item,index)" :src="
          activeId === item.id ? 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox-checked.png' : 
          'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/checkbox.png'" class="checkbox"></image>
					<image :src="item.pic" mode="aspectFill" class="img"></image>
					<view class="center">
						<view class="title">{{ item.name }}</view>
						<view class="price-wrapper">
							<view class="price-smalls">{{ item.type }} | {{ item.duration }}｜{{ item.difficulty }}
							</view>
						</view>
						<!-- <view class="price-wrapper price-top"> -->
						<!-- <view class="price">￥{{ item.discountPrice }}/人</view> -->
						<!-- <view class="price-smalls">门市价</view> -->
						<!-- </view> -->
						<view class="address">
							<p>{{ item.brief }}</p>
						</view>
					</view>
				</view>
			</u-card>
			
		</view>
		<Default v-else text='未找到相关剧本' class="">
		</Default>
	</view>
</template>
<script>
	import Default from '../../components/default.vue'
	export default {
		components: {
			Default,
		},
		props: {
			activeId: {
				type: [String, Number],
			},
			status: {
				type: String,
			},
			value: {
				type: [String, Number],
			},
			tabs: {
				type: Array,
				required: true,
			},
			cardTitle: {
				type: String,
				default: () => {
					return "活动规则";
				},
			},
			cardList: {
				type: Array,
				required: true,
			},
			query: {
				type: Object,
				required: true,
			},
		},
		data(){
			return {
				typeId:'all'
			}
		},
		methods: {
			getMoreList() {
				this.$emit("loadMore", {
					pageNum: this.query.pageNum + 1,
					status: "loading",
				});
			},
			handlerChange(item, index) {
				this.$emit('handlerChange', item)
			},
			handlerTab(item) {
				this.$emit("input", item.id);
				this.$emit("loadMore", {
					pageNum: 1,
					status: "loading",
					cardList: []
				});
			},
		},
	};
</script>
<style lang="scss" scoped>
	.card-active{
		background: #171C33;
	}
	.card-tabs {
		display: flex;
		flex-direction: row;
		margin: 0 auto;
		justify-content: space-between;
		padding-bottom: 32upx;
		padding-top: 32upx;
		padding: 32upx 15upx;
		background: #171c33;
		overflow-x: scroll;

		.name {
			min-width: 70upx;
			font-size: 24upx;
			font-weight: bold;
			color: #8C91A9;
			line-height: 56upx;
		}

		.card-tab {
			flex: 1;
			border-radius: 36upx;
			font-size: 24upx;
			line-height: 44upx;
			font-weight: 400;
			color: #000000;
			text-align: center;
			border: 2upx solid #FDE5A4;
			background: #171C33;
			color: #FDE5A4;
			margin-right: 20upx;
			min-width: 164upx;
			padding: 6upx 16upx;
		}

		.card-tab-title {
			border: 0;
			font-size: 24upx;
			line-height: 56upx;
			margin-right: 0;
			text-align: right;
			color: #000000;
			;
			background: transparent;
		}
	}

	.active {
		background: #FDE5A4 !important;
		color: #171C33 !important;
	}

	.card-item {
		display: flex;
		flex-direction: row;
		position: relative;

		.img {
			width: 144upx;
			height: 202upx;
			display: inline-block;
			border-radius: 8upx;
		}

		.center {
			flex: 1;
			padding-left: 36upx;
			display: flex;
			flex-direction: column;
			width: 300rpx;

			.title {
				height: 44upx;
				font-size: 32upx;
				font-weight: 600;
				line-height: 44upx;
				color: #000000;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.price-wrapper {
				padding: 11upx 0;
				display: flex;
				flex-direction: row;
				vertical-align: bottom;

				.price-smalls {
					font-size: 24upx;
					color: #666666;
					line-height: 40upx;
					align-self: flex-end;
				}

				.price {
					font-size: 40upx;
					font-weight: 600;
					color: #ff4f3a;
					line-height: 40upx;
					align-self: flex-end;
					margin-right: 16rpx;
				}
			}

			.price-top {
				padding-top: 0;
			}

			.address {
				p {
					line-height: 36upx;
					font-size: 24upx;
					color: #666666;
					overflow: hidden;
					text-overflow: ellipsis;
					display: -webkit-box;
					-webkit-line-clamp: 3;
					-webkit-box-orient: vertical;
				}
			}
		}
	}

	.card-box-center {
		background: #fff;
		border-radius: 32upx 32upx 0 0;
	}

	.checkbox {
		height: 48upx;
		width: 48upx;
		margin-top: 34upx;
		margin-right: 28upx;
		position: absolute;
		right: 0;
		top: 0;
	}
</style>
