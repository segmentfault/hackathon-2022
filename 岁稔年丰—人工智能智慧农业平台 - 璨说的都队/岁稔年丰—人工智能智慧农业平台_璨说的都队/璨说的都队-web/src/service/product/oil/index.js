import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function OilList(params){
    return get(api.api.oilList)(params);
}

export function removeOil(params){
    return del(api.api.removeoil)(params);
}

export function addOil(params){
    return post(api.api.addoil)(params);
}
export function updataOil(params){
    return put(api.api.updataoil)(params);
}
