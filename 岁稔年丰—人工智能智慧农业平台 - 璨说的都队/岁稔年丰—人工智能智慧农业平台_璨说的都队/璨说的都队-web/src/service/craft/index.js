import {get, del, post, put} from '../index';
import * as api from '../../service/api'

export function craftList(params){
    return get(api.api.craftList)(params);
}

export function removeCraft(params){
    return del(api.api.removeCraft)(params);
}

export function addCraft(params){
    return post(api.api.addCraft)(params);
}
export function updataCraft(params){
    return put(api.api.updataCraft)(params);
}

export function updataCraftc(params){
    return put(api.api.updataCraftc)(params);
}
