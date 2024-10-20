<template>
  <div class="page-container">
    <el-row>
      <el-col :span="8">
        <el-table :data="records"  :border="true" :stripe="true" :highlight-current-row="true" @row-dblclick="rowChange">
          <el-table-column prop="name" label="类型">
            <template #default="scope">
               <span v-if="scope.row.clevel==1">分公司</span>
               <span v-else-if="scope.row.clevel==2">股东</span>
               <span v-else-if="scope.row.clevel==3">总代理</span>
               <span v-else-if="scope.row.clevel==4">代理</span>
               <span v-else-if="scope.row.clevel==-1">全部会员</span>
            </template>
          </el-table-column>
          <el-table-column prop="num" label="数量"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="16">
          <!--工具栏-->
          <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
            <el-form :inline="true" :model="filters" :size="size">
              <el-form-item>
                <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
              </el-form-item>
              <el-form-item>
                <el-input v-model="filters.parentno" placeholder="所属上级"></el-input>
              </el-form-item>
              <el-form-item>
                <kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:member:view" type="primary" @click="findPage(null)"/>
              </el-form-item>
            </el-form>
          </div>

          <!--表格内容栏-->
          <kt-table :data="pageResult" :columns="filterColumns" :showOperation="false"
            @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
            <template #sex="scope">
              <el-tag type="success" v-if="scope.row.sex==0">正常</el-tag>
              <el-tag type="danger" v-else>冻结</el-tag>
          </template>
          </kt-table>
      </el-col>
    </el-row>
	

  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format } from "@/utils/datetime"
export default {
	components:{
		KtTable,
		KtButton
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        sex:'',
        openid: '',
        balance:'',
        orgtype:'',
        clevel:'',
        parentno:''

      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},
      dataForm:{},
      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      records:[]
    }
	},
	methods: {
    rowChange(row,column,event){
      if(row.clevel > 0){
        this.filters.orgtype = 1
      }else{
        this.filters.orgtype = 2
      }
      this.filters.clevel = row.clevel
      this.findPage(null)
    },
    findRecord(){
      this.$api.firm.findOrgNum(this.pageRequest).then((res) => {
				this.records = res.data
			})
    },
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
			let params = Object.assign({}, this.filters)
      delete params.fdate
      this.pageRequest.params = params
			this.$api.firm.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    handleDelete: function (data) {
      this.$confirm('确认删除该记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        let callback = res => {
          if(res.code == 200) {
            this.$message({message: '注销成功', type: 'success'})
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.loading = false
          this.findPage(null)
        }
        this.$api.firm.batchDelete(data.row).then(data!=null?callback:'')
      }).catch(() => {
      })

		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      //this.setSelectArea()
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
		}, 

		// 处理表格列过滤显示
	displayFilterColumnsDialog: function () {
			this.$refs.tableColumnFilterDialog.setDialogVisible(true)
	},
		// 处理表格列过滤显示
	handleFilterColumns: function (data) {
			this.filterColumns = data.filterColumns
			this.$refs.tableColumnFilterDialog.setDialogVisible(false)
	},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"userno", label:"编号", minWidth:120},
				{prop:"username", label:"名称", minWidth:120},
				{prop:"nickname", label:"昵称", minWidth:100},
				{prop:"sex", label:"状态", minWidth:120},
        {prop:"parentno", label:"所属上级", minWidth:120},
        {prop:"balance", label:"当前余额", width:100},
        {prop:"enable", label:"可用余额", width:100},
        {prop:"freeze", label:"冻结余额", width:100},
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
    this.findRecord()
	}
}
</script>

<style scoped>

</style>
