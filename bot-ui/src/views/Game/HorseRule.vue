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
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:horseRule:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:horseRule:add" type="primary" @click="handleAdd" />
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" label="类似添加" perms="gm:horseRule:addlike" type="primary" @click="handleAddLike" />
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
	<kt-table permsEdit="gm:horseRule:edit" permsDelete="gm:horseRule:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
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
      <el-form-item label="规则" prop="ruleno" >
        <el-cascader
          v-model="dataForm.ruleno"
          :options="dicts"
          ref="cascaderRule"
          :props="{ expandTrigger: 'hover' }"
          style="width:98%"
          :disabled="!operation"
          @change="handleChange"></el-cascader>
			</el-form-item>
      <el-form-item label="描述" prop="mem" >
				<el-input v-model="dataForm.mem" placeholder="请输入描述"  maxlength="32"></el-input>
			</el-form-item>
      <el-form-item label="速度加成" prop="speed" >
				<el-input v-model="dataForm.speed" placeholder="请输入速度加成"  maxlength="8">
          <template slot="append">%</template>
        </el-input>
			</el-form-item>
      <el-form-item label="随机最小加成" prop="randmin" >
				<el-input v-model="dataForm.randmin" placeholder="请输入随机最小加成"  maxlength="8">
          <template slot="append">%</template>
        </el-input>
			</el-form-item>
      <el-form-item label="随机最大加成" prop="randmax" >
				<el-input v-model="dataForm.randmax" placeholder="请输入随机最大加成"  maxlength="8">
          <template slot="append">%</template>
        </el-input>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

  <el-dialog title="类似新增" width="40%" :visible="true" v-if="addDialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="addForm" label-width="120px" :rules="addFormRules" ref="addForm" :size="size" style="height: 280px;overflow: auto;"
			label-position="right">
      <el-form-item label="原活动编号" prop="actno" >
				<el-select v-model="addForm.actno" placeholder="请选择活动" style="width: 98%;">
              <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
            </el-select>
			</el-form-item>
      <el-form-item label="新增活动编号" prop="newno">
				<el-select v-model="addForm.newno" placeholder="请选择活动" style="width: 98%;" >
              <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
            </el-select>
			</el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="addDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitAddForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
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
        actno: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      addDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        actno: [
          {required: true, message: '请选择活动编号', trigger: 'blur'}
        ],
        ruleno: [
          {required: true, message: '请设置规则', trigger: 'blur'}
        ],
        mem: [
          {required: true, message: '请输入描述', trigger: 'blur'}
        ],
        speed: [
          {required: true, message: '请输入速度加成', trigger: 'blur'}
        ],
        randmin: [
          {required: true, message: '请输入随机最小加成', trigger: 'blur'}
        ],
        randmax: [
          {required: true, message: '请输入随机最大加成', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actno: '',
        ruleno: '',
        mem: '',
        speed: '',
        randmin: '',
        randmax: ''
      },
      addFormRules: {
        actno: [
          {required: true, message: '请选择活动编号', trigger: 'blur'}
        ],
        newno: [
          {required: true, message: '请选择活动编号', trigger: 'blur'}
        ],
      },
      // 新增编辑界面数据
      addForm: {
        actno: '',
        newno: ''
      },
      acts: [],
      dicts:[]
    }
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
      this.tabList = []
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.horseRule.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    initDict:function(){
      this.$api.activity.findList().then(res=>{
	      this.acts = res.data
      })
      this.$api.horseRule.dictTree({}).then(res => {
        this.dicts = res.data
      })
    },
    handleChange(value){
      if(value && value.length == 2){
        var nodes = this.$refs['cascaderRule'].getCheckedNodes()
        this.dataForm.mem = nodes[0].label
      }else{
        this.dataForm.ruleno = ''
        this.dataForm.mem = ''
      }

    },
    setSelectRule(){
		  if(!this.$refs.cascaderRule) return
      this.$refs.cascaderRule.panel.checkedValue = [];//也可以是指定的值，默认返回值是数组，也可以返回单个值
      this.$refs.cascaderRule.panel.activePath = [];
      this.$refs.cascaderRule.panel.syncActivePath();
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.horseRule.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        actno: '',
        ruleno: '',
        mem: '',
        speed: '',
        randmin: '',
        randmax: ''
      }
      this.setSelectRule();
		},
    handleAddLike:function(){
      this.addDialogVisible = true
    },

		// 显示编辑界面
		handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dataForm.ruleno = this.dataForm.ruleno.split('_')
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            this.dataForm.ruleno = this.dataForm.ruleno.join('_').trim()
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.horseRule.save(params).then((res) => {
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
              this.$api.horseRule.edit(params).then((res) => {
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

    submitAddForm: function () {
			this.$refs.addForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.addForm)
            this.$api.horseRule.saveLike(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.addDialogVisible = false
                  this.$refs['addForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })

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
        {prop:"ruleno", label:"规则编号", minWidth:120},
        {prop:"mem", label:"描述", minWidth:180},
				{prop:"speed", label:"速度加成(%)", minWidth:100},
				{prop:"randmin", label:"随机最小加成(%)", minWidth:100},
				{prop:"randmax", label:"随机最大加成(%)", minWidth:100}
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
