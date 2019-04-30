<template>
  <div class="container">
    <el-card class="search-container">
      <el-form :inline="true" :model="searchFrom" class="demo-form-inline">
        <el-form-item
          label="用户名"
          prop="userName"
          label-width="80px"
        >
          <el-col :span="22">
            <el-input
              :rows="5"
              v-model="searchFrom.userName"
              clearable
              type="text"
              placeholder="请输入用户名"/>
          </el-col>
        </el-form-item>
        <el-form-item
          label="日志类型"
          prop="type"
          label-width="80px"
        >
          <el-col :span="22">
            <el-input
              :rows="5"
              v-model="searchFrom.type"
              clearable
              type="text"
              placeholder="请输入日志类型"/>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-container">
      <el-table
        :data="tableData.list"
        :header-row-class-name="'table-head-th'"
        row-key="id"
        fit
        highlight-current-row>
        <el-table-column
          key="id"
          show-overflow-tooltip
          prop="id"
          label="编号"/>
        <el-table-column
          key="userName"
          show-overflow-tooltip
          prop="userName"
          label="用户名"/>
        <el-table-column
          key="type"
          show-overflow-tooltip
          prop="type"
          label="日志类型"/>
        <el-table-column
          key="info"
          show-overflow-tooltip
          prop="info"
          label="日志详情"/>
        <el-table-column
          key="date"
          :formatter="dateFormat"
          show-overflow-tooltip
          prop="date"
          label="日期"/>
        <!-- 表格操作列 -->
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                title="查看"
                type="primary"
                @click="handleDetail(scope.row)">
                <i class="fa fa-eye"/>
              </el-button>
              <el-button
                size="mini"
                title="编辑"
                type="primary"
                @click="handleEdit(scope.row)">
                <i class="fa fa-pencil"/>
              </el-button>
              <el-button
                size="mini"
                title="删除"
                type="danger"
                @click="handleDel(scope.row)">
                <i class="fa fa-trash-o"/>
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="searchFrom['pageNum']"
          :total="tableData.total"
          background
          layout="total, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"/>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="mode==='add'?'新增':'编辑'"
      :visible.sync="showEditDialog"
      :before-close="handleDialogClose">
      <logs-edit
        ref="editForm"
        :mode="mode"
        :record="currentRecord"
        @after-edit="handleAfterEdit"/>
    </el-dialog>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import LogsApi from '../api/logs'
import logsEdit from './logsEdit'

export default {
  components: {
    logsEdit
  },
  data() {
    return {
      /* 搜索请求数据*/
      searchFrom: {
        'pageSize': 10,
        'pageNum': 1
      },
      // 表格数据
      tableData: {
        results: []
      },
      mode: 'edit',
      // 是否显示添加/编辑对话框
      showEditDialog: false,
      // 当前处理行
      currentRecord: {}
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.handleQuery()
    },
    handleQuery() {
      this.searchFrom['pageNum'] = 1
      this.query()
    },
    // 时间格式化
    dateFormat(row, column, cellValue, index) {
      const daterc = row[column.property]
      var date = new Date(daterc)
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? ('0' + m) : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      var h = date.getHours()
      h = h < 10 ? ('0' + h) : h
      var minute = date.getMinutes()
      var second = date.getSeconds()
      minute = minute < 10 ? ('0' + minute) : minute
      second = second < 10 ? ('0' + second) : second
      return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second
    },
    query() {
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.searchFrom)
        LogsApi
          .queryByCond(param)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.tableData = data
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
    handleAdd() {
      this.mode = 'add'
      this.currentRecord = {}
      this.showEditDialog = true
    },
    handleDetail(row) {
      this.mode = 'details'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
    },

    // 处理翻页事件
    handlePageChange(currentPage) {
      this.searchFrom['pageNum'] = currentPage
      this.query()
    },
    // 处理页面总条数事件
    handleSizeChange(val) {
      // console.log(`每页  条`);
      this.searchFrom['pageSize'] = val
      this.query()
    },
    // 处理对话框关闭事件
    handleDialogClose(done) {
      this.$refs.editForm.resetFields()
      done()
    },
    handleDel(row) {
      this.$confirm('确定删除吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          LogsApi
            .delete(row.id)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                })
                this.handleQuery()
              } else {
                Message.error(data.message)
              }
            })
            .catch(error => {
              Message.error(error)
            })
        })
        .catch(() => {
        })
    },
    // 处理编辑对话框关闭事件
    handleAfterEdit() {
      this.showEditDialog = false
      this.$refs.editForm.resetFields()
      this.query()
    }
  }
}
</script>
<style>
    .container {
        margin: 20px;
    }

    .table-container {
        margin-top: 20px;
    }
    .pagination-container {
      margin-top: 30px;
      text-align: right;
    }
</style>

