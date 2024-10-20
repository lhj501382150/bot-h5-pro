<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.chatname" placeholder="群名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:chat:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:chat:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="sys:chat:edit" permsDelete="sys:chat:del" :buttons="buttons"
		:data="pageResult" :columns="filterColumns" @down="down" @pause="pause" @restart="restart"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="群ID" prop="chatid" maxlength="16">
				<el-input v-model="dataForm.chatid" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="群名称" prop="chatname"  maxlength="64">
				<el-input v-model="dataForm.chatname" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="庄主昵称" prop="nickname"  maxlength="64" v-if="!operation">
				<el-input v-model="dataForm.nickname" :disabled="true" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="抢庄金额" prop="intege"  maxlength="64" v-if="!operation">
				<el-input v-model="dataForm.intege"  auto-complete="off" maxlength="8"></el-input>
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
				chatid: '',
        chatname: ''
			},
			columns: [],
      buttons:[],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				chatid: [
					{ required: true, message: '请输入群ID', trigger: 'blur' }
				],
        chatname: [
          { required: true, message: '请输入群名称', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        chatid: '',
        chatname: ''
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
        'chatname@LIKE':this.filters.chatname
			}
			this.$api.chat.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    down(data){
        this.$confirm('下庄后，从下轮开始将会重新竞选庄家', '下庄', {
        type: 'warning'
        }).then(() => {
          this.loading = true
          let callback = res => {
            if(res.code == 200) {
              this.$message({message: '操作成功', type: 'success'})
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.loading = false
            this.findPage(null)
          }
          this.$api.chat.down(data.row).then(data!=null?callback:'')
        }).catch(() => {
        })
    },
    pause(data){
        this.$confirm('暂停后，机器人会停止发言，不再接收任何订单，且开奖号码不会公布', '消息暂停', {
        type: 'warning'
        }).then(() => {
          this.loading = true
          let callback = res => {
            if(res.code == 200) {
              this.$message({message: '操作成功', type: 'success'})
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.loading = false
            this.findPage(null)
          }
          this.$api.chat.pause(data.row).then(data!=null?callback:'')
        }).catch(() => {
        })
    },
    restart(data){
        this.$confirm('启动后，机器人重新开始发，立即启动', '消息启动', {
        type: 'warning'
        }).then(() => {
          this.loading = true
          let callback = res => {
            if(res.code == 200) {
              this.$message({message: '操作成功', type: 'success'})
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.loading = false
            this.findPage(null)
          }
          this.$api.chat.restart(data.row).then(data!=null?callback:'')
        }).catch(() => {
        })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.chat.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        chatid: '',
        chatname: ''
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
              this.$api.chat.save(params).then((res) => {
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
              this.$api.chat.edit(params).then((res) => {
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
  isvalidFormat: function (row, column, cellValue, index){
    if(cellValue==='Y') {
      return '正常'
    } else {
      return "暂停"
    }
	},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"chatid", label:"群编号", minWidth:200},
				{prop:"chatname", label:"群名称", minWidth:120},
        {prop:"nickname", label:"庄主昵称", minWidth:120},
        {prop:"intege", label:"抢庄金额", minWidth:120},
        {prop:"status", label:"状态", minWidth:120,formatter:this.isvalidFormat}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
		  this.buttons=[
        {icon:'fa fa-edit',label:'下庄',perms:'sys:chat:down',type:'warning',callback:'down'},
        {icon:'fa fa-edit',label:'启动',perms:'sys:chat:start',type:'primary',callback:'restart'},
        {icon:'fa fa-edit',label:'暂停',perms:'sys:chat:pause',type:'danger',callback:'pause'}
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
