import {get,del,post,put} from '../index';
import * as api from '../../service/api'

export function ShopList(params){
    return get(api.api.shopList)(params);
}

export function removeShop(params){
    return del(api.api.removeshop)(params);
}

export function addShop(params){
    return post(api.api.Shopadd)(params);
}

export function updataShop(params){
    return put(api.api.shopmf)(params);
}
