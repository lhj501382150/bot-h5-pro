<template>
  <div>
    <editor id="editor_id" height="500px" width="100%" :content="content"
            :items="items"
            :afterChange="afterChange()"
            :loadStyleMode="false"
            :uploadJson="uploadUrl"
            filePostName="file"
            @on-content-change="onContentChange"></editor>
  </div>
</template>

<script>
import { fileUrl } from '@/utils/global'
export default {
  name: 'Kindeditoruse',
  props:{
    content:String
  },
  data () {
    return {
      uploadUrl:  fileUrl + '/uploadImg',
      text: '',
      items:[
        'source', '|', 'undo', 'redo', '|', 'preview',  'cut', 'copy', 'paste',
        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent',  'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen',
        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'table', 'emoticons',   '|'
      ]
    }
  },
  /*
'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
'anchor', 'link', 'unlink', '|', 'about'
*/
  created () {
  },
  methods: {
    onContentChange (val) {
      //this.content = val
      this.text = val
    },
    afterChange () {
    },
    getContentData(){
      return this.text
    }
  }
}
</script>

<style>

</style>

