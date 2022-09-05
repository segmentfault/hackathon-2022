<template>
  <view @click="goSearch()">
    <u-sticky>
      <view class="sticky" style="background: #fff">
        <u-search
          :placeholder="placeholder"
          disabled
          :value="kewords"
          :show-action="false"
        ></u-search>
      </view>
    </u-sticky>
  </view>
</template>
<script>
export default {
  props: {
    kewords: {
      type: [String, Number],
      default: '',
    },
    placeholder: {
      type: String,
      default: '请输入商品或商家名称',
    },
    type: { 
      type: [String, Number],
      default: 1,
    }
  },
  data() {
    return {};
  },
  methods: {
    goSearch() {
			if (!uni.getStorageSync('userInfo')) {
			  this.$toast('请先登录');
			  setTimeout(() => {
			    uni.navigateTo({
			      url: `/pagesA/user/login?isOnload=${true}`,
			    });
			  });
			  return;
			}
      uni.navigateTo({
        url: `/pagesA/search/search?type=${this.type}`,
      });
    },
  },
};
</script>
