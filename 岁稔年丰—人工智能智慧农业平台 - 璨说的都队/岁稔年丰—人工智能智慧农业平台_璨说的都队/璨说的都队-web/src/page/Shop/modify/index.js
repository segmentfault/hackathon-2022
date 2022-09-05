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
         const {Id,Title,Url,Timea,spaces,Tele,Info,Areas} = item||{};
         this.Title = Title;
         this.Id = Id;
         this.Timea = Timea;
         this.spaces = spaces;
         this.Info = Info;
         this.Areas = Areas;
         this.Tele = Tele;
        this.Url = Url;

    }

    formRef = React.createRef();

    onFinish=({Id,Title,Timea,Url,Spaces,Info,Areas,Tele})=>{
        Title = this.Title;
        Id = this.Id;
        Url = this.Url;
        Timea = this.Timea;
        Spaces = this.spaces;
        Info = this.Info;
        Areas = this.Areas;
        Tele = this.Tele;
        api.updataShop({Title,Url,Timea,Spaces,Info,Areas,Tele})(Id)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/shoplist');
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
            <Form.Item name='Timea'
                       label='日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Timea = value;
                    }}
                    defaultValue={this.Timea}
                    placeholder='请输入...'
                    disabled={this.Timea == null}></Input>
            </Form.Item>
            <Form.Item name='spaces'
                       label='空间'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.spaces = value;
                    }}
                    defaultValue={this.spaces}
                    placeholder='请输入...'
                    disabled={this.spaces == null}></Input>
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
            <Form.Item name='Url'
                       label='链接'
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
            <Form.Item name='Info'
                       label='内容'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Info = value;
                    }}
                    defaultValue={this.Info}
                    placeholder='请输入...'
                    disabled={this.Info == null}></Input>
            </Form.Item>
            <Form.Item name='Areas'
                       label='区域'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Areas = value;
                    }}
                    defaultValue={this.Areas}
                    placeholder='请输入...'
                    disabled={this.Areas == null}></Input>
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
