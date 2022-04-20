<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @cancel="handleCancel"
    cancelText="关闭">
    <div id="domId" style="width:700px; height:400px"></div>
  </j-modal>
</template>

<script>


export default {
  name: 'BimDemoPreview',
  data() {
    return {
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false
    }
  },
  methods: {
    init(viewToken) {
      this.visible = true
      let loaderConfig = new BimfaceSDKLoaderConfig()
      loaderConfig.viewToken = viewToken
      BimfaceSDKLoader.load(loaderConfig, this.successCallback, this.failureCallback)
    },
    successCallback(viewMetaData) {
      // 创建WebApplication，直接显示模型或图纸
      let domShow = document.getElementById('domId')
      new Glodon.Bimface.Application.WebApplicationDemo(viewMetaData, domShow)

      // if (viewMetaData.viewType == "drawingView") {
      //   let view = document.getElementById('view');
      //   let WebAppConfig = new Glodon.Bimface.Application.WebApplicationDrawingConfig();
      //   WebAppConfig.domElement = view;
      //   this.app = new Glodon.Bimface.Application.WebApplicationDrawing(WebAppConfig);
      //   this.viewer = app.getViewer();
      //   this.viewer.addModel(viewMetaData);
      //
      // } else if (viewMetaData.viewType == "3DView") {
      //   let view = document.getElementById('view')
      //   let config = new Glodon.Bimface.Application.WebApplication3DConfig();
      //   config.domElement = view;
      //   let eventManager = Glodon.Bimface.Application.WebApplication3DEvent;
      //   this.app = new Glodon.Bimface.Application.WebApplication3D(config);
      //   this.viewer = this.app.getViewer();
      //   this.viewer.addModel(viewMetaData);
      // } else if (viewMetaData.viewType == "rfaView") {
      //   let view = document.getElementById('view')
      //   let config = new Glodon.Bimface.Application.WebApplicationRfaConfig();
      //   config.domElement = view;
      //   let eventManager = Glodon.Bimface.Application.WebApplicationRfaEvent;
      //   this.app = new Glodon.Bimface.Application.WebApplicationRfa(config);
      //   this.viewer = this.app.getViewer();
      //   this.viewer.addModel(viewMetaData);
      // }
    },
    failureCallback(error) {
      console.log(error)
    },
    handleCancel() {
      this.visible = false
    }

  }
}
</script>