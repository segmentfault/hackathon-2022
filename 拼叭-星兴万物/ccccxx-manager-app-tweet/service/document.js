import { service } from '../framework';
export default {
  // 剧本列表
  documentPageInfoApi(data) {
    return service.postJson({
      url: '/document/api/script/pageInfo',
      data,
    });
  },
  // 剧本列表
  documentPageList(data) {
    return service.postJson({
      url: '/document/api/script/list',
      data,
    });
  },
  // 剧本列表
  scriptList(data) {
    return service.postJson({
      url: '/document/api/script/list',
      data: data,
    });
  },
  // 剧本详情
  scriptInfo(id) {
    return service.post({
      url: `/document/api/script/info?scriptId=${id}`,
    });
  },
  // 密逃列表
  secretEscapeList(data) {
    return service.postJson({
      url: '/document/api/secretEscape/list',
      data: data,
    });
  },
  //密逃详情
  secretEscapeInfo(id) {
    return service.post({
      url: `/document/api/secretEscape/info?secretEscapeId=${id}`,
    });
  },
  // 员工列表
  DMList(data) {
    return service.postJson({
      url: '/document/api/employee/list',
      data: data,
    });
  },
  // 密室到
  secretEscopePageInfo(data) {
    return service.postJson({
      url: '/document/api/secretEscape/pageInfo',
      data: data,
    });
  },

  // 密室到
  secretEscopePageList(data) {
    return service.postJson({
      url: '/document/api/secretEscape/list',
      data: data,
    });
  },

  // 剧本标签列表
  scriptTypeList() {
    return service.get({
      url: '/document/api/label/list',
    });
  },

  // 剧本人数列表
  scriptUserconutList() {
    return service.get({
      url: '/document/api/script/people/list',
    });
  },

  // 密逃标签列表
  penetraliumTypeList() {
    return service.get({
      url: '/document/api/labelSecret/list',
    });
  },

  // 密逃人数列表
  penetraliumUserconutList() {
    return service.get({
      url: '/document/api/labelSecret/people/list',
    });
  },
};
