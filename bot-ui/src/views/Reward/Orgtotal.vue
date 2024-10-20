<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
          <el-form :inline="true" :model="filters" :size="size">
            <el-form-item>
              <el-date-picker
                v-model="filters.fdate"
                type="datetimerange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-input v-model="filters.orgno" placeholder="用户ID"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="filters.username" placeholder="用户名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="filters.status" placeholder="状态" style="width: 100%;" clearable>
                <el-option v-for="item in statuss" :key="item.key" :label="item.name" :value="item.key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <kt-button icon="fa fa-search" :label="$t('action.search')" perms="reward:orgtotal:view" type="primary" @click="findPage(null)"/>
            </el-form-item>
            <el-form-item>
              <kt-button icon="fa fa-plus" label="计算" perms="reward:orgtotal:add" type="primary" @click="handleAdd" />
            </el-form-item>
            <el-form-item>
              <kt-button icon="fa fa-trash" label="删除" perms="reward:orgtotal:del" type="danger" @click="handleDel" />
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
        <table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns"
                                    @handleFilterColumns="handleFilterColumns">
        </table-column-filter-dialog>
      </el-col>
    </el-row>
	<!--表格内容栏-->
	<jl-table :data="pageResult" :columns="filterColumns" permsEdit="reward:orgtotal:edit" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleBatch="handleBatch" batchlabel="批量办理">
    <template #userno1="scope">
      <div style="text-align:left;">
          <p style="padding: 0;margin:0;height:18px;">推荐用户：{{ scope.row.userno1 }}</p>
          <p style="padding: 0;margin:0;height:18px;">推荐比率：{{ scope.row.crate1 }}%</p>
          <p style="padding: 0;margin:0;height:18px;">推荐奖励：{{ scope.row.commbonus1 }}</p>
        </div>
    </template>
    <template #userno2="scope">
      <div style="text-align:left;" v-if="scope.row.userno2">
          <p style="padding: 0;margin:0;height:18px;">推荐用户：{{ scope.row.userno2 }}</p>
          <p style="padding: 0;margin:0;height:18px;">推荐比率：{{ scope.row.crate2 }}%</p>
          <p style="padding: 0;margin:0;height:18px;">推荐奖励：{{ scope.row.commbonus2 }}</p>
        </div>
    </template>
    <template #userno3="scope">
      <div style="text-align:left;" v-if="scope.row.userno3">
          <p style="padding: 0;margin:0;height:18px;">推荐用户：{{ scope.row.userno3 }}</p>
          <p style="padding: 0;margin:0;height:18px;">推荐比率：{{ scope.row.crate3 }}%</p>
          <p style="padding: 0;margin:0;height:18px;">推荐奖励：{{ scope.row.commbonus3 }}</p>
        </div>
    </template>
	</jl-table>
	<!--新增编辑界面-->
	<el-dialog title="计算" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">

			<el-form-item label="计算开始时间" prop="btime" >
        <el-date-picker
          v-model="dataForm.btime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择开始时间">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="计算结束时间" prop="etime" >
        <el-date-picker
          v-model="dataForm.etime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择结束时间">
        </el-date-picker>
			</el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  <el-dialog title="删除" width="40%" :visible.sync="dialogDelVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">

			<el-form-item label="开始时间" prop="btime" >
        <el-date-picker
          v-model="dataForm.btime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择开始时间">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="结束时间" prop="etime" >
        <el-date-picker
          v-model="dataForm.etime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择结束时间">
        </el-date-picker>
			</el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogDelVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitFormDel" :loading="editLoading">{{$t('action.submit')}}</el-button>
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
        orgno: '',
        username:'',
        status: ''
      },
      columns: [],
      buttons: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      dialogDelVisible:false,
      editLoading: false,
      dataFormRules: {
        btime: [
          {required: true, message: '请输入计算开始时间', trigger: 'blur'}
        ],
        etime: [
          {required: true, message: '请输入计算结束时间', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        btime: '',
        etime: ''
      },
      statuss: [
        {'key':0,'name':'未办理'},
        {'key':1,'name':'已办理'}
      ],
       
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
        'btime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'etime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'status':this.filters.status,
        'username@like': this.filters.username
			}
			this.$api.orgtotal.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.orgtotal.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        fdate: getCurrentDate()
      }
		},
    handleDel:function () {
			this.dialogDelVisible = true
			this.operation = true
		},
    selectionChange:function(params){
		  console.log(params)
    },
    handleBatch:function(ids){
      let params = []
      for(var i=0; i<ids.length; i++) {
        params.push({'waterno':ids[i].waterno})
      }
      this.handleEdit(params)
    },
		// 显示编辑界面
		handleEdit: function (para) {
      this.$confirm('确认办理选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$api.orgtotal.handle(para).then(res=>{
          if(res.code == 200) {
            this.$message({ message: '办理成功', type: 'success' })
            this.findPage(null)
          } else {
            this.$message({message: '办理失败, ' + res.msg, type: 'error'})
          }
        });
      })
		},
    submitFormDel:function(){
      this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认删除该时间段吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({},this.dataForm)
            this.$api.orgtotal.delOrg(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '删除成功', type: 'success' })
                this.dialogDelVisible = false
                this.$refs['dataForm'].resetFields()
                this.findPage(null)
              } else {
                this.$message({message: '删除失败, ' + res.msg, type: 'error'})
              }
            })

					})
				}
			})
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({},this.dataForm)
            this.$api.orgtotal.count(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '计算成功', type: 'success' })
                this.dialogVisible = false
                this.$refs['dataForm'].resetFields()
                this.findPage(null)
              } else {
                this.$message({message: '计算失败, ' + res.msg, type: 'error'})
              }
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
				{prop:"btime", label:"开始时间", minWidth:150},
        {prop:"etime", label:"结束时间", minWidth:150},
        {prop:"orgno", label:"USERID", minWidth:100},
        {prop:"username", label:"用户名", minWidth:100},
        {prop:"nickname", label:"用户昵称", minWidth:100},
        {prop:"comms", label:"手续费合计", minWidth:100},
        {prop:"userno1", label:"一级推荐", minWidth:150},
        {prop:"userno2", label:"二级推荐", minWidth:150},
        {prop:"userno3", label:"三级推荐", minWidth:100},
				{prop:"status", label:"办理状态", minWidth:100,formatter:this.statusFormat}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-check',label:'办理',perms:'reward:orgtotal:edit',type:'warning',callback:'handleEdit',show:0}
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
