import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../service'
import './index.less'
export default class Index extends React.Component{
    state = {
        configText:null,
        submit:false,
        visibleAlert:false,
        errorMsg:null
    };

    constructor(props) {
        super(props);
         const {location:{state:{item} = {} } = {}}= this.props;
         const {tVendName,tVendId,tVendAddress,tVendTele} = item||{};
         this.tVendName = tVendName;
         this.tVendId = tVendId;
         this.tVendAddress = tVendAddress;
         this.tVendTele = tVendTele;

    }

    formRef = React.createRef();

    onFinish=({VendName,Vendadress,Vendtele,VendID})=>{
        VendName = this.tVendName;
        VendID = this.tVendId;
        Vendadress = this.tVendAddress;
        Vendtele = this.tVendTele;
        console.log(VendName+Vendadress+Vendtele)
        api.updataVendor({VendName,Vendadress,Vendtele})(VendID)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/vendor');
            }).catch(e=>{
                console.log(e)
                this.showFailResultc();
        })
    };





    onRest=()=>{
        this.formRef.current.resetFields();
    };
    render() {
        return <Form
            className='add'
            ref={this.formRef}
            name='control-ref'
            onFinish={this.onFinish}
        >
            <Form.Item name='tVendId'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.tVendId = value;
                }}
                        defaultValue={this.tVendId}
                        placeholder='请输入...'
                        disabled={this.tVendId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='tVendName'
                       label='姓名'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tVendName = value;
                    }}
                    defaultValue={this.tVendName}
                    placeholder='请输入...'
                    disabled={this.tVendName == null}></Input>
            </Form.Item>
            <Form.Item name='tVendAddress'
                       label='位置'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tVendAddress = value;
                    }}
                    defaultValue={this.tVendAddress}
                    placeholder='请输入...'
                    disabled={this.tVendAddress == null}></Input>
            </Form.Item>
            <Form.Item name='tVendTele'
                       label='电话号'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tVendTele = value;
                    }}
                    defaultValue={this.tVendTele}
                    placeholder='请输入...'
                    disabled={this.tVendTele == null}></Input>
            </Form.Item>

            <Form.Item
                className='add-btn-layout'
            >
                <Button className='add-btn' type='primary' htmlType='submit'>提交</Button>
                <Button  htmlType='button' type='white' onClick={this.onRest}>重置</Button>
            </Form.Item>

        </Form>
    }

    showSuccessResultc(successArray){
        notification['success']({
            placement:'bottomRight',
            duration:3,
            message:'更新成功',
            description:'数据更新成功，请检查数据是否错漏'
        })
        this.onRest();
    }

    showFailResult(failArray){
        const shows = [];
        failArray.forEach(val=>{
            const {categoryName,message} = val;
            shows.push(<div key={categoryName}>{categoryName}:{message}</div>)
        });
        notification['error']({
            duration:null,
            placement:'bottomRight',
            message:'添加失败',
            description:'数据添加失败'
        })
        this.onRest();
    }

    showFailResultc(){
        notification['error']({
            duration:null,
            placement:'bottomRight',
            message:'更新失败',
            description:'数据更新失败'
        })
        this.onRest();
    }
}
