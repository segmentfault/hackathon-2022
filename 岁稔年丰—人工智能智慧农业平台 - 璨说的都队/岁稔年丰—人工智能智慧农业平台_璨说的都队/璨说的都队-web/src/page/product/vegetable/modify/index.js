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

    onFinish=({ProdId,VegetableName,VegetablePrice,VegetableUrl,Days,From,Details,Fee,Types})=>{
        VegetableName = this.ProdTitle;
        ProdId = this.ProdId;
        VegetablePrice = this.ProdPrice;
        VegetableUrl = this.ProdUrl;
        Days = this.ProdDays;
        Details = this.ProdDetails;
        Fee = this.ProdFee;
        From = this.ProdFrom;
        Types = this.ProdTypes;
        api.updataVegetable({VegetableName,VegetablePrice,VegetableUrl,Days,From,Details,Fee,Types})(ProdId)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/vegetable');
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
                       label='???????????????ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.ProdId = value;
                }}
                        defaultValue={this.ProdId}
                        placeholder='?????????...'
                        disabled={this.ProdId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='ProdTitle'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdTitle = value;
                    }}
                    defaultValue={this.ProdTitle}
                    placeholder='?????????...'
                    disabled={this.ProdTitle == null}></Input>
            </Form.Item>
            <Form.Item name='ProdPrice'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdPrice = value;
                    }}
                    defaultValue={this.ProdPrice}
                    placeholder='?????????...'
                    disabled={this.ProdPrice == null}></Input>
            </Form.Item>
            <Form.Item name='ProdUrl'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdUrl = value;
                    }}
                    defaultValue={this.ProdUrl}
                    placeholder='?????????...'
                    disabled={this.ProdUrl == null}></Input>
            </Form.Item>
            <Form.Item name='ProdFrom'
                       label='?????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdFrom = value;
                    }}
                    defaultValue={this.ProdFrom}
                    placeholder='?????????...'
                    disabled={this.ProdFrom == null}></Input>
            </Form.Item>
            <Form.Item name='ProdDetails'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdDetails = value;
                    }}
                    defaultValue={this.ProdDetails}
                    placeholder='?????????...'
                    disabled={this.ProdDetails == null}></Input>
            </Form.Item>
            <Form.Item name='ProdFee'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdFee = value;
                    }}
                    defaultValue={this.ProdFee}
                    placeholder='?????????...'
                    disabled={this.ProdFee == null}></Input>
            </Form.Item>
            <Form.Item name='ProdTypes'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdTypes = value;
                    }}
                    defaultValue={this.ProdTypes}
                    placeholder='?????????...'
                    disabled={this.ProdTypes == null}></Input>
            </Form.Item>
            <Form.Item name='ProdDays'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.ProdDays = value;
                    }}
                    defaultValue={this.ProdDays}
                    placeholder='?????????...'
                    disabled={this.ProdDays == null}></Input>
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
