<template>
  <div>
    <el-form
      ref="editForm"
      :model="record"
      :rules="formRules">
      <!-- <el-form-item
        label="ID"
        prop="id"
        label-width="70px">
        <el-col :span="22">
          <el-input v-model="record.id"/>
        </el-col>
      </el-form-item> -->
      <el-form-item
        label="名称"
        prop="name"
        label-width="70px">
        <el-col :span="22">
          <el-input v-model="record.name"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="菜单角色"
        prop="checkedPart1"
        label-width="100px">
        <el-col :span="22" class="checkbox-group">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="checkedPart" size="mini" @change="handlecheckedPartChange">
            <el-checkbox v-for="role in rolesOptions" :label="role['role']" :key="role['id']" name="checkedPart" class="checkboxContent" border>{{ role['description'] }}</el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-form-item>

      <!-- <el-form-item
        label="类型"
        prop="type"
        label-width="70px">
        <el-col :span="22">
          <ef-dict-select
            v-model="record.type"
            clearable
            style="width: 100%;"
            prop="type"
            placeholder="类型"
            dict-type="MENU_TYPE"/>
        </el-col>
      </el-form-item> -->

      <el-form-item
        label="链接"
        prop="path"
        label-width="70px">
        <el-col :span="22">
          <el-input v-model="record.path"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="文件路径"
        prop="component"
        label-width="70px">
        <el-col :span="22">
          <el-input v-model="record.component"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="图标"
        prop="icon"
        label-width="70px">
        <el-col :span="22">
          <el-input v-model="record.icon"/>
        </el-col>
      </el-form-item>
      <el-form-item
        label="排序"
        prop="sort"
        label-width="70px">
        <el-col :span="22">
          <el-input-number
            v-model="record.sort"
            class="ef-input-number"
            controls-position="right"/>
        </el-col>
      </el-form-item>

      <el-form-item
        prop="isShow"
        label="是否显示"
        label-width="70px">
        <el-switch
          v-model="record.isShow"
          :active-value="true"
          :inactive-value="false"/>
      </el-form-item>
      <el-form-item align="center">
        <el-col :span="24">
          <el-button
            type="primary"
            @click.native.prevent="handleSubmit">
          <i class="fa fa-save"/> 保存</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
// import { lengthValidator } from '@/utils/index'
import EfDictSelect from '@/components/EfDictSelect'
import MenuApi from '../api/menu-api'
import SysRoleApi from '@/modules/sys_role/api/sysRole'
// import store from '@/store'
/* eslint-disable */
export default {
  name: 'SysMenuEdit',
  components: {
    EfDictSelect
  },
  props: {
    mode: {
      type: String
    },
    record: {
      type: Object
    }
  },
  data() {
    return {
      checkAll: false,
      isIndeterminate: true,
      // 角色列表
      rolesOptions: [],
      rolesList: [],
      checkedPart:[],
      checkedPart1:[],
      formRules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          {
            max: 32,
            message: '最大长度不能超过100个字符',
            trigger: 'blur'
          }
        ],
        checkedPart: [{ type: 'array', required: true, message: '至少选择一个部分', trigger: 'change' }],
        path: [
          { required: true, message: '请输入链接', trigger: 'blur' },
          {
            max: 200,
            message: '最大长度不能超过200个字符',
            trigger: 'blur'
          }
        ],
        permission: [
          {
            max: 100,
            message: '最大长度不能超过100个字符',
            trigger: 'blur'
          }
        ],
        menuIcon: [
          {
            max: 255,
            message: '最大长度不能超过255个字符',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted() {
    this.init(this.record)
  },
  methods: {
    init(row){
      this.record = row;
      this.getAllRole();
      this.checkedPart = row.meta.roles;
    },
    handleSubmit() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          let parentId;
          let meta = {
            "title":"",
            "icon":"",
            "roles":[]
          }
          //添加二级菜单
          meta.title = this.record.name;
          meta.icon = this.record.icon;
          meta.roles = this.checkedPart;
          const newRecord = Object.assign({}, this.record, {
            meta:meta
          })
          if (this.mode === 'add') {
            //新增需要添加isChildren字段
            let isChildren;
            let node;
            if(!this.record.parentId){
              isChildren = false
              node = 0
            }else{
              parentId = this.record.parentId
              isChildren = true
              node = 1
            }
            const newRecord = Object.assign({}, this.record, {
              meta:meta,
              isChildren:isChildren,
              node:node
            })
            //添加菜单
            MenuApi.add(newRecord).then(response =>{
              const data = response.data
              if (data.code === 200) {
                // console.log(data)
                this.$message({
                  message: "父菜单添加成功!",
                  type: 'success'
                });
                this.$emit('after-edit')
              }else{
                this.$message({
                  message: data.message,
                  type: 'danger'
                });
              }
            })
            // console.log(newRecord)
          } else {
            this.$emit('after-edit')
            const newRecord = Object.assign({}, this.record, {
              meta:meta
            })
            //修改菜单信息
            MenuApi.update(newRecord).then(response =>{
              const data = response.data
              if (data.code === 200) {
                // console.log(data)
                this.$message({
                  message: "菜单修改成功!",
                  type: 'success'
                });
                debugger
                this.$emit('after-edit')
              }else{
                this.$message({
                  message: data.message,
                  type: 'danger'
                });
              }
            })
          }
        } else {
          return false
        }
      })
    },
    resetFields() {
      this.$refs['editForm'].resetFields()
    },
    handleCheckAllChange(val) {
      this.checkedPart = val ? this.rolesList : []
      this.isIndeterminate = false
    },
    handlecheckedPartChange(value) {
      console.log(this.checkedPart)
      const checkedCount = value.length
      this.checkAll = checkedCount === this.rolesList.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.rolesList.length
    },
    getAllRole() {
        SysRoleApi.query().then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.rolesOptions = data.list
              for (var role in data.list) {
                this.rolesList.push(data.list[role].role)
              }
            }
        })
    }
  }
}
</script>

