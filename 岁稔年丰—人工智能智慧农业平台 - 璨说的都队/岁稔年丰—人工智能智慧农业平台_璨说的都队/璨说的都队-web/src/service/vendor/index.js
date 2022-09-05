import {get,del,post,put} from '../index';
import * as api from '../../service/api'

export function vendorList(params){
    return get(api.api.vendorList)(params);
}

export function removeVendor(params){
    return del(api.api.removeVendor)(params);
}

export function addVendor(params){
    return post(api.api.addVendor)(params);
}

export function updataVendor(params){
    return put(api.api.updataVendor)(params);
}
