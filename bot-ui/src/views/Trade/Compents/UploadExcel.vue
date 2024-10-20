<template>
<div>
  <el-upload
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="onSuccess"
        :on-error="onError"
        :disabled="importDisabled"
        accept=".xlsx"
        style="display: inline-flex;margin-right: 10px;"
        :action="uploadUrl">
    <el-button size="mini" :disabled="importDisabled" type="primary" :icon="importDataIcon">
        {{importDataText}}
    </el-button>
</el-upload>
</div>
</template>

<script>
import { baseUrl } from '@/utils/global'
export default {
  name: 'UploadExcel',
  props:{
    url:''
  },
  data() {
    return {
      uploadUrl: baseUrl + this.url,
      // 导入按钮的文本
      importDataText: '导入数据',
      // 导入按钮的图标
      importDataIcon: 'el-icon-upload2',
      // 导入按钮是否被禁用
      importDisabled: false
    };
  },
  methods: {
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogVisible = true;
    },
    beforeAvatarUpload(file){
      const type = file.type
      const isExcel = 'xls,xlsx'.indexOf(type) > -1;

      if (!isExcel) {
        this.$message.error('上传文件只能是 Excel 格式!');
      }
      return isExcel;
    },
    // 导入文件失败后回调
      onError() {
          this.importDataText = '导入数据';
          this.importDataIcon = 'el-icon-upload2';
          this.importDisabled = false;
          this.initEmps();
          this.$message.success("导入失败！");
      },
      // 导入文件成功后回调
      onSuccess(res) {
         // 成功后文本修改为原来的导入数据
          this.importDataText = '导入数据';
         // 图标修改
          this.importDataIcon = 'el-icon-upload2';
        // 将上传组件改为允许使用
          this.importDisabled = false;
        if(res.code == 200){
          // 调用刷新数据的方法
          this.$emit('findPage',null)
          // message 弹出消息
          this.$message.success("导入成功！");
        }else{
          this.$message.error("导入失败："+ res.msg);
        }

      },
      // 上传文件调用
      async beforeUpload(file) {
         let flag = await this.$confirm("确认要导入吗？","提示",{})
          if(flag){
              // 将文本修改为正在导入
            this.importDataText = '正在导入';
            // 修改其图标
            this.importDataIcon = 'el-icon-loading';
            // 将其上传组件暂时禁用
            this.importDisabled = true;
          }
          return flag;
      }
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>
