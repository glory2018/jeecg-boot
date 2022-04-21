<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('建筑信息模型')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
                     @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handlePreview(record)">预览</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <bim-demo-modal ref="modalForm" @ok="modalFormOk"></bim-demo-modal>
    <bim-demo-preview ref="modalView"></bim-demo-preview>
  </a-card>
</template>

<script>

import '@assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BimDemoModal from './modules/BimDemoModal'
import BimDemoPreview from '@views/bim/modules/BimDemoPreview'
import { deleteAction, getAction } from '@api/manage'

export default {
  name: 'BimDemoList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BimDemoPreview,
    BimDemoModal
  },
  data() {
    return {
      description: '建筑信息模型管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '模型编号',
          align: 'center',
          dataIndex: 'modelCode'
        },
        {
          title: '模型名称',
          align: 'center',
          dataIndex: 'modelName'
        },
        {
          title: '模型类型',
          align: 'center',
          dataIndex: 'modelType'
        },
        {
          title: '模型版本',
          align: 'center',
          dataIndex: 'modelVersion'
        },
        {
          title: '模型大小',
          align: 'center',
          dataIndex: 'modelSize'
        },
        {
          title: '转换状态',
          align: 'center',
          dataIndex: 'modelStatus'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/bim/bimDemo/list',
        delete: '/bim/bimDemo/delete',
        deleteBatch: '/bim/bimDemo/deleteBatch',
        exportXlsUrl: '/bim/bimDemo/exportXls',
        importExcelUrl: 'bim/bimDemo/importExcel',
        getViewToken: '/bim/model/getViewToken'
      },
      dictOptions: {},
      superFieldList: []
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'modelCode', text: '模型编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'modelName', text: '模型名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'modelType', text: '模型类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'modelVersion', text: '模型版本', dictCode: '' })
      fieldList.push({ type: 'string', value: 'modelSize', text: '模型大小', dictCode: '' })
      fieldList.push({ type: 'string', value: 'modelStatus', text: '转换状态', dictCode: '' })
      this.superFieldList = fieldList
    },
    handlePreview(record) {
      this.loading = true
      getAction(this.url.getViewToken, { fileId: record.modelCode }).then((res) => {
        if (res.success) {
          this.$refs.modalView.init(res.result)
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>