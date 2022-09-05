import React from 'react';
import {Image, Layout, Menu} from 'antd'
import {withRouter} from 'react-router-dom'
import './DrawerMenu.less'
import {HomeOutlined,ProjectOutlined,
    AppstoreAddOutlined,
    PicCenterOutlined,
    ShopOutlined,
    FileAddOutlined,
    TeamOutlined,
    FundOutlined,
    FolderAddOutlined,
    TagOutlined,
    HighlightOutlined} from "@ant-design/icons";
import NavigationUtil,{RouteConfig as MENUS} from './Navigator';
const {Sider} = Layout
const {SubMenu} = Menu


class Index extends React.Component  {
    state={
      selectKeys:['home']
    };
    onCollapse = collapsed=>{
        this.setState({collapsed})
    };
    onSelect = selectedKeys=>{
        const menu = MENUS[selectedKeys.key];
        let pathname = (menu || {}).pathname;
        const {history,onMenuSelect} = this.props;
        if(pathname){
            NavigationUtil.goto(menu,history)();
            onMenuSelect&&onMenuSelect(pathname,menu.title);
        }
    };
    menu(){
        return <Menu theme='dark' defaultSelectedKeys={this.state.selectKeys}
                     mode='inline' onSelect={this.onSelect}>
            <Menu.Item key={MENUS.home.key} icon={<HomeOutlined/>}>
                {MENUS.home.title}
            </Menu.Item>
            <Menu.Item key={MENUS.user.key} icon={<TeamOutlined></TeamOutlined>}>
                {MENUS.user.title}</Menu.Item>
            <Menu.Item key={MENUS.Chart.key} icon={<FundOutlined></FundOutlined>}>
                {MENUS.Chart.title}</Menu.Item>
            <SubMenu Key='ProductCenter' title='特色农产品' icon={<ProjectOutlined></ProjectOutlined>}>
                <SubMenu key='productCenter' title="精选" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Recommend.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Recommend.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Recommendadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Recommendadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Recommendmd.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Recommendmd.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='GrainCenter' title="粮油" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Oil.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Oil.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Oiladd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Oiladd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Oilmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Oilmf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='FruitsCenter' title="水果" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Fruit.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Fruit.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Fruitadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Fruitadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Fruitmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Fruitmf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='VegetablesCenter' title="蔬菜" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Vegetable.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Vegetable.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Vegetableadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Vegetableadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Vegetablemf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Vegetablemf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='MeatCenter' title="肉禽类" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Meat.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Meat.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Meatadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Meatadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Meatmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Meatmf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='OtherCenter' title="其他" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.Other.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.Other.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Otheradd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.Otheradd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.Othermf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.Othermf.title}
                    </Menu.Item>
                </SubMenu>

            </SubMenu>
            <SubMenu Key='category' title='订单管理' icon={<ProjectOutlined></ProjectOutlined>}>
                <Menu.Item key={MENUS.category.key} icon={<AppstoreAddOutlined></AppstoreAddOutlined>}>
                    {MENUS.category.title}
                </Menu.Item>
                <Menu.Item key={MENUS.addCategory.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                    {MENUS.addCategory.title}</Menu.Item>
                <Menu.Item key={MENUS.categoryMf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                    {MENUS.categoryMf.title}</Menu.Item>
            </SubMenu>
            {/*<SubMenu Key='Videocenter' title='视频列表' icon={<ProjectOutlined></ProjectOutlined>}>*/}
            {/*    <Menu.Item key={MENUS.Videolist.key} icon={<AppstoreAddOutlined></AppstoreAddOutlined>}>*/}
            {/*        {MENUS.Videolist.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Videoadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>*/}
            {/*        {MENUS.Videoadd.title}</Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Videomf.key} icon={<HighlightOutlined></HighlightOutlined>}>*/}
            {/*        {MENUS.Videomf.title}</Menu.Item>*/}
            {/*</SubMenu>*/}
            {/*<SubMenu Key='category' title='招聘推荐' icon={<ProjectOutlined></ProjectOutlined>}>*/}
            {/*    <Menu.Item key={MENUS.Employ.key} icon={<AppstoreAddOutlined></AppstoreAddOutlined>}>*/}
            {/*        {MENUS.Employ.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Employadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>*/}
            {/*        {MENUS.Employadd.title}</Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Employmf.key} icon={<HighlightOutlined></HighlightOutlined>}>*/}
            {/*        {MENUS.Employmf.title}</Menu.Item>*/}
            {/*</SubMenu>*/}
            {/*<SubMenu Key='configCenter' title='非遗管理' icon={<ProjectOutlined></ProjectOutlined>}>*/}
            {/*    <Menu.Item key={MENUS.configList.key} icon={<ShopOutlined></ShopOutlined>}>*/}
            {/*        {MENUS.configList.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.configAdd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>*/}
            {/*        {MENUS.configAdd.title}</Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.configMf.key} icon={<HighlightOutlined></HighlightOutlined>}>*/}
            {/*        {MENUS.configMf.title}</Menu.Item>*/}
            {/*</SubMenu>*/}
            {/*<SubMenu Key='VendorCenter' title='商贩管理' icon={<ProjectOutlined></ProjectOutlined>}>*/}
            {/*    <Menu.Item key={MENUS.Vendorlist.key} icon={<ShopOutlined></ShopOutlined>}>*/}
            {/*        {MENUS.Vendorlist.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Vendoradd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>*/}
            {/*        {MENUS.Vendoradd.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Vendormf.key} icon={<HighlightOutlined></HighlightOutlined>}>*/}
            {/*        {MENUS.Vendormf.title}*/}
            {/*    </Menu.Item>*/}
            {/*</SubMenu>*/}

            <SubMenu Key='LocalCenter' title='特色当地' icon={<ProjectOutlined></ProjectOutlined>}>
                <SubMenu key='KidCenter' title="亲子教育" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.LocalKid.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.LocalKid.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalKidadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.LocalKidadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalKidmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.LocalKidmf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='OldCenter' title="老年回忆" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.LocalOld.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.LocalOld.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalOldadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.LocalOldadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalOldmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.LocalOldmf.title}
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='TeenCenter' title="青年体验" icon={<TagOutlined></TagOutlined>}>
                    <Menu.Item key={MENUS.LocalTeen.key} icon={<ShopOutlined></ShopOutlined>}>
                        {MENUS.LocalTeen.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalTeenadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>
                        {MENUS.LocalTeenadd.title}
                    </Menu.Item>
                    <Menu.Item key={MENUS.LocalTeenmf.key} icon={<HighlightOutlined></HighlightOutlined>}>
                        {MENUS.LocalTeenmf.title}
                    </Menu.Item>
                </SubMenu>
            </SubMenu>
            {/*<SubMenu key='ShopCenter' title="店铺管理" icon={<ProjectOutlined></ProjectOutlined>}>*/}
            {/*    <Menu.Item key={MENUS.ShopList.key} icon={<ShopOutlined></ShopOutlined>}>*/}
            {/*        {MENUS.ShopList.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Shopadd.key} icon={<FolderAddOutlined></FolderAddOutlined>}>*/}
            {/*        {MENUS.Shopadd.title}*/}
            {/*    </Menu.Item>*/}
            {/*    <Menu.Item key={MENUS.Shopmf.key} icon={<HighlightOutlined></HighlightOutlined>}>*/}
            {/*        {MENUS.Shopmf.title}*/}
            {/*    </Menu.Item>*/}
            {/*</SubMenu>*/}
        </Menu>
    }
    render() {
        const {collapsed} = this.props;
        const headerTitle = collapsed ? null : <div className='drawer-header-text-container'>
            <label className='drawer-header-text'>中北农服</label>
            <label className='drawer-header-text'>管理后台</label>
        </div>
        return (
            <Sider trigger={null} collapsed={collapsed} collapsible onCollapse={this.onCollapse}>
                <div className='drawer-header'>
                    <img className='drawer-logo'  alt='logo' src='http://www.twoyl.cn/xczxpt/01.jpg'></img>
                    {headerTitle}
                </div>
                {this.menu()}
            </Sider>
        );
    }
}

//withrouter是react-router的一个高阶组件
//获取history render可以把match，location传过去
export default withRouter(Index);
