import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function LocalteenList(params){
    return get(api.api.LocalteenList)(params);
}

export function removeLocalteen(params){
    return del(api.api.removeLocalteen)(params);
}

export function Localteenadd(params){
    return post(api.api.Localteenadd)(params);
}
export function Localteenmf(params){
    return put(api.api.Localteenmf)(params);
}

