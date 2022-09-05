import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function FruitList(params){
    return get(api.api.fruitList)(params);
}

export function removeFruit(params){
    return del(api.api.removefruit)(params);
}

export function addFruit(params){
    return post(api.api.addfruit)(params);
}
export function updataFruit(params){
    return put(api.api.updatafruit)(params);
}

