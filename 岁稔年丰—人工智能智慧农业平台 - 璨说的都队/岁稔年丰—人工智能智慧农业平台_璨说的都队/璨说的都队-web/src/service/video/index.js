import {get, del, post, put} from '../index';
import * as api from '../../service/api'

export function VideoList(params){
    return get(api.api.VideoList)(params);
}

export function removeVideo(params){
    return del(api.api.removeVideo)(params);
}

export function Videoadd(params){
    return post(api.api.Videoadd)(params);
}
export function Videomf(params){
    return put(api.api.Videomf)(params);
}

