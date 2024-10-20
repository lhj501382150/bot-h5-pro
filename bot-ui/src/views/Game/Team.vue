
<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-select v-model="filters.actno" placeholder="赛事活动" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
            </el-select>
          </el-form-item>
           <el-form-item>
            <el-input v-model="filters.userno" placeholder="客户编号" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:team:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:team:add" type="primary" @click="handleAdd" />
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
	<kt-table permsDelete="gm:team:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage"  @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
      <el-form-item label="活动编号" prop="actno" :disabled="!operation">
				<el-select v-model="dataForm.actno" placeholder="请选择活动" style="width: 98%;" clearable :disabled="!operation">
              <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
            </el-select>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
        <el-input v-model="dataForm.userno" placeholder="请输入客户编号" maxlength="16" :disabled="!operation"></el-input>
      </el-form-item>
      <el-form-item label="赛马编号" prop="memno">
        <el-input v-model="dataForm.memno" placeholder="请输入赛马编号" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="门票" prop="bmmoney">
        <el-input v-model="dataForm.bmmoney" placeholder="请输入门票" maxlength="16"></el-input>
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
import { format } from "@/utils/datetime"
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
        actno: '',
        userno:''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        actno: [
          {required: true, message: '请选择活动编号', trigger: 'blur'}
        ],
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        memno: [
          {required: true, message: '请输入赛马编号', trigger: 'blur'}
        ],
        bmmoney: [
          {required: true, message: '请输入门票', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actno:'',
        userno: '',
        memno: '',
        bmmoney:''
      },
      acts: []
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
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.team.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    initDict:function(){
      this.$api.activity.findList().then(res=>{
	      this.acts = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.team.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        actno:'',
        userno: '',
        memno: '',
        bmmoney:''
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
              this.$api.team.save(params).then((res) => {
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
              this.$api.team.edit(params).then((res) => {
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
        {prop:"actno", label:"活动编号", minWidth:120},
        {prop:"actname", label:"活动名称", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
        {prop:"username", label:"客户名称", minWidth:120},
				{prop:"memno", label:"赛马编号", minWidth:100},
				{prop:"bmmoney", label:"门票", minWidth:100},
        {prop:"money", label:"奖励积分", minWidth:100},
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
    this.initDict()
	}
}
</script>

<style scoped>

</style>
