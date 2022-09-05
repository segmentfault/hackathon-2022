<template>
    <view class="page">
        <comHead>志愿测试</comHead>
        <view class="box u-flex">
            <view class="fs36 u-flex title fw600 colorW">
                <view>出题{{ index + 1 }}</view>
                <view class="fs24 colorW u-m-l-4">/5</view>
            </view>
            <u-line-progress class="u-m-t-16 line" height="24" active-color="#4674F4" inactive-color="rgba(72, 116, 242, 0.4)" :show-percent="false" :percent="percent"></u-line-progress>
            <view class="colorW fs36 fw600 u-m-t-48">{{ info[index].title }}</view>
            <view v-show="show">
                <view class="chooseBtn fs28" :class="{ active: navId == 0 }" @click="toggleNav(0)">{{ info[index].A }}</view>
                <view class="chooseBtn fs28" :class="{ active: navId == 1 }" @click="toggleNav(1)">{{ info[index].B }}</view>
                <view class="chooseBtn fs28" :class="{ active: navId == 2 }" @click="toggleNav(2)">{{ info[index].C }}</view>
                <view class="chooseBtn fs28" :class="{ active: navId == 3 }" @click="toggleNav(3)">{{ info[index].D }}</view>
            </view>
            <view v-show="!show" class="select u-flex">
                <view class="chooseBtn2 fs28" :class="{ active:index==3? answer4.indexOf(i) != -1:answer5.indexOf(i) != -1 }" v-for="(item, i) in info[index].t4" @click="appointment(i)" :key="i">
                    {{ item.name }}
                </view>
            </view>
        </view>
        <button class="btn" @click="next(index)">{{msg}}</button>
    </view>
</template>

<script>
import comHead from "@/components/com-head.vue";
const db = wx.cloud.database();
export default {
    components: {
        comHead,
    },
    data() {
        return {
            background: {
                backgroundColor: "#7d9fed ",
            },
            msg: '下一题',
            info: [{
                    title: "1、关于人生观，我的内心其实是：",
                    A: "A、希望能有各种人生体验，想法多样化",
                    B: "B、在合理的基础上，谨慎确定目标、确定就会坚定不移做",
                    C: "C、更加在乎取得一切可能的成就",
                    D: "D、毫不喜欢风险，喜欢享受稳定或现状",
                },
                {
                    title: "2、如果爬山旅游，大多数情况下，在下山回来的路线我最可能：",
                    A: "A、好玩又有趣，所以宁愿新路线会巢",
                    B: "B、安全稳妥，所以宁愿原路线返回",
                    C: "C、跳转困难，所以宁愿新路线会巢",
                    D: "D、方便省心，所以宁愿原路线返回",
                },
                {
                    title: "3、说话时，我更看重",
                    A: "A、感觉效果。有时可能会略显得夸张",
                    B: "B、秒杀精准。有时可能过于略过冗长",
                    C: "C、达成结果。有时可能过于直接让别人不开心",
                    D: "D、人际感受，有时可能会不愿讲真话",
                },
                {
                    title: "4、在以下服务领域中，您会愿意参加哪些项目？（多选）",
                    t4: [{
                            name: "扶贫济困",
                        },
                        {
                            name: "助老助残",
                        },
                        {
                            name: "社区服务",
                        },
                        {
                            name: "生态建设",
                        },
                        {
                            name: "大型活动",
                        },
                        {
                            name: "抢险救灾",
                        },
                        {
                            name: "社会管理",
                        },
                        {
                            name: "文化建设",
                        },
                        {
                            name: "西部开发",
                        },
                        {
                            name: "综合/其他",
                        },
                    ],
                },
                {
                    title: "5、您比较擅长于以下哪些技能？（多选）",
                    t4: [
                        { name: '设计/平面' },
                        { name: '摄影剪辑' },
                        { name: '活动策划' },
                        { name: '线下执行' },
                        { name: '文化艺术' },
                        { name: 'IT互联网技术' },
                        { name: '新媒体运营' },
                        { name: '教育培训' },
                        { name: '应急救援' },
                        { name: '医疗护理' },
                        { name: '交通运输' },
                        { name: '环境科学' },
                        { name: '体育' },
                        { name: '会计审计' },
                        { name: '文化和旅游' },
                        { name: '我都可以' },

                    ]
                },
                {},
            ],

            show: true,
            percent: 20,
            index: 0,
            navId: -1,
            answer: [
                0,
                0,
                0,
            ],
            answer4: [],
            answer5: [],
        };
    },
    onLoad(option) {},
    methods: {
        next() {
            if (this.index == 4) {
                console.log("答案", this.answer)
                console.log(this.answer4)
                console.log(this.answer5)
                uni.redirectTo({
                    url: `/pagesA/voTest/testResults?answer=${this.answer}&answer4=${this.answer4}&answer5=${this.answer5}`
                })
            }
            if (this.navId == -1) {
                uni.showToast({
                    title: "请选择答案",
                    icon: "none",
                });
                return;
            }
            if (this.index == 3 || this.index == 4) {
                if (this.answer4.length == 0) {
                    uni.showToast({
                        title: "请选择项目",
                        icon: "none",
                    });
                    return;
                }
                if (this.answer5.length == 0 && this.index == 4) {
                    uni.showToast({
                        title: "请选择项目",
                        icon: "none",
                    });
                }
            }

            this.percent += 20;
            this.answer[this.index] = this.navId;
            this.index++;
            if (this.index == 4) {
                this.msg = '提交'
            }
            this.index == 3 ? (this.show = false) : 0;
            this.navId = -1;
            console.log("dasb", this.answer);
        },
        toggleNav(id) {
            this.navId = id;
        },

        appointment(index) {
            console.log(index);
            let that = this;
            if (this.index == 3) {
                if (that.answer4.indexOf(index) == -1) {
                    // console.log(index); //打印下标
                    that.answer4.push(index); //选中添加到数组里
                    this.navId = 1;
                } else {
                    that.answer4.splice(that.answer4.indexOf(index), 1); //取消
                }
            } else {
                if (that.answer5.indexOf(index) == -1) {
                    // console.log(index); //打印下标
                    that.answer5.push(index); //选中添加到数组里
                    this.navId = 1;
                } else {
                    that.answer5.splice(that.answer5.indexOf(index), 1); //取消
                }
            }


        },
    },
};
</script>

<style scoped lang="scss">
.page {
    display: flex;
    flex-direction: column;
    background: url(https://717a-qzycloud-1grvormi21122cee-1312385448.tcb.qcloud.la/%E5%BF%97%E6%84%BF%E6%B5%8B%E8%AF%95.jpg?sign=3385c4169b98766ee8db6faf660521f0&t=1661482999) no-repeat center;
    background-size: 100% 100%;
    min-height: 100vh;
    .box {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 48rpx 48rpx 74rpx 48rpx;
        margin: 224rpx 32rpx 0 32rpx;
        height: 956rpx;
        background: rgba(255, 255, 255, 0.05);
        box-shadow: 0px 0px 106rpx 0px rgba(77, 119, 237, 0.39);
        opacity: 1;
        border-radius: 32rpx;
        border: 2rpx solid;
        border-color: white;
        .title {
            width: 100%;
            align-items: baseline;
        }
        .line {
            width: 100%;
        }
    }
    .colorW {
        color: #ffffff;
    }
    .btn {
        color: white;
        margin-top: 194rpx;
        border: 2rpx solid;
        border-color: white;
        width: 560rpx;
        height: 96rpx;
        border-radius: 56rpx;
        background: rgba(69, 117, 246, 0.5);
    }
    .chooseBtn {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        margin-top: 32rpx;
        padding: 24rpx 60rpx 24rpx 60rpx;
        width: 100%;
        background: rgba(255, 255, 255, 0.3);
        text-align: center;
        border-radius: 82rpx;
        color: #ffffff;
        font-size: 28rpx;
    }
    .chooseBtn2 {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        margin-left: 32rpx;
        margin-top: 32rpx;
        padding: 8rpx 24rpx 8rpx 24rpx;
        background: rgba(255, 255, 255, 0.3);
        text-align: center;
        border-radius: 82rpx;
        color: #ffffff;
    }
    .select {
        flex-wrap: wrap;
    }
    .active {
        background: rgba(70, 116, 244, 1);
        border: 2rpx solid;
        border-color: white;
    }
    .end {
        text-align: center;
    }
}
</style>
