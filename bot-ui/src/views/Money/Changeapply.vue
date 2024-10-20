<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.username" placeholder="客户用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.nickname" placeholder="客户昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.status" placeholder="审核状态" style="width: 100%;">
          <el-option v-for="item in statuss" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:changeapply:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="money:changeapply:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table permsDelete="money:changeapply:del" :buttons="buttons"
    :data="pageResult" :columns="columns" @findPage="findPage"
      @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components: {
		KtTable,
		KtButton,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
      acctlist: [],//账本列表
			filters: {
        userno: '',
        username:'',
        nickname:'',
        status:''
			},
			columns: [],
      buttons:[],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
      statuss:[
        {key:'0',val:'未审核'},
        {key:'1',val:'已审核'}
      ],
       exportData:[],
      exportInfo:{
        id:'record-table',
        name:'手工调账数据.xlsx'
      }
		}
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
      this.pageRequest.params = {
        'status':this.filters.status,
        'userno':this.filters.userno,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.changeapply.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params =  {
         'status':this.filters.status,
        'userno':this.filters.userno,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
      this.$api.changeapply.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    findacct(){
		  this.$api.account.findList({}).then(res =>{
		    this.acctlist = res.data
      })
    },
    initButtons(){
      this.buttons=[
        {icon:'fa fa-check',label:'审核',type:'warning',perms:'money:changeapply:edit',callback:'handleEdit'}
      ];
    },
    handleEdit: function (data){
      this.$confirm('确认提交吗？', '提示', {}).then(() => {
        let params = data.row
        this.$api.changeapply.edit(params).then((res) => {
          this.editLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
            this.dataForm = {}
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })

      })
    },
    handleDelete: function (data) {
      this.$api.changeapply.batchDelete(data.params).then(data!=null?data.callback:'')
    },
    statusFormat: function (row, column, cellValue, index){
      if(cellValue===0) {
        return "未审核"
      } else {
        return "已审核"
      }
    },
    initColumns: function () {
      this.columns = [
        {prop:"userno", label:"USERID", minWidth:120},
        {prop:"username", label:"用户名", minWidth:120},
        {prop:"nickname", label:"用户昵称", minWidth:120},
        // {prop:"acctno", label:"账户编号", minWidth:120},
        // {prop:"acctname", label:"账户名称", minWidth:120},
        {prop:"subno", label:"科目编号", minWidth:100},
        {prop:"subname", label:"科目名称", minWidth:100},
        {prop:"chmoney", label:"变动金额", minWidth:120},
        // {prop:"exten", label:"业务单号", minWidth:100},
        {prop:"memo", label:"备注", minWidth:100},
        {prop:"status", label:"状态", minWidth:100,formatter:this.statusFormat},
        {prop:"chkuser", label:"审核人", minWidth:100},
        {prop:"chktime", label:"审核时间", minWidth:150},
        {prop:"chkip", label:"审核地址", minWidth:100}
      ]
    }
	},
	mounted() {
	  this.findacct()
    this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
