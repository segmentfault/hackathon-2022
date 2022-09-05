import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../service'
import './index.less'
export default class Index extends React.Component{
    formRef = React.createRef();

    onFinish=({VideoUrl,Title,Label,Hours,Persons,TeaId,TeaName,TeaUrl,VideoPic,Price})=>{
        api.Videoadd()({VideoUrl,Title,Label,Hours,Persons,TeaId,TeaName,TeaUrl,VideoPic,Price})
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
            <Form.Item name='VideoId'
                       label='描述'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Title'
                       label='地址'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Label'
                       label='标签'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Hours'
                       label='课时'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Persons'
                       label='人数'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='TeaId'
                       label='教师id'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='TeaName'
                       label='教师姓名'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='TeaUrl'
                       label='视频链接'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='VideoPic'
                       label='视频图片'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='VideoUrl'
                       label='视频地址'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='Price'
                       label='视频地址'
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
