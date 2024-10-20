
<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-select v-model="filters.ltype" placeholder="榜单类型" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in ltypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.cyctype" placeholder="周期类型" clearable>
              <el-option v-for="(item,index) in cyctypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:ranklist:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:ranklist:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="gm:ranklist:edit" permsDelete="gm:ranklist:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
      <el-form-item label="榜单类型" prop="ltype" >
        <el-select v-model="dataForm.ltype" placeholder="榜单类型" :disabled="!operation" style="width: 98%;">
          <el-option v-for="(item,index) in ltypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="周期类型" prop="cyctype" >
        <el-select v-model="dataForm.cyctype" placeholder="周期类型" :disabled="!operation" style="width: 98%;">
          <el-option v-for="(item,index) in cyctypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名次" prop="seq">
        <el-input v-model.number="dataForm.seq" placeholder="请输入名次" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname" >
        <el-input v-model="dataForm.nickname" placeholder="请输入昵称" maxlength="16"></el-input>
      </el-form-item>

      <el-form-item label="金额" prop="award">
        <el-input v-model="dataForm.award" placeholder="请输入金额" maxlength="16"></el-input>
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
    var validateNumber = (rule, value, callback) => {
      var regPos = /^\d+(\.\d+)?$/; //非负浮点数
      if (regPos.test(value)) {
        callback();
      } else {
        callback(new Error('金额只能输入数字'));
      }
    };
		return {
      size: 'small',
      filters: {
        ltype: '',
        cyctype:''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        ltype: [
          {required: true, message: '请选择榜单类型', trigger: 'blur'}
        ],
        cyctype: [
          {required: true, message: '请选择周期类型', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'}
        ],
        award: [
          {required: true, message: '请输入金额', trigger: 'blur'},
          {validator:validateNumber,trigger: 'blur'}
        ],
        seq: [
          {required: true, message: '请输入名词', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        ltype: '',
        cyctype: '',
        nickname: '',
        award: '',
        seq: 0,
      },
      ltypes:[
        {key:'1',val:'钻石榜'},
        {key:'2',val:'金豆榜'},
      ],
      cyctypes:[
        {key:'1',val:'月'},
        {key:'2',val:'周'},
        {key:'3',val:'日'},
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
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.ranklist.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.ranklist.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
       ltype: '',
       cyctype: '',
       nickname: '',
       award: '',
       seq: 0,
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
              this.$api.ranklist.save(params).then((res) => {
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
              this.$api.ranklist.edit(params).then((res) => {
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
    ltypeFormat: function (row, column, cellValue, index){
      let t = this.ltypes.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t) val = t[0].val
      return val
    },
    cyctypeFormat: function (row, column, cellValue, index){
      let t = this.cyctypes.filter(function(item){
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
				{prop:"ltype", label:"榜单类型", minWidth:120,formatter: this.ltypeFormat},
        {prop:"cyctype", label:"周期类型", minWidth:120,formatter: this.cyctypeFormat},
        {prop:"seq", label:"名次", minWidth:120},
        {prop:"nickname", label:"昵称", minWidth:120},
				{prop:"award", label:"金额", minWidth:100}

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
