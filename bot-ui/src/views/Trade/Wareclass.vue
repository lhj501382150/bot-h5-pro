<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.cno" placeholder="分类编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.cname" placeholder="分类名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.sysblkno" placeholder="系统类型" style="width: 100%;" clearable>
          <el-option v-for="item in sysblknos" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareclass:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:wareclass:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="trade:wareclass:edit" permsDelete="trade:wareclass:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
             label-position="right">
    <el-tabs v-model="activeName" @tab-click="handleClick" type="card" style="margin-top: 15px;">
      <el-tab-pane name="first" label="基础信息">
          <el-form-item label="分类编号" prop="cno">
            <el-input v-model="dataForm.cno" :disabled="!operation" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="分类名称" prop="cname">
            <el-input v-model="dataForm.cname" auto-complete="off"></el-input>
          </el-form-item>
        <el-form-item label="系统类型" prop="sysblkno">
          <el-select v-model="dataForm.sysblkno" placeholder="请选择" style="width: 100%;">
            <el-option v-for="(item,index) in sysblknos" :key="index" :label="item.val" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
          <el-form-item label="当前级别" prop="clevel" >
            <el-input v-model.number="dataForm.clevel" auto-complete="off" @blur="findParentWareclass"></el-input>
          </el-form-item>
          <el-form-item label="上级编号" prop="parentno" v-if="dataForm.clevel > 1">
            <el-select v-model="dataForm.parentno" placeholder="请选择" style="width: 100%;">
              <el-option v-for="(c,index) in pwcs" :key="index" :label="c.cname" :value="c.cno"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="显示顺序" prop="seq">
            <el-input v-model="dataForm.seq" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="mem">
            <el-input v-model="dataForm.mem" auto-complete="off"></el-input>
          </el-form-item>
      </el-tab-pane>
      <el-tab-pane name="second" label="属性信息">
        <WareclassAttr :attrcols="dataForm.attrcols" ref="wareclassAttr"></WareclassAttr>
      </el-tab-pane>
      <el-tab-pane name="third" label="分类图标">
        <upload-file :model="'wareclass' + dataForm.cno" :img-path="dataForm.filepath" @fillpath="handleFillpath"></upload-file>
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
import WareclassAttr from "@/views/Trade/Compents/WareclassAttr";
import UploadFile from "@/views/Trade/Compents/UploadFile";
export default {
	components:{
    UploadFile,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    WareclassAttr
	},
	data() {
	  var checkClevel = (rule, value, callback) => {
      if (value ==='') {
        return callback(new Error('当前级别不能为空'));
      }
      if (!Number.isInteger(value)) {
        callback(new Error('请输入数字值'));
      }
      if(value > 14){
        callback(new Error('最多支持14级'));
      }
      callback();
    };
    var checkParentNo = (rule, value, callback) => {
          if(this.dataForm.clevel > 1 && !value){
        return callback(new Error('上级编号不能为空'));
      }
      callback();
    }
		return {
			size: 'small',
			filters: {
				cno: '',
        cname: '',
        sysblkno:''
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
				cno: [
					{ required: true, message: '请输入分类编号', trigger: 'blur' }
				],
        cname: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        sysblkno: [
          { required: true, message: '请选择系统类型', trigger: 'blur' }
        ],
        clevel: [
          { validator: checkClevel, trigger: 'blur' }
        ],
        parentno: [
          { validator: checkParentNo, trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {},
      pwcs:[],  //上级分类列表
      sysblknos:[
        {key:'01',val:'商城'},
        {key:'02',val:'游戏'}
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
			this.pageRequest.params = {'cno':this.filters.cno,'cname@like':this.filters.cname,sysblkno: this.filters.sysblkno}
			this.$api.wareclass.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    //公共详情
    handleShow:function(data){

    },
    //Tab 单机事件
    handleClick(tab, event) {

    },
    findParentWareclass: function(){
		  this.dataForm.parentno = ''
      let level = this.dataForm.clevel - 1
      if(level <= 0){
        this.pwcs = []
      }else{
        let para = {
          clevel : level,
          sysblkno:this.dataForm.sysblkno
        }
        this.$api.wareclass.findList(para).then(res => {
          this.pwcs = res.data
        })
      }
    },
		// 删除
		handleDelete: function (data) {
      this.$api.wareclass.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.initFormData()
      this.activeName = 'first'
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
      this.activeName = 'first'
			this.dataForm = Object.assign({}, params.row)
      this.findParentWareclass()
      this.dataForm = Object.assign({}, params.row)
      this.$api.wareclass.findAttrList({cno:this.dataForm.cno}).then(res =>{
        this.dataForm.attrcols = res.data
      });
		},
    handleFillpath(path){
		  this.dataForm.filepath = path
    },
		// 编辑
		submitForm: function () {
		  let ret = this.$refs.wareclassAttr.getAttr()
      if(!ret) return;
      this.dataForm.attrcols = ret
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.wareclass.save(params).then((res) => {
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
              this.$api.wareclass.edit(params).then((res) => {
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
        cno: '',
        cname: '',
        clevel: 1,
        parentno: '',
        seq: 0,
        cno1: '',
        cno2: '',
        cno3: '',
        attrcols:[],
        filepath:'',
        sysblkno:'',
        mem:''
      }
    },
  sysblknoFormatter: function (row, column, cellValue, index){
      let res = this.sysblknos.filter(function (item){
        return item.key == cellValue
      });
      if(res){
        return res[0].val
      }else{
        return cellValue
      }
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"cno", label:"分类编号", minWidth:120},
        {prop:"cname", label:"分类名称", minWidth:120},
        {prop:"sysblkno", label:"系统类型", minWidth:120,formatter:this.sysblknoFormatter},
				{prop:"clevel", label:"当前级别", minWidth:120},
				{prop:"parentno", label:"上级分类编号", minWidth:100},
				{prop:"seq", label:"显示顺序", minWidth:100},
        {prop:"cno1", label:"一级分类", minWidth:100},
        {prop:"cno2", label:"二级分类", minWidth:100},
        {prop:"cno3", label:"三级分类", minWidth:100},
				{prop:"mem", label:"描述", minWidth:100}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
		  this.buttons=[
		   // {icon:'fa fa-delete',label:'注销',perms:'firm:member:del',type:'warning',callback:'handleDelete'}
      ];
    }
	},
	mounted() {
		this.initColumns()
	}
}
</script>

<style scoped>

</style>
