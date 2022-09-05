import React from 'react'
import {Table, Switch, Popconfirm, notification,Space,Input,Button} from 'antd';
import Highlighter from 'react-highlight-words';
import { SearchOutlined } from '@ant-design/icons';
import './index.less'
import api from '../../service'

const pageSize = 10;

export default class Index extends React.Component{
    state={
        data:[],
        total:0,
        loading:false,
        searchText:'',
        searchedColumn:''
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
        super();
        this.columns=[
            {
                title:'UID',
                dataIndex:'uid',
                ...this.getColumnSearchProps('uid')
            },
            {
                title: "用户ID",
                dataIndex: 'userId',
                ...this.getColumnSearchProps('userId')
            },
            {
                title: '用户名',
                dataIndex: 'userName',
                ...this.getColumnSearchProps('userName')
            },
            {
                title: '创建时间',
                dataIndex: 'creatTime',
                ...this.getColumnSearchProps('creatTime')
            },
            {
                title: '是否封禁',
                dataIndex: 'forbid',
                render:(text,record) => {
                     return<Popconfirm
                      title = {`确定要${record.forbid==='1'?'解禁':'冻结'}`}
                      onConfirm = {() => this.toggleForbid(record)}
                    >
                        <Switch checkedChildren="正常"
                                    unCheckedChildren="冻结"
                                    checked={text != "1"}>
                     </Switch>
                    </Popconfirm>
                },
                width: '20%'
            }
        ];
    }
    render() {
        const {data,total,loading} = this.state;
        return(
            <Table
                loading={loading}
                rowKey={item=>item.uid}
                dataSource={data}
                pagination={{
                    total,
                    pageSize : pageSize,
                    onChange: (page,pageSize) =>{
                        this.loadData(page)
                    }

                }}
                columns={this.columns}>
            </Table>
        )
    }

    componentDidMount() {
        this.setState({
            loading:true
        });
        this.loadData(1);
    }

    loadData = (pageIndex)=>{

        this.pageIndex = pageIndex;
        api.userList({pageIndex,pageSize:pageSize})
            .then(res=>res.json())
            .then(result=> {
                const {code, message, data: {list, total} = {}} = result;
                this.setState({
                    loading: false,
                    data: list,
                    total: total
                });
            }).catch(e=>{
                    this.setState({
                        loading:false
                    })
                    console.log(e)
                })
    };


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




    toggleForbid=(record)=>{
        const forbid = record.forbid === '1' ? 0 : 1;
        api.updataUser({forbid})(record.uid)
            .then(res=>res.json())
            .then(result=>{
                this.loadData(this.pageIndex);
                this.openNotification();
            }).catch(e=>{
                console.log(e)
        })

    }

    openNotification = () => {
        notification['success']({
            message: '操作成功',
            duration: 3 ,
            description:
                '请认真确定您的操作是否存在错误，以免误封造成的不必要影响',
            onClick: () => {
                console.log('操作成功');
            },
        });
    };
}
