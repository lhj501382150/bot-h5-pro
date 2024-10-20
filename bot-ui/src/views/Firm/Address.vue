<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.recvname" placeholder="收货人"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.telno" placeholder="手机号"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:address:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="firm:address:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="firm:address:edit" permsDelete="firm:address:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno" :disabled="!operation" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
			<el-form-item label="收货人" prop="recvname" >
				<el-input v-model="dataForm.recvname" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="手机号" prop="telno">
				<el-input v-model="dataForm.telno" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="选择地区" prop="areaid">
        <el-cascader v-model="dataForm.areaid" :options="provinces" :props="props" style="width: 100%" ref="cascaderAddr" @change="handleAreaLabel"></el-cascader>
			</el-form-item>
      <el-form-item label="详细地址" prop="street" maxlength="64">
        <el-input v-model="dataForm.street" auto-complete="off"></el-input>
      </el-form-item>

      <el-form-item label="是否设置" prop="isdefault">
        <el-radio-group v-model="dataForm.isdefault">
          <el-radio label="Y">是</el-radio>
          <el-radio label="N">否</el-radio>
        </el-radio-group>
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
import { format } from "@/utils/datetime"
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
        recvname: '',
        telno: ''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        recvname: [
          {required: true, message: '请输入收货人', trigger: 'blur'}
        ],
        telno: [
          {required: true, message: '请输入手机号', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        recvno: 0,
        userno: '',
        recvname: '',
        telno: '',
        areaid: '',
        province: '',
        city: '',
        county: '',
        street: '',
        isdefault: 'N'
      },
      provinces: [],
      props: {
        value: 'code',
        label: 'name'
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
			  'userno':this.filters.userno,
        'recvname@LIKE':this.filters.recvname,
        'telno@LIKE':this.filters.telno
			}
			this.$api.address.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
        this.handleSelectArea()
			}).then(data!=null?data.callback:'')
		},
    //查询省市县
    handleSelectArea(){
		  let para = {parentid:0}
      this.$api.area.findList(para).then(res =>{
          this.provinces = res.data
      })
    },
    setSelectArea(){
		  if(!this.$refs.cascaderAddr) return
      this.$refs.cascaderAddr.panel.checkedValue = [];//也可以是指定的值，默认返回值是数组，也可以返回单个值
      this.$refs.cascaderAddr.panel.activePath = [];
      this.$refs.cascaderAddr.panel.syncActivePath();
    },
    handleAreaLabel(row){
      const nodes  = this.$refs['cascaderAddr'].getCheckedNodes()
      const area = nodes[0].pathLabels;
      if(area.length === 3){
        this.dataForm.province = area[0]
        this.dataForm.city = area[1]
        this.dataForm.county = area[2]
        this.dataForm.areaid = row[2]
      }else if(area.length === 2){
        this.dataForm.province = area[0]
        this.dataForm.city = area[1]
        this.dataForm.areaid = row[1]
      }
    },
    initAreaid(){
      let areaid = this.dataForm.areaid
      if(areaid > 0){
        this.dataForm.areaid = [
          parseInt(areaid/10000),parseInt(areaid/100),areaid
        ]
      }
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.address.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
		  this.setSelectArea()
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        recvno: 0,
        userno: '',
        recvname: '',
        telno: '',
        areaid: '',
        province: '',
        city: '',
        county: '',
        street: '',
        isdefault: 'N'
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      //this.setSelectArea()
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
      this.initAreaid()
		},
		// 编辑
		submitForm: function () {
		  console.log(this.dataForm)
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.address.save(params).then((res) => {
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
              this.$api.address.edit(params).then((res) => {
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
  isdefaultFormat: function (row, column, cellValue, index){
    if(cellValue==='Y') {
      return "是"
    } else {
      return "否"
    }
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
				{prop:"recvno", label:"收货编号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"recvname", label:"收货人", minWidth:120},
				{prop:"telno", label:"手机号", minWidth:100},
				{prop:"province", label:"省", minWidth:120},
				{prop:"city", label:"市", minWidth:100},
				{prop:"county", label:"县", minWidth:100},
				{prop:"street", label:"详细地址", minWidth:100},
				{prop:"isdefault", label:"是否默认", minWidth:70,formatter:this.isdefaultFormat}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
	}
}
</script>

<style scoped>

</style>
