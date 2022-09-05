import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'


export function RecommeadList(params){
    return get(api.api.recommeadList)(params);
}

export function removeRecommead(params){
    return del(api.api.removerecommead)(params);
}

export function addRecommead(params){
    return post(api.api.addrecommead)(params);
}
export function updataRecommead(params){
    return put(api.api.updatarecommead)(params);
}

