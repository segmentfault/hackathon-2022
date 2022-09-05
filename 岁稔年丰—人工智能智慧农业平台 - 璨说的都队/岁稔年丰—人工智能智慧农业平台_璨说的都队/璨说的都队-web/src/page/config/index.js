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
        NavigationUtil.goto(RouteConfig.configMf,this.props.history)({state:{item}})
    }
    constructor(props) {
        console.log(props)
        super(props);
        this.columns=[
            {
                title:'手工艺品ID',
                dataIndex:'tCraftId',
                width:'10%',
                key:'id',
                fixed: 'left',
                ...this.getColumnSearchProps('tCraftId')
            },
            {
                title:'手工艺名称',
                key:'name',
                fixed: 'left',
                dataIndex:'tCraftName',
                ...this.getColumnSearchProps('tCraftName')
            },
            {
                title:'手工艺种类',
                dataIndex:'tCraftKind',
                key: '1',
                ...this.getColumnSearchProps('tCraftKind')
            },
            {
                title:'手工艺价格',
                key: '2',
                dataIndex:'tCraftPrice',
                ...this.getColumnSearchProps('tCraftPrice')
            },
            {
                title:'创建时间',
                key: '3',
                dataIndex:'CreatTime',
                ...this.getColumnSearchProps('CreatTime')
            },
            {
                title:'图片链接',
                key: '4',
                dataIndex:'CraftUrl',
                 ...this.getColumnSearchProps('CraftUrl')
            },
            {
                title:'描述',
                key: '5',
                dataIndex:'CraftDesc',
                 ...this.getColumnSearchProps('CraftDesc')
            },
            {
                title:'商贩_id',
                key: '6',
                dataIndex:'VendId',
                 ...this.getColumnSearchProps('VendId')
            },
            {
                title:'删除',
                key: '7',
                dataIndex: 'operation',
                render:(text,record)=>{
                    return <Popconfirm
                        title={`确定要删除${record.tCraftName}?`}
                        onConfirm={() => this.removecraft(record)}
                    >
                        <a>删除</a>
                    </Popconfirm>
                },
                width: "6%"
            },
            {
                title: '是否非遗',
                key: '5',
                fixed: 'right',
                width: '6%',
                dataIndex: 'tCulHeritage',
                render: (texts, record) => {
                    return <Popconfirm
                        title={`确定要${record.tCulHeritage==='1'?'是':'否'}`}
                        onConfirm={() => this.toggleheritage(record)}
                    >
                        <Switch checkedChildren="是"
                                unCheckedChildren="否"
                                checked={texts != "0"}>
                        </Switch>
                    </Popconfirm>
                }
            },
            {
                title: <div className='config-operation'>操作</div>,
                dataIndex: 'operation',
                width: '6%',
                key: 'operation',
                fixed: 'right',
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
            rowKey={item=>item.tCraftId}
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
        api.craftList({pageIndex,pageSize:PAGE_SIZE})
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
        api.removeCraft(record.tCraftId)
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
    toggleheritage=(record)=>{
        const CulHeritage = record.tCulHeritage === '1' ? 0 : 1;
        console.log(CulHeritage);
        api.updataCraft({CulHeritage})(record.tCraftId)
            .then(res=>res.json())
            .then(result=>{
                this.loadData(this.pageIndex);
                this.fOpenNotification();
            }).catch(e=>{
            console.log(e)
        })

    }
}
