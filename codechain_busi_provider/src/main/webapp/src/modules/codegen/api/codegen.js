import request from '@/utils/request'

class CodeGenApi {
  genCode(data) {
    return request({
      url: '/code/genBaseCodeByCond2',
      method: 'post',
      // headers: {
      //   'Content-Type': 'application/x-www-form-urlencoded'
      // },
      data
    })
  }
  codeDownload(projectName) {
    return request({
      url: `/code/downloadFile?projectName=${projectName}`,
      method: 'get',
      responseType: 'blob'
    })
  }
  getTableNameList() {
    return request({
      url: '/database/listTable',
      method: 'get'
    })
  }
  getTableColumn(tableName) {
    return request({
      url: `/database/listTableColumn?tableName=${tableName}`,
      method: 'get'
    })
  }
  genMenuCode(data) {
    return request({
      url: '/code/genMenuCode',
      method: 'post',
      data
    })
  }
}

export default new CodeGenApi()
