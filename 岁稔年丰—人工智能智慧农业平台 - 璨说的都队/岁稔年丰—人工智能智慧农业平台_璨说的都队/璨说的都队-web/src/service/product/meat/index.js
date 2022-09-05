import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function MeatList(params){
    return get(api.api.meatList)(params);
}

export function removeMeat(params){
    return del(api.api.removemeat)(params);
}

export function addMeat(params){
    return post(api.api.addmeat)(params);
}
export function updataMeat(params){
    return put(api.api.updatameat)(params);
}

