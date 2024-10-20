
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
            <el-select v-model="filters.c1cno" placeholder="游戏类型" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in c1cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.tertype" placeholder="端游类型" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in tertypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:userinfo:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:userinfo:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="gm:userinfo:edit" permsDelete="gm:userinfo:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
			<el-form-item label="编号" prop="ugno" v-show="!operation">
				<el-input v-model="dataForm.ugno" :disabled="!operation"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
        <el-input v-model="dataForm.userno" placeholder="请输入客户编号" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="游戏类型" prop="c1cno" >
        <el-select v-model="dataForm.c1cno" placeholder="游戏类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in c1cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="端游类型" prop="tertype" >
        <el-select v-model="dataForm.tertype" placeholder="端游类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in tertypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="游戏账号" prop="gameno" maxlength="32">
        <el-input v-model="dataForm.gameno" placeholder="请输入游戏账号" maxlength="32"></el-input>
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
        userno: '',
        c1cno: '',
        tertype:''
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
          {required: true, message: '请输入主持人编号', trigger: 'blur'}
        ],
        c1cno: [
          {required: true, message: '请选择游戏分类', trigger: 'blur'}
        ],
        tertype: [
          {required: true, message: '请选择端游分类', trigger: 'blur'}
        ],
        gameno: [
          {required: true, message: '请输入游戏账号', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        ugno: 0,
        userno: '',
        c1cno: '',
        gameno: '',
        tertype:''
      },
      c1cnos:[],
      tertypes:[
        {key:'1',val:'微信区'},
        {key:'2',val:'QQ区'}
      ]
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
			this.$api.userinfo.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
        this.findWareClass1()
			}).then(data!=null?data.callback:'')
		},
    //查询分类
    findWareClass1:function(){
		  let para = {sysblkno:'02',clevel:1}
		  this.$api.wareclass.findList(para).then(res=>{
          this.c1cnos = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userinfo.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
       ugno: 0,
       userno: '',
       c1cno: '',
       gameno: '',
        tertype:''
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
              this.$api.userinfo.save(params).then((res) => {
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
              this.$api.userinfo.edit(params).then((res) => {
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
    tertypeFormat: function (row, column, cellValue, index){
      switch (cellValue) {
        case '1': return "微信区";
        case '2': return "QQ区";
        default: return cellValue;
      }
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"ugno", label:"编号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
        {prop:"username", label:"客户名称", minWidth:120},
				{prop:"c1cno", label:"游戏类型编号", minWidth:100},
				{prop:"cname", label:"游戏类型名称", minWidth:120},
				{prop:"tertype", label:"端游类型", minWidth:120,formatter: this.tertypeFormat},
        {prop:"gameno", label:"游戏账号", minWidth:120}
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
