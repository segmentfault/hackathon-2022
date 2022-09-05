### 目录

- <a href="#c1">概要</a>
- <a href="#c2">平台支持度</a>
- <a href="#c3">效果图</a>
- <a href="#c4">属性说明</a>
- <a href="#c5">使用示例</a>
- <a href="#c6">Tips</a>
- <a href="#c7">更多前端知识</a>

<div id="c1"></div>

### 概要

1. 需求：点击一个按钮去关注微信公众号，该按钮可以自定义样式。
2. 实际开发中，发现微信官网只提供了一个组件```official-account```，并且几乎没有可扩展的内容，没有可配置的属性参数等，是固定样式。
3. 于是自己开发插件解决该需求，能满足自定义的要求。

<div id="c2"></div>

### 平台支持度

微信小程序

<div id="c3"></div>

### 效果图

处理前：

![image](https://img-blog.csdnimg.cn/e78cd78089eb4aadb48ae80b8908eea9.jpg#pic_center)

处理后：

![image](https://img-blog.csdnimg.cn/3f99e9fddda84253890384be1d814aa4.jpg#pic_center)

<div id="c4"></div>

### 属性说明


属性名 | 类型 | 默认值  | 说明
-|-|-|-|
msg | String | 关注微信公众号 | 提示的文字 |
isFollow | Boolean | false | 是否已经关注过公众号，关注过就不显示（这个值可以根据后端接口判断） |
max | Number | 3 | 关闭几次后就不再显示该组件 |


<div id="c5"></div>

### 使用示例

```
<template>
	<view>
		<custom-official-account class="cont" msg="关注微信公众号，海量资料任你查阅！" :isFollow="false" :max="5"></custom-official-account>
	</view>
</template>
<style scoped>
	.cont {
		position: fixed;
		top: 60rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 700rpx;
	}
</style>
```

<div id="c6"></div>

### Tips

1. 组件中不满足需求时，可以在uni_modules/custom-official-account/components/custom-official-account/custom-official-account.vue文件中去修改，与普通的uniapp中的vue页面开发是一样的。
2. 该组件中'立即关注'按钮宽高使用px单位，这个按钮的宽高最好是不变，因为提供的组件的按钮差不多就是这么大，宽高也是固定的，并不会根据屏幕大小而改变。
3. 该组件主要提供的是一种思路，所以最主要是读懂自己修改。
4. 组件调试的时候，进入场景必须是1011（扫描二维码）才会生效。可以在微信开发工具里面添加该编译模式。
5. 实现此功能之前最好是先了解下官方文档：[开放能力 /official-account](https://developers.weixin.qq.com/miniprogram/dev/component/official-account.html)


<div id="c7"></div>
---
如果对此插件有任何疑问，更多相关技术可以加QQ群交流：568984539

更多前端、uniapp、nodejs等相关知识可关注我个人博客：https://blog.csdn.net/qq_42961150?spm=1011.2124.3001.5343