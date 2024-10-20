<template>
  <el-upload
    class="avatar-uploader"
    :action="uploadUrl"
    :data="data"
    :show-file-list="false"
    :on-preview="handlePictureCardPreview"
    :on-success="handleAvatarSuccess"
    :before-upload="beforeAvatarUpload">
    <video v-if="imgUrl" :src="imgUrl" class="avatar" controls></video>
    <i class="el-icon-plus avatar-uploader-icon"></i>
  </el-upload>
</template>

<script>
import { fileUrl } from '@/utils/global'
export default {
  name: 'UploadVideo',
  props:{
    model:String,
    imgPath:String
  },
  data() {
    return {
      uploadUrl: fileUrl + '/uploadFile',
      dialogVisible: false,
      disabled: false,
      imgUrl:'',
      vediopath:'',
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
      this.data.type = this.model
      this.data.path = this.vediopath
    },
    handleAvatarSuccess(res, file) {
      if(res.error===0){
        this.imgUrl = fileUrl + '/downloadFile?path=' +res.url
        this.vediopath = res.url
      }else{
        this.$message.error("上传文件失败:"+ res.message)
      }
    },
    getPath(){
      return this.vediopath
    }
  },
  mounted() {
    if(this.imgPath){
      this.imgUrl =  fileUrl + '/downloadFile?path=' +this.imgPath
      this.vediopath = this.imgPath
    }
  }
}
</script>

<style scoped>
.avatar-uploader{
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 50%;
  height: 200px;
  margin: 0 auto;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 200px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 100%;
  height: 200px;
  display: block;
}
</style>
