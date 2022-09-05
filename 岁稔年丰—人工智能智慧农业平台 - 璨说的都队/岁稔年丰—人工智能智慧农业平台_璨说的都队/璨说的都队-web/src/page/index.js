import React from 'react'
import './index.less'
import moment from 'moment'
import  api from '../service'
import * as echarts from 'echarts';
import {Carousel, PageHeader, Button, Tag, Typography,Comment, Tooltip, List  } from 'antd'
import { DislikeOutlined, LikeOutlined, DislikeFilled, LikeFilled } from '@ant-design/icons';
import { ArrowUpOutlined, ArrowDownOutlined } from '@ant-design/icons';

import { Tabs, Statistic, Descriptions, Card, Row, Col} from 'antd'
const { TabPane } = Tabs;
const { Paragraph } = Typography;
const Content = ({ children, extra }) => (
    <div className="content">
        <div className="main">{children}</div>
        <div className="extra">{extra}</div>
    </div>
);
const contentStyle = {
    height: '320px',
    color: '#fff',
    lineHeight: '250px',
    textAlign: 'center',
    background: '#364d79',
};

const data = [
    {
        actions: [<span key="comment-list-reply-to-0">Reply to</span>],
        author: '中北大学',
        avatar: 'https://www.twoyl.cn/xczx/au.jpg',
        content: (
            <p>
                乡村民宿全国大数据建设亟待提速作为乡村振兴新业态，乡村民宿亟待加快全国大数据建设，满足市场要求。
                中国旅游协会民宿客栈与精品酒店分会会长张晓军26日表示，乡村民宿高质量发展，需要加快建设大数据库，
                更好地面向全域市场。不同地区市场特征不同，需要权威的信息统计、反馈、处理机制
                ，要尽快把全国乡村民宿数据中心建起来。
            </p>
        ),
        datetime: (
            <Tooltip>
                2021-07-28 18:41:16
            </Tooltip>
        ),
    },
    {
        actions: [<span key="comment-list-reply-to-0">Reply to</span>],
        author: '中北大学',
        avatar: 'https://www.twoyl.cn/xczx/au.jpg\n',
        content: (
            <p>
                建设数字乡村是乡村振兴战略方向，也是建设数字中国的重要内容。
                加强数字农业核心技术攻关，将农业大数据、农情立体感知、农作智慧管理等作为重要方向，加强生产性社会化服务，建立信息化服务平台，
                发展规模化生产、标准化协作的“云服务”模式，促进农业生产提质增效等。
            </p>
        ),
        datetime: (
            <Tooltip>
                       2021-07-28 18:45:16
            </Tooltip>
        ),
    },
];

const renderContent = (column = 2) => (
    <Descriptions size="small" column={column}>
        <Descriptions.Item label="管理">中北大学软件学院</Descriptions.Item>
        <Descriptions.Item label="用户量">
            <a>421421</a>
        </Descriptions.Item>
        <Descriptions.Item label="软件著作权">2021SR0675789</Descriptions.Item>
        <Descriptions.Item label="当前日期">{moment().format('YYYY-MM-DD HH:mm:ss')}</Descriptions.Item>
        <Descriptions.Item label="项目单位">
            常家坡村、新狮子沟村、新狮子沟南村
        </Descriptions.Item>
    </Descriptions>
);

const extraContent = (
    <div
        style={{
            display: 'flex',
            width: 'max-content',
            justifyContent: 'flex-end',
        }}
    >
        <Statistic
            title="状态"
            value="运行中"
            style={{
                marginTop: 22,
                marginRight: 66,
            }}
        />
        <Statistic title="销售额" prefix="" value={956.08}
                   style={{
                       marginTop: 22,
                   }}/>
        <div className="site-statistic-demo-card">
            <Row gutter={50} >
                <Col span={12} >
                    <Card    style={{backgroundColor: '#F0F2F5'}}>
                        <Statistic
                            title="用户增长率"
                            value={11.28}
                            precision={2}
                            valueStyle={{ color: '#cf1322' }}
                            prefix={<ArrowUpOutlined />}
                            suffix="%"
                        />
                    </Card>
                </Col>
                <Col span={12}>
                    <Card    style={{backgroundColor: '#F0F2F5'}}>
                        <Statistic
                            title="用户留存率"
                            value={12.3}
                            precision={2}
                            valueStyle={{ color: '#3f8600' }}
                            prefix={<ArrowDownOutlined />}
                            suffix="%"
                        />
                    </Card>
                </Col>
            </Row>
        </div>
    </div>

);
export default class Index extends React.Component{
    state={
        result: {}
    }
    componentDidMount() {
        // 初始化
        var myCharts = echarts.init(document.getElementById('main'));
        // 绘制图表
        myCharts.setOption({
            title: { text: '平台销售量' },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['农家乐','非遗文化']
            },
            toolbox: {
                show : true,
                feature : {
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {
                        show: true,
                        type: 'jpg'
                    }
                }
            },
            xAxis : [
                {
                    type : 'category',
                    data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'农家乐',
                    type:'bar',
                    data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大销售'},
                            {type : 'min', name: '最小销售'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均销售'}
                        ]
                    }
                },
                {
                    name:'非遗文化',
                    type:'bar',
                    data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大销售'},
                            {type : 'min', name: '最小销售'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均销售'}
                        ]
                    }
                },
            ]
        });

    }
    fire = ()=>{
       api.userList({pageIndex:1,pageSize:10})
            .then(res => res.json())
            .then(result=>{
               this.setState({
                   result
               });
            }).catch(e=>{
                console.log(e);
        })
    };

    render() {
        const {result} = this.state;
        return <PageHeader
            className="Homes"
            avatar={{ src: 'https://www.twoyl.cn/xczx/au.jpg\n' }}
            title="中北农服"
            subTitle="后台管理端"
            tags={<Tag color="blue">Running</Tag>}
            extra={[
                <Button key="1" type="primary">
                    移动端下载
                </Button>,
            ]}
            footer={
                <Tabs defaultActiveKey="1">
                    <TabPane tab="数据统计" key="1" >
                        <div id="main" style={{ width:'1200px',height:'400px', marginTop:'23px'}}></div>
                    </TabPane>
                    <TabPane tab="新闻通知" key="2" >
                        <List
                            className="comment-list"
                            header={`${data.length} 条信息`}
                            itemLayout="horizontal"
                            dataSource={data}
                            renderItem={item => (
                                <li>
                                    <Comment
                                        actions={item.actions}
                                        author={item.author}
                                        avatar={item.avatar}
                                        content={item.content}
                                        datetime={item.datetime}
                                    />
                                </li>
                            )}
                        />,
                    </TabPane>
                </Tabs>
            }
        >
            <Content extra={extraContent}>{renderContent()}</Content>
        </PageHeader>
    }
}
