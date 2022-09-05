import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../../service'
import './index.less'
export default class Index extends React.Component{
    formRef = React.createRef();

    onFinish=({MeatName,MeatPrice,MeatUrl,From,Details,Fee,Types,Days})=>{
        api.addMeat()({MeatName,MeatPrice,MeatUrl,From,Details,Fee,Types,Days})
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResult();
            }).catch(e=>{
                console.log(e)
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
            <Form.Item name='MeatName'
                       label='商品'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='MeatPrice'
                       label='价格'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='MeatUrl'
                       label='图片地址'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='From'
                       label='发货地'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Details'
                       label='标题'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Fee'
                       label='运费'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Types'
                       label='商品类型'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Days'
                       label='发货天数'
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
        notification['success']({
            placement:'bottomRight',
            duration:3,
            message:'添加成功',
            description:'数据添加成功，请检查数据是否错漏'
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
}
