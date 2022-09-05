import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../service'
import './index.less'
import Bmob from "hydrogen-js-sdk";
export default class Index extends React.Component{
    state = {
        configText:null,
        submit:false,
        visibleAlert:false,
        errorMsg:null
    };

    constructor(props) {
        super(props);
         console.log(props)
         const {location:{state:{item} = {} } = {}}= this.props;
         const {objectId,price} = item||{};
         this.tobjectId = objectId;
         this.tprice = price;
    }

    formRef = React.createRef();

    onFinish=({objectId,price})=>{
        objectId = this.tobjectId;
        price = this.tprice;
        const query = Bmob.Query('order_buy');
        query.set('objectId', objectId) //需要修改的objectId
        query.set('price', price)
        query.save().then(res => {
            this.showSuccessResultc();
            this.props.history.push('/category');
        }).catch(err => {
            console.log(err)
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
            <Form.Item name='objectId'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.tobjectId = value;
                }}
                        defaultValue={this.tobjectId}
                        placeholder='请输入...'
                        disabled={this.tobjectId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='price'
                       label='下单总价'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tprice = value;
                    }}
                    defaultValue={this.tprice}
                    placeholder='请输入...'
                    disabled={this.tprice == null}></Input>
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
    showSuccessResult(successArray){
        if(!successArray || successArray.length === 0){
            return;
        }
        notification['success']({
            placement:'bottomRight',
            duration:3,
            message:'添加成功',
            description:'数据添加成功，请检查数据是否错漏'
        })
        this.onRest();
    }

    showFailResult(failArray){
        if(!failArray || failArray.length === 0){
            return;
        }
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
