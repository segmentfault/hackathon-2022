import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../service'
import './index.less'
export default class Index extends React.Component{
    formRef = React.createRef();

    onFinish=({Title,Timea,Spaces,Tele,Info,Url,Areas})=>{
        api.addShop()({Title,Timea,Spaces,Tele,Info,Url,Areas})
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
            <Form.Item name='Title'
                       label='标题'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Timea'
                       label='时期'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Spaces'
                       label='空间'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Tele'
                       label='手机号'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Info'
                       label='内容'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Url'
                       label='链接'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Areas'
                       label='区域'
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
