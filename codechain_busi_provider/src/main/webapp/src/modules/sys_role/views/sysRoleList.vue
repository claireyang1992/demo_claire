<template>
  <div class="container">
    <el-card class="search-container">
      <el-form :inline="true" :model="searchFrom" class="demo-form-inline">
        <el-form-item
          label="编号"
          prop="id"
        >
          <el-col :span="22">
            <el-input :rows="5" v-model="searchFrom.id" clearable type="text" placeholder="请输入编号"/>
          </el-col>
        </el-form-item>
        <el-form-item
          label="角色"
          prop="role"
          label-width="80px">
          <el-col :span="22">
            <el-input :rows="5" v-model="searchFrom.role" clearable type="text" placeholder="请输入角色"/>
          </el-col>
        </el-form-item>
        <el-form-item
          label="角色描述"
          prop="description"
          label-width="80px">
          <el-col :span="22">
            <el-input :rows="5" v-model="searchFrom.description" clearable type="text" placeholder="请输入角色描述"/>
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
        :data="tableData.results"
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
          key="role"
          show-overflow-tooltip
          prop="role"
          label="角色"/>
        <el-table-column
          key="description"
          show-overflow-tooltip
          prop="description"
          label="角色描述"/>
        <!-- 表格操作列 -->
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                title="查看"
                type="primary"
                @click="handleSee(scope.$index, scope.row)">
                <i class="fa fa-eye"/>
              </el-button>
              <el-button
                size="mini"
                title="编辑"
                type="primary"
                @click="handleEdit(scope.$index, scope.row)">
                <i class="fa fa-pencil"/>
              </el-button>
              <el-button
                size="mini"
                title="删除"
                type="danger"
                @click="handleDel(scope.$index, scope.row)">
                <i class="fa fa-trash-o"/>
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页信息 -->
    <!-- <div class="pagination-container">
        <el-pagination background
                       layout="total, prev, pager, next, jumper"
                       @current-change="handlePageChange"
                       @size-change="handleSizeChange"
                       :current-page="queryParam['pageNum']"
                       :total="tableData.total">
        </el-pagination>
      </div> -->

    <!-- 详情弹窗 -->
    <el-dialog :visible.sync="dialogFormVisible" :title="dialogType=='see'?'查看详情':'编辑'">
      <el-form :model="dialogForm" :rules="formRules">
        <el-form-item key="id" label="编号" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['id']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item prop="role" label="角色" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['role']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item prop="description" label="角色描述" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['description']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="SubmitCancle">取 消</el-button>
        <el-button type="primary" @click="Submit">确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
import { Message } from 'element-ui'
import SysRoleApi from '../api/sysRole'
export default {
  data() {
    return {
      /* 搜索面板数据*/
      searchConfig: [],
      /* 搜索请求数据*/
      searchFrom: {},
      /* 表格面板数据*/
      tableConfig: [],
      // 表格数据
      tableData: {
        results: []
      },
      dialogForm: {},
      dialogType: 'see',
      dialogFormVisible: false,
      formLabelWidth: '120px',
      formRules: {
        role: [{ required: true, message: '请输入角色', trigger: 'blur' }],
        description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.query()
    },
    handleQuery() {
      this.query()
    },
    query() {
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.searchFrom)
        SysRoleApi
          .queryByCond(param)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.tableData.results = data.list
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
      this.dialogType = 'add'
      this.dialogForm = {}
      this.dialogFormVisible = true
    },
    handleSee(index, row) {
      this.dialogType = 'see'
      this.dialogForm = row
      this.dialogFormVisible = true
    },
    handleEdit(index, row) {
      this.dialogType = 'edit'
      this.dialogForm = row
      this.dialogFormVisible = true
    },
    handleDel(index, row) {
      return new Promise((resolve, reject) => {
        SysRoleApi
          .delete(row.id)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.query()
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
    SubmitCancle() {
      this.dialogFormVisible = false
      this.query()
    },
    Submit() {
      if (this.dialogType === 'see') {
        this.dialogFormVisible = false
      } else if (this.dialogType === 'add') {
        this.dialogFormVisible = false
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.dialogForm)
          SysRoleApi
            .add(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
                this.query()
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
      } else if (this.dialogType === 'edit') {
        this.dialogFormVisible = false
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.dialogForm)
          SysRoleApi
            .update(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                this.query()
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
      }
    }

  }
}
</script>
<style>
  .container{
    margin: 20px;
  }

  .table-container{
    margin-top: 20px;
  }
</style>

