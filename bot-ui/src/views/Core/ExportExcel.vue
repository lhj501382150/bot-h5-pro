<template>
  <div v-show='false'>
    <el-table :data="tableData" :id="info.id">
      <el-table-column v-for="column in columns"
          :type="column.type" :prop="column.prop" :label="column.label"  :key="column.prop"  :formatter="column.formatter">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'
export default {
name: "ExportExcel",
props: {
  info: {
    type: Object,
    default: {}
  },
  columns: {
    type: Array,
    default: []
  },
  tableData: {
    type: Array,
    default: []
  }
},
  methods:{
    //excel导出
    exportExcel () {
      var xlsxParam = { raw: true } // 导出的内容只做解析，不进行格式转换
      var wb = XLSX.utils.table_to_book(document.querySelector('#'+this.info.id),xlsxParam)
      var wbout = XLSX.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        FileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), this.info.name)
      } catch (e) {
        if (typeof console !== 'undefined') console.log(e, wbout)
      }
      return wbout
    },
  }
}
</script>

<style scoped>

</style>
