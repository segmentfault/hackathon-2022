### gitee 完整可运行项目，并包含使用案例

## 使用前请看这里
* 评论区有人发布了APP的，未验证，
* 目前只测试了h5与微信小程序，代码中的#ifdef判断 暂时也限制为 H5 与 MP,其它的后续会视情况增加，也欢迎其他人提交
* H5查看示例时，建议内置浏览器查看， uni.canvasToTempFilePath在 <font color=#FF000 >__H5端需要支持跨域才能访问__</font>
* 根据微信小程序wxml-to-canvas封装，<font color=#FF000 >__支持的 css 属性及写法 一定一定 要 按__ </font> [官方文档](https://developers.weixin.qq.com/miniprogram/dev/extended/component-plus/wxml-to-canvas.html)

## canvasToTempFilePath H5端 
* uni.canvasToTempFilePath(object, component) ，H5端Canvas 内绘制的图像需要支持跨域访问才能成功
* [H5端 Canvas 内绘制的图像需要支持跨域访问才能成功](https://uniapp.dcloud.io/api/canvas/canvasToTempFilePath?id=canvastotempfilepath)

#### 支持的 css 属性及写法
![支持的 css 属性及写法](https://vkceyugu.cdn.bspapp.com/VKCEYUGU-xyzgy/07272e50-5eb2-11eb-8ff1-d5dcf8779628.png)

---

### 相关字段
|  字段   | 类型  |默认值  |描述  |
|  ----  | ----  |----  |----  |
| hide  | Boolean |false| canvas是否在页面可见 true 通过fixed将canvas移至屏幕外
| width  | Number |300| canvas宽度
| height  | Number |300| canvas高度
| xWxml  | String || wxml 模板
| xStyle  | Object |{}| 样式
|useCORS|Boolean | true | canvas 图片跨域

#### xWxml
* 支持 view、text、image 三种标签，通过 class 匹配 style 对象中的样式。

#### xStyle （注意支持的css属性）
* 对象属性值为对应 wxml 标签的 class <font color=#FF000 >驼峰形式。需为每个元素指定 width 和 height 属性</font>，否则会导致布局错误。
* 存在多个 className 时，位置靠后的优先级更高，子元素会继承父级元素的可继承属性。
* 元素均为 <font color=#FF000 >__flex 布局__</font>。left/top 等 仅在 absolute 定位下生效。

#### 支持的 css 属性及写法 （或文档末尾的截图）
* 根据微信小程序wxml-to-canvas封装，支持的 css 属性及写法参考官方文档即可
* [wxml-to-canvas 文档](https://developers.weixin.qq.com/miniprogram/dev/extended/component-plus/wxml-to-canvas.html)
---
### 相关方法
|  方法名    |描述  | 返回值 |
|  ----    |----  |----  |
| renderToCanvas  | 将wxml渲染至页面|无 |
| canvasToTempFilePath  | 将canvas转为图片地址 (H5端 Canvas 内绘制的图像需要支持跨域访问才能成功)|promise函数，返回 图片地址，h5为base64 |
| getCanvasImage  | renderToCanvas 与 canvasToTempFilePath 合并 |promise函数，返回 图片地址，h5为base64 |
| saveImageToPhotosAlbum  | 图片保存至本地 |promise函数，返回 true / flase|

#### canvasToTempFilePath H5端 
* uni.canvasToTempFilePath(object, component) ，H5端Canvas 内绘制的图像需要支持跨域访问才能成功
* [H5端 Canvas 内绘制的图像需要支持跨域访问才能成功](https://uniapp.dcloud.io/api/canvas/canvasToTempFilePath?id=canvastotempfilepath)

---

### 使用
#### 1、页面引用
``` 
import xWxmlToCanvas from '@/components/x-wxml-to-canvas/x-wxml-to-canvas';

```
#### 2、components声明
```
components: {
		xWxmlToCanvas
	} 
```
#### 3、使用
```
<xWxmlToCanvas ref="xWxmlToCanvas" :hide="true" :width="300" :height="300" :xStyle="style" :xWxml="wxml"></xWxmlToCanvas>
```
#### 4、方法调用

```
this.$refs.xWxmlToCanvas.renderToCanvas() //渲染至canvas
this.$refs.xWxmlToCanvas.canvasToTempFilePath() //生成图片
this.$refs.xWxmlToCanvas.getCanvasImage() //获取图片
this.$refs.xWxmlToCanvas.saveImageToPhotosAlbum() //下载图片

```

