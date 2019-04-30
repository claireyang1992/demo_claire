<template>
  <el-select
    :clearable="clearable"
    :placeholder="placeholder"
    :value="selectedValue"
    @input="handleInput($event)">
    <el-option
      v-for="item in dictData"
      :key="item.value"
      :label="item.label"
      :value="item.value" />
  </el-select>
</template>
<script>
/* eslint-disable */
  export default {
  name: 'EfDictSelect',
  props: {
    value: {
      required: true
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    clearable: {
      type: Boolean,
      default: false
    },
    dictType: {
      type: String,
      required: true
    }
  },
  data() {
    return {}
  },
  computed: {
    selectedValue() {
      return typeof this.value === 'number' ? this.value + '' : this.value
    },
    dictData() {
      return Object.values([
        {
          'code': 'MENU_TYPE',
          'value': 'sys_menu',
          'label': '系统菜单'
        },
        {
          'code': 'MENU_TYPE',
          'value': 'b_menu',
          'label': 'B端菜单'
        },
        {
          'code': 'MENU_TYPE',
          'value': 'c_menu',
          'label': 'C端菜单'
        }
      ])
    }
  },
  methods: {
    handleInput(newValue) {
      this.$emit('input', newValue) // 触发 input 事件，并传入新值
    }
  }
}
</script>
