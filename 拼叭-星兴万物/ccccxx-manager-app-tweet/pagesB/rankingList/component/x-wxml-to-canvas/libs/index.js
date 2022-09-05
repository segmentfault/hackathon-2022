import xmlParse from './xml-parser.js';
import {
	Widget
} from './widget.js';
import {
	Draw
} from './draw.js';

import {saveImageToPhotosAlbum} from './save-image-to-album.js'
// 获取canvas对象
const selectNodesRefInfo = function(selector, _this) {
	// #ifdef H5
	const canvas = document.querySelector(`${selector} canvas`);
	const ctx = canvas.getContext('2d');
	return {
		canvas,
		ctx,
		width: canvas.width,
		height: canvas.height
	};
	// #endif
	// #ifdef MP
	return new Promise((resolve, reject) => {
		const query = uni.createSelectorQuery().in(_this);
		query
			.select(selector)
			.fields({
				node: true,
				size: true
			})
			.exec(res => {
				const canvas = res[0].node;
				const ctx = canvas.getContext('2d');
				resolve({
					canvas,
					ctx,
					width: res[0].width,
					height: res[0].height
				});
			});
	});
	// #endif
}

// 将wxml渲染至页面
const renderToCanvas = async function({
	ctx,
	canvas,
	useCORS,
	xWxml,
	xStyle
}) {
	if (!ctx || !canvas) {
		return Promise.reject(new Error('renderToCanvas: fail canvas has not been created'));
	}
	const {
		root: xom
	} = xmlParse(xWxml);
	const widget = new Widget(xom, xStyle);
	const container = widget.init();
	const draw = new Draw(canvas, ctx, useCORS);
	await draw.drawNode(container);
	return Promise.resolve(container);
}

// 将canvas转为图片 H5端 Canvas 内绘制的图像需要支持跨域访问才能成功。
// https://uniapp.dcloud.io/api/canvas/canvasToTempFilePath?id=canvastotempfilepath
const canvasToTempFilePath = function({
	top,
	left,
	width,
	height,
	canvas,
	canvasId,
	quality = 1,
	fileType = 'png'
}) {
	if (!canvas) {
		return Promise.reject(new Error('canvasToTempFilePath: fail canvas has not been passed'));
	}
	return new Promise((resolve, reject) => {
		const dpr = uni.getSystemInfoSync().pixelRatio;
		uni.canvasToTempFilePath({
			x: left,
			y: top,
			width,
			height,
			destWidth: width * dpr,
			destHeight: height * dpr,
			canvasId: canvasId,
			canvas: canvas,
			fileType: fileType || 'png',
			quality: quality || 1,
			success: function(res) {
				resolve(res.tempFilePath)
			},
			fail: reject
		})
	})
}


export {
	xmlParse,
	Widget,
	Draw,
	selectNodesRefInfo,
	renderToCanvas,
	canvasToTempFilePath,
	saveImageToPhotosAlbum
}
