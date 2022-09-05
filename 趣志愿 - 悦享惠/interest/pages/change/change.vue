<template>
  <view></view>
</template>
<script>
	export default {
		data() {
		},
		onLoad(){
			this.judge();
		},
	
		methods:{
			judge(){
				if(uni.getStorageSync("login")){
					uni.switchTab({
            url: "/pages/tabbar/tabbar-4/tabbar-4",
          });
				}else{
						uni.redirectTo({
					url:"/pagesA/voTest/start"
				})
				}
				
			}
			
		}
	}
</script>