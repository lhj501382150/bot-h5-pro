<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.wareno" placeholder="商品编号"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareFirmPara:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:wareFirmPara:add" type="primary" @click="handleAdd" />
        </el-form-item>
        <el-form-item>
        <upload-excel url='/wareFirmPara/importData' @findPage="findPage"></upload-excel>
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
	<kt-table permsEdit="trade:wareFirmPara:edit" permsDelete="trade:wareFirmPara:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno" v-if="operation"   placeholder="请选择客户" :disabled="true" auto-complete="off" maxlength="16">
           <el-button slot="append" icon="el-icon-search" @click="findFirm"></el-button>
        </el-input>
        <el-input v-model="dataForm.userno" v-if="!operation"  placeholder="请选择客户" :disabled="true" auto-complete="off" maxlength="16">
        </el-input>
			</el-form-item>
			<el-form-item label="商品编号" prop="wareno" >
				<el-select v-model="dataForm.wareno"  v-if="operation" style="width:98%" filterable multiple>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
        </el-select>
        <el-input v-model="dataForm.wareno" v-if="!operation"  :disabled="true" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item v-for="(item,index) in dataForm.items" :key="index" :label="item.paraname" :prop="item.paraval">
        <el-input v-if="!item.listData" v-model="item.paraval"  auto-complete="off"></el-input>
        <el-select v-else v-model="item.paraval" style="width: 98%">
          <el-option v-for="opt in item.items" :key="opt.key" :value="opt.key" :label="opt.val"></el-option>
        </el-select>
      </el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

  <el-dialog title="客户列表" width="70%" :visible.sync="firmDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag >
    <div>
      <el-form :inline="true" :model="firm" :size="size" style="text-align:left;">
        <el-form-item>
        <el-date-picker
          v-model="firm.fdate"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="注册开始日期"
          end-placeholder="注册结束日期"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
			</el-form-item>
        <el-form-item>
          <el-input v-model="firm.userno" placeholder="客户编号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.username" placeholder="客户名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.parentno" placeholder="所属代理"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.tjno" placeholder="推荐人编号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="firm.tjnum" placeholder="推荐人数(&gt;)"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="firm.realstatus" placeholder="认证状态" clearable>
            <el-option label="未申请" value="0"></el-option>
            <el-option label="未认证" value="1"></el-option>
            <el-option label="已认证" value="2"></el-option>
            <el-option label="已驳回" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="firm.iscard" placeholder="是否绑定银行卡" clearable>
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:activeInfo:view" type="primary" @click="findFirm(null)"/>
        </el-form-item>
      </el-form>
    </div>
      <el-table :data="firmData.content" style="width: 100%" ref="multipleTable" @selection-change="handleSelectionChange" :loading="firmLoading" max-height="400px">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="userno" label="客户编号" width="120"></el-table-column>
        <el-table-column prop="username" label="客户名称" width="120"></el-table-column>
        <el-table-column prop="regdate" label="注册时间" width="180"></el-table-column>
        <el-table-column prop="parentno" label="所属代理" width="100"></el-table-column>
        <el-table-column prop="tjno" label="推荐人" width="150"></el-table-column>
        <el-table-column prop="tjnum" label="推荐人数" width="120"></el-table-column>
        <el-table-column prop="realstatus" label="状态" width="150">
          <template slot-scope="s">
            <span v-if="s.row.realstatus==0">未申请</span>
            <span v-else-if="s.row.realstatus==1">未认证</span>
            <span v-else-if="s.row.realstatus==2">已认证</span>
            <span v-else-if="s.row.realstatus==3">已驳回</span>
          </template>
        </el-table-column>
        <el-table-column prop="iscard" label="是否绑定银行卡" width="150">
          <template slot-scope="s">
            <span v-if="s.row.iscard=='Y'">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination layout="total, prev, pager, next, jumper" @current-change="refreshPageFirm"
        :current-page.sync="firmData.pageNum" :page-size.sync="firmData.pageSize" :total="firmData.totalSize" style="float:right;">
      </el-pagination>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="firmDialogVisible = false" type="primary">确定</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { format } from "@/utils/datetime"
import UploadExcel from "@/views/Trade/Compents/UploadExcel"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadExcel
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        wareno: ''
      },
      firm: {
        fdate: '',
				userno: '',
        username: '',
        paentno:'',
        tjno:'',
        tjnum:'',
        iscard:'',
        realstatus:''
			},
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      firmPage:{pageNum: 1, pageSize: 100},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      firmDialogVisible:false,
      editLoading: false,
      firmLoading: false,
      dataFormRules: {
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请输入商品编号', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        userno: '',
        wareno: '',
        p1001: '',
        p1002: '',
        p1003: '',
        items:[]
      },
      warenos:[],
      firmData:{},
      selections:[]
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
			this.$api.wareFirmPara.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findPara(){
      this.paras = []
		  let para = {}
      if(!this.operation){
        para = {
          wareno:this.dataForm.wareno,
          userno:this.dataForm.userno
        }
      }
      this.$api.wareFirmPara.findParalist(para).then(res =>{
        this.dataForm.items = res.data
      })
    },
    findWares(){
      this.$api.ware.findList({}).then(res => {
        this.warenos = res.data
      })
    },
    refreshPageFirm(pageNum){
        this.firmPage.pageNum = pageNum
        this.findFirm()
    },
    findFirm(){
      this.firmLoading = true
      this.firmDialogVisible = true
      let para = this.firmPage
			let params = Object.assign({}, this.firm)
      params['username@like'] = this.firm.username
      params['regdate@ge'] = this.firm.fdate == null ? '' : this.firm.fdate[0],
      params['regdate@le'] = this.firm.fdate == null ? '' : this.firm.fdate[1],
      delete params.username
      delete params.fdate
      para.params = params
			this.$api.firm.findPage(para).then((res) => {
        this.firmLoading = false
				this.firmData = res.data

        // if (this.selections) {
        //    this.$nextTick(()=>{
        //       this.selections.forEach(row => {
        //       console.log(row)
        //       this.$refs.multipleTable.toggleRowSelection(row);
        //     });
        //   })
        // }
			})
    },
    handleSelectionChange(val) {
      this.dataForm.userno = ''
      this.selections = val
     this.dataForm.userno =  val.map(item => {
        return item.userno
      }).join(',')
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.wareFirmPara.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
      this.operation = true
			this.dataForm= {
        userno: '',
        wareno: '',
        p1001: '',
        p1002: '',
        p1003: '',
        items:''
      }
       this.findPara()
      this.dialogVisible = true
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.findPara()
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
               params.wareno = this.dataForm.wareno.join(",")
              this.$api.wareFirmPara.save(params).then((res) => {
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
              this.$api.wareFirmPara.edit(params).then((res) => {
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

		// 是否有效格式化
  isdefaultFormat: function (row, column, cellValue, index){
    if(cellValue==='Y') {
      return "是"
    } else {
      return "否"
    }
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
  statusformat: function (row, column, cellValue, index){
    let val = ""
    if(cellValue=='Y'){
      val = '开启'
    }else if(cellValue == 'N'){
      val = '关闭'
    }else{
      val = cellValue
    }
    return val
	},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
        {prop:"wareno", label:"商品编号", minWidth:120},
				{prop:"warename", label:"商品名称", minWidth:120},
				{prop:"p1001", label:"优先购权益", minWidth:100,formatter:this.statusformat},
				{prop:"p1002", label:"折扣优惠权益", minWidth:120},
				{prop:"p1003", label:"空投权益", minWidth:100,formatter:this.statusformat}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
    this.findWares()
	}
}
</script>

<style scoped>

</style>
