<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleDelete"
    okText="删除模型"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-row>
      <a-col :span="12">
        <a-form-model-item label="编号" prop="modelCode">
          <a-input v-model="modelCode"></a-input>
        </a-form-model-item>
      </a-col>
    </a-row>
  </j-modal>
</template>
<script>

import { httpAction } from '@api/manage'

export default {
  name: 'BimDemoDelete',
  data() {
    return {
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false,
      modelCode: ''
    }
  },
  methods: {
    init() {
      this.visible = true
    },
    handleCancel() {
      this.visible = false
    },
    handleDelete() {
      httpAction('/bim/model/deleteFile', { fileId: this.modelCode }, 'get').then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.$emit('ok')
        } else {
          this.$message.warning(res.message)
        }
      })
    }
  }
}
</script>