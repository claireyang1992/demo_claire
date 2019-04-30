<template>
  <div class="container">
    <!-- 数据表格 -->
    <el-card>
      <div slot="header">
        <span style="line-height: 36px;">菜单管理</span>
        <el-button
          type="primary"
          style="float: right;"
          @click="handleAdd">
          <i class="fa fa-plus"/> 添加
        </el-button>
      </div>

      <ef-tree-table
        :columns="columns"
        :data="treePageData"
        :expand-change="handleExpandChange">
        <el-table-column
          key="id"
          prop="id"
          label="ID"/>
        <el-table-column
          key="path"
          prop="path"
          label="链接"/>
        <el-table-column
          key="component"
          prop="component"
          label="文件路径"/>
        <el-table-column
          key="sort"
          prop="sort"
          label="排序"/>
        <el-table-column
          key="isShow"
          prop="isShow"
          label="是否显示"/>
        <!-- 操作列 -->
        <el-table-column
          header-align="center"
          align="center"
          width="200"
          label="操作">
          <template slot-scope="scope">
            <el-dropdown
              split-button
              size="small"
              type="primary"
              @click="handleEdit(scope.row)">
              <i class="fa fa-pencil"/> 编辑
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item><span @click="handleAddChild(scope.row)"><i class="fa fa-indent"/> 添加下级</span></el-dropdown-item>
                <!-- <el-dropdown-item><span @click="handleMenuPermission(scope.row)"><i class="fa fa-th-large"></i> 功能权限</span></el-dropdown-item> -->
                <el-dropdown-item><span @click="handleDelete(scope.row)"><i class="fa fa-trash-o"/> 删除菜单</span></el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </ef-tree-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="mode==='add'?'添加菜单':'编辑菜单'"
      :visible.sync="showDialog"
      :before-close="handleDialogClose">
      <sys-menu-edit
        ref="editForm"
        :record="currentRecord"
        :mode="mode"
        @after-edit="afterEdit"/>
    </el-dialog>

  </div>

</template>

<script>
import EfTreeTable from '@/components/TreeTable/index'
import sysMenuEdit from '@/modules/menu/views/sysMenuEdit'
import MenuApi from '@/modules/menu/api/menu-api'

import router from '@/router'
import store from '@/store'
// import { changeToComponent } from '@/utils/replaceUtil'
// import Layout from '@/views/layout/Layout'

export default {
  components: {
    EfTreeTable,
    sysMenuEdit
  },
  data() {
    return {
      mode: 'edit',
      treePageData: [],
      currentRecord: {},
      showDialog: false,
      columns: [
        {
          value: 'name',
          text: '名称'
        }
      ]
    }
  },
  mounted() {
    this.handleQuery()
    this.addRoutes()
  },
  methods: {
    // 获取菜单树初始数据
    handleQuery() {
      //      this.axios.get('static/json-data/menudemo/treeTable.json').then((response) => {
      //        this.treePageData = response.data
      //      }).catch((response) => {
      //        console.log(response)
      //      })
      MenuApi.getParentNode().then(response => {
        const data = response.data
        if (data.code === 200) {
          // console.log(data.data)
          this.treePageData = data.data
        } else {
          this.$message({
            message: data.message,
            type: 'danger'
          })
        }
      })
    },
    // 动态更新路由表
    addRoutes() {
      // 插入路由表
      store.dispatch('GetUserInfo').then(res => {
        // 拉取user_info
        const roles = res.data.data.roles
        store.dispatch('GenerateRoutes', { roles }).then(() => {
          // 根据roles权限生成可访问的路由表
          router.addRoutes(store.getters.addRouters)
        })
      })
    },
    handleExpandChange(record, expanded) {
      if (expanded) {
        MenuApi.getChildrenNode(record.id).then(response => {
          const data = response.data
          if (data.code === 200) {
            // console.log(data.data)
            this.$set(record, 'children', data.data)
          }
        })
      }
    },
    afterEdit() {
      this.showDialog = false
      this.handleQuery()
      this.addRoutes()
    },
    handleAdd() {
      this.mode = 'add'
      this.showDialog = true
      this.currentRecord = { isshow: 1 }
    },
    handleAddChild(record) {
      this.mode = 'add'
      this.showDialog = true
      this.currentRecord = { parentId: record.id, isshow: 1 }
    },
    handleEdit(record) {
      this.mode = 'edit'
      this.showDialog = true
      debugger
      this.currentRecord = Object.assign({}, record, { parent: null })
      if (this.$refs.editForm) {
        this.$refs.editForm.init(this.currentRecord)
      }
    },
    handleDelete(record) {
      const self = this
      if (record.hasChildren) {
        this.$confirm('确定删除此菜单及其下面的子菜单吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            MenuApi.delete(record.id).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除' + record.id + '成功!'
                })
              } else {
                self.$message({
                  type: 'danger',
                  message: data.message
                })
              }
              self.handleQuery()
            })
          })
          .catch(() => {})
      } else {
        this.$confirm('确定删除此菜单吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            MenuApi.delete(record.id).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除' + record.id + '成功!'
                })
              } else {
                self.$message({
                  type: 'danger',
                  message: data.message
                })
              }
              self.handleQuery()
            })
          })
          .catch(() => {})
      }
    },
    handleDialogClose(done) {
      this.currentRecord = {}
      this.$refs.editForm.resetFields()
      done()
    }
  }
}
</script>
<style>
.container {
  margin: 20px;
}
</style>

