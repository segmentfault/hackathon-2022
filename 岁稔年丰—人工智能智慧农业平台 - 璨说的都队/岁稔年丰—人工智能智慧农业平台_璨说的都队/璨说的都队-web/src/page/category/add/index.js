import React from 'react'
import {Form,Input,Button,notification} from "antd";
import api from '../../../service'
import './index.less'
import Bmob from "hydrogen-js-sdk";
export default class Index extends React.Component{
    formRef = React.createRef();

    /**
     * 批量操作封装
     * @param categoryName
     */

    onFinish=({prodtitle,phone,address,name,price,number})=>{
        console.log(prodtitle,phone,address,name,price)
        const query = Bmob.Query('order_buy');
        query.set("name",name)
        query.set("prodtitle",prodtitle)
        query.set("phone",phone)
        query.set("address",address)
        query.set("price",price)
        query.set("number",number)
        query.save().then(res => {
            this.showSuccessResult();
        }).catch(err => {
            this.showFailResult();
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

            <Form.Item name='prodtitle'
                        label='下单产品'
                        rules={[
                            {required:true}
                        ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='phone'
                       label='用户手机号'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='address'
                       label='地址'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='name'
                       label='用户姓名'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='number'
                       label='下单数量'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='price'
                       label='下单总价'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>

            <Form.Item
                className='add-btn-layout'
            >
                <Button className='add-btn' type='primary' htmlType='submit'>提交</Button>
                <Button  htmlType='button' type='white' onClick={this.onRest}>重置</Button>
            </Form.Item>

        </Form>
    }
    showSuccessResult(successArray){
        if(!successArray || successArray.length === 0){
            return;
        }
        notification['success']({
            placement:'bottomRight',
            duration:3,
            message:'添加成功',
            description:successArray.toString()
        })
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
            description:shows
        })
    }
}
