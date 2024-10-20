<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.leveno" placeholder="等级"></el-input>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.rtype" placeholder="类型" style="width: 100%;" clearable>
          <el-option v-for="acct in rtypes" :key="acct.key" :label="acct.val" :value="acct.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:drawparas:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="spread:drawparas:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="spread:drawparas:edit" permsDelete="spread:drawparas:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="等级" prop="leveno">
				<el-input v-model="dataForm.leveno" :disabled="!operation" auto-complete="off" ></el-input>
			</el-form-item>
			<el-form-item label="名称" prop="levname" >
				<el-input v-model="dataForm.levname" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="奖励物品" prop="rewinfo" >
        <el-input v-model="dataForm.rewinfo" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="中奖比例(‰)" prop="ratio">
        <el-input v-model.number="dataForm.ratio" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="奖品个数" prop="num" >
        <el-input v-model.number="dataForm.num" auto-complete="off"  maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="抽中个数" prop="outnum" >
        <el-input v-model.number="dataForm.outnum" auto-complete="off"  maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="rtype" >
        <el-select v-model="dataForm.rtype" placeholder="类型" style="width: 98%;" >
          <el-option v-for="acct in rtypes" :key="acct.key" :label="acct.val" :value="acct.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="等级标签" prop="picpath" >
        <upload-file :model="'userinof'" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
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
import UploadFile from "@/views/Trade/Compents/UploadFile"
import { format } from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadFile
	},
	data() {
		return {
      size: 'small',
      filters: {
        leveno: '',
        rtype:''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        leveno: [
          {required: true, message: '等级', trigger: 'blur'}
        ],
        levname: [
          {required: true, message: '名称', trigger: 'blur'}
        ],
        rewinfo: [
          {required: true, message: '请输入奖励物品', trigger: 'blur'}
        ],
        ratio: [
          {required: true, message: '请输入中奖比例(‰)', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '请输入奖品个数', trigger: 'blur'}
        ],
        outnum: [
          {required: true, message: '请输入抽中个数', trigger: 'blur'}
        ],
        rtype: [
          {required: true, message: '请选择类型', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        leveno: '',
        levname: '',
        rewinfo: '',
        ratio: '',
        num:'',
        outnum:'',
        picpath:'',
        rtyoe:''

      },
      rtypes:[
        {key:'0',val:'谢谢参与'},
        {key:'1',val:'虚拟值'},
        {key:'2',val:'实物'},
        {key:'3',val:'盲盒'},
        {key:'4',val:'卡牌'},
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
			this.pageRequest.params = {
			  'leveno':this.filters.leveno,
			  'rtype':this.filters.rtype
			}
			this.$api.drawparas.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.drawparas.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        leveno: '',
        levname: '',
        rewinfo: '',
        ratio: '',
        num:'',
        outnum:'',
        picpath:'',
        rtyoe:''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
    handleFillpath(path){
      this.dataForm.picpath = path
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.drawparas.save(params).then((res) => {
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
              this.$api.drawparas.edit(params).then((res) => {
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
  rtypeFormat: function (row, column, cellValue, index){
      let items = this.rtypes.filter(function(item){
        return item.key == cellValue
      })
      if(items && items.length > 0){
        return items[0].val
      }
      return cellValue
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"leveno", label:"等级", minwidth:120},
        {prop:"levname", label:"名称", minwidth:120},
				{prop:"rewinfo", label:"奖励物品", minwidth:120},
				{prop:"ratio", label:"中奖比例(‰)", minwidth:100},
				{prop:"num", label:"奖品个数", minwidth:100},
				{prop:"outnum", label:"抽中个数", minwidth:100},
				{prop:"rtype", label:"类型", minwidth:100,formatter:this.rtypeFormat}

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
