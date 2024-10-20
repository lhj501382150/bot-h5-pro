<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.wareno" placeholder="商品编号" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.hstatus" placeholder="状态" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in hstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareMem:view" type="primary" @click="findPage(null)"/>
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
	<kt-table permsEdit="trade:wareMem:edit" permsDelete="trade:wareMem:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
      <el-form-item label="商品编号" prop="wareno" >
        <el-input v-model="dataForm.wareno" placeholder="请输入商品编号编号" maxlength="16" :disabled="!operation"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="hstatus">
        <el-select v-model="dataForm.hstatus" placeholder="状态" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in hstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="数量" prop="num">
        <el-input type="text" v-model.number="dataForm.num" placeholder="请输入数量" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="销售量" prop="salnum">
        <el-input type="text" v-model.number="dataForm.salnum" placeholder="请输入销售量" maxlength="16"></el-input>
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
        wareno: '',
        hstatus:''
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
        wareno: [
          {required: true, message: '请输入商品编号编号', trigger: 'blur'}
        ],
        hstatus: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '请输入数量', trigger: 'blur'}
        ],
        salnum: [
          {required: true, message: '请输入销售量', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        wareno: '',
        hstatus: '',
        num: '',
        salnum: ''
      },
      hstatuss:[
        {key:'Y',val:'挂起'},
        {key:'N',val:'正常'}
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
			  'wareno':this.filters.wareno,
        'hstatus':this.filters.hstatus
			}
			this.$api.wareMem.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.wareMem.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {

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
              this.$api.wareMem.save(params).then((res) => {
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
              this.$api.wareMem.edit(params).then((res) => {
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
    hstatusFormat: function (row, column, cellValue, index){
      let t = this.hstatuss.filter(function(item){
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
          {prop:"wareno", label:"商品编号", minWidth:120},
          {prop:"warename", label:"商品名称", minWidth:120},
          {prop:"hstatus", label:"活动状态", minWidth:100,formatter:this.hstatusFormat},
          {prop:"num", label:"数量", minWidth:100},
          {prop:"salnum", label:"销售量", minWidth:120}
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
