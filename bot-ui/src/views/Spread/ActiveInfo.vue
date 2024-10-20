<template>
  <div class="page-container">
    <el-row>
      <el-col :span="20">
        	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-select v-model="filters.actid" placeholder="活动" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in actives" :key="index" :label="item.sname" :value="item.actid"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.artid" placeholder="资产编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.sycn" placeholder="同步状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in sycns" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:activeInfo:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="spread:activeInfo:add" type="primary" @click="handleAdd" />
			</el-form-item>
      <el-form-item>
        <upload-excel url='/activeInfo/importData' @findPage="findPage"></upload-excel>
      </el-form-item>
      <!-- <el-form-item>
          <kt-button icon="fa fa-edit" label="批量同步" perms="spread:activeInfo:sync" type="success" @click="handleBatchSync" :loading="editLoading"/>
        </el-form-item> -->
        <el-form-item>
          <kt-button icon="fa fa-times" label="批量删除" perms="spread:activeInfo:del" type="danger" @click="handleBatchDel" :loading="delLoading"/>
        </el-form-item>
		</el-form>
	</div>
      </el-col>
      <el-col :span="4">
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
      </el-col>
    </el-row>

	<!--表格内容栏-->
	<kt-table permsEdit="spread:activeInfo:edit" permsDelete="spread:activeInfo:del" :showCheckbox="true" @selectionChange="selectionChange"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog-height"
			label-position="right">
			<el-form-item label="活动名称" prop="actid" >
				 <el-select v-model="dataForm.actid" placeholder="请选择活动" style="width: 98%;" @change="chooseAct">
          <el-option v-for="(item,index) in actives" :key="index" :label="item.sname" :value="item.actid"></el-option>
        </el-select>
			</el-form-item>
      <!-- <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  placeholder="请选择客户" :disabled="true" auto-complete="off" maxlength="16">
           <el-button slot="append" icon="el-icon-search" @click="findFirm"></el-button>
        </el-input>
			</el-form-item> -->
      <div style="padding-left:50px;margin-bottom:20px;font-size:20px;text-align:left;border-bottom: 1px solid #F2F2F2;">满足以下条件客户</div>
      <el-form-item label="注册日期" v-show="actype == 1  ">
        <el-date-picker
          v-model="firm.fdate"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="注册开始日期"
          end-placeholder="注册结束日期"
          style="width:100%"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
			</el-form-item>
        <el-form-item label="客户编号" v-show="actype == 1  ">
          <el-input v-model="firm.userno" placeholder="客户编号"></el-input>
        </el-form-item>
        <el-form-item label="客户名称" v-show="actype == 1  ">
          <el-input v-model="firm.username" placeholder="客户名称"></el-input>
        </el-form-item>
        <el-form-item label="所属代理" v-show="actype == 1  ">
          <el-input v-model="firm.parentno" placeholder="所属代理"></el-input>
        </el-form-item>
        <el-form-item label="推荐人编号" v-show="actype == 1  ">
          <el-input v-model="firm.tjno" placeholder="推荐人编号"></el-input>
        </el-form-item>
        <el-form-item label="推荐人数(&gt;)" v-show="actype == 1  ">
          <el-input v-model="firm.tjnum" placeholder="推荐人数(&gt;)"></el-input>
        </el-form-item>
        <el-form-item label="登录次数(&gt;)" v-show="actype == 1  ">
          <el-input v-model="firm.lognum" placeholder="登录次数(&gt;)"></el-input>
        </el-form-item>
        <el-form-item label="认证状态" v-show="actype == 1  ">
          <el-select v-model="firm.realstatus" placeholder="认证状态" clearable style="width:100%">
            <el-option label="未申请" value="0"></el-option>
            <el-option label="未认证" value="1"></el-option>
            <el-option label="已认证" value="2"></el-option>
            <el-option label="已驳回" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否绑定银行卡" v-show="actype == 1  ">
          <el-select v-model="firm.iscard" placeholder="是否绑定银行卡" clearable style="width:100%">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单日期" v-show="actype == 2 ||  actype == 7">
        <el-date-picker
          v-model="firm.ordDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="订单开始日期"
          end-placeholder="订单结束日期"
          style="width:100%"
          value-format="yyyy-MM-dd">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="名次" v-show="actype == 7">
         <div style="text-align:left">
          <el-input v-model.number="firm.start" style="width:40%;"></el-input>
          -
          <el-input v-model.number="firm.end" style="width:40%;"></el-input>
         </div>
			</el-form-item>
      <el-form-item label="商品编号" v-show="actype == 2 || actype == 3 || actype == 4 ">
           <el-table :data="firm.wareno" size="small" border>
            <el-table-column label="商品" prop="WARENO">
              <template slot-scope="scope">
                  <el-select v-model="scope.row.WARENO"  style="width:98%">
                    <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
                  </el-select>
              </template>
            </el-table-column>
            <el-table-column label="数量" prop="NUM">
              <template slot-scope="scope">
                   <el-input v-model.number="scope.row.NUM" placeholder="请输入数量"  maxlength="8"></el-input>
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
        <el-date-picker
          v-model="firm.ordDate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="订单开始日期"
          end-placeholder="订单结束日期"
          value-format="yyyy-MM-dd">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
          <el-input v-model="firm.wareno" placeholder="商品编号"></el-input>
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
        actid: '',
        userno:'',
        artid:'',
        sycn:''
      },
      firm: {
        fdate: '',
				userno: '',
        username: '',
        paentno:'',
        tjno:'',
        tjnum:'',
        iscard:'',
        realstatus:'',
        actid:'',
        wareno:[],
        ordDate:'',
        start:'',
        end:''

			},
      columns: [],
      filterColumns: [],
      pageRequest: {pagemem: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      firmDialogVisible:false,
      editLoading: false,
      delLoading:false,
      firmLoading: false,
      dataFormRules: {
        actid: [
          {required: true, message: '请选择活动', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actid:'',
        userno: '',
        sycn:''
      },
      actives: [],
      sycns:[
        {key:'Y',val:'已同步'},
        {key:'N',val:'未同步'}
      ],
      firmData:{},
      selections:[],
      firmPage:{pageNum: 1, pageSize: 100},
      syncList:[],//批量同步选择
      actype:'',
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
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.activeInfo.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findWare:function(){
      let para = {}
      this.$api.ware.findList(para).then(res=>{
        this.warenos = res.data
      })
    },
    addRow(){
      this.firm.wareno.push({WARENO:'',NUM:''})
    },
    delRow(row,index){
      this.firm.wareno.splice(index,1)
    },
    chooseAct(val){
      let items = this.actives.filter(item => {
        return item.actid === val
      })
      if(items && items.length > 0){
        this.actype = items[0].actype
      }
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.activeInfo.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
      this.initFirmData()
			this.dataForm= {
         actid:'',
        userno: '',
        artid: '',
        sycn:''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
    refreshPageFirm(pageNum){
        this.firmPage.pageNum = pageNum
        this.findFirm()
    },
    handleSelectionChange(val) {
      this.dataForm.userno = ''
      this.selections = val
     this.dataForm.userno =  val.map(item => {
        return item.userno
      }).join(',')
    },
    findFirm(){
      if(!this.dataForm.actid){
        this.$message.error('请选择活动！')
        return
      }
      this.firmLoading = true
      this.firmDialogVisible = true
      let para = this.firmPage
      this.firm.actid = this.dataForm.actid
			let params = Object.assign({}, this.firm)
      params['regdate_ge'] = this.firm.fdate == null ? '' : this.firm.fdate[0],
      params['regdate_le'] = this.firm.fdate == null ? '' : this.firm.fdate[1],
      params['bdate'] = this.firm.ordDate == null ? '' : this.firm.ordDate[0],
      params['edate'] = this.firm.ordDate == null ? '' : this.firm.ordDate[1],
      delete params.fdate
      delete params.ordDate
      para.params = params
			this.$api.activeInfo.findUser(para).then((res) => {
        this.firmLoading = false
				this.firmData = res.data
			})
    },
    selectionChange(selections){
      this.syncList = selections.map(item=>{
        return {actid:item.actid,userno:item.userno}
      })
      console.log(this.syncList)
    },
    handleBatchDel:function(){
      if(this.syncList.length <= 0){
        this.$message.error('请选择删除数据！')
        return
      }
      this.$confirm('确定要删除吗？', '提示', {}).then(() => {
        this.delLoading = true
        let para = this.syncList
        this.$api.activeInfo.del(para).then(res =>{
          this.delLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error',dangerouslyUseHTMLString: true})
          }
          this.findPage(null)
        })
      })
    },
    handleBatchSync:function(){
      if(this.syncList.length <= 0){
        this.$message.error('请选择同步数据！')
        return
      }
      this.$confirm('确认同步吗？', '提示', {}).then(() => {
        this.editLoading = true
        let para = this.syncList
        this.$api.activeInfo.sync(para).then(res =>{
          this.editLoading = false
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败: ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){

              let para = Object.assign({}, this.firm)
              para['regdate_ge'] = this.firm.fdate == null ? '' : this.firm.fdate[0]
              para['regdate_le'] = this.firm.fdate == null ? '' : this.firm.fdate[1]
              para['bdate'] = this.firm.ordDate == null ? '' : this.firm.ordDate[0]
              para['edate'] = this.firm.ordDate == null ? '' : this.firm.ordDate[1]
              if(!(para.wareno && para.wareno.length > 0)){
                delete para.wareno
              }
              delete para.fdate
              delete para.ordDate

              params.params = para
              this.$api.activeInfo.save(params).then((res) => {
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
              this.$api.activeInfo.edit(params).then((res) => {
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
  sycnFormat: function (row, column, cellValue, index){
    let item = this.sycns.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"actid", label:"活动编号", minwidth:120},
				{prop:"sname", label:"活动名称", minwidth:120},
        {prop:"userno", label:"客户编号", minwidth:120},
				{prop:"username", label:"客户名称", minwidth:120},
        {prop:"warename", label:"商品", minwidth:120},
        {prop:"num", label:"数量", minwidth:120},
        {prop:"sycn", label:"同步状态", minwidth:120,formatter:this.sycnFormat},
				{prop:"mem", label:"备注", minwidth:100}
			]
			this.filterColumns = this.columns;
	},
  initFirmData(){
    this.firm =  {
        fdate: '',
				userno: '',
        username: '',
        paentno:'',
        tjno:'',
        tjnum:'',
        iscard:'',
        realstatus:'',
        actid:'',
        wareno:[],
        ordDate:''
			}
  },
  initData(){
    this.$api.active.list({}).then(res =>{
      this.actives = res.data
    })
  }
	},
	mounted() {
		this.initColumns()
    this.initData()
    this.findWare()
	}
}
</script>

<style scoped>

</style>
