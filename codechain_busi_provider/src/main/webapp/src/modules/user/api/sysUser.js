import request from '@/utils/request'

class SysUserApi {
  query(data) {
    return request({
      url: '/sysuser/list',
      method: 'get',
      data
    })
  }

  queryByCond(data) {
    return request({
      url: '/sysuser/queryByCond',
      method: 'post',
      data
    })
  }

  add(data) {
    return request({
      url: '/sysuser/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/sysuser/update',
      method: 'post',
      data
    })
  }
  delete(id) {
    return request({
      url: `/sysuser/delete?id=${id}`,
      method: 'get'
    })
  }
}

export default new SysUserApi()
