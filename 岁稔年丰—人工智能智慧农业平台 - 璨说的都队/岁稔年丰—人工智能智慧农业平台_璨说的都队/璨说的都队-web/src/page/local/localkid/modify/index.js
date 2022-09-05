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
         const {Id,Descs,Address,Type1,Prize,Timea,Timeb,Type1Timea,Type1Timeb,Type1Prize,Type2,Type2Timea,
             Type2Timeb,Type2Prize,Type3,Type3Timea,Type3Timeb,Type3Prize,Maintype} = item||{};
         this.Descs = Descs;
         this.Address = Address;
         this.Id = Id;
         this.Type1 = Type1;
        this.Prize = Prize;
        this.Maintype = Maintype;
        this.Timea = Timea;
        this.Timeb = Timeb;
        this.Type1Timea = Type1Timea;
        this.Type1Timeb = Type1Timeb;
        this.Type1Prize = Type1Prize;
        this.Type2 = Type2;
        this.Type2Timea = Type2Timea;
        this.Type2Timeb = Type2Timeb;
        this.Type2Prize = Type2Prize;
        this.Type3 = Type3;
        this.Type3Timea = Type3Timea;
        this.Type3Timeb = Type3Timeb;
        this.Type3Prize = Type3Prize;

    }

    formRef = React.createRef();

    onFinish=({Id,Descs,Address,Type1,Prize,Timea,Timeb,Type1Timea,Type1Timeb,Type1Prize,Type2,Type2Timea,
                  Type2Timeb,Type2Prize,Type3,Type3Timea,Type3Timeb,Type3Prize,Maintype})=>{
        Id = this.Id;
        Descs = this.Descs;
        Address = this.Address;
        Type1 = this.Type1;
        Prize = this.Prize;
        Timea = this.Timea;
        Timeb = this.Timeb;
        Maintype = this.Maintype;
        Type1Timea = this.Type1Timea;
        Type1Timeb = this.Type1Timeb;
        Type1Prize = this.Type1Prize;
        Type2 = this.Type2;
        Type2Timea = this.Type2Timea;
        Type2Timeb = this.Type2Timeb;
        Type2Prize = this.Type2Prize;
        Type3 = this.Type3;
        Type3Timea = this.Type3Timea;
        Type3Timeb = this.Type3Timeb;
        Type3Prize = this.Type3Prize;
        api.Localkidmf({Descs,Address,Timea,Timeb,Prize,Type1,Type1Prize,Type1Timea,Type1Timeb,
        Type2,Type2Prize,Type2Timeb,Type2Timea,Type3,Type3Prize,Type3Timea,Type3Timeb,Maintype})(Id)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/localkid');
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
            <Form.Item nname='Descs'
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
            <Form.Item nname='套餐'
                       label='描述'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Maintype = value;
                    }}
                    defaultValue={this.Maintype}
                    placeholder='请输入...'
                    disabled={this.Maintype == null}></Input>
            </Form.Item>
            <Form.Item name='Address'
                       label='地址'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Address = value;
                    }}
                    defaultValue={this.Address}
                    placeholder='请输入...'
                    disabled={this.Address == null}></Input>
            </Form.Item>
            <Form.Item name='Prize'
                       label='价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>

            <Form.Item name='Timea'
                       label='起始日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Timeb'
                       label='结束日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1'
                       label='类型1'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Timea'
                       label='类型1起始日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Timeb'
                       label='类型1结束日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Prize'
                       label='类型1价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2'
                       label='类型2'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2Timea'
                       label='类型2起始日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2Timeb'
                       label='类型2结束日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type2Prize'
                        label='类型2价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type3'
                        label='类型3'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type3Timea'
                        label='类型3起始日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type3Timeb'
                       label='类型3结束日期'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type3Prize'
                       label='类型3价格'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='请输入...'
                    disabled={this.Prize == null}></Input>
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
