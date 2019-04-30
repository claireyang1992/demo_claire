<template>
  <div class="app-container">
    <el-card class="box-card search-container">
      <el-form
        ref="codeGenForm"
        :inline="true"
        :model="codeGenForm"
        :rules="codeGenRules">
        <el-form-item prop="source" label="源" label-width="50px">
          <el-select v-model="sourceValue" disabled placeholder="code_gen"/>
        </el-form-item>
        <el-form-item prop="tableName" label="表名" label-width="50px">
          <el-select
            v-model="codeGenForm.tableName"
            placeholder="请选择表名"
            @keyup.enter.native="handleQuery">
            <el-option
              v-for="item in tableNameList"
              :key="item.id"
              :label="item.value"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="handleQuery">
            <i class="fa fa-search"/> 查询
          </el-button>
          <el-button
            :disabled="genCodeDisable"
            type="success"
            @click="handleCoenGen">
            <i class="fa fa-search"/> 生成代码
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 页面数据表格 -->
    <el-card class="box-card">
      <!-- 数据表格 -->
      <el-table
        :data="tableData.results"
        :header-row-class-name="'table-head-th'"
        row-key="id"
        fit
        highlight-current-row>
        <el-table-column
          key="columnName"
          show-overflow-tooltip
          prop="columnName"
          label="字段名称"/>
        <el-table-column
          key="privileges"
          show-overflow-tooltip
          prop="privileges"
          label="权限"/>
        <el-table-column
          key="columnType"
          show-overflow-tooltip
          prop="columnType"
          label="字段类型"/>
        <el-table-column
          key="columnKey"
          show-overflow-tooltip
          prop="columnKey"
          label="主键"/>
        <el-table-column
          key="isNullable"
          show-overflow-tooltip
          prop="isNullable"
          label="是否可为空"/>
        <el-table-column
          key="columnComment"
          show-overflow-tooltip
          prop="columnComment"
          label="备注"/>
      </el-table>
      <!-- 分页信息 -->
      <!--<div class="pagination-container">-->
      <!--<el-pagination-->
      <!--:current-page="queryParam['pageNum']"-->
      <!--:total="tableData.total"-->
      <!--background-->
      <!--layout="total, prev, pager, next, jumper"-->
      <!--@current-change="handlePageChange"-->
      <!--@size-change="handleSizeChange"/>-->
      <!--</div>-->
      <!-- 新增/编辑对话框 -->
      <el-dialog
        :title="'生成代码'"
        :visible.sync="showEditDialog"
        :before-close="handleDialogClose">
        <menu-code-gen-edit
          ref="editForm"
          :tabledata="codeGenForm.tableName"
          @after-edit="handleAfterEdit"/>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import CodeGenApi from '../api/codegen'
import MenuCodeGenEdit from './menuCodeGenEdit'
export default {
  name: 'MenuCodeGen',
  components: {
    MenuCodeGenEdit
  },
  data() {
    return {
      codeGenForm: {
        tableName: ''
      },
      // 数据库表列表
      tableNameList: [],
      sourceValue: 'code_gen',
      // 表格数据
      tableData: {
        results: []
      },
      codeGenRules: {
        tableName: [{ required: true, message: '请选择表名', trigger: 'blur' }]
      },
      loading: false,
      genCodeDisable: false,
      // 是否显示添加/编辑对话框
      showEditDialog: false
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.handleTableNameList()
    },
    handleTableNameList() {
      return new Promise((resolve, reject) => {
        CodeGenApi
          .getTableNameList()
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.tableNameList = data
            } else {
              Message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            Message.error(error)
            reject(error)
          })
      })
    },
    handleCoenGen() {
      this.showEditDialog = true
    },
    handleClick() {
      this.$refs.codeGenForm.validate(valid => {
        if (valid) {
          console.log('yyy')
        }
      })
    },
    handleQuery() {
      this.query()
    },
    query() {
      return new Promise((resolve, reject) => {
        CodeGenApi
          .getTableColumn(this.codeGenForm.tableName)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.tableData.results = data
            } else {
              Message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            Message.error(error)
            reject(error)
          })
      })
    },
    // 处理翻页事件
    handlePageChange(currentPage) {
      this.queryParam['pageNum'] = currentPage
      this.query()
    },
    // 处理页面总条数事件
    handleSizeChange(val) {
      this.queryParam['pageSize'] = val
      this.query()
    },
    // 处理编辑对话框关闭事件
    handleAfterEdit() {
      this.showEditDialog = false
      this.$refs.editForm.resetFields()
      // this.query()
    },
    // 处理对话框关闭事件
    handleDialogClose(done) {
      this.$refs.editForm.resetFields()
      done()
    }
  }
}
</script>

<style scoped>
  .box-card {
    width: 100%;
  }
  .app-container {
    background-color: #f0f2f5;
    padding: 30px;
    min-height: calc(100vh - 84px);
  }
  .button-container{
    margin-top: 30px;
    position: relative;
  }
  .search-container {
    margin-bottom: 20px;
  }
  .pagination-container {
    margin-top: 30px;
    text-align: right;
  }
</style>
