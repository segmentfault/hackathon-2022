<template>
	<view :class="{ hide: hide }">
		<canvas id="canvas" canvas-id="canvas" type="2d" :style="{ width: width + 'px', height: height + 'px' }"
			@error="canvasIdErrorCallback"></canvas>
	</view>
</template>

<script>
	import {
		xmlParse,
		Widget,
		Draw,
		selectNodesRefInfo,
		renderToCanvas,
		canvasToTempFilePath,
		saveImageToPhotosAlbum
	} from './libs/index.js';
	export default {
		props: {
			hide: {
				//canvas是否可见
				type: Boolean,
				default: true
			},
			width: {
				type: Number,
				default: 300
			},
			height: {
				type: Number,
				default: 300
			},
			xWxml: {
				type: String,
				default: ''
			},
			xStyle: {
				type: Object,
				default: () => {
					return {};
				}
			},
			useCORS: {
				type: Boolean,
				default: true
			}
		},
		data() {
			return {
				canvasId: 'canvas',
				canvas:'',
				ctx:'',
				boundary:{}
			};
		},
		methods: {
			canvasIdErrorCallback(err) {
				console.log('canvasIdErrorCallback', err);
			},
			onRenderToCanvas() {
				const {
					ctx,
					canvas,
					useCORS,
					xWxml,
					xStyle
				} = this;
				console.log(ctx,canvas)
				uni.showLoading({
					title: '生成中'
				})
				return new Promise((resolve, reject) => {
					renderToCanvas({
						ctx,
						canvas,
						useCORS,
						xWxml,
						xStyle
					}).then(res => {
						uni.hideLoading()
						this.boundary = {
							top: res.layoutBox.top,
							left: res.layoutBox.left,
							width: res.computedStyle.width,
							height: res.computedStyle.height
						};
						resolve(res)
					}).catch(err => {
						console.log(err)
						uni.hideLoading()
						uni.showToast({
							title: '生成失败，稍后重试',
							icon: 'none'
						})
						reject(new Error('canvasToTempFilePath: renderToCanvas 执行失败'))
					});
				});
			},
			onCanvasToTempFilePath() {
				const {
					canvas,
					canvasId,
					boundary
				} = this;
				if (!boundary) {
					return Promise.reject(new Error('canvasToTempFilePath: renderToCanvas 函数未执行'));
				}
				const {
					top,
					left,
					width,
					height
				} = boundary;
				let _this = this;
				return new Promise((resolve, reject) => {
					canvasToTempFilePath({
						top,
						left,
						width,
						height,
						canvasId,
						canvas
					}).then(res => {
						console.log(res)
						 resolve(res);
					}).catch(err => {
						reject(new Error('canvasToTempFilePath: canvasToTempFilePath 执行失败'))
					});
				});
			},
			getCanvasImage() {
				let _this = this;
				return new Promise((resolve, reject) => {
					_this.onRenderToCanvas().then(async res => {
						resolve(await _this.onCanvasToTempFilePath())
					}).catch(err => {
						reject(new Error('canvasToTempFilePath: getCanvasImage 执行失败'))
					})
				});
			},
			onSaveImageToPhotosAlbum(path, filename, callback) {
				saveImageToPhotosAlbum(path, filename, callback)
			}
		},
		mounted() {
			this.$nextTick(async () => {
				const {
					canvas,
					ctx,
					width,
					height
				} = await selectNodesRefInfo(`#${this.canvasId}`, this);
				const dpr = uni.getSystemInfoSync().pixelRatio;
				canvas.width = width * dpr;
				canvas.height = height * dpr;
				ctx.scale(dpr, dpr);
				this.canvas = canvas;
				this.ctx = ctx;
			});
		}
	};
</script>

<style scoped lang="scss">
	.hide {
		position: fixed;
		left: -800rpx;
	}
</style>
