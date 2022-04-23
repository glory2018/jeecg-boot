<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelCode">
              <a-input disabled v-model="model.modelCode"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelName">
              <a-input v-model="model.modelName" placeholder="请输入名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelType">
              <a-input v-model="model.modelType" placeholder="请输入类型"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelVersion">
              <a-input v-model="model.modelVersion" placeholder="请输入版本"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大小" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelSize">
              <a-input v-model="model.modelSize" placeholder="请输入大小" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模型状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modelStatus">
              <a-input v-model="model.modelStatus" placeholder="请输入模型状态"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="uploadUrl"
                        @change="handleUpload">
                <a-button type="primary" icon="import"></a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction } from '@api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'

export default {
  name: 'BimDemoForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      model: { modelCode: '', modelName: '', modelType: '', modelSize: '', modelStatus: '' },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/bim/bimDemo/add',
        edit: '/bim/bimDemo/edit',
        queryById: '/bim/bimDemo/queryById',
        uploadUrl: '/bim/model/uploadUrl'
      }
    }
  },
  computed: {
    //token header
    tokenHeader() {
      let head = { 'X-Access-Token': Vue.ls.get(ACCESS_TOKEN) }
      let tenantid = Vue.ls.get(TENANT_ID)
      if (tenantid) {
        head['tenant-id'] = tenantid
      }
      return head
    },
    uploadUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.uploadUrl}`
    },
    formDisabled() {
      return this.disabled
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }
      })
    },
    /* 上传 */
    handleUpload(info) {
      this.loading = true
      if (info.file.status === 'done') {
        this.loading = false
        if (info.file.response.success) {
          this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
          this.model = info.file.response.result
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.message}.`)
        }

      } else if (info.file.status === 'error') {
        this.$message.error(`文件上传失败: ${info.file.msg} `)
      }
    }
  }
}
</script>