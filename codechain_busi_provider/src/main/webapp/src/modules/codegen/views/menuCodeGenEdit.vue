<template>
  <div>
    <el-form
      ref="editForm"
      :model="editForm"
      :rules="formRules">
      <el-form-item
        label="表名"
        prop="tableName"
        label-width="80px">
        <el-col :span="22">
          <el-input :rows="5" v-model="editForm.tableName" :disabled="true" type="text" placeholder="表名"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="模块名"
        prop="moduleName"
        label-width="80px">
        <el-col :span="22">
          <el-input :rows="5" v-model="editForm.moduleName" type="text" placeholder="请输入模块名"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="作者"
        prop="author"
        label-width="80px">
        <el-col :span="22">
          <el-input :rows="5" v-model="editForm.author" type="text" placeholder="请输入模块名"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="生成代码"
        prop="checkedPart"
        label-width="80px">
        <el-col :span="22" class="checkbox-group">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="editForm.checkedPart" size="mini" @change="handlecheckedPartChange">
            <el-checkbox v-for="module in modules" :label="module" :key="module" name="checkedPart" class="checkboxContent" border>{{ module }}</el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-form-item>
      <div class="button-container">
        <el-button :loading="loading" type="primary" style="width:40%;" @click.native.prevent="handleCodeGen">生成代码</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import CodeGenApi from '../api/codegen'
const moduleOptions = ['Controller', 'Entity', 'Service', 'ServiceImpl', 'Mapper', 'xmlMapper', 'vueApi', 'vueView']
export default {
  name: 'MenuCodeGenEdit',
  components: {
  },
  props: {
    tabledata: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      checkAll: false,
      modules: moduleOptions,
      isIndeterminate: true,
      editForm: {
        tableName: '',
        moduleName: '',
        author: '',
        checkedPart: []
      },
      loading: false,
      formRules: {
        moduleName: [{ required: true, message: '模块名不能为空', trigger: 'blur' }],
        checkedPart: [{ type: 'array', required: true, message: '至少选择一个部分', trigger: 'change' }]
      }
    }
  },
  watch: {
    tabledata: function(oldValue, newValue) {
      this.editForm.tableName = this.tabledata
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.editForm.tableName = this.tabledata
    },
    handleCheckAllChange(val) {
      this.editForm.checkedPart = val ? moduleOptions : []
      this.isIndeterminate = false
    },
    handlecheckedPartChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.modules.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.modules.length
      if (value.includes('Mapper')) {
        value.includes('Entity') ? value : this.editForm.checkedPart.push('Entity')
        value.includes('xmlMapper') ? value : this.editForm.checkedPart.push('xmlMapper')
      }
      if (value.includes('xmlMapper')) {
        value.includes('Entity') ? value : this.editForm.checkedPart.push('Entity')
      }
    },
    resetFields() {
      this.$refs['editForm'].resetFields()
      this.editForm.tableName = this.tabledata
    },
    handleCodeGen() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.loading = true
          const params = Object.assign({}, this.editForm)
          return new Promise((resolve, reject) => {
            CodeGenApi
              .genMenuCode(params)
              .then(response => {
                const data = response.data
                this.loading = false
                if (data.code === 200) {
                  Message.success('生成代码成功，请于工程目录中查看')
                  this.$emit('after-edit')
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
    }
  }
}
</script>
<style scoped>
  .checkbox-group {
    float: left;
  }
  .checkboxContent {
    width: 20%;
    margin-right: 10px;
  }
  .el-checkbox.is-bordered+.el-checkbox.is-bordered {
    margin-left: 0px;
    margin-right: 10px;
  }
  .button-container{
    position: relative;
    text-align: center;
  }

</style>
