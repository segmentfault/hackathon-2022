import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../service'
import './index.less'
export default class Index extends React.Component{
    formRef = React.createRef();

    onFinish=({CraftName,CraftKind,CraftPrice,CulHeritage})=>{
        api.addCraft()({CraftName,CraftKind,CraftPrice,CulHeritage})
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
            <Form.Item name='CraftName'
                       label='手工艺品名称'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='CraftKind'
                       label='手工艺品种类'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='CraftPrice'
                       label='手工艺品价格'
                       rules={[
                           {required:true}
                       ]}>
                <Input></Input>
            </Form.Item>
            <Form.Item name='CulHeritage'
                       label='是否非遗产品'
                       rules={[
                           {required:true}
                       ]}>
                <Radio.Group defaultValue="0" buttonStyle="solid">
                    <Radio.Button value="1">是</Radio.Button>
                    <Radio.Button value="0">否</Radio.Button>
                </Radio.Group>
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
}