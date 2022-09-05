import {get, del, post, put} from '../../index';
import * as api from '../../../service/api'

export function LocalKidList(params){
    return get(api.api.LocalkidList)(params);
}

export function removeLocalkid(params){
    return del(api.api.removeLocalkid)(params);
}

export function Localkidadd(params){
    return post(api.api.Localkidadd)(params);
}
export function Localkidmf(params){
    return put(api.api.Localkidmf)(params);
}

