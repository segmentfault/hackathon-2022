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
         const {Id,Title,Price,UserName,Url,ShopName,Tele,Types,Descs} = item||{};
         this.Title = Title;
         this.Id = Id;
         this.Price = Price;
         this.Url = Url;
         this.UserName = UserName;
         this.ShopName = ShopName;
         this.Tele = Tele;
         this.Types = Types;
         this.Descs = Descs;

    }

    formRef = React.createRef();

    onFinish=({Id,Title,Price,UserName,Url,ShopName,Tele,Types,Desc})=>{
        Title = this.Title;
        Id = this.Id;
        Price = this.Price;
        Url = this.Url;
        UserName = this.UserName;
        ShopName = this.ShopName;
        Tele = this.Tele;
        Types = this.Types;
        Desc = this.Descs;
        api.updataEmploy({Title,Price,Url,UserName,ShopName,Tele,Types,Desc})(Id)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/employ');
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
            <Form.Item name='Id'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.Id = value;
                }}
                        defaultValue={this.Id}
                        placeholder='请输入...'
                        disabled={this.Id != null}
                ></Input>
            </Form.Item>
            <Form.Item name='Title'
                       label='标题'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Title = value;
                    }}
                    defaultValue={this.Title}
                    placeholder='请输入...'
                    disabled={this.Title == null}></Input>
            </Form.Item>
            <Form.Item name='Price'
                       label='价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Price = value;
                    }}
                    defaultValue={this.Price}
                    placeholder='请输入...'
                    disabled={this.Price == null}></Input>
            </Form.Item>
            <Form.Item name='Types'
                       label='类型'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Types = value;
                    }}
                    defaultValue={this.Types}
                    placeholder='请输入...'
                    disabled={this.Types == null}></Input>
            </Form.Item>
            <Form.Item name='UserName'
                       label='用户名'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.UserName = value;
                    }}
                    defaultValue={this.UserName}
                    placeholder='请输入...'
                    disabled={this.UserName == null}></Input>
            </Form.Item>
            <Form.Item name='Url'
                       label='头像链接'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Url = value;
                    }}
                    defaultValue={this.Url}
                    placeholder='请输入...'
                    disabled={this.Url == null}></Input>
            </Form.Item>
            <Form.Item name='Tele'
                       label='手机号'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Tele = value;
                    }}
                    defaultValue={this.Tele}
                    placeholder='请输入...'
                    disabled={this.Tele == null}></Input>
            </Form.Item>
            <Form.Item name='ShopName'
                       label='店铺名'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ShopName = value;
                    }}
                    defaultValue={this.ShopName}
                    placeholder='请输入...'
                    disabled={this.ShopName == null}></Input>
            </Form.Item>
            <Form.Item name='Descs'
                       label='描述'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Descs = value;
                    }}
                    defaultValue={this.Descs}
                    placeholder='请输入...'
                    disabled={this.Descs == null}></Input>
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
