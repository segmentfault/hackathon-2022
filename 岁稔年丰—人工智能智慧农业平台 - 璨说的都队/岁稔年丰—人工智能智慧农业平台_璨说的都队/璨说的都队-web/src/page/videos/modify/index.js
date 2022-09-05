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
         const {VideoId,Title,Label,Hours,TeaId,TeaName,TeaUrl,VideoUrl,VideoPic,Persons,Price} = item||{};
         this.VideoId = VideoId;
         this.Persons = Persons;
         this.Title = Title;
         this.Label = Label;
         this.Hours = Hours;
        this.TeaId = TeaId;
        this.TeaName = TeaName;
        this.TeaUrl = TeaUrl;
        this.VideoUrl = VideoUrl;
        this.VideoPic = VideoPic;
        this.Price = Price;

    }

    formRef = React.createRef();

    onFinish=({VideoId,Title,Persons,Label,Hours,TeaId,TeaName,TeaUrl,VideoUrl,VideoPic,Price})=>{
        VideoId = this.VideoId;
        Title = this.Title;
        Price = this.Price;
        Label = this.Label;
        Hours = this.Hours;
        TeaId = this.TeaId;
        TeaName = this.TeaName;
        TeaUrl = this.TeaUrl;
        VideoUrl = this.VideoUrl;
        VideoPic = this.VideoPic;
        Persons = this.Persons;

        api.Videomf({Title,Label,Hours,TeaId,TeaName,TeaUrl,VideoUrl,VideoPic,Persons,Price})(VideoId)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/videolist');
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
            <Form.Item name='VideoId'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.VideoId = value;
                }}
                        defaultValue={this.VideoId}
                        placeholder='请输入...'
                        disabled={this.VideoId != null}
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
            <Form.Item name='Label'
                       label='标签'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Label = value;
                    }}
                    defaultValue={this.Label}
                    placeholder='请输入...'
                    disabled={this.Label == null}></Input>
            </Form.Item>
            <Form.Item name='Hours'
                       label='课时'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Hours = value;
                    }}
                    defaultValue={this.Hours}
                    placeholder='请输入...'
                    disabled={this.Hours == null}></Input>
            </Form.Item>
            <Form.Item name='Persons'
                       label='报名人数'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Persons = value;
                    }}
                    defaultValue={this.Persons}
                    placeholder='请输入...'
                    disabled={this.Persons == null}></Input>
            </Form.Item>

            <Form.Item name='TeaName'
                       label='老师姓名'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.TeaName = value;
                    }}
                    defaultValue={this.TeaName}
                    placeholder='请输入...'
                    disabled={this.TeaName == null}></Input>
            </Form.Item>
            <Form.Item name='TeaUrl'
                       label='老师头像'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.TeaUrl = value;
                    }}
                    defaultValue={this.TeaUrl}
                    placeholder='请输入...'
                    disabled={this.TeaUrl == null}></Input>
            </Form.Item>
            <Form.Item name='VideoPic'
                       label='视频图片'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.VideoPic = value;
                    }}
                    defaultValue={this.VideoPic}
                    placeholder='请输入...'
                    disabled={this.VideoPic == null}></Input>
            </Form.Item>
            <Form.Item name='VideoUrl'
                       label='视频链接'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.VideoUrl = value;
                    }}
                    defaultValue={this.VideoUrl}
                    placeholder='请输入...'
                    disabled={this.VideoUrl == null}></Input>
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
