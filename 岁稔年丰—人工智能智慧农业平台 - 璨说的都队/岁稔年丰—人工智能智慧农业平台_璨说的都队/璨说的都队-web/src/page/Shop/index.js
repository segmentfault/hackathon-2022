import React from 'react'
import './index.less'
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {notification, Popconfirm, Switch, Table,Input,Button} from "antd";
import api from "../../service";
import NavigationUtil,{RouteConfig}  from '../../component/Navigator'
const PAGE_SIZE  = 10;
export default class Index extends React.Component{
    state={
        data:[],
        total:0,
        loading:false,
        searchText:'',
        searchedColumn:''
    };

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

    handleClick(item){
        NavigationUtil.goto(RouteConfig.Shopmf,this.props.history)({state:{item}})
    }
    constructor(props) {
        super(props);
        this.columns=[
            {
                title:'ID',
                dataIndex:'Id',
                fixed:'left',
                ...this.getColumnSearchProps('Id')
            },
            {
                title:'标题',
                dataIndex:'Title',
                fixed:'left',
                ...this.getColumnSearchProps('Title')
            },
            {
                title:'日期',
                dataIndex:'Timea',
                ...this.getColumnSearchProps('Timea')
            },
            {
                title:'空间',
                dataIndex:'spaces',
                ...this.getColumnSearchProps('spaces')
            },{
                title:'手机号',
                dataIndex:'Tele',
                ...this.getColumnSearchProps('Tele')
            },{
                title:'内容',
                dataIndex:'Info',
                ...this.getColumnSearchProps('Info')
            },{
                title:'区域',
                dataIndex:'Areas',
                ...this.getColumnSearchProps('Areas')
            },{
                title:'链接',
                dataIndex:'Url',
                ...this.getColumnSearchProps('Url')
            },
            {
                title:'删除',
                dataIndex: 'operation',
                render:(text,record)=>{
                    return <Popconfirm
                        title={`确定要删除${record.ProdTitle}?`}
                        onConfirm={() => this.removecraft(record)}
                    >
                        <a>删除</a>
                    </Popconfirm>
                },
                width: "10%"
            },
            {
                title: <div className='config-operation'>操作</div>,
                dataIndex: 'operation',
                width: '10%',
                render:(text,record)=>{
                    return <div className='config-operation' onClick={()=>this.handleClick(record)}>
                        <a>编辑</a>
                    </div>
                }
            }
        ];
        const data = [];
        for (let i = 0; i < 100; i++) {
            data.push({
                key: i,
                name: `Edrward ${i}`,
                age: 32,
                address: `London Park no. ${i}`,
            });
        }
    }
    render() {
        const {data,total,loading} = this.state;
        return <Table
            columns={this.columns}
            scroll={{ x: 1500}}
            rowKey={item=>item.Id}
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
    componentDidMount() {
        this.setState({
            loading:true
        });
        this.loadData(1);
    }

    loadData=pageIndex=>{
        this.pageIndex = pageIndex
        api.ShopList({pageIndex,pageSize:PAGE_SIZE})
            .then(res=>res.json())
            .then(result=>{
                const {data:{list,total} = {}} = result;
                this.setState(
                    {
                        loading : false,
                        data :list,
                        total:total
                    }
                )
            }).catch(e=>{
            console.log(e)
            this.setState({
                loading:false
            })
        });
    };
    removecraft = record => {
        api.removeShop(record.Id)
            .then(res=>res.json())
            .then(result=>{
                this.loadData(this.pageIndex);
                this.openNotification();
            })
            .catch(e=>{
                console.log(e);
            });
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
    fOpenNotification = () => {
        notification['success']({
            message: '操作成功',
            duration: 3 ,
            description:
                '请认真确定您的操作是否存在错误，以免失误造成的不必要影响',
            onClick: () => {
                console.log('操作成功');
            },
        });
    };

}
