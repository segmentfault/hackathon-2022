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
        console.log(props)
        super(props);
         const {location:{state:{item} = {} } = {}}= this.props;
         const {tCraftId,tCraftName,tCraftKind,tCraftPrice,CraftUrl,CraftDesc,VendId} = item||{};
         this.tCraftId = tCraftId;
         this.tCraftName = tCraftName;
         this.tCraftKind = tCraftKind;
         this.tCraftPrice = tCraftPrice;
        this.CraftUrl = CraftUrl;
        this.CraftDesc = CraftDesc;
        this.VendId = VendId;


        // console.log(props)
    }

    formRef = React.createRef();

    onFinish=({craftname,craftkind,craftprice,CraftID,CraftUrl,CraftDesc,VentID})=>{
        craftname = this.tCraftName;
        craftkind = this.tCraftKind;
        craftprice = this.tCraftPrice;
        CraftID = this.tCraftId;
        CraftUrl = this.CraftUrl;
        CraftDesc = this.CraftDesc;
        VentID = this.VendId;

        api.updataCraftc({craftname,craftkind,craftprice,CraftUrl,CraftDesc,VentID})(CraftID)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/config');
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
            <Form.Item name='CraftID'
                       label='需要修改的ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.tCraftId = value;
                }}
                        defaultValue={this.tCraftId}
                        placeholder='请输入...'
                        disabled={this.tCraftId != null}
                ></Input>
            </Form.Item>
            <Form.Item name='craftname'
                       label='手工艺品名称'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tCraftName = value;
                    }}
                    defaultValue={this.tCraftName}
                    placeholder='请输入...'
                    disabled={this.tCraftName == null}></Input>
            </Form.Item>
            <Form.Item name='craftkind'
                       label='手工艺品种类'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tCraftKind = value;
                    }}
                    defaultValue={this.tCraftKind}
                    placeholder='请输入...'
                    disabled={this.tCraftKind == null}></Input>
            </Form.Item>
            <Form.Item name='craftprice'
                       label='手工艺品价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.tCraftPrice = value;
                    }}
                    defaultValue={this.tCraftPrice}
                    placeholder='请输入...'
                    disabled={this.tCraftPrice == null}></Input>
            </Form.Item>
            <Form.Item name='crafturl'
                       label='手工艺品图片链接'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.CraftUrl = value;
                    }}
                    defaultValue={this.CraftUrl}
                    placeholder='请输入...'
                    disabled={false}></Input>
            </Form.Item>
            <Form.Item name='craftdesc'
                       label='手工艺品描述'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.CraftDesc = value;
                    }}
                    defaultValue={this.CraftDesc}
                    placeholder='请输入...'
                    disabled={false}></Input>
            </Form.Item>
            <Form.Item name='vendid'
                       label='商贩ID'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.VendId = value;
                    }}
                    defaultValue={this.VendId}
                    placeholder='请输入...'
                    disabled={false}></Input>
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
