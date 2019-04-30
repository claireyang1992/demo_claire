<template>
  <div class="container">
    <el-card class="search-container">
      <el-form :inline="true" :model="searchFrom" class="demo-form-inline">
        <el-form-item
          label="用户名"
          prop="userName">
          <el-col :span="22">
            <el-input :rows="5" v-model="searchFrom.userName" clearable type="text" placeholder="请输入用户名"/>
          </el-col>
        </el-form-item>
        <el-form-item
          label="电子邮件"
          prop="email"
          label-width="80px">
          <el-col :span="22">
            <el-input :rows="5" v-model="searchFrom.email" clearable type="text" placeholder="请输入电子邮件"/>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
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
          key="userName"
          show-overflow-tooltip
          prop="userName"
          label="用户名"/>
        <el-table-column
          key="email"
          show-overflow-tooltip
          prop="email"
          label="电子邮件"/>
        <el-table-column
          key="introduction"
          show-overflow-tooltip
          prop="introduction"
          label="介绍"/>
        <el-table-column
          key="state"
          show-overflow-tooltip
          prop="state"
          label="状态"/>
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
                title="权限管理"
                type="primary"
                @click="handlePerm(scope.$index, scope.row)">
                <i class="fa fa-pencil"/>
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
      <el-form :model="dialogForm">
        <el-form-item key="id" label="编号" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['id']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="userName" label="用户名" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['userName']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="email" label="电子邮件" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['email']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="password" label="密码" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['password']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="salt" label="盐" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['salt']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="token" label="用户令牌" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['token']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="avatar" label="头像" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['avatar']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="introduction" label="介绍" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['introduction']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item key="state" label="状态" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['state']" :disabled="dialogType=='see'" :rows="5" auto-complete="off"/>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="SubmitCancle">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog :visible.sync="PermFormVisible" title="权限管理">
      <el-form :model="PermForm" :rules="formRules">
        <el-form-item key="userName" label="用户名" label-width="100px">
          <el-col :span="22">
            <el-input v-model="PermForm['userName']" :rows="5" :disabled="true" auto-complete="off"/>
          </el-col>
        </el-form-item>
        <el-form-item
          label="用户角色"
          prop="checkedPart"
          label-width="100px">
          <el-col :span="22" class="checkbox-group">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <el-checkbox-group v-model="PermForm['checkedPart']" size="mini" @change="handlecheckedPartChange">
              <el-checkbox v-for="role in rolesOptions" :label="role['role']" :key="role['id']" name="checkedPart" class="checkboxContent" border>{{ role['description'] }}</el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="SubmitCancle">取 消</el-button>
        <el-button type="primary" @click="SubmitPerForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { Message } from 'element-ui'
import SysUserApi from '../api/sysUser'
import SysRoleApi from '@/modules/sys_role/api/sysRole'
import UserRoleRelApi from '@/modules/sys_role/api/userRoleRel'
export default {
  data() {
    return {
      checkAll: false,
      isIndeterminate: true,
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
      PermForm: {
        id: '',
        userName: '',
        checkedPart: [] },
      dialogType: 'see',
      dialogFormVisible: false,
      PermFormVisible: false,
      formLabelWidth: '120px',
      rolesOptions: [],
      // 角色列表
      rolesList: [],
      formRules: {
        checkedPart: [{ type: 'array', required: true, message: '至少选择一个部分', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.getAllRole()
      this.query()
    },
    handleQuery() {
      this.query()
    },
    handleCheckAllChange(val) {
      this.PermForm.checkedPart = val ? this.rolesList : []
      this.isIndeterminate = false
    },
    handlecheckedPartChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.rolesList.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.rolesList.length
    },
    getThisUserRole(id) {
      return new Promise((resolve, reject) => {
        SysRoleApi
          .queryUserRole(id)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.PermForm.checkedPart = data
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
    getAllRole() {
      return new Promise((resolve, reject) => {
        SysRoleApi
          .query()
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.rolesOptions = data.list
              for (var role in data.list) {
                this.rolesList.push(data.list[role].role)
              }
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
    query() {
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.searchFrom)
        SysUserApi
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
    handlePerm(index, row) {
      this.PermForm.id = row.id
      this.PermForm.userName = row.userName
      this.getThisUserRole(row.id)
      this.PermFormVisible = true
    },
    SubmitCancle() {
      this.dialogFormVisible = false
      this.PermFormVisible = false
      this.query()
    },
    SubmitPerForm(row) {
      this.loading = true
      const params = Object.assign({}, this.PermForm)
      return new Promise((resolve, reject) => {
        UserRoleRelApi
          .updateUserRoleRel(params)
          .then(response => {
            this.loading = false
            const data = response.data
            if (data.code === 200) {
              this.$message({
                message: '修改角色成功',
                type: 'success'
              })
              this.PermFormVisible = false
              this.query()
            } else {
              Message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            this.loading = false
            Message.error(error)
            reject(error)
          })
      })
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

