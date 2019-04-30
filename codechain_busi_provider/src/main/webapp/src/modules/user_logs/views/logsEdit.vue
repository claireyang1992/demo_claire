<template>
    <div>
        <el-form :model="currentRecord"
                 ref="editForm"
                 :rules="formRules">
                    <el-form-item label="编号"
                      prop="id"
                      label-width="80px">
            <el-col :span="22">
                <el-input v-model="currentRecord.id"
                          :disabled="this.mode=='details'" placeholder="请输入编号"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="用户名"
                      prop="userName"
                      label-width="80px">
            <el-col :span="22">
                <el-input v-model="currentRecord.userName"
                          :disabled="this.mode=='details'" placeholder="请输入用户名"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="日志类型"
                      prop="type"
                      label-width="80px">
            <el-col :span="22">
                <el-input v-model="currentRecord.type"
                          :disabled="this.mode=='details'" placeholder="请输入日志类型"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="日志详情"
                      prop="info"
                      label-width="80px">
            <el-col :span="22">
                <el-input v-model="currentRecord.info"
                          :disabled="this.mode=='details'" placeholder="请输入日志详情"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="日期"
                      prop="date"
                      label-width="80px">
            <el-col :span="22">
                <el-input v-model="currentRecord.date"
                          :disabled="this.mode=='details'" placeholder="请输入日期"></el-input>
            </el-col>
        </el-form-item>
    <el-form-item align="center">
        <el-col :span="24">
            <el-button type="primary"
                       @click.native.prevent="handleSubmit">
                <i class="fa fa-save"></i> 保存
            </el-button>
        </el-col>
    </el-form-item>
    </el-form>
    </div>
</template>

<script>
    import {Message} from 'element-ui'
    import LogsApi from '../api/logs'

    export default {
        name: 'LogsEdit',
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
                currentRecord: this.record,
                formRules: {}
            }
        },
        watch: {
            record: function (oldValue, newValue) {
                this.currentRecord = this.record
                // this.getDetail()
            }
        },
        methods: {

            handleSubmit() {
                this.$refs['editForm'].validate(valid => {
                    if (valid) {
                        let newRecord = Object.assign({}, this.currentRecord)
                        if (this.mode === 'add') {
                            LogsApi.add(newRecord).then(rspData => {
                                if (rspData.data.code === 200) {
                                    Message.success('新增成功')
                                }
                                this.$emit('after-edit')
                            })
                        }
                        else if (this.mode === 'details') {
                            this.$emit('after-edit')
                        }
                        else {
                            LogsApi.update(newRecord).then(rspData => {
                                if (rspData.data.code === 200) {
                                    Message.success('修改成功')
                                }
                                this.$emit('after-edit')
                            })
                        }
                    } else {
                        return false
                    }
                })
            },
            resetFields() {
                this.$refs['editForm'].resetFields()
            }
        }
    }
</script>
