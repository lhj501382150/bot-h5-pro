<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.userno" placeholder="客户编号" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
        <el-select v-model="filters.clevel" placeholder="级别" clearable>
          <el-option v-for="(item,index) in clevels" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
        <el-select v-model="filters.orgtype" placeholder="类型" clearable>
          <el-option v-for="(item,index) in orgtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
			</el-form-item>
          
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:userLimit:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:userLimit:add" type="primary" @click="handleAdd" />
          </el-form-item>

        </el-form>
      </el-col>
      <el-col :span="4">
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
        <table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns" @handleFilterColumns="handleFilterColumns">
        </table-column-filter-dialog>
      </el-col>
    </el-row>
	<!--表格内容栏-->
	<kt-table permsEdit="sys:userLimit:edit" permsDelete="sys:userLimit:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
    <template #clevel="scope">
        <el-tag type="danger" v-if="scope.row.orgtype==1">
          {{ getClevel(scope.row.clevel) }}
        </el-tag>
        <el-tag v-if="scope.row.orgtype==2">普通会员</el-tag>
    </template>
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="140px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
      <el-form-item label="用户编号" prop="userno" >
        <el-input v-model="dataForm.userno" placeholder="请输入用户编号" maxlength="16"></el-input>
      </el-form-item>
			<el-form-item label="宝斗最小限额" prop="minDou" >
				<el-input type="number" v-model="dataForm.minDou" placeholder="请输入宝斗最小限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="宝斗最大限额" prop="maxDou" >
				<el-input type="number" v-model="dataForm.maxDou" placeholder="请输入宝斗最大限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="牛牛平投最小限额" prop="minNiuP" >
				<el-input type="number" v-model="dataForm.minNiuP" placeholder="请输入牛牛平投最小限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="牛牛平投最大限额" prop="maxNiuP" >
				<el-input type="number" v-model="dataForm.maxNiuP" placeholder="请输入牛牛平投最大限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="牛牛倍投最小限额" prop="minNiuB" >
				<el-input type="number" v-model="dataForm.minNiuB" placeholder="请输入牛牛倍投最小限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="牛牛倍投最大限额" prop="maxNiuB" >
				<el-input type="number" v-model="dataForm.maxNiuB" placeholder="请输入牛牛倍投最大限额"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item >
        <span style="color:red;">*0 代表不限制</span>
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
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { format,subTime } from "@/utils/datetime"
export default {
	components:{
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        orgtype:'',
        clevel:''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      addDialogVisible:false,
      resultDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        userno: [
          {required: true, message: '请输入用户编号', trigger: 'blur'}
        ], 
      },
      // 新增编辑界面数据
      dataForm: {
        userno: '',
        minDou: 0,
        maxDou: 0,
        minNiuP: 0,
        maxNiuP: 0,
        minNiuB: 0,
        maxNiuB: 0,
      },
      clevels:[
        {key:1,val:'分公司'},
        {key:2,val:'股东'},
        {key:3,val:'总代理'},
        {key:4,val:'代理'}
      ],
      orgtypes:[
        {key:1,val:'非会员'},
        {key:2,val:'普通会员'}
      ],
    }
	},
	methods: {
    getClevel(level){
      return this.clevels.find(item=>item.key==level).val
    },
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
      if(this.filters.clevel > 0){
        this.filters.orgtype = 1
      } 
			this.pageRequest.params = Object.assign({},this.filters)
      
			this.$api.userlimit.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		}, 
		handleDelete: function (data) {
			this.$api.userlimit.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true 
			this.dataForm= {
        userno: '',
        minDou: 0,
        maxDou: 0,
        minNiuP: 0,
        maxNiuP: 0,
        minNiuB: 0,
        maxNiuB: 0,
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row) 
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.userlimit.save(params).then((res) => {
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
              this.$api.userlimit.edit(params).then((res) => {
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
        {prop:"userno", label:"用户编号", minWidth:120},
        // {prop:"username", label:"用户名称", minWidth:120},
        {prop:"clevel", label:"类型", width:120},
        {prop:"minDou", label:"宝斗最小限额", minWidth:120},
        {prop:"maxDou", label:"宝斗最大限额", minWidth:120},
        {prop:"minNiuP", label:"牛牛平投最小限额", minWidth:120},
        {prop:"maxNiuP", label:"牛牛平投最大限额", minWidth:120},
        {prop:"minNiuB", label:"牛牛倍投最小限额", minWidth:120},
        {prop:"maxNiuB", label:"牛牛倍投最大限额", minWidth:120},
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      
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
