import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function LocaloldList(params){
    return get(api.api.LocaloldList)(params);
}

export function removeLocalold(params){
    return del(api.api.removeLocalold)(params);
}

export function Localoldadd(params){
    return post(api.api.Localoldadd)(params);
}
export function Localoldmf(params){
    return put(api.api.Localoldmf)(params);
}

