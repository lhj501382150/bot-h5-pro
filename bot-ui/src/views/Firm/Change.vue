<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.chtype" placeholder="变更信息类型" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in chtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.checkstatus" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in checkstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:change:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
		</el-form>
	</div>
	<div class="toolbar" style="float:right;padding-top:10px;padding-right:15px;">
		<el-form :inline="true" :size="size">
			<el-form-item>
				<el-button-group>
				<el-tooltip content="刷新" placement="top">
					<el-button icon="fa fa-refresh" @click="findPage(null)"></el-button>
				</el-tooltip>
				<el-tooltip content="列显示" placement="top">
					<el-button icon="fa fa-filter" @click="displayFilterColumnsDialog"></el-button>
				</el-tooltip>
				</el-button-group>
			</el-form-item>
		</el-form>
		<!--表格显示列界面-->
		<table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns"
			@handleFilterColumns="handleFilterColumns">
		</table-column-filter-dialog>
	</div>
	<!--表格内容栏-->
	<kt-table
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleAudit="handleAudit" @handleRefuse="handleRefuse">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'审核':'驳回'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno" :disabled="true" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
			<el-form-item label="变更信息类型" prop="chtype" >
        <el-select v-model="dataForm.chtype" :disabled="true" placeholder="变更信息类型" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in chtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item label="认证图片"  v-if="dataForm.chtype==1">
        <el-image v-for="(item,index) in piclist" :key="index"
                  style="width: 200px; height: 200px;margin: 10px 15px"
                  :src="item"
                  :preview-src-list="piclist">
        </el-image>
      </el-form-item>
      <el-form-item label="VIP等级" v-if="dataForm.chtype==2">
        <el-select v-model="content.viplev" :disabled="true" placeholder="VIP等级" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in vips" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="驳回原因" prop="checkmsg" v-if="!operation">
        <el-input v-model="dataForm.checkmsg" auto-complete="off" maxlength="64"></el-input>
      </el-form-item>


		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { fileUrl } from '@/utils/global'
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        chtype: '',
        checkstatus:''
      },
      columns: [],
      buttons: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
      },
      content:{},
      // 新增编辑界面数据
      dataForm: {
        wno: 0,
        userno: '',
        chtype: '',
        chcontent: '',
        checkstatus: '',
        checkmsg: '',
        chkuser: '',
        chktime: '',
        chkip: ''
      },
      chtypes: [
        {key:'1',val:'实名认证'},
        {key:'2',val:'VIP等级'}
      ],
      checkstatuss:[
        {key:'0',val:'未申请'},
        {key:'1',val:'未审核'},
        {key:'2',val:'已审核'},
        {key:'3',val:'已驳回'}
      ],
      vips:[
        {key:'0',val:'普通'},
        {key:'1',val:'青铜'},
        {key:'2',val:'白银'},
        {key:'3',val:'黄金'}
      ],
      reals:[
        {key:'0',val:'未申请'},
        {key:'1',val:'未认证'},
        {key:'2',val:'已认证'},
        {key:'3',val:'已驳回'}
      ],
      piclist:[]
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
			  'userno':this.filters.userno,
        'chtype':this.filters.chtype,
        'checkstatus':this.filters.checkstatus
			}
			this.$api.userchange.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    initPiclist(){
      this.piclist = this.getFiles()
    },
    getFiles(){
      let ret = [];
      if(this.content.REALFILE1){
        let path = fileUrl + '/downloadFile?path=' +this.content.REALFILE1
        ret.push(path)
      }
      if(this.content.REALFILE2){
        let path = fileUrl + '/downloadFile?path=' +this.content.REALFILE2
        ret.push(path)
      }
      if(this.content.REALFILE3){
        let path = fileUrl + '/downloadFile?path=' +this.content.REALFILE3
        ret.push(path)
      }
      if(this.content.REALFILE4){
        let path = fileUrl + '/downloadFile?path=' +this.content.REALFILE4
        ret.push(path)
      }
      if(this.content.REALFILE5){
        let path = fileUrl + '/downloadFile?path=' +this.content.REALFILE5
        ret.push(path)
      }
      return ret;
    },
    // 显示审核界面
    handleAudit: function (params) {
      this.dialogVisible = true
      this.operation = true
      this.dataForm = Object.assign({}, params.row)
      this.content = JSON.parse(this.dataForm.chcontent)
      this.initPiclist()
    },
    // 显示驳回界面
    handleRefuse: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.content = JSON.parse(this.dataForm.chcontent)
      this.initPiclist()
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.userchange.audit(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }else{
              this.$api.userchange.reject(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }

					})
				}
			})
		},

		// 是否有效格式化
    statusFormat: function (row, column, cellValue, index){
      let t = this.checkstatuss.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length >0) val = t[0].val
      return val
	},
    chtypeFormat: function (row, column, cellValue, index){
      let t = this.chtypes.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length >0) val = t[0].val
      return val
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
				{prop:"wno", label:"流水号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
				{prop:"chtype", label:"变更信息类型", minWidth:100,formatter:this.chtypeFormat},
				{prop:"checkstatus", label:"状态", minWidth:120,formatter:this.statusFormat},
				{prop:"checkmsg", label:"驳回原因", minWidth:100},
				{prop:"chkuser", label:"审核人", minWidth:100},
				{prop:"chktime", label:"审核时间", minWidth:100},
				{prop:"chkip", label:"审核IP", minWidth:120}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
      this.buttons=[
        {icon:'fa fa-check',label:'审核',perms:'firm:change:audit',type:'',callback:'handleAudit'},
        {icon:'fa fa-times',label:'驳回',perms:'firm:change:reject',type:'danger',callback:'handleRefuse'}
      ];
    }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
