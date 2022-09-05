import React from "react";
import './App.less';
import {Layout} from 'antd'
import DrawerMenu from "./component/DrawerMenu";
import Header from "./component/Header";
import {MenuFoldOutlined,MenuUnfoldOutlined} from '@ant-design/icons';
import {Switch,Route} from 'react-router-dom'
import Home from './page/index'
import User from './page/user/index'
import AddCategory from './page/category/add/index'
import Category from './page/category/index'
import ConfigList from './page/config/index'
import AddConfig from './page/config/add/index'
import ConfigMf from './page/config/modify/index'
import CategoryMf from './page/category/modify/index'
import Vendor from './page/vendor/index'
import Vendoradd from './page/vendor/add/index'
import Vendormf from './page/vendor/modify/index'
import Fruit from './page/product/fruit/index'
import Fruitadd from './page/product/fruit/add/index'
import Fruitmf from './page/product/fruit/modify/index'
import Oil from './page/product/oil/index'
import Oiladd from './page/product/oil/add/index'
import Oilmf from './page/product/oil/modify/index'
import Meat from './page/product/meat/index'
import Meatadd from './page/product/meat/add/index'
import Meatmf from './page/product/meat/modify/index'
import Other from './page/product/other/index'
import Otheradd from './page/product/other/add/index'
import Othermf from './page/product/other/modify/index'
import Recommead from './page/product/recommead/index'
import Recommeadadd from './page/product/recommead/add/index'
import Recommeadmf from './page/product/recommead/modify/index'
import Vegetable from './page/product/vegetable/index'
import Vegetableadd from './page/product/vegetable/add/index'
import Charts from './page/charts/index'
import Vegetablemf from './page/product/vegetable/modify/index'
import Employ from './page/employ/index'
import Employadd from './page/employ/add/index'
import Employmf from './page/employ/modify/index'
import Localkid from './page/local/localkid/index'
import Localkidadd from './page/local/localkid/add/index'
import Localkidmf from './page/local/localkid/modify/index'
import Localold from './page/local/localold/index'
import Localoldadd from './page/local/localold/add/index'
import Localoldmf from './page/local/localold/modify/index'
import Localteen from './page/local/localteen'
import Localteenadd from './page/local/localteen/add/index'
import Localteenmf from './page/local/localteen/modify/index'
import videoList from './page/videos/index'
import videoadd from './page/videos/add/index'
import videomf from './page/videos/modify/index'
import shoplist from './page/Shop/index'
import shopadd from './page/Shop/add/index'
import shopmf from './page/Shop/modify/index'
const {Content, Footer} = Layout;



class App extends React.Component{
    state={
        title:'首页',
        collapsed:false

    };
    render() {
        const {collapsed,title} = this.state;
        return (
            <Layout className="App">
                <DrawerMenu collapsed={collapsed} onMenuSelect={(pathname,title)=>{
                    this.setState({
                        title
                    })
                }}/>
                <Layout>
                    <Header title={title} toggle={(collapsed)=>{
                        this.setState({collapsed});
                    }}/>
                    <Content className='App-content'>
                       <Switch>
                           <Route exact path='/' component={Home} ></Route>
                           <Route path='/category' component={Category} ></Route>
                           <Route path='/categoryadd' component={AddCategory} ></Route>
                           <Route path='/user' component={User} ></Route>
                           <Route path='/config' component={ConfigList} ></Route>
                           <Route path='/configadd' component={AddConfig} ></Route>
                           <Route path='/configmf' component={ConfigMf} ></Route>
                           <Route path='/categorymf' component={CategoryMf} ></Route>
                           <Route path='/vendor' component={Vendor} ></Route>
                           <Route path='/vendoradd' component={Vendoradd} ></Route>
                           <Route path='/vendormf' component={Vendormf} ></Route>
                           <Route path='/fruit' component={Fruit} ></Route>
                           <Route path='/fruitadd' component={Fruitadd} ></Route>
                           <Route path='/fruitmf' component={Fruitmf} ></Route>
                           <Route path='/oil' component={Oil} ></Route>
                           <Route path='/oiladd' component={Oiladd} ></Route>
                           <Route path='/oilmf' component={Oilmf} ></Route>
                           <Route path='/recommead' component={Recommead} ></Route>
                           <Route path='/recommeadadd' component={Recommeadadd} ></Route>
                           <Route path='/recommeadmf' component={Recommeadmf} ></Route>
                           <Route path='/other' component={Other} ></Route>
                           <Route path='/otheradd' component={Otheradd} ></Route>
                           <Route path='/othermf' component={Othermf} ></Route>
                           <Route path='/meat' component={Meat} ></Route>
                           <Route path='/meatadd' component={Meatadd} ></Route>
                           <Route path='/meatmf' component={Meatmf} ></Route>
                           <Route path='/vegetable' component={Vegetable} ></Route>
                           <Route path='/vegetableadd' component={Vegetableadd} ></Route>
                           <Route path='/vegetablemf' component={Vegetablemf} ></Route>
                           <Route path='/charts' component={Charts} ></Route>
                           <Route path='/employ' component={Employ} ></Route>
                           <Route path='/employadd' component={Employadd} ></Route>
                           <Route path='/employmf' component={Employmf} ></Route>
                           <Route path='/localkid' component={Localkid} ></Route>
                           <Route path='/localkidadd' component={Localkidadd} ></Route>
                           <Route path='/localkidmf' component={Localkidmf} ></Route>
                           <Route path='/localold' component={Localold} ></Route>
                           <Route path='/localoldadd' component={Localoldadd} ></Route>
                           <Route path='/localoldmf' component={Localoldmf} ></Route>
                           <Route path='/localteen' component={Localteen} ></Route>
                           <Route path='/localteenadd' component={Localteenadd} ></Route>
                           <Route path='/localteenmf' component={Localteenmf} ></Route>
                           <Route path='/shoplist' component={shoplist} ></Route>
                           <Route path='/shopadd' component={shopadd} ></Route>
                           <Route path='/shopmf' component={shopmf} ></Route>
                           <Route path='/videolist' component={videoList} ></Route>
                           <Route path='/videoadd' component={videoadd} ></Route>
                           <Route path='/videomf' component={videomf} ></Route>
                       </Switch>
                    </Content>
                    <Footer className='App-footer'>
                        中北农服后台管理平台<br/>©2021 晋ICP备2021007958号
                        <a href='https://www.miit.gov.cn/'>  中华人民共和国工业和信息化部</a>
                    </Footer>
                </Layout>
            </Layout>
        );
    }
}

export default App;
