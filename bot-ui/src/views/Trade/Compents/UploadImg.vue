<template>
<div>
  <el-upload
    class="avatar-uploader"
    :action="uploadUrl"
    :data="data"
    :show-file-list="false"
    :on-preview="handlePictureCardPreview"
    :on-remove="handleRemove"
    :on-success="handleAvatarSuccess"
    :before-upload="beforeAvatarUpload">
    <img v-if="imgUrl" :src="imgUrl" class="avatar">
    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
  </el-upload>
</div>
</template>

<script>
import { fileUrl } from '@/utils/global'
export default {
  name: 'UploadImg',
  props:{
    model:String,
    imgPath:String
  },
  data() {
    return {
      uploadUrl: fileUrl + '/uploadFileCheck',
      dialogVisible: false,
      disabled: false,
      imgUrl:'',
      code:'',
      data:{}
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
      const isJPG = 'image/jpeg,image/png,image/bmp'.indexOf(type) > -1;
      const isLt2M = file.size / 1024 / 1024 < 10;

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 10MB!');
      }
      this.data.type = this.model
      this.data.path = this.imgPath
      return isJPG && isLt2M;
    },
    handleAvatarSuccess(res, file) {
      if(res.error===0){
        this.imgUrl = fileUrl + '/downloadFile?path=' +res.url
        this.$emit("fillpath",res)
      }else{
        this.$message.error("上传文件失败:"+ res.message)
      }
    },
  },
  mounted() {
    console.log("this.imgPath",this.imgPath)
    if(this.imgPath){
      this.imgUrl =  fileUrl + '/downloadFile?path=' +this.imgPath
    }else{
      this.imgUrl = ''
    }
  }
}
</script>

<style scoped>
.avatar-uploader{
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
