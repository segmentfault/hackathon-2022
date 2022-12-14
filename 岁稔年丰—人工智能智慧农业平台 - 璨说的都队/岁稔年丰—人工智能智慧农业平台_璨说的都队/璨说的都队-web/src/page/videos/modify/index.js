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
                       label='???????????????ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.VideoId = value;
                }}
                        defaultValue={this.VideoId}
                        placeholder='?????????...'
                        disabled={this.VideoId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='Title'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Title = value;
                    }}
                    defaultValue={this.Title}
                    placeholder='?????????...'
                    disabled={this.Title == null}></Input>
            </Form.Item>
            <Form.Item name='Label'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Label = value;
                    }}
                    defaultValue={this.Label}
                    placeholder='?????????...'
                    disabled={this.Label == null}></Input>
            </Form.Item>
            <Form.Item name='Hours'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Hours = value;
                    }}
                    defaultValue={this.Hours}
                    placeholder='?????????...'
                    disabled={this.Hours == null}></Input>
            </Form.Item>
            <Form.Item name='Persons'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Persons = value;
                    }}
                    defaultValue={this.Persons}
                    placeholder='?????????...'
                    disabled={this.Persons == null}></Input>
            </Form.Item>

            <Form.Item name='TeaName'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.TeaName = value;
                    }}
                    defaultValue={this.TeaName}
                    placeholder='?????????...'
                    disabled={this.TeaName == null}></Input>
            </Form.Item>
            <Form.Item name='TeaUrl'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.TeaUrl = value;
                    }}
                    defaultValue={this.TeaUrl}
                    placeholder='?????????...'
                    disabled={this.TeaUrl == null}></Input>
            </Form.Item>
            <Form.Item name='VideoPic'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.VideoPic = value;
                    }}
                    defaultValue={this.VideoPic}
                    placeholder='?????????...'
                    disabled={this.VideoPic == null}></Input>
            </Form.Item>
            <Form.Item name='VideoUrl'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.VideoUrl = value;
                    }}
                    defaultValue={this.VideoUrl}
                    placeholder='?????????...'
                    disabled={this.VideoUrl == null}></Input>
            </Form.Item>
            <Form.Item name='Price'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Price = value;
                    }}
                    defaultValue={this.Price}
                    placeholder='?????????...'
                    disabled={this.Price == null}></Input>
            </Form.Item>
            <Form.Item
                className='add-btn-layout'
            >
                <Button className='add-btn' type='primary' htmlType='submit'>??????</Button>
                <Button  htmlType='button' type='white' onClick={this.onRest}>??????</Button>
            </Form.Item>

        </Form>
    }

    showSuccessResultc(successArray){
        notification['success']({
            placement:'bottomRight',
            duration:3,
            message:'????????????',
            description:'????????????????????????????????????????????????'
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
            message:'????????????',
            description:'??????????????????'
        })
        this.onRest();
    }

    showFailResultc(){
        notification['error']({
            duration:null,
            placement:'bottomRight',
            message:'????????????',
            description:'??????????????????'
        })
        this.onRest();
    }
}
