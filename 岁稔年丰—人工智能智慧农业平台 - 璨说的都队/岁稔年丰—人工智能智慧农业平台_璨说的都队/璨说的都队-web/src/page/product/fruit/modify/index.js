import React from 'react'
import {Form,Input,Button,notification,Radio } from "antd";
import api from '../../../../service'
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
         const {ProdId,ProdTitle,ProdPrice,ProdUrl,ProdDays,ProdFrom,ProdDetails,ProdFee,ProdTypes} = item||{};
         this.ProdTitle = ProdTitle;
         this.ProdId = ProdId;
         this.ProdPrice = ProdPrice;
         this.ProdUrl = ProdUrl;
         this.ProdDays = ProdDays;
         this.ProdFrom = ProdFrom;
         this.ProdDetails = ProdDetails;
         this.ProdFee = ProdFee;
         this.ProdTypes = ProdTypes;

    }

    formRef = React.createRef();

    onFinish=({ProdId,FruitName,FruitPrice,FruitUrl,Days,From,Details,Fee,Types})=>{
        FruitName = this.ProdTitle;
        ProdId = this.ProdId;
        FruitPrice = this.ProdPrice;
        FruitUrl = this.ProdUrl;
        Days = this.ProdDays;
        Details = this.ProdDetails;
        Fee = this.ProdFee;
        From = this.ProdFrom;
        Types = this.ProdTypes;
        api.updataFruit({FruitName,FruitPrice,FruitUrl,Days,Details,Fee,From,Types})(ProdId)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/fruit');
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
            <Form.Item name='ProdId'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.ProdId = value;
                }}
                        defaultValue={this.ProdId}
                        placeholder='请输入...'
                        disabled={this.ProdId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='ProdTitle'
                       label='商品名称'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdTitle = value;
                    }}
                    defaultValue={this.ProdTitle}
                    placeholder='请输入...'
                    disabled={this.ProdTitle == null}></Input>
            </Form.Item>
            <Form.Item name='ProdPrice'
                       label='商品价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdPrice = value;
                    }}
                    defaultValue={this.ProdPrice}
                    placeholder='请输入...'
                    disabled={this.ProdPrice == null}></Input>
            </Form.Item>
            <Form.Item name='ProdUrl'
                       label='图片地址'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdUrl = value;
                    }}
                    defaultValue={this.ProdUrl}
                    placeholder='请输入...'
                    disabled={this.ProdUrl == null}></Input>
            </Form.Item>
            <Form.Item name='ProdFrom'
                       label='发货地'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdFrom = value;
                    }}
                    defaultValue={this.ProdFrom}
                    placeholder='请输入...'
                    disabled={this.ProdFrom == null}></Input>
            </Form.Item>
            <Form.Item name='ProdDetails'
                       label='商品标题'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdDetails = value;
                    }}
                    defaultValue={this.ProdDetails}
                    placeholder='请输入...'
                    disabled={this.ProdDetails == null}></Input>
            </Form.Item>
            <Form.Item name='ProdFee'
                       label='运费'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdFee = value;
                    }}
                    defaultValue={this.ProdFee}
                    placeholder='请输入...'
                    disabled={this.ProdFee == null}></Input>
            </Form.Item>
            <Form.Item name='ProdTypes'
                       label='商品类型'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdTypes = value;
                    }}
                    defaultValue={this.ProdTypes}
                    placeholder='请输入...'
                    disabled={this.ProdTypes == null}></Input>
            </Form.Item>
            <Form.Item name='ProdDays'
                       label='发货天数'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdDays = value;
                    }}
                    defaultValue={this.ProdDays}
                    placeholder='请输入...'
                    disabled={this.ProdDays == null}></Input>
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
