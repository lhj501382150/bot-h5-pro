<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-select v-model="filters.imgtype" placeholder="图片类型" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in imgtypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:img:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:img:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="sys:img:edit" permsDelete="sys:img:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" >
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
			<el-form-item label="编号" prop="id" v-show="!operation">
				<el-input v-model="dataForm.id" :disabled="!operation"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="图片类型" prop="imgtype" >
        <el-select v-model="dataForm.imgtype" placeholder="图片类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in imgtypes" :key="index" :label="item.val" :value="item.key + ''"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="seq">
        <el-input v-model.number="dataForm.seq" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="图片" prop="imgpath" >
        <upload-file :model="'img' + dataForm.id" :img-path="dataForm.imgpath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
      <el-form-item label="功能路径" prop="path" v-if="dataForm.imgtype==1">
        <el-input v-model.number="dataForm.path" auto-complete="off"></el-input>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading" >{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import UploadFile from "@/views/Trade/Compents/UploadFile";
import { format } from "@/utils/datetime"
export default {
	components:{
    UploadFile,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        imgtype: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      resultDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        imgtype: [
          {required: true, message: '请选择图片类型', trigger: 'blur'}
        ],
        seq: [
          {required: true, message: '请输入次序', trigger: 'blur'}
        ],
        imgpath: [
          {required: true, message: '请上传图片', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        imgtype: '',
        seq: '',
        imgpath: '',
        mem: ''
      },
      imgtypes:[
        {key:1,val:'功能图片'},
        {key:2,val:'形象图片'}
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
			this.$api.imglist.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    handleFillpath(path){
      this.dataForm.imgpath = path
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.imglist.del(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
      this.optbtn = true
			this.dataForm= {
        id: '',
        imgtype: '',
        seq: '',
        imgpath: '',
        path:'',
        mem: ''
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
              this.$api.imglist.save(params).then((res) => {
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
              this.$api.imglist.edit(params).then((res) => {
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
		//
  imgtypeFormat: function (row, column, cellValue, index){
    let t = this.imgtypes.filter(function(item){
      return item.key == cellValue
    });
    let val = cellValue;
    if(t && t.length > 0) val = t[0].val
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
				{prop:"id", label:"编号", minWidth:120},
        {prop:"imgtype", label:"图片类型", minWidth:120,formatter:this.imgtypeFormat},
        {prop:"seq", label:"次序", minWidth:100},
        {prop:"imgpath", label:"图片路径", minWidth:150},
				{prop:"path", label:"功能路径", minWidth:100}
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
