import {url} from './api'
import {userList,updataUser} from "./user";
import {craftList,addCraft,removeCraft,updataCraft,updataCraftc} from "./craft";
import {categoryList,removeCatgory,addCategory,updatacategory} from "./category";
import {vendorList,addVendor,updataVendor,removeVendor} from './vendor'
import {addFruit,FruitList,removeFruit,updataFruit} from './product/fruit'
import {addMeat,MeatList,removeMeat,updataMeat} from './product/meat'
import {OilList,addOil,updataOil,removeOil} from './product/oil'
import {OtherList,addOther,updataOther,removeOther} from './product/other'
import {RecommeadList,removeRecommead,addRecommead,updataRecommead} from './product/recommead'
import {VegetableList,addVegetable,updataVegetable,removeVegetable} from './product/vegetable'
import {employList,removeEmploy,addEmploy,updataEmploy} from "./employ";
import {LocalKidList, Localkidadd, Localkidmf ,removeLocalkid} from "./local/localkid";
import {Localoldadd,removeLocalold,LocaloldList,Localoldmf} from "./local/localold";
import {LocalteenList,removeLocalteen,Localteenadd,Localteenmf} from "./local/localteen";
import {Videoadd,VideoList,Videomf,removeVideo} from "./video";
import {ShopList,removeShop,addShop,updataShop} from "./shop";

export default {
    userList,
    updataUser,
    categoryList,
    removeCatgory,
    addCategory,
    craftList,
    addCraft,
    removeCraft,
    updataCraft,
    updataCraftc,
    updatacategory,
    vendorList,
    addVendor,
    updataVendor,
    removeVendor,
    FruitList,
    addFruit,
    removeFruit,
    updataFruit,
    RecommeadList,
    addRecommead,
    updataRecommead,
    removeRecommead,
    VegetableList,
    removeVegetable,
    updataVegetable,
    addVegetable,
    MeatList,
    updataMeat,
    removeMeat,
    addMeat,
    removeOil,
    OilList,
    updataOil,
    addOil,
    OtherList,
    removeOther,
    updataOther,
    addOther,
    employList,
    removeEmploy,
    addEmploy,
    updataEmploy,
    Localteenadd,
    Localteenmf,
    LocalteenList,
    removeLocalteen,
    Localoldmf,
    Localoldadd,
    LocaloldList,
    removeLocalold,
    Localkidmf,
    Localkidadd,
    LocalKidList,
    removeLocalkid,
    Videomf,
    VideoList,
    Videoadd,
    removeVideo,
    ShopList,
    addShop,
    removeShop,
    updataShop

};

const AUTH_TOKEN = 'MTU5Mjg1MDg3NDcwNw=='

/**
 * get请求 查
 * @param api
 * @returns {function(*=): Promise<Response>}
 */

export function get(api){
    return params => fetch(buildParams(url+api,params))
}

/**
 * put请求 改
 * 第一个参数为body参数
 * 第二个参数为url path参数，查询参数
 */
export function put(api){
    return params=>{
        const formData = new FormData();
        Object.entries(params).forEach(([k,v])=>{
            formData.append(k,v);
        });
            return queryParams=>fetch(buildParams(url+api,queryParams),{
                method:'PUT',
                body:formData,
            });
    }
}

/**
 * DELETE网络请求 删
 * @param api
 * @returns {function(*=): Promise<Response>}
 */
export function del(api) {
    return queryParams=>fetch(buildParams(url+api,queryParams),{
        method:'DELETE'
    });
};

/**
 * post请求 增
 * 第一个参数作为body参数，第二个参数作为url，path或者查询参数
 * @param api
 * @returns {*}
 */
export function post(api) {

    return params=>{
        console.log(params)
        return queryParams=>fetch(buildParams(url+api,queryParams),
            {
                method:'POST',
                body:JSON.stringify(params),
                headers:{
                    'content-type':'application/json'
                }
            })
    }
}

/**
 * 连接拼接
 * @param url
 * @param params
 * @returns {string|*|string}
 */
function buildParams(url,params={}){
    let newurl = new URL(url);
    if(typeof params === 'object'){
        Object.keys(params).forEach(key => {
            newurl.searchParams.append(key,params[key]);
        });
        // console.log(newurl.toString())
        return newurl.toString();
    }else {
        //适配path参数
        // console.log(newurl.toString())
        return url.endsWith("/") ? url+params : url+"/"+params;
    }
}



