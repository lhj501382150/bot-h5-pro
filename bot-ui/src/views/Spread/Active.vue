<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.sname" placeholder="活动名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.wareno" placeholder="商品" style="width: 100%;" clearable>
          <el-option v-for="item in warenos" :key="item.wareno" :label="item.warename" :value="item.wareno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.actype" placeholder="活动类型" style="width: 100%;" clearable>
          <el-option v-for="item in actypes" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.istri" placeholder="是否开启" style="width: 100%;" clearable>
          <el-option v-for="item in istris" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:active:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="spread:active:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="spread:active:edit" permsDelete="spread:active:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="活动编号" prop="actid" v-if="!operation">
				<el-input v-model="dataForm.actid" :disabled="!operation" auto-complete="off" ></el-input>
			</el-form-item>
      <el-form-item label="活动名称" prop="sname" >
				<el-input v-model="dataForm.sname" auto-complete="off" ></el-input>
			</el-form-item>
      <el-form-item label="活动类型" prop="actype">
        <el-select v-model="dataForm.actype" placeholder="请选择活动类型" style="width: 98%;">
          <el-option v-for="item in actypes" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="空投商品" prop="wareno">
        <el-select v-model="dataForm.wareno" placeholder="请选择商品" style="width: 98%;">
          <el-option v-for="item in warenos" :key="item.wareno" :label="item.warename" :value="item.wareno"></el-option>
        </el-select>
      </el-form-item>
       <el-form-item label="数量" prop="num" >
				<el-input v-model.number="dataForm.num" auto-complete="off" ></el-input>
        <div style="color:red;width:100%;text-align:left;">
          说明：1、拉新推荐：推荐【数量】个送一个,最多送【最大数量】<br/>
          2、持有资产：同时持有商品A,B,C的数量*【数量】，最多送【最大数量】<br/>
          3、购买资产：购买过首发的商品A,B,C的数量*【数量】，最多送【最大数量】<br/>
        </div>
			</el-form-item>
       <el-form-item label="最大数量" prop="maxnum" >
				<el-input v-model.number="dataForm.maxnum" auto-complete="off" ></el-input>
			</el-form-item>
       <el-form-item label="触发时间" prop="tritime" >
        <el-date-picker
          v-model="dataForm.tritime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择触发时间">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="备注" prop="mem">
        <el-input v-model="dataForm.mem" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="是否开启" prop="istri">
        <el-select v-model="dataForm.istri" placeholder="请选择是否开启" style="width: 98%;">
          <el-option v-for="item in istris" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
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
import {getStatus} from "@/utils/status"
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
        sname: '',
        actype:'',
        wareno:'',
        istri:''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pagemem: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        sname: [
          {required: true, message: '请输入活动名称', trigger: 'blur'}
        ],
        actype: [
          {required: true, message: '请选择活动类型', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请选择商品', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '请输入数量', trigger: 'blur'}
        ],
        maxnum: [
          {required: true, message: '请输入最大数量', trigger: 'blur'}
        ],
        tritime: [
          {required: true, message: '请输入触发时间', trigger: 'blur'}
        ],
        istri: [
          {required: true, message: '请选择是否开启', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actid:'',
        sname: '',
        actype:'1',
        warno:'',
        num:'',
        maxnum:'',
        tritime:'',
        istri:'Y',
        mem: ''
      },
      actypes:[
        {sval:'1',sshow:'拉新推荐人数空投'},
        {sval:'2',sshow:'购买资产空投'},
        {sval:'3',sshow:'持有资产空投'},
        {sval:'4',sshow:'资产送盲盒'},
        {sval:'5',sshow:'导入送盲盒'},
        {sval:'6',sshow:'导入空投'},
        {sval:'7',sshow:'消费排行'}
      ],
      istris:[
        {sval:'Y',sshow:'开启'},
        {sval:'N',sshow:'未开启'}
      ],
      warenos:[]
    }
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pagemem: 1, pageSize: 50}
      }
			this.pageRequest.params = {
			  'sname@like':this.filters.sname,
        istri:this.filters.istri,
        actype: this.filters.actype,
        wareno: this.filters.wareno
			}
			this.$api.active.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findWare:function(){
      let para = {}
      this.$api.ware.findList(para).then(res=>{
        this.warenos = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.active.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        sname: '',
        actype:'',
        warno:'',
        num:'',
        maxnum:'',
        tritime:'',
        istri:'',
        mem: ''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.active.save(params).then((res) => {
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
              this.$api.active.edit(params).then((res) => {
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
   statusFormatter: function (row, column, cellValue, index){
     return getStatus(cellValue,this.actypes)
    },
    stypeFormatter: function (row, column, cellValue, index){
      return getStatus(cellValue,this.istris)
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"actid", label:"活动编号", minwidth:120},
				{prop:"actype", label:"活动类型", minwidth:120,formatter:this.statusFormatter},
				{prop:"sname", label:"活动名称", minwidth:120},
				{prop:"wareno", label:"商品编号", minwidth:120},
				{prop:"warename", label:"商品名称", minwidth:120},
				{prop:"num", label:"数量", minwidth:120},
				{prop:"maxnum", label:"最大数量", minwidth:120},
				{prop:"tritime", label:"触发时间", minwidth:120},
				{prop:"istri", label:"是否开启", minwidth:120,formatter:this.stypeFormatter},
				{prop:"mem", label:"备注", minwidth:100}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
    this.findWare()
	}
}
</script>

<style scoped>

</style>
