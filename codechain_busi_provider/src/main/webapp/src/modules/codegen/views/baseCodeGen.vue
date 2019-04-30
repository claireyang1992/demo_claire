<template>
  <div class="baseCodeGen-container">
    <el-row>
      <el-form ref="codeGenForm" :model="codeGenForm" :rules="codeGenRules" class="register-form" auto-complete="on" label-position="left">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>填写基础信息</span>
          </div>
          <div>
            <el-form-item prop="jdbcUrl" label="数据库地址" label-width="100px">
              <el-input
                v-model="codeGenForm.jdbcUrl"
                placeholder="请输入数据库地址"
                name="jdbcUrl"
                type="text"
                auto-complete="on"
              />
            </el-form-item>
            <el-form-item prop="username" label="用户名" label-width="100px">
              <el-input
                v-model="codeGenForm.username"
                placeholder="请输入用户名"
                name="username"
                type="text"
                auto-complete="on"
              />
            </el-form-item>
            <el-form-item prop="password" label="密码" label-width="100px">
              <el-input
                v-model="codeGenForm.password"
                placeholder="请输入密码"
                name="password"
                type="password"
                auto-complete="on"
              />
            </el-form-item>
            <el-form-item prop="tableName" label="表名" label-width="100px">
              <el-input
                v-model="codeGenForm.tableName"
                placeholder="多个表用英文逗号分隔"
                name="tableName"
                type="text"
                auto-complete="on"
              />
            </el-form-item>
            <div class="button-container">
              <el-button :loading="loading" :disabled="genCodeDisable" type="primary" style="width:30%;height:50px" @click.native.prevent="handleCodeGen">生成代码</el-button>
              <el-button :loading="loading" :disabled="DownloadDisable" type="success" style="width:30%;height:50px" @click.native.prevent="handleDownload">下载代码</el-button>
            </div>
          </div>
        </el-card>
      </el-form>
    </el-row>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import CodeGenApi from '../api/codegen'
export default {
  name: 'BaseCodeGen',
  components: {

  },
  data() {
    return {
      codeGenForm: {
        jdbcUrl: '',
        password: '',
        username: '',
        tableName: ''
      },
      codeGenRules: {
        jdbcUrl: [{ required: true, message: '数据库地址不能为空', trigger: 'blur' }],
        password: [],
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        tableName: [{ required: true, message: '数据库地址不能为空', trigger: 'blur' }]
      },
      loading: false,
      genCodeDisable: false,
      DownloadDisable: true
    }
  },
  methods: {
    handleCodeGen() {
      this.$refs.codeGenForm.validate(valid => {
        if (valid) {
          this.loading = true
          const params = Object.assign({}, this.codeGenForm)
          return new Promise((resolve, reject) => {
            CodeGenApi
              .genCode(params)
              .then(response => {
                const data = response.data
                this.loading = false
                if (data.code === 200) {
                  Message.success('生成代码成功..')
                  this.genCodeDisable = true
                  this.DownloadDisable = false
                  this.projectName = data.data
                } else {
                  Message.error(data.message)
                  return
                }
                resolve(data)
              })
              .catch(error => {
                this.loading = false
                Message.error(error)
                reject(error)
              })
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    handleDownload() {
      return new Promise((resolve, reject) => {
        CodeGenApi
          .codeDownload(this.projectName)
          .then(response => {
            this.download(response)
            this.genCodeDisable = false
            this.DownloadDisable = true
          })
          .catch(error => {
            this.loading = false
            reject(error)
          })
      })
    },
    // 下载文件
    download(data) {
      const blob = data.data
      const reader = new FileReader()
      reader.readAsDataURL(blob)
      reader.onload = (e) => {
        const a = document.createElement('a')
        a.download = this.projectName
        a.href = e.target.result
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
      }
    }
  }
}
</script>

<style scoped>
  .box-card {
    width: 50%;
  }
  .baseCodeGen-container {
    background-color: #f0f2f5;
    padding: 30px;
    min-height: calc(100vh - 84px);
  }
  .button-container{
    margin-top: 30px;
    position: relative;
  }
</style>
