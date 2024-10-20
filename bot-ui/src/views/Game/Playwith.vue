
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
            <el-input v-model="filters.userno" placeholder="客户编号" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
              <el-select v-model="filters.checkstatus" placeholder="审核状态" style="width: 100%;" clearable>
                <el-option v-for="(item,index) in checkstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
              </el-select>
          </el-form-item>

          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:playwith:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:playwith:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="gm:playwith:edit" permsDelete="gm:playwith:del"
		:data="pageResult" :columns="filterColumns"
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
      <el-form-item label="客户编号" prop="userno" >
        <el-input v-model="dataForm.userno" :disabled="!operation" placeholder="请输入客户编号" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="ptype"  >
          <el-select v-model="dataForm.ptype" placeholder="类型" style="width: 98%;" :disabled="!operation">
            <el-option v-for="(item,index) in ptypes" :key="index" :label="item.val" :value="item.key"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="点赞费" prop="commrate">
        <el-input v-model="dataForm.commrate" placeholder="请输入佣金比例" maxlength="16"></el-input>
      </el-form-item>
       <el-form-item label="奖励积分" prop="jlmoney">
        <el-input v-model="dataForm.jlmoney" placeholder="请输入奖励积分" maxlength="16"></el-input>
      </el-form-item>
       <el-form-item label="点赞账号" prop="opuser" >
        <el-input v-model="dataForm.opuser" placeholder="请输入点赞账号" maxlength="16"></el-input>
      </el-form-item>
     <el-form-item label="审核状态" prop="checkstatus" v-if="!operation">
          <el-select v-model="dataForm.checkstatus" placeholder="审核状态" style="width: 98%;">
            <el-option v-for="(item,index) in checkopr" :key="index" :label="item.val" :value="item.key"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="驳回原因" prop="checkmsg" v-if="!operation">
        <el-input v-model="dataForm.checkmsg" placeholder="请输入驳回原因" maxlength="64"></el-input>
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
    var validateNumber = (rule, value, callback) => {
      var regPos = /^\d+(\.\d+)?$/; //非负浮点数
      if (regPos.test(value)) {
        callback();
      } else {
        callback(new Error('佣金比例只能输入数字'));
      }
    };
		return {
      size: 'small',
      filters: {
        actno:'',
        userno: '',
        checkstatus:''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        actno: [
          {required: true, message: '请输入活动编号', trigger: 'blur'}
        ],
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        ptype: [
          {required: true, message: '请选择类型', trigger: 'blur'}
        ],
        commrate: [
          {required: true, message: '请输入点赞费', trigger: 'blur'},
          {validator:validateNumber,trigger: 'blur'}
        ],
        jlmoney: [
          {required: true, message: '请输入奖励积分', trigger: 'blur'},
          {validator:validateNumber,trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actno:'',
        userno: '',
        ptype:'',
        commrate: '',
        checkstatus: '',
        checkmsg: '',
        jlmoney:'',
        opuser:''
      },
      checkstatuss:[
        {key:'0',val:'初始'},
        {key:'3',val:'已申请'},
        {key:'1',val:'已审核'},
        {key:'2',val:'已驳回'}
      ],
      checkopr:[
        {key:'1',val:'审核'},
        {key:'2',val:'驳回'}
      ],
      ptypes:[
        {key:'1',val:'关注'},
        {key:'2',val:'点赞'}
      ],
      acts: []
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
			this.$api.playwith.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    initDict:function(){
      this.$api.activity.findList().then(res=>{
	      this.acts = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.playwith.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        actno:'',
        userno: '',
        commrate: '',
        checkstatus: '',
        checkmsg: '',
        jlmoney:'',
        opuser:''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.dataForm = Object.assign({}, params.row)
      if(this.dataForm.checkstatus != '3'){
        this.$message.error("只有已申请数据才可修改！")
        return
      }
      this.dialogVisible = true
      this.operation = false
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.playwith.save(params).then((res) => {
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
              this.$api.playwith.edit(params).then((res) => {
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
    statusFormat: function (row, column, cellValue, index){
      let t = this.checkstatuss.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length > 0) val = t[0].val
      return val
    },
    ptypeFormat: function (row, column, cellValue, index){
      let t = this.ptypes.filter(function(item){
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
        {prop:"actno", label:"活动编号", minWidth:120},
        {prop:"actname", label:"活动名称", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
        {prop:"username", label:"客户名称", minWidth:120},
        {prop:"ptype", label:"类型", minWidth:100,formatter:this.ptypeFormat},
        {prop:"commrate", label:"点赞费", minWidth:120},
        {prop:"jlmoney", label:"奖励积分", minWidth:120},
				{prop:"checkstatus", label:"审核状态", minWidth:100,formatter:this.statusFormat},
        {prop:"checkmsg", label:"驳回原因", minWidth:120},
        {prop:"opuser", label:"点赞账号", minWidth:120},
        {prop:"optime", label:"操作时间", minWidth:150},
        {prop:"opaddr", label:"操作地址", minWidth:120}
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
