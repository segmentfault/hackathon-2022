import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'


export function VegetableList(params){
    return get(api.api.vegetableList)(params);
}

export function removeVegetable(params){
    return del(api.api.removevegetable)(params);
}

export function addVegetable(params){
    return post(api.api.addvegetable)(params);
}
export function updataVegetable(params){
    return put(api.api.updatavegetable)(params);
}
