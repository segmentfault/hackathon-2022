<!-- 头像组件，没有头像的用户给个默认的图片 -->
<template>
	<image :src="avatarUrl || 'https://basic-1258404477.cos.ap-nanjing.myqcloud.com/mp-static/icon-pinbar2.png'" class="avatar" :class="{im}" @click="$emit('click')"></image>
</template>

<script>
	export default {
		props: {
			im: Boolean,
			avatarUrl: String
		}
	}
</script>

<style lang="scss">
	.avatar {
		width:100%;
		height:100%;
		border-radius: 50%;
		flex-shrink: 0;

		&.im {
			width: 112upx;
			height: 112upx;
		}
	}
</style>
