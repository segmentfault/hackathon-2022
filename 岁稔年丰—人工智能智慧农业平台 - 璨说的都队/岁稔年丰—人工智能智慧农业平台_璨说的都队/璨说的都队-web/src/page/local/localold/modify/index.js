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
         this.Maintype = Maintype;
         this.Address = Address;
         this.Id = Id;
         this.Type1 = Type1;
        this.Prize = Prize;
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
        Maintype = this.Maintype;
        Descs = this.Descs;
        Address = this.Address;
        Type1 = this.Type1;
        Prize = this.Prize;
        Timea = this.Timea;
        Timeb = this.Timeb;
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
        api.Localoldmf({Descs,Address,Timea,Timeb,Prize,Type1,Type1Prize,Type1Timea,Type1Timeb,
        Type2,Type2Prize,Type2Timeb,Type2Timea,Type3,Type3Prize,Type3Timea,Type3Timeb,Maintype})(Id)
            .then(res=>res.json())
            .then(result=>{
                this.showSuccessResultc();
                this.props.history.push('/localold');
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
                       label='???????????????ID'
                       required={true}
                       rules={[

                       ]}>
                <Input  onChange={({target:{value}})=>{
                    this.Id = value;
                }}
                        defaultValue={this.Id}
                        placeholder='?????????...'
                        disabled={this.Id != null}
                ></Input>
            </Form.Item>
            <Form.Item nname='Descs'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Descs = value;
                    }}
                    defaultValue={this.Descs}
                    placeholder='?????????...'
                    disabled={this.Descs == null}></Input>
            </Form.Item>
            <Form.Item name='Address'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Address = value;
                    }}
                    defaultValue={this.Address}
                    placeholder='?????????...'
                    disabled={this.Address == null}></Input>
            </Form.Item>
            <Form.Item name='Maintype'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Maintype = value;
                    }}
                    defaultValue={this.Maintype}
                    placeholder='?????????...'
                    disabled={this.Maintype == null}></Input>
            </Form.Item>
            <Form.Item name='Prize'
                       label='??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>

            <Form.Item name='Timea'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Timeb'
                       label='????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1'
                       label='??????1'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Timea'
                       label='??????1????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Timeb'
                       label='??????1????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type1Prize'
                       label='??????1??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2'
                       label='??????2'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2Timea'
                       label='??????2????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type2Timeb'
                       label='??????2????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type2Prize'
                        label='??????2??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type3'
                        label='??????3'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item  name='Type3Timea'
                        label='??????3????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type3Timeb'
                       label='??????3????????????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
            </Form.Item>
            <Form.Item name='Type3Prize'
                       label='??????3??????'
                       rules={[

                       ]}>
                <Input
                    onChange={({target:{value}})=>{
                        this.Prize = value;
                    }}
                    defaultValue={this.Prize}
                    placeholder='?????????...'
                    disabled={this.Prize == null}></Input>
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
