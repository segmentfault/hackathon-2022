import {get,del,post,put} from '../index';
import * as api from '../../service/api'

export function employList(params){
    return get(api.api.employ)(params);
}

export function removeEmploy(params){
    return del(api.api.removeemploy)(params);
}

export function addEmploy(params){
    return post(api.api.employadd)(params);
}

export function updataEmploy(params){
    return put(api.api.employmf)(params);
}
