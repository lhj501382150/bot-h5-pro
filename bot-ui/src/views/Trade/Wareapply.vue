<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.cno" placeholder="分类编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.wareno" placeholder="商品编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.warename" placeholder="商品名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareapply:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:wareapply:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="trade:wareapply:edit" permsDelete="trade:wareapply:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @handleApply="handleApply">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="60%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog_height"
             label-position="right">
    <el-tabs v-model="activeName" tab-position="left"  style="margin-top: 15px;">
      <el-tab-pane name="first" label="基础信息">
          <!--<el-form-item label="商品编号" prop="wareno">
            <el-input v-model="dataForm.wareno" :disabled="!operation" auto-complete="off"></el-input>
          </el-form-item>-->
          <el-form-item label="商品名称" prop="warename">
            <el-input v-model="dataForm.warename" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="商品分类" prop="cno">
            <el-select v-model="dataForm.cno" placeholder="请选择" style="width: 100%;" @change="findWareAttr">
              <el-option v-for="(c,index) in pwcs" :key="index" :label="c.clevel + '级-----' +c.cname" :value="c.cno"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="显示顺序" prop="seq">
            <el-input v-model="dataForm.seq" auto-complete="off"></el-input>
          </el-form-item>
        <el-form-item label="报价单位名称" prop="unit">
          <el-input v-model="dataForm.unit" auto-complete="off"></el-input>
        </el-form-item>
      </el-tab-pane>
      <el-tab-pane name="second" label="商品信息">
        <ware-sc :data-form="dataForm" ref="waresc"></ware-sc>
      </el-tab-pane>
      <el-tab-pane name="third" label="商品属性">
        <ware-attr :attrs="dataForm.wareAttrs" ref="wareAttr"></ware-attr>
      </el-tab-pane>
      <el-tab-pane name="five" label="商品详情">
        <UploadFileMulty :model="'ware'+dataForm.wareno" :imgPath="dataForm.filepath" ref="waredetail" :info="picinfo"></UploadFileMulty>
      </el-tab-pane>
      <el-tab-pane name="six" label="商品附件">
        <WareAttach :model="'ware'+dataForm.wareno" :imgPath="dataForm.picpath" :videoPath="dataForm.videopath" ref="attach"></WareAttach>
      </el-tab-pane>
    </el-tabs>
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
import WareAttr from "@/views/Trade/Compents/WareAttr"
import WareAttach from "@/views/Trade/Compents/WareAttach"
import UploadFileMulty from "@/views/Trade/Compents/UploadFileMulty"
import WareSc from "@/views/Trade/Compents/WareSc"
export default {
	components:{
    WareAttr,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    WareAttach,
    UploadFileMulty,
    WareSc
	},
	data() {

		return {
			size: 'small',
			filters: {
			  cno:'',
				wareno: '',
        warename: ''
			},
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
      activeName:'first',
			dataFormRules: {
				wareno: [
					{ required: true, message: '请输入分类编号', trigger: 'blur' }
				],
        warename: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        cno: [
          { required: true, message: '请选择商品分类', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {},
      pwcs:[],  //商品分类列表
      initCno: '',
      picinfo: '尺寸建议宽度720，高度不限制'

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
			this.pageRequest.params = {'cno':this.filters.cno,'wareno':this.filters.wareno,'warename@like':this.filters.warename}
			this.$api.wareapply.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    //公共详情
    handleShow:function(data){

    },
    //Tab 单机事件
    handleClick(tab, event) {

    },
    findWareclass: function(){
      this.$api.wareclass.findList({}).then(res => {
        this.pwcs = res.data
      })
    },
		// 删除
		handleDelete: function (data) {
      this.$api.wareapply.batchDelete(data.params).then(data!=null?data.callback:'')
		},
    handleApply: function(data){
		  console.log("Apply",data)
		  let para = data.row
      this.$confirm('确认提交申请吗？', '提示', {}).then(() => {
        this.$api.wareapply.apply(para).then(res =>{
          if(res.code == 200) {
            this.$message({message: '提交申请成功', type: 'success'})
            this.findPage(null)
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
        })
      })
    },
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.initFormData()
      this.activeName = 'first'
      this.findWareclass()
      this.initCno = '';
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
      this.activeName = 'first'
      this.findWareclass()
      this.dataForm = Object.assign({}, params.row)
      this.initCno = params.row.cno
      let para = {waterid:params.row.waterid}
      this.$api.wareapply.getByNo(para).then(res =>{
        if(res.code!=200){
          this.$message.error(res.msg);
        }else{
          this.dataForm = res.data
        }
      })
		},
    //查询商品属性信息
    findWareAttr(cno){
		  if(!cno) return;
      if(this.initCno === cno){//修改
        let para = {waterid:this.dataForm.waterid}
        this.$api.wareapply.findAttrList(para).then(res =>{
          this.dataForm.wareAttrs = res.data
        })
      }else{//新增
        let para ={cno:cno}
        this.$api.wareclass.findAttrList(para).then(res =>{
          this.dataForm.wareAttrs = res.data
        })
      }

    },
    //加载属性信息
    handleWareAttr(){
      this.dataForm.wareAttrs = this.$refs.wareAttr.getWareAttrData()
    },
    handleWareSc(){
		  let data = this.$refs.waresc.getFormData();
		  this.dataForm = Object.assign(this.dataForm,data);
    },
		// 编辑
		submitForm: function () {
      this.handleWareAttr()
      this.handleWareSc()
      if(!this.dataForm.valid){
        this.$message.error("商品信息设置有误，请检查！")
        return
      }
      this.dataForm.filepath = this.$refs.waredetail.getPath()
      this.dataForm.picpath = this.$refs.attach.getPicpath()
      this.dataForm.videopath = this.$refs.attach.getVideopath()
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.wareapply.save(params).then((res) => {
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
              this.$api.wareapply.edit(params).then((res) => {
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
initFormData(){
		  this.dataForm = {
		    waterid:'',
        wareno: '',
        cno: '',
        warename: '',
        seq: 0,
        unit: '',
        wareAttrs:[],
        filepath:'',
        picpath:'',
        videopath: ''

      }
    },
// 是否有效格式化
statusFormat: function (row, column, cellValue, index){
  switch (cellValue) {
      case 0: return "待提交"
      case 1: return "待审核"
      case 2: return "已审核"
      case 3: return "已驳回"
      default: return cellValue
  }
},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"waterid", label:"流水号", minWidth:120},
        {prop:"warename", label:"商品名称", minWidth:120},
        {prop:"cno", label:"分类编号", minWidth:120},
        {prop:"cname", label:"分类名称", minWidth:120},
				{prop:"seq", label:"显示顺序", minWidth:100},
        {prop:"unit", label:"报价单位", minWidth:100},
        {prop:"status", label:"状态", minWidth:240,formatter:this.statusFormat},
        {prop:"memo", label:"驳回原因", minWidth:100}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
		  this.buttons=[
		   {icon:'fa fa-edit',label:'提交申请',perms:'trade:wareapply:apply',type:'warning',callback:'handleApply'}
      ];
    }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>
.dialog_height{
  height: 380px;
  overflow: auto;
}
</style>
