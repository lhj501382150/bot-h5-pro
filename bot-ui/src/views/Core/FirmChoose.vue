<template>
 <el-dialog title="客户列表" width="80%" :visible.sync="firmDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag >
    <div>
      <el-form :inline="true" :model="firm" :size="size">
        <el-form-item>
        <el-date-picker
          v-model="firm.fdate"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="注册开始日期"
          end-placeholder="注册结束日期"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
			</el-form-item>
        <el-form-item>
          <el-input v-model="firm.userno" placeholder="客户编号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.username" placeholder="客户名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.parentno" placeholder="所属代理"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.tjno" placeholder="推荐人编号"></el-input>
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:activeInfo:view" type="primary" @click="findFirm(null)"/>
        </el-form-item>
      </el-form>
    </div>
      <el-table :data="firmData.content" style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange" :loading="firmLoading" max-height="400px">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="userno" label="客户编号" width="180"></el-table-column>
        <el-table-column prop="username" label="客户名称" width="180"></el-table-column>
        <el-table-column prop="regdate" label="注册时间" width="180"></el-table-column>
        <el-table-column prop="parentno" label="所属代理" width="180"></el-table-column>
        <el-table-column prop="tjno" label="推荐人" width="180"></el-table-column>
      </el-table>
      <el-pagination layout="total, prev, pager, next, jumper" @current-change="refreshPageFirm"
        :current-page.sync="firmData.pageNum" :page-size.sync="firmData.pageSize" :total="firmData.totalSize" style="float:right;">
      </el-pagination>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="firmDialogVisible = false">确定</el-button>
		</div>
	</el-dialog>
</template>

<script>
export default {
name: "FirmChoose",
props: {

},
data(){
  return {
    firmData:{},
    selections:[],
    firmPage:{pageNum: 1, pageSize: 100},
    firmLoading: false,
    firmDialogVisible:false
  }
},
  methods:{
    refreshPageFirm(pageNum){
        this.firmPage.pageNum = pageNum
        this.findFirm()
    },
    handleSelectionChange(val) {
      this.dataForm.userno = ''
      this.selections = val
     this.dataForm.userno =  val.map(item => {
        return item.userno
      }).join(',')
    },
    findFirm(){
      this.firmLoading = true
      this.firmDialogVisible = true
      let para = this.firmPage
			let params = Object.assign({}, this.firm)
      params['username@like'] = this.firm.username
      params['regdate@ge'] = this.firm.fdate == null ? '' : this.firm.fdate[0],
      params['regdate@le'] = this.firm.fdate == null ? '' : this.firm.fdate[1],
      delete params.username
      delete params.fdate
      para.params = params
			this.$api.firm.findPage(para).then((res) => {
        this.firmLoading = false
				this.firmData = res.data
			})
    },
  }
}
</script>

<style scoped>

</style>
