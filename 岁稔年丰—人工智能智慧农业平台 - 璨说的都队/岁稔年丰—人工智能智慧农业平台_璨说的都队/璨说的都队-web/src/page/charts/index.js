import React, { Component } from 'react'
import { Chart } from '@antv/g2';
class Hello extends Component {
    constructor(props){
        super(props);
        this.state = {
            data: [
                { name: '平台商品量', '名称': '精选', '平台商品量': 14 },
                { name: '平台商品量', '名称': '粮油', '平台商品量': 12 },
                { name: '平台商品量', '名称': '水果', '平台商品量': 13 },
                { name: '平台商品量', '名称': '蔬菜', '平台商品量': 10 },
                { name: '平台商品量', '名称': '肉禽', '平台商品量': 12 },
                { name: '平台商品量', '名称': '其他', '平台商品量': 14 },
            ],
            pieData:[
                { item: '精选', count: 14, percent: 0.186 },
                { item: '粮油', count: 12, percent: 0.16 },
                { item: '水果', count: 13, percent: 0.173 },
                { item: '蔬菜', count: 10, percent: 0.133 },
                { item: '肉禽', count: 12, percent: 0.16 },
                { item: '其他', count: 14, percent: 0.186 },
            ],
            tDate:[
                { date: '2021/6',type: '手工艺品', value: 55 },
                { date: '2021/6',type: '农产品购买', value: 25 },
                { date: '2021/6',type: '农家乐住宿', value: 13 },
                { date: '2021/6',type: '课程学习', value: 42 },
                { date: '2021/6',type: '促就业人数', value: 91 },
                { date: '2021/6',type: '用户总量', value: 234 },
                { date: '2021/7',type: '手工艺品', value: 14 },
                { date: '2021/7',type: '农产品购买', value: 85 },
                { date: '2021/7',type: '农家乐住宿', value: 103 },
                { date: '2021/7',type: '课程学习', value: 142 },
                { date: '2021/7',type: '促就业人数', value: 491 },
                { date: '2021/7',type: '用户总量', value: 1234 },
                { date: '2021/8',type: '手工艺品', value: 34 },
                { date: '2021/8',type: '农产品购买', value: 185 },
                { date: '2021/8',type: '农家乐住宿', value: 123 },
                { date: '2021/8',type: '课程学习', value: 442 },
                { date: '2021/8',type: '促就业人数', value: 591 },
                { date: '2021/8',type: '用户总量', value: 1334 },
            ]
        }
    }
    componentDidMount(){
        this.readHistogram()
        this.readPieChart()
        this.readt()
    }
    readHistogram(){
        const chart = new Chart({
            container: 'container',
            autoFit: true,
        });
        chart.data(this.state.data);
        chart.scale('平台商品量', {
            nice: true,
        });
        chart.tooltip({
            showMarkers: false,
            shared: true,
        });

        chart
            .interval()
            .position('名称*平台商品量')
            .color('name')
            .adjust([
                {
                    type: 'dodge',
                    marginRatio: 0,
                },
            ]);

        chart.interaction('active-region');
        chart.render();
    }
    readPieChart(){
        const chart = new Chart({
            container: 'container2',
            autoFit: true,
        });
        chart.data(this.state.pieData);

        chart.coordinate('theta', {
            radius: 0.85
        });

        chart.scale('item', {
            // formatter: (val) => {
            //     val = val * 100 + '%';
            //     return val;
            // },
        });
        chart.tooltip({
            showTitle: false,
            showMarkers: false,
        });
        chart.axis(false); // 关闭坐标轴
        const interval = chart
            .interval()
            .adjust('stack')
            .position('percent')
            .color('item')
            .label('item', {
                offset: -40,
                style: {
                    textAlign: 'center',
                    shadowBlur: 2,
                    shadowColor: 'rgba(0, 0, 0, .45)',
                    fill: '#fff',
                },
            })
            .tooltip('item*percent', (item, percent) => {
                percent = percent * 100 + '%';
                return {
                    name: item,
                    value: percent,
                };
            })
            .style({
                lineWidth: 1,
                stroke: '#fff',
            });
        chart.interaction('element-single-selected');
        chart.render();

        // 默认选择
        interval.elements[0].setState('selected', true);
    }
    readt(){
        const chart = new Chart({
            container: 'container3',
            autoFit: true,
            height: 500,
        });
        chart.data(this.state.tDate);
        chart.scale({
            value: {
                max: 1500,
                min: 0,
                alias: '人数（千）',
            },
        });
        chart.axis('type', {
            title: null,
            tickLine: null,
            line: null,
        });

        chart.axis('value', {
            label: null,
            title: {
                offset: 30,
                style: {
                    fontSize: 12,
                    fontWeight: 300,
                },
            },
        });
        chart.legend({
            marker: 'circle',  //图例的 marker 样式
            position: 'bottom', //位置
            offsetX: 30,
            useHtml: true,//使用Html绘制图例
            flipPage:true//图例超出容器是否滚动
        });

        //绘制折线图
        chart.line().position('date*value').color('type');  //x轴:date  y轴：value ，折线根据type分颜色
        //设置折点样式
        chart.point().position('date*value').size(3).color('type').shape('circle');
        chart.render();
    }
    render() {
        const itemStyle = {width: '40vw',height:'50vh' ,marginTop:'30px' };
        const itemStyle3 = {width: '79vw',height:'50vh',marginTop:'50px'};
        const divStyle = {display:'flex',flexWrap:'wrap'};
        return (
            <div style = {divStyle}>
                <div style = {itemStyle} id="container"></div>
                <div style = {itemStyle} id="container2"></div>
                <div style = {itemStyle3} id="container3"></div>
            </div>
        );
    }
}

export default Hello;
