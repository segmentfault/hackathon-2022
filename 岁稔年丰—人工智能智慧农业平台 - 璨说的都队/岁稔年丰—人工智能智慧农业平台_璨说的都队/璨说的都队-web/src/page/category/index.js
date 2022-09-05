import React from 'react'
import './index.less'
import {Table, Popconfirm, notification, Input, Button} from 'antd';
import api from '../../service'
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import NavigationUtil, {RouteConfig} from "../../component/Navigator";
import Bmob from "hydrogen-js-sdk";

const PAGE_SIZE  = 10;
Bmob.initialize("afe45ecf591b5301", "as7897189");

export default class Index extends React.Component{
    state={
        data:[],
        total:0,
        loading:false,
        searchText:'',
        searchedColumn:''

    };
    handleClick(item){
        NavigationUtil.goto(RouteConfig.categoryMf,this.props.history)({state:{item}})
    }
    getColumnSearchProps = dataIndex=>({
        filterDropdown:({ setSelectedKeys, selectedKeys, confirm, clearFilters }) =>(
            <div style={{padding:8}}>
                <Input   ref={node => {
                    this.searchInput = node;
                }}
                         placeholder={`搜索 ${dataIndex}`}
                         value={selectedKeys[0]}
                         onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                         onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
                         style={{ marginBottom: 8, display: 'block' }}
                ></Input>
                <Button type='primary'
                        onClick={()=>this.handleSearch(selectedKeys,confirm,dataIndex)}
                        icon={<SearchOutlined />}
                        size='small'
                        style={{width : 90,marginRight: 8}}
                >
                    搜索
                </Button>
                <Button
                    onClick={()=>this.handleRestet(clearFilters)}
                    size='small'
                    style={{width : 90}}
                >
                    重置
                </Button>
            </div>
        ),
        filterIcon:filtered=>(
            <SearchOutlined style={{color:filtered ? '#1990ff' : undefined}}></SearchOutlined>
        ),
        onFilter:(value,record)=>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase())
        ,onFilterDropdownVisibleChange: visible => {
            if (visible) {
                console.log(visible)
                setTimeout(() => this.searchInput.select());
            }
        },
        render:text=>(
            (this.state.searchedColumn === dataIndex)?
                <Highlighter highlightStyle={{backgroundColor:'#ffc069',padding:0}}
                             searchWords={[this.state.searchText]}
                             autoEscape
                             textToHighlight={text.toString()}
                ></Highlighter>:text&&text.toString()
        )
    });

    constructor(props) {
        super(props);
        this.columns=[
            {
                title:'ID',
                dataIndex:'objectId',
                width:'13%',
                fixed: 'left',
                key:'id',
                ...this.getColumnSearchProps('objectId')
            },
            {
                title:'下单产品',
                key: '1',
                dataIndex:'prodtitle',
                ...this.getColumnSearchProps('prodtitle')
            },
            {
                title:'用户手机号',
                dataIndex:'phone',
                fixed: 'left',
                key:'name',
                ...this.getColumnSearchProps('phone')
            },
            {
                title:'地址',
                dataIndex:'address',
                fixed: 'left',
                key:'name',
                ...this.getColumnSearchProps('address')
            },
            {
                title:'用户姓名',
                dataIndex:'name',
                fixed: 'left',
                key:'name',
                ...this.getColumnSearchProps('name')
            }
            ,
            {
                title:'下单时间',
                key: '1',
                dataIndex:'createdAt',
                ...this.getColumnSearchProps('createdAt')
            },
            {
                title: '下单总价',
                key: '1',
                dataIndex: 'price',
                ...this.getColumnSearchProps('price')
            }
            ,
            {
                title:'删除',
                dataIndex: 'operation',
                render:(text,record)=>{
                    return <Popconfirm
                     title={`确定要删除${record.categoryName}?`}
                     onConfirm={() => this.removeCategory(record)}
                    >
                        <a>删除</a>
                    </Popconfirm>
            },
                width: "7%"
            },
            {
                title: <div className='config-operation'>操作</div>
                ,
                dataIndex: 'operation',
                render:(text,record)=>{
                    return <div className='config-operation' onClick={()=>this.handleClick(record)}>
                        <a>编辑</a>
                    </div>
                }
            }



        ];
    }


    render() {
        const {data,total,loading} = this.state;
        return <Table
               columns={this.columns}
               rowKey={item=>item.categoryId}
               dataSource={data}
               pagination={
                   {
                    total,
                    pageSize:PAGE_SIZE,
                       onChange:(page,pageSize)=>{
                        this.loadData(page);
                       }
                   }
               }
               loading={loading}
        ></Table>
    }

    handleSearch=(selectedKeys,confirm,dataIndex) => {
        confirm();
        this.setState({
            searchText:selectedKeys[0],
            searchedColumn:dataIndex
        })
    };

    handleRestet=clearFilters=>{
        clearFilters();
        this.setState({
            searchText:''
        })
    }

    componentDidMount() {
        this.setState({
            loading:true
        });
        this.loadData(1);
    }

    loadData=pageIndex=>{
        this.pageIndex = pageIndex
        const query = Bmob.Query("order_buy");
        query.find().then(res => {
            const {userid = {}} = res;
            console.log(userid)
            this.setState(
                {
                    loading : false,
                    data :res,
                    total:res.length,
                }
            )
        }).catch(e => {
            console.log(e)
            this.setState({
                loading:false
            })
        });
            // api.categoryList({pageIndex,pageSize:PAGE_SIZE})
            //     .then(res=>res.json())
            //     .then(result=>{
            //         const {data:{list,total} = {}} = result;
            //         this.setState(
            //             {
            //                 loading : false,
            //                 data :list,
            //                 total:total
            //             }
            //         )
            //     }).catch(e=>{
            //         console.log(e)
            //     this.setState({
            //         loading:false
            //     })
            // });
    };
    removeCategory = record => {
        const query = Bmob.Query('order_buy');
        query.destroy(record.objectId).then(res => {
            this.loadData(this.pageIndex);
            this.openNotification();
        }).catch(err => {
            console.log(err)
        })

    };
    openNotification = () => {
        notification['success']({
            message: '删除成功',
            duration: 3 ,
            description:
                '请认真确定您的操作是否存在错误，以免误删造成的不必要影响',
            onClick: () => {
                console.log('删除成功');
            },
        });
    };
}
