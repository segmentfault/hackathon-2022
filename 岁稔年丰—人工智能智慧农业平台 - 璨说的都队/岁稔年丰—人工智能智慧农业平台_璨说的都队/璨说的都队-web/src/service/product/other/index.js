import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'


export function OtherList(params){
    return get(api.api.otherList)(params);
}

export function removeOther(params){
    return del(api.api.removeother)(params);
}

export function addOther(params){
    return post(api.api.addother)(params);
}
export function updataOther(params){
    return put(api.api.updataother)(params);
}

