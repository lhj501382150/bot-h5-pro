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
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareapply:auditlist" type="primary" @click="findPage(null)"/>
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
	<kt-table :data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleAudit="handleAudit" @handleRefuse="handleRefuse">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'审核':'驳回'" width="60%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog_height"
             label-position="right">
    <el-tabs v-model="activeName" tab-position="left"  style="margin-top: 15px;">
      <el-tab-pane name="first" label="基础信息">
          <el-form-item label="商品编号" prop="wareno" v-show="operation">
            <el-input v-model="dataForm.wareno"  auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="商品名称" prop="warename">
            {{dataForm.warename}}
          </el-form-item>
          <el-form-item label="商品分类" prop="cno">
            {{dataForm.cno}} - {{dataForm.cname}}
          </el-form-item>
          <el-form-item label="显示顺序" prop="seq">
            {{dataForm.seq}}
          </el-form-item>
        <el-form-item label="报价单位名称" prop="unit">
         {{dataForm.unit}}
        </el-form-item>
      </el-tab-pane>
      <el-tab-pane name="second" label="商品信息">
        <el-form-item label="商品标题" prop="title">
          {{dataForm.title}}
        </el-form-item>
        <el-form-item label="规格" prop="spec">
          {{dataForm.spec}}
        </el-form-item>
        <el-form-item label="推广标签" prop="flag">
            <span v-if="dataForm.flag==0">普通</span>
            <span v-else-if="dataForm.flag==1">特价</span>
            <span v-else-if="dataForm.flag==2">热卖</span>
        </el-form-item>
        <el-form-item label="数量" prop="num">
          {{dataForm.num}}
        </el-form-item>
        <el-form-item label="销售数量" prop="salnum">
          {{dataForm.salnum}}
        </el-form-item>
        <el-form-item label="原价" prop="price">
          {{dataForm.price}}
        </el-form-item>
        <el-form-item label="折扣价" prop="salprice">
          {{ dataForm.salprice }}
        </el-form-item>
        <el-form-item label="积分报价" prop="inteprice">
          {{ dataForm.inteprice }}
        </el-form-item>
        <el-form-item label="积分购买最大数量" prop="intenum">
          {{ dataForm.intenum }}
        </el-form-item>
        <el-form-item label="重量" prop="weight">
          {{ dataForm.weight }}
        </el-form-item>
        <el-form-item label="运费" prop="transcomm">
          {{ dataForm.transcomm }}
        </el-form-item>
        <el-form-item label="物流公司" prop="transcomp">
          {{ dataForm.transcomp }}
        </el-form-item>
      </el-tab-pane>
      <el-tab-pane name="third" label="商品属性">
        <el-form-item v-for="(item,index) in dataForm.wareAttrs" :key="index" :label="item.attrName" :prop="item.attrVal">
          {{item.attrVal}}
        </el-form-item>
      </el-tab-pane>
      <el-tab-pane name="five" label="商品详情">
        <el-image v-for="(item,index) in piclist" :key="index"
          style="width: 200px; height: 200px"
          :src="item"
          :preview-src-list="piclist">
        </el-image>
      </el-tab-pane>
      <el-tab-pane name="six" label="商品附件">
        <el-form-item label="视频" >
          <video v-if="dataForm.videopath" :src="videoPath"
                 style="width: 200px; height: 200px;" controls></video>
        </el-form-item>
        <el-form-item label="图片" >
          <el-image v-for="(item,index) in attachlist" :key="index"
                    style="width: 200px; height: 200px;margin: 10px 15px"
                    :src="item"
                    :preview-src-list="piclist">
          </el-image>
        </el-form-item>

      </el-tab-pane>
    </el-tabs>
      <el-form-item v-show="!operation" label="驳回原因" prop="memo">
        <el-input type="textarea" v-model="dataForm.memo"></el-input>
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
import { fileUrl } from '@/utils/global'
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

			},
			// 新增编辑界面数据
			dataForm: {},
      pwcs:[],  //商品分类列表
      piclist:[],   //详情列表
      attachlist:[], //附件列表
      videoPath:''

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
			this.$api.wareapply.findAuditPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    //公共详情
    handleShow:function(data){

    },
    //Tab 单机事件
    handleClick(tab, event) {

    },
    getWareno(waterid){
      var date = new Date();
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
      }
      var currentdate = year + month  + strDate + waterid;
      return currentdate;
    },
		// 显示审核界面
    handleAudit: function (params) {
			this.dialogVisible = true
			this.operation = true
      this.activeName = 'first'
      this.dataForm = Object.assign({}, params.row)
      let para = {waterid:params.row.waterid}
      this.$api.wareapply.getByNo(para).then(res =>{
        if(res.code!=200){
          this.$message.error(res.msg);
        }else{
          this.dataForm = res.data
          this.dataForm.cname = params.row.cname
          this.dataForm.wareno = this.getWareno(this.dataForm.waterid)
          this.initPiclist()
        }
      })
		},
		// 显示驳回界面
    handleRefuse: function (params) {
			this.dialogVisible = true
			this.operation = false
      this.activeName = 'first'
      this.dataForm = Object.assign({}, params.row)
      let para = {waterid:params.row.waterid}
      this.$api.wareapply.getByNo(para).then(res =>{
        if(res.code!=200){
          this.$message.error(res.msg);
        }else{
          this.dataForm = res.data
          this.dataForm.cname = params.row.cname
          this.initPiclist()
        }
      })

		},
    initPiclist(){
		  this.piclist = this.getFiles(this.dataForm.filepath)
		  this.attachlist = this.getFiles(this.dataForm.picpath)
      this.videoPath = fileUrl + '/downloadFile?path=' + this.dataForm.videopath
    },
    getFiles(path){
		  let ret = [];
      if(path){
        let paths = path.split(";")
        for(var i=0;i<paths.length;i++){
          let path = paths[i];
          if(!path) continue;
          path = fileUrl + '/downloadFile?path=' +path
          ret.push(path)
        }
      }
      return ret;
    },

		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.wareapply.audit(params).then((res) => {
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
              this.$api.wareapply.refuse(params).then((res) => {
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
        videopath: '',
        memo:'',
        status:0

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
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
		  this.buttons=[
		   {icon:'fa fa-check',label:'审核',perms:'trade:wareapply:audit',type:'',callback:'handleAudit'},
        {icon:'fa fa-times',label:'驳回',perms:'trade:wareapply:refuse',type:'danger',callback:'handleRefuse'}
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
