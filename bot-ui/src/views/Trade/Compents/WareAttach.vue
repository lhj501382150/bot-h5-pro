<template>
<div>
  <el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
        <el-form-item label="视频" style="height: 200px;width: 100%">
          <upload-video :model="model" :imgPath="videoPath" ref="videoupload"></upload-video>
        </el-form-item>
        <el-form-item label="图片(首张为缩略图)">
          <upload-file-multy :model="model" :imgPath="imgPath" ref="picupload" :info="picinfo"></upload-file-multy>
        </el-form-item>

  </el-form>
</div>
</template>

<script>
import UploadFileMulty from "@/views/Trade/Compents/UploadFileMulty"
import UploadVideo from "@/views/Trade/Compents/UploadVideo"
export default {
name: "WareAttach",
components:{
    UploadFileMulty,
    UploadVideo
},
props:{
  model: String,
  imgPath: String,
  videoPath: String
},
data(){
  return {
    dataForm:{},
    dataFormRules:{

    },
    picinfo:'第一张为缩略图，其他为商品轮播图片，尺寸建议宽度为640*640，并保持图片大小一致'
  }
},
methods:{
  getPicpath(){
    let path = this.$refs.picupload.getPath()
    return path
  },
  getVideopath(){
    let path = this.$refs.videoupload.getPath()
    return path
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
