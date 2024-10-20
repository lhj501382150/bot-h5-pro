<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
        <el-date-picker
          v-model="filters.fdate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="用户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.status" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="item in statuss" :key="item.key" :label="item.name" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="reward:sharetotal:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" label="计算" perms="reward:sharetotal:add" type="primary" @click="handleAdd" />
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
	<jl-table :data="pageResult" :columns="filterColumns" permsEdit="reward:sharetotal:edit" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleBatch="handleBatch" batchlabel="批量办理">
	</jl-table>
	<!--新增编辑界面-->
	<el-dialog title="计算" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="100px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">

			<el-form-item label="计算日期" prop="fdate" >
        <el-date-picker
          v-model="dataForm.fdate"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 100%;"
          placeholder="选择日期">
        </el-date-picker>
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
import JlTable from "@/views/Reward/components/JlTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { getCurrentDate } from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
    JlTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        fdate: '',
        userno: '',
        status: ''
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
        fdate: [
          {required: true, message: '请输入计算日期', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        fdate: ''
      },
      statuss: [
        {'key':0,'name':'未办理'},
        {'key':1,'name':'已办理'}
      ],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
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
        'pdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'pdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'status':this.filters.status,
        'userno':this.filters.userno
			}
			this.$api.sharetotal.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.sharetotal.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        fdate: getCurrentDate()
      }
		},
    selectionChange:function(params){
		  console.log(params)
    },
    handleBatch:function(ids){
      let params = []
      for(var i=0; i<ids.length; i++) {
        params.push({'pdate':ids[i].pdate,'userno':ids[i].userno})
      }
      this.handleEdit(params)
    },
		// 显示编辑界面
		handleEdit: function (para) {
      this.$confirm('确认办理选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$api.sharetotal.handle(para).then(res=>{
          if(res.code == 200) {
            this.$message({ message: '办理成功', type: 'success' })
          } else {
            this.$message({message: '办理失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        });
      })
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = this.dataForm.fdate
            this.$api.sharetotal.count(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '计算成功', type: 'success' })
                this.dialogVisible = false
                this.$refs['dataForm'].resetFields()
              } else {
                this.$message({message: '计算失败, ' + res.msg, type: 'error'})
              }
              this.findPage(null)
            })

					})
				}
			})
		},

		// 是否有效格式化
  statusFormat: function (row, column, cellValue, index){
		  let para = ['未办理','已办理'];
    return para[cellValue]
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
				{prop:"pdate", label:"日期", minWidth:120},
        {prop:"userno", label:"用户编号", minWidth:120},
        {prop:"username", label:"用户名称", minWidth:120},
        {prop:"pnum", label:"分享人数", minWidth:120},
				{prop:"rate", label:"奖励比率(%)", minWidth:100},
				{prop:"consumn", label:"消费合计", minWidth:100},
				{prop:"bonus", label:"奖励金", minWidth:100},
				{prop:"status", label:"办理状态", minWidth:100,formatter:this.statusFormat}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-delete',label:'办理',perms:'reward:sharetotal:edit',type:'warning',callback:'handleEdit',show:0}
      ];
    },
	},
	mounted() {
		this.initColumns(),
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
