<template>
	<view class="w">
		<u-navbar back-icon-size="48" :border-bottom="false" title="数据中心" title-color="white" :is-back="true"
			back-icon-color="white" :background="background">
		</u-navbar>
		<view class="content">
			<view class="service">
				<view class="card">
					<view class="t1 u-m-l-20 u-m-t-12">志愿人数</view>
					<view class="bottom u-m-l-20 u-m-t-16">
						<view class="left" style="color:#0076FF">
							<view class="num">
								<view class="n">{{info.voNumber}}</view>
								<view class="tx">个</view>
							</view>
							<view class="EN u-m-t-5">volunteer</view>
						</view>
						<view class="right">
							<img class="img" src="/static/img/people.png" alt="">
						</view>
					</view>
				</view>
				<view class="card">
					<view class="t1 u-m-l-20 u-m-t-12">人均参与</view>
					<view class="bottom u-m-l-20 u-m-t-16">
						<view class="left" style="color:#FF9A48 ">
							<view class="num">
								<view class="n">3</view>
								<view class="tx">次</view>
							</view>
							<view class="EN u-m-t-5">Participation</view>
						</view>
						<view class="right">
							<img class="img" src="/static/img/z.png" alt="">
						</view>
					</view>
				</view>
				<view class="card">
					<view class="t1 u-m-l-20 u-m-t-12">服务时长</view>
					<view class="bottom u-m-l-20 u-m-t-16">
						<view class="left" style="color:#EB626A">
							<view class="num">
								<view class="n">{{info.serviceTime}}</view>
								<view class="tx">小时</view>
							</view>
							<view class="EN u-m-t-5">Hours of Service</view>
						</view>
						<view class="right">
							<img class="img" src="/static/img/my/aix.png" alt="">
						</view>
					</view>
				</view>
			</view>
			<view class="right u-m-t-64">
				<view :class="{ active: navId == 1 }" class="bt" @click="toggleNav(1)"><text>志愿人数</text></view>
				<view :class="{ active: navId == 2 }" class="bt u-m-l-16" @click="toggleNav(2)"><text>累积服务时长</text></view>
				<view :class="{ active: navId == 3 }" class="bt u-m-l-16" @click="toggleNav(3)"><text>人均参与</text></view>
				<view :class="{ active: navId == 4 }" class="bt u-m-l-16" @click="toggleNav(4)"><text>人均服务时长</text></view>
			</view>
			<view >
		<canvas canvas-id="vqRFKDqyikohqxUjPEEvMWgfCoIghlMQ" id="vqRFKDqyikohqxUjPEEvMWgfCoIghlMQ" class="charts" @touchend="tap"/>
			</view>
        <view class="fs28 ti">当前为内测体验版本，仅提供模拟数据</view>
				<button class="btn" @click="savaNotice">导出PDF</button>
	</view>
	
    <canvas style="width:750rpx; height:500rpx; position:fixed; left:100%" canvas-id="UrrAcuZxsqKCyCYUFtds" id="UrrAcuZxsqKCyCYUFtds" @touchend="tap2"/>

	</view>
</template>

<script>
import uCharts from '@/uni_modules/qiun-data-charts/js_sdk/u-charts/u-charts.js';
const db = wx.cloud.database();
var uChartsInstance = {};
var uChartsInstanceTwo = {};
	export default {
		data() {
			return {
				 cWidth: 750,
        cHeight: 500,
				info:{},
				background: {
					backgroundColor: '#4575F6 ',
				},
				navId:1,
				chartData:{
			 		 categories: [],
        		series: [
          	    {
                name: "志愿人数",
                data: [35,8,25,37,4],
              }
             ]
			  },
				chartDataTwo:{
			 		 categories: [],
        		series: [
          	    {
                name: "志愿人数",
                data: [35,8,25,37,4],
								color:''
              }
             ]
			  },
				chart:{
					0:[
          	 {
              name: "志愿人数",
              data: [35,8,25,37,4],
              }
             ],
					1:[
          	 {
              name: "累计服务时长",
              data: [20,36,50,60,70],
              }
             ],
						2:[
          	 {
              name: "人均参与",
              data: [2,4,3,6,2],
              }
             ],
						 3:[
          	 {
              name: "人均服务时长",
              data: [3,2,1,4,2],
              }
             ], 
				},
				chart2:{
					0:[
          	 {
              name: "志愿人数",
              data: [35,8,25,37,4],
              }
             ],
					1:[
          	 {
              name: "累计服务时长",
              data: [20,36,50,60,70],
              }
             ],
						2:[
          	 {
              name: "人均参与",
              data: [2,4,3,6,2],
              }
             ],
						 3:[
          	 {
              name: "人均服务时长",
              data: [3,2,1,4,2],
              }
             ], 
				},
			};
		},
		onLoad(){
			this.GetTime();
		},
		onShow(){
				this.getInfo();
		},
	 onReady() {
    //这里的 750 对应 css .charts 的 width
    this.cWidth = uni.upx2px(750);
    //这里的 500 对应 css .charts 的 height
    this.cHeight = uni.upx2px(500);
    this.getServerData();
    this.getServerData2();
  },
		methods:{
			toggleNav(id) {
				this.navId = id;
				console.log("打死你",this.chart[id-1])
				this.getServerData(this.chart[id-1])
			 this.getServerData2(this.chart2[id-1])
			},
		 getServerData(data) {
			if(data){
				this.chartData.series[0]=data[0];
				 this.drawCharts('vqRFKDqyikohqxUjPEEvMWgfCoIghlMQ', this.chartData);
			}else{
				 this.drawCharts('vqRFKDqyikohqxUjPEEvMWgfCoIghlMQ', this.chartData);
        this.drawChartsTwo('UrrAcuZxsqKCyCYUFtds', this.chartDataTwo);
			}
       
    },
		 getServerData2(data) {
			if(data){
				this.chartDataTwo.series[0]=data[0];
        this.drawChartsTwo('UrrAcuZxsqKCyCYUFtds', this.chartDataTwo);
			}else{
        this.drawChartsTwo('UrrAcuZxsqKCyCYUFtds', this.chartDataTwo);
			}
       
    },
		GetTime() {
      var date = new Date();
      var base = Date.parse(date); // 转换为时间戳
      // var year = date.getFullYear(); //获取当前年份
      var mon = date.getMonth() + 1; //获取当前月份
      var week = date.getDay();
      var day = date.getDate(); //获取当前日
      var oneDay = 24 * 3600 * 1000;
      var daytime = `${mon >= 10 ? mon : "0" + mon}${
        day >= 10 ? day : "0" + day
      }${week}`; //今日时间
      this.$data.daytime = daytime; // 今日时间赋值给变量
      var daytimeArr = [];
      for (var i = 1; i < 6; i++) {
        //前七天的时间
        var now = new Date((base -= oneDay));
        var month = now.getMonth() + 1;
        var mday = now.getDate();
        var week = now.getDay();
        daytimeArr.unshift(
          [
            month >= 10 ? month : "0" + month,
            mday >= 10 ? mday : "0" + mday,
          ].join("-")
        );
      }
			this.chartDataTwo.categories=daytimeArr;
			this.chartData.categories=daytimeArr;
			console.log("sdf",this.chartDataTwo);
    },
    drawCharts(id,data){
			data.series[0].color="#FFFFFF"
      const ctx = uni.createCanvasContext(id, this);
      uChartsInstance[id] = new uCharts({
        type: "line",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        categories: data.categories,
        series: data.series,
        animation: true,
        timing: "easeOut",
        duration: 1000,
        rotate: false,
        rotateLock: false,
        color: ["white"],
        padding: [15,10,0,0],
        fontSize: 11,
        fontColor: "#FFFFFF",
        dataLabel: true,
        dataPointShape: true,
        dataPointShapeType: "solid",
        touchMoveLimit: 60,
        enableScroll: false,
        enableMarkLine: false,
        legend: {
          show: false,
          position: "bottom",
          float: "center",
          padding: 5,
          margin: 5,
          backgroundColor: "rgba(0,0,0,0)",
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 0,
          fontSize: 13,
          fontColor: "#666666",
          lineHeight: 11,
          hiddenColor: "#CECECE",
          itemGap: 10
        },
        xAxis: {
          disableGrid: true,
          disabled: false,
          axisLine: true,
          axisLineColor: "#CCCCCC",
          calibration: false,
          fontColor: "#fff",
          fontSize: 11,
          rotateLabel: false,
          rotateAngle: 45,
          itemCount: 5,
          boundaryGap: "center",
          splitNumber: 5,
          gridColor: "#CCCCCC",
          gridType: "solid",
          dashLength: 4,
          gridEval: 1,
          scrollShow: false,
          scrollAlign: "left",
          scrollColor: "#A6A6A6",
          scrollBackgroundColor: "#EFEBEF",
          format: ""
        },
        yAxis: {
          gridType: "solid",
          dashLength: 2,
          disabled: false,
          data:[{
            min:0
          }],
          disableGrid: false,
          splitNumber: 5,
          gridColor: "#CCCCCC",
          padding: 10,
          showTitle: false,
          data: []
        },
        extra: {
          line: {
            type: "curve",
            width: 2
          },
          tooltip: {
            showBox: true,
            showArrow: true,
            showCategory: false,
            borderWidth: 0,
            borderRadius: 8,
            borderColor: "#000000",
            borderOpacity: 0.7,
            bgColor: "#FFFFFF",
            bgOpacity: 1,
            gridType: "solid",
            dashLength: 4,
            gridColor: "#CCCCCC",
            fontColor: "#4575F6",
            splitLine: false,
            horizentalLine: false,
            xAxisLabel: false,
            yAxisLabel: false,
            labelBgColor: "#FFFFFF",
            labelBgOpacity: 0.7,
            labelFontColor: "#666666"
          },
          markLine: {
            type: "solid",
            dashLength: 4,
            data: []
          }
        }
      });
    },
    tap(e){
      uChartsInstance[e.target.id].touchLegend(e);
      uChartsInstance[e.target.id].showToolTip(e);
    },
		drawChartsTwo(id,data){
			data.series[0].color="#1890FF"
      const ctx = uni.createCanvasContext(id, this);
      uChartsInstanceTwo[id] = new uCharts({
        type: "line",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        categories: data.categories,
        series: data.series,
        animation: false,
        timing: "easeOut",
        duration: 1000,
        rotate: false,
        rotateLock: false,
        background: "rgba(224,57,151,1)",
        color: ["#1890FF"],
        padding: [15,10,0,15],
        fontSize: 13,
        fontColor: "#666666",
        dataLabel: true,
        dataPointShape: true,
        dataPointShapeType: "solid",
        touchMoveLimit: 60,
        enableScroll: false,
        enableMarkLine: false,
        legend: {
          show: true,
          position: "bottom",
          float: "center",
          padding: 5,
          margin: 5,
          backgroundColor: "rgba(0,0,0,0)",
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 0,
          fontSize: 13,
          fontColor: "#666666",
          lineHeight: 11,
          hiddenColor: "#CECECE",
          itemGap: 10
        },
        xAxis: {
          disableGrid: true,
          disabled: false,
          axisLine: true,
          axisLineColor: "#CCCCCC",
          calibration: false,
          fontColor: "#666666",
          fontSize: 13,
          rotateLabel: false,
          rotateAngle: 45,
          itemCount: 5,
          boundaryGap: "center",
          splitNumber: 5,
          gridColor: "#CCCCCC",
          gridType: "solid",
          dashLength: 4,
          gridEval: 1,
          scrollShow: false,
          scrollAlign: "left",
          scrollColor: "#A6A6A6",
          scrollBackgroundColor: "#EFEBEF",
          formatter: ""
        },
        yAxis: {
          gridType: "dash",
          dashLength: 2,
          data:[{
            min:0
          }],
          disabled: false,
          disableGrid: false,
          splitNumber: 5,
          gridColor: "#CCCCCC",
          padding: 10,
          showTitle: false,
          data: []
        },
        extra: {
          line: {
            type: "curve",
            width: 2
          },
          tooltip: {
            showBox: true,
            showArrow: true,
            showCategory: false,
            borderWidth: 0,
            borderRadius: 0,
            borderColor: "#000000",
            borderOpacity: 0.7,
            bgColor: "#000000",
            bgOpacity: 0.7,
            gridType: "solid",
            dashLength: 4,
            gridColor: "#CCCCCC",
            fontColor: "#FFFFFF",
            splitLine: true,
            horizentalLine: false,
            xAxisLabel: false,
            yAxisLabel: false,
            labelBgColor: "#FFFFFF",
            labelBgOpacity: 0.7,
            labelFontColor: "#666666"
          },
          markLine: {
            type: "solid",
            dashLength: 4,
            data: []
          }
        }
      });
    },
    tap2(e){
      uChartsInstanceTwo[e.target.id].touchLegend(e);
      uChartsInstanceTwo[e.target.id].showToolTip(e);
    },
		savaNotice(ctx) {
			const that = this
			uni.showLoading()
			const timeId = setTimeout(() => {
				//生成图片
				uni.canvasToTempFilePath({
					canvasId: 'UrrAcuZxsqKCyCYUFtds',
					fileType: 'jpg',
					quality: 1,
					success(res) {
						uni.hideLoading()
						// 保存到相册
						uni.saveImageToPhotosAlbum({
							filePath: res.tempFilePath,
							success(data) {
								console.log("保存成功")
							},
							fail(err) {
								if (err.errMsg == 'saveImageToPhotosAlbum:fail auth deny') {
									that.showOpenstting = true
								}
							}
						})
					},
					fail(err) {
						console.log(err)
						uni.hideLoading()
					}
				}, that)
			}, 500)
		},
		getInfo(){
      this.id=uni.getStorageSync('or');
      db.collection('organization').doc(this.id).get().then(res=>{
        console.log("dsa",res)
        this.info=res.data;
        console.log("sdf",this.info)
      })
    },
		}
	}
</script>

<style scoped lang="scss">
.ti{
  margin-top: 160rpx;
  color:#FFFFFF;
  text-align: center;
}
.w{
	background-color: #4575F6;
		height: 100vh;
}
.content {  
		background-color: #4575F6;
		padding: 48rpx 32rpx 20rpx 32rpx;
	}

	.service {
		display: flex;

		.card {
			margin-left: 8rpx;
			width: 224rpx;
			height: 160rpx;
			border-radius: 16rpx;
			background-color: white;

			.t1 {
				font-size: 24rpx;
				font-weight: 600;
				color: #333333;
			}

			.bottom {
				display: flex;
				align-items: flex-end;
				justify-content: space-between;

				.num {
					display: flex;
					align-items: flex-end;

					.n {
						font-size: 32rpx;
					}

					.tx {
						padding-left: 5rpx;
						font-size: 20rpx;
					}

				}

				.EN {
					font-size: 16rpx;
				}
			}

			.right {
				margin-right: 16rpx;

				.img {
					width: 48rpx;
					height: 48rpx;
				}
			}
		}
	}

	.right {
		display: flex;
		justify-content: center;
		.bt {
			display: flex;
			font-size: 24rpx;
			padding:8rpx 12rpx  8rpx 12rpx;
			justify-content: center;
			align-items: center;
			color: #FFFFFF;
			border: 1px solid #FFFFFF ;
			background: #4575F6;
			border-radius: 120rpx;
		}
		.active {
			background: #FFFFFF;
			color:#4575F6;
		}
	}
	.charts{
    width: 700rpx;
    height: 500rpx;
  }
	.btn{
	  color:#4575F6;
		margin-top: 15rpx;
		width: 560rpx;
		height: 96rpx;
		border-radius: 56rpx;
		background: #FFFFFF;
	}
	button::after {
	   border: none;
	}
	button::after{ 
		border: none; 
	}
</style>
