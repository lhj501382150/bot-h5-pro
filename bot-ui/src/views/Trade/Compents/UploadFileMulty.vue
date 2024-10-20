<template>
  <div>
    <el-upload
      :action="uploadUrl"
      list-type="picture-card"
      :file-list="fileList"
      :data="data"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <i class="el-icon-plus"></i>
      <div slot="tip" class="el-upload__tip">{{info}}</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body v-dialog-drag width="500px" >
      <img width="100%" :src="imgUrl" alt="">
    </el-dialog>
  </div>

</template>
<script>
import { fileUrl } from '@/utils/global'
export default {
  name: 'UploadFileMutly',
  props:{
    model:String,
    imgPath:String,
    info:String
  },
  data() {
    return {
      uploadUrl: fileUrl + '/uploadFile',
      dialogVisible: false,
      disabled: false,
      imgUrl:'',
      files:[],
      fileList:[],
      data:{}
    };
  },
  methods: {
    initFileUrl(fileList){
      this.files = fileList
    },
    handleRemove(file, fileList) {
      this.initFileUrl(fileList)
    },
    handlePictureCardPreview(file) {
      this.dialogVisible = true;
      this.imgUrl = file.url
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
      this.data.path = file.url
      return isJPG && isLt2M;
    },
    handleAvatarSuccess(res, file,fileList) {
      if(res.error===0){
        file.url = fileUrl + '/downloadFile?path=' +res.url
      }else{
        this.$message.error("上传文件失败:"+ res.message)
      }
      this.initFileUrl(fileList)
    },
    getPath(){
      let path = '';

      for(var i=0;i<this.files.length;i++){
        const file = this.files[i];
        if(file.response.url){
          path += file.response.url + ";"
        }
      }
      if(path){
        path = path.substring(0,path.length-1)
      }
      return path
    },
    initFileList(){
      if(this.imgPath){
        let paths = this.imgPath.split(";")
        for(var i=0;i<paths.length;i++){

          let path = paths[i];
          if(!path) continue;
          let response = {
            error: 0,
            url: path
          }
          let file = {
            response: response,
            url: fileUrl + '/downloadFile?path=' +path
          }
          this.fileList.push(file)
        }
      this.initFileUrl(this.fileList)
      }
    }
  },
  mounted() {
    this.initFileList()
  }
}
</script>
