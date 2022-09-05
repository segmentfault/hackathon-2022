import {get,del,post,put} from '../index';
import * as api from '../../service/api'

export function categoryList(params){
    return get(api.api.categoryList)(params);
}

export function removeCatgory(params){
    return del(api.api.removeCategory)(params);
}

export function addCategory(params){
    return post(api.api.addCategory)(params);
}

export function updatacategory(params){
    return put(api.api.updataCategory)(params);
}
