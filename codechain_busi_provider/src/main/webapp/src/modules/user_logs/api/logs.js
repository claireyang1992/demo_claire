import request from '@/utils/request'

class LogsApi {
  query(data) {
    return request({
      url: '/logs/list',
      method: 'get',
      data
    })
  }

  queryByCond(data) {
    return request({
      url: '/logs/queryByCond',
      method: 'post',
      data
    })
  }

  add(data) {
    return request({
      url: '/logs/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/logs/update',
      method: 'post',
      data
    })
  }
  delete(id) {
    return request({
      url: `/logs/delete?id=${id}`,
      method: 'get'
    })
  }
}

export default new LogsApi()
