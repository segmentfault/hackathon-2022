<template>
	<div class="count">
		<span class="count-tips">倒计时</span>
		<u-count-down :timestamp="timestamp" height="40" separator="zh" separator-size="24" separator-color="#333" font-size="20" color="#fff" bg-color="#000"></u-count-down>
	</div>
</template>

<script>
	export default {
		props: {
			timestamp: {
        type: Number,
        default: function() {
          return 864000
        }
      },
		},
		methods: {
				
		}
	}
</script>

<style lang="scss" scoped>
.count-tips {
		font-size: 12px;
		color: #333;
		margin-right: 7px;
	}
</style>
