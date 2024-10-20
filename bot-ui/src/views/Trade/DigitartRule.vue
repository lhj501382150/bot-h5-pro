<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.nwareno" placeholder="商品编号" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.sname" placeholder="规则名称" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:digitartRule:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:digitartRule:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="trade:digitartRule:edit" permsDelete="trade:digitartRule:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
			<el-form-item label="编号" prop="id" v-show="!operation">
				<el-input v-model="dataForm.id" :disabled="!operation"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="规则名称" prop="sname" >
				<el-input v-model="dataForm.sname" placeholder="请输入规则名称"  maxlength="32"></el-input>
			</el-form-item>
       <el-form-item label="规则说明" prop="mem" >
				<!-- <el-input v-model="dataForm.mem" type="textarea" :rows="4" placeholder="请输入规则说明" ></el-input> -->
        <upload-file :model="'digitart'" :img-path="dataForm.mem" @fillpath="handleFillpath"></upload-file>
			</el-form-item>
      <el-form-item label="新商品编号" prop="wareno" >
				<el-select v-model="dataForm.nwareno" style="width:98%" filterable>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.title" :value="item.wareno"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item label="合成信息">
				 <el-table :data="tabList" size="small" border>
            <el-table-column label="商品" prop="WARENO">
              <template slot-scope="scope">
                  <el-select v-model="scope.row.WARENO"  style="width:98%">
                    <el-option v-for="(item,index) in warenos" :key="index" :label="item.title" :value="item.wareno"></el-option>
                  </el-select>
              </template>
            </el-table-column>
            <el-table-column label="数量" prop="NUM">
              <template slot-scope="scope">
                   <el-input v-model.number="scope.row.NUM" placeholder="请输入数量"  maxlength="8"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="等级" prop="CARDGRADE">
              <template slot-scope="scope">
                   <el-input v-model.number="scope.row.CARDGRADE" placeholder="请输入等级"  maxlength="8"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="材料类型" prop="TYPE">
              <template slot-scope="scope">
                  <el-select v-model="scope.row.TYPE"  style="width:98%">
                    <el-option label="必需品" value="1"></el-option>
                    <el-option label="任意品" value="2"></el-option>
                  </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="danger"  @click="delRow(scope.row,scope.$index)" size="small">删除</el-button>
              </template>
            </el-table-column>
         </el-table>
         <el-button type="primary" @click="addRow()" size="small">添加</el-button>
			</el-form-item>
      <el-form-item style="text-align:left">
            <div style="color:red;">说明：1、不限制等级时填空</div>
            <div style="color:red;">2、必需品数量必须大于 0 ，任意品数量的和满足条件即可</div>
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
import UploadFile from "@/views/Trade/Compents/UploadFile";
import { format } from "@/utils/datetime"
export default {
	components:{
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadFile
	},
	data() {
		return {
      size: 'small',
      filters: {
        nwareno: '',
        sname: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        nwareno: [
          {required: true, message: '请选择商品编号', trigger: 'blur'}
        ],
        sname: [
          {required: true, message: '请输入规则名称', trigger: 'blur'}
        ],
        mem: [
          {required: true, message: '请输入规则说明', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        nwareno: '',
        sname: '',
        mem: '',
        c2cno: '',
        owareinfo: ''
      },
      warenos:[],
      tabList:[]
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
			this.pageRequest.params = {
			  'nwareno':this.filters.nwareno,
        'sname@LIKE':this.filters.sname
			}
			this.$api.userdigitartRule.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findWare:function(){
      let para = {}
      this.$api.wareFirmRule.findWare(para).then(res=>{
        this.warenos = res.data
      })
    },
    handleFillpath(path){
      this.dataForm.mem = path
    },
    addRow(){
      this.tabList.push({WARENO:'',NUM:'',CARDGRADE:'',TYPE:''})
    },
    delRow(row,index){
      this.tabList.splice(index,1)
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userdigitartRule.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
         id: 0,
        nwareno: '',
        sname: '',
        mem: '',
        c2cno: '',
        owareinfo: ''
      }
		},

		// 显示编辑界面
		handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.tabList = JSON.parse(this.dataForm.owareinfo);
		},
		// 编辑
		submitForm: function () {
      if(this.tabList.length <= 0){
        this.$message.error("请设置合成商品信息")
        return
      }
      this.dataForm.owareinfo = JSON.stringify(this.tabList);
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.userdigitartRule.save(params).then((res) => {
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
              this.$api.userdigitartRule.edit(params).then((res) => {
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
  tertypeFormat: function (row, column, cellValue, index){
    let val = ""
    try{
      let arr = JSON.parse(cellValue)
      for(var i=0;i<arr.length;i++){
        val += "商品编号：" +arr[i].WARENO + ",数量：" + arr[i].NUM + '\n'
      }
    }catch(e){
      val = cellValue
    }

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
        {prop:"sname", label:"规则名称", minWidth:120},
        {prop:"mem", label:"规则说明", minWidth:180},
        {prop:"nwareno", label:"新商品编号", minWidth:120},
				{prop:"warename", label:"新商品名称", minWidth:100},
				{prop:"owareinfo", label:"合成信息", minWidth:160,formatter:this.tertypeFormat}
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
