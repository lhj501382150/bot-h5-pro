<template>
  <div class="page-container">
	<!--工具栏-->
   <el-row>
      <el-col class="toolbar" :span="20">
        	<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
       <el-form-item>
        <el-select v-model="filters.wareno" clearable="" placeholder="商品">
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
         </el-select>
      </el-form-item>
	    <el-form-item>
				<el-input v-model="filters.orguserno" placeholder="原始发行方"></el-input>
			</el-form-item>
      <el-form-item>
				<el-input v-model="filters.cardno" placeholder="卡券编号"></el-input>
			</el-form-item>
      <el-form-item>
				<el-input v-model="filters.chkhash" placeholder="资产校验码"></el-input>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.bustype"  placeholder="业务分类" clearable>
          <el-option v-for="(item,index) in bustypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
    </el-form-item>
      <el-form-item>
        <el-select v-model="filters.cardstatus"  placeholder="状态" clearable>
          <el-option v-for="(item,index) in cardstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
    </el-form-item>
      <el-form-item>
        <el-select v-model="filters.upstatus"  placeholder="上链状态" clearable>
          <el-option v-for="(item,index) in upstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
    </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:digitart:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="trade:digitart:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:digitart:add" type="primary" @click="handleAdd" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" label="批量新增" perms="trade:digitart:add" type="warning" @click="handleBatchAdd" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-edit" label="批量发行" perms="trade:digitart:issue" type="success" @click="handleBatchIssue" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-check" label="批量转赠" perms="trade:digitart:send" type="success" @click="handleSend" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-unlock" label="批量解锁" perms="trade:digitart:unlock" type="success" @click="handleBatchUnlock" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-times" label="批量删除" perms="trade:digitart:batchDel" type="danger" @click="handleBatchDel" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-times" label="批量终止" perms="trade:digitart:batchTermination" type="danger" @click="handleBatchTermination" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-lock" label="批量锁定" perms="trade:digitart:lock" type="danger" @click="handleBatchlock" />
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-times" label="清理未发行资产" perms="trade:digitart:clear" type="danger" @click="handleClear" />
        </el-form-item>
		</el-form>
      </el-col>
       <el-col class="toolbar" :span="4">
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
      </el-col>
   </el-row>
	<!--表格内容栏-->
	<kt-table permsEdit="trade:digitart:edit" permsDelete="trade:digitart:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" :showCheckbox="true" @selectionChange="selectionChange" @handleBatchDel="handleBatchDel"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @handleIssue="handleIssue" @handleStop="handleStop" @handleUp="handleUp">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height:500px; overflow:auto"
			label-position="right">
			<el-form-item label="资产编号" prop="artid" v-if="!operation">
				<el-input v-model="dataForm.artid" :disabled="!operation" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="原始资产编号" prop="orgartid" v-if="!operation">
				<el-input v-model="dataForm.orgartid"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="原始发行方" prop="orguserno"  v-if="!operation">
				<el-input v-model="dataForm.orguserno"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="商品" prop="wareno" >
        <el-select v-model="dataForm.wareno" style="width:98%" filterable>
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="业务分类" prop="bustype" >
        <el-select v-model="dataForm.bustype"  placeholder="请选择业务分类" style="width:98%">
          <el-option v-for="(item,index) in bustypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
    </el-form-item>
			<el-form-item label="卡券编号" prop="cardno" >
				<el-input v-model="dataForm.cardno" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="卡券名称" prop="cardname">
				<el-input v-model="dataForm.cardname" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="份数" prop="num" >
         <el-input v-model.number="dataForm.num" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
        <el-form-item label="性别" prop="cardsex" >
         <el-select v-model="dataForm.cardsex" style="width:98%">
           <el-option v-for="(item,index) in sexs" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
      <el-form-item label="材质" prop="cardrace" >
        <el-select v-model="dataForm.cardrace" style="width:98%">
           <el-option v-for="(item,index) in cardraces" :key="index" :label="item" :value="item"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="等级" prop="cardgrade" >
        <el-select v-model="dataForm.cardgrade" style="width:98%">
           <el-option v-for="(item,index) in cardgrades" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="arttype" >
        <el-select v-model="dataForm.arttype" style="width:98%">
           <el-option v-for="(item,index) in arttypes" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="属性" prop="mem" >
        <el-input v-model="dataForm.mem" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
      <el-form-item label="时间" prop="date" >
         <el-date-picker
          v-model="dataForm.date"
          type="date"
          placeholder="时间"
          value-format="yyyy-MM-dd" style="width:98%">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="所在区块" prop="blocknum" v-if="!operation">
        <el-input v-model="dataForm.blocknum" auto-complete="off" maxlength="32" :disabled="!operation"></el-input>
      </el-form-item>
      <el-form-item label="图片上传" prop="spath" >
        <upload-img :model="'digitart' + dataForm.artid" :img-path="dataForm.spath" @fillpath="handleFillpath" :key="dataForm.artid"></upload-img>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

  <!--新增编辑界面-->
	<el-dialog title="批量新增" width="40%" :visible.sync="batchDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height:500px; overflow:auto"
			label-position="right">
			<el-form-item label="资产编号" prop="artid" v-if="!operation">
				<el-input v-model="dataForm.artid" :disabled="!operation" auto-complete="off"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16" placeholder="请输入客户编号"></el-input>
			</el-form-item>
      <el-form-item label="原始资产编号" prop="orgartid" v-if="!operation">
				<el-input v-model="dataForm.orgartid"  auto-complete="off" maxlength="16" placeholder="请输入原始资产编号"></el-input>
			</el-form-item>
      <el-form-item label="原始发行方" prop="orguserno"  v-if="!operation">
				<el-input v-model="dataForm.orguserno"  auto-complete="off" maxlength="16" placeholder="请输入原始发行方"></el-input>
			</el-form-item>
      <el-form-item label="商品" prop="wareno" >
        <el-select v-model="dataForm.wareno" style="width:98%" filterable placeholder="请选择商品">
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="业务分类" prop="bustype" >
        <el-select v-model="dataForm.bustype"  placeholder="请选择业务分类" style="width:98%">
          <el-option v-for="(item,index) in bustypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
    </el-form-item>
			<el-form-item label="编号规则" prop="cardpre" >
        <el-input v-model="dataForm.cardpre" auto-complete="off" placeholder="编号前缀"  maxlength="4" style="width:50%"></el-input>
				<el-input v-model.number="dataForm.cardlength" auto-complete="off" placeholder="编号长度"  maxlength="2" style="width:50%"></el-input>
			</el-form-item>
			<el-form-item label="卡券名称" prop="cardname">
				<el-input v-model="dataForm.cardname" auto-complete="off" maxlength="16" placeholder="请输入卡券名称"></el-input>
			</el-form-item>
      <el-form-item label="份数" prop="num" >
         <el-input v-model.number="dataForm.num" auto-complete="off" maxlength="16" placeholder="请输入份数"></el-input>
          <span style="color:red">输入份数N,选择目录下所有的文件，每个文件生成N份资产</span>
      </el-form-item>
        <el-form-item label="性别" prop="cardsex" >
         <el-select v-model="dataForm.cardsex" style="width:98%" placeholder="请选择性别">
           <el-option v-for="(item,index) in sexs" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
      <el-form-item label="材质" prop="cardrace" >
        <el-select v-model="dataForm.cardrace" style="width:98%" placeholder="请选择材质">
           <el-option v-for="(item,index) in cardraces" :key="index" :label="item" :value="item"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="等级" prop="cardgrade" >
        <el-select v-model="dataForm.cardgrade" style="width:98%" placeholder="请选择等级">
           <el-option v-for="(item,index) in cardgrades" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="arttype" >
        <el-select v-model="dataForm.arttype" style="width:98%" placeholder="请选择类型">
           <el-option v-for="(item,index) in arttypes" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="属性" prop="mem" >
        <el-input v-model="dataForm.mem" auto-complete="off" maxlength="128" placeholder="请输入属性"></el-input>
      </el-form-item>
      <el-form-item label="时间" prop="date" >
         <el-date-picker
          v-model="dataForm.date"
          type="date"
          placeholder="请输入时间"
          value-format="yyyy-MM-dd" style="width:98%">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="所在区块" prop="blocknum" v-if="!operation">
        <el-input v-model="dataForm.blocknum" auto-complete="off" maxlength="32" :disabled="!operation" placeholder="请输入所在区块"></el-input>
      </el-form-item>
      <el-form-item label="图片上传" prop="spath" >
        <upload-dir ref="uploadDir"></upload-dir>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="batchDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitFormBatch" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

<!--资产赠送-->
	<el-dialog title="赠送" width="40%" :visible.sync="sendDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" label-position="right">


      <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16" @blur="getUserName"></el-input>
			</el-form-item>
      <el-form-item label="客户名称" prop="username" >
				<el-input v-model="dataForm.username"  auto-complete="off" maxlength="16" disabled="true"></el-input>
			</el-form-item>
      <el-form-item label="操作类型" prop="wareno" >
          <el-select v-model="dataForm.optType" style="width:98%" filterable>
            <el-option label="发行" value="1"></el-option>
            <el-option label="转赠" value="2"></el-option>
          </el-select>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="sendDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitFormSend" :loading="editLoading">{{$t('action.submit')}}</el-button>
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
import UploadImg from '@/views/Trade/Compents/UploadImg'
import UploadDir from '@/views/Trade/Compents/UploadDir'
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadImg,
    UploadDir,
    ExportExcel
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        wareno: '',
        orguserno: '',
        chkhash: '',
        cardno: '',
        cardstatus: '',
        upstatus:'',
        bustype:''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      sendDialogVisible:false,
      batchDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        artid: [
          {required: true, message: '请输入资产编号', trigger: 'blur'}
        ],
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请选择商品', trigger: 'blur'}
        ],
        bustype: [
          {required: true, message: '请选择业务分类', trigger: 'blur'}
        ],
        cardno: [
          {required: true, message: '请输入卡券编号', trigger: 'blur'}
        ],
        cardname: [
          {required: true, message: '请输入卡券名称', trigger: 'blur'}
        ],
        cardsex: [
          {required: true, message: '请选择性别', trigger: 'blur'}
        ],
        cardrace: [
          {required: true, message: '请选择材质', trigger: 'blur'}
        ],
        cardgrade: [
          {required: true, message: '请选择等级', trigger: 'blur'}
        ],
        arttype: [
          {required: true, message: '请选择类型', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '请输入份数', trigger: 'blur'}
        ],
        cardpre: [
          {required: true, message: '请输入编号规则', trigger: 'blur'}
        ],
      },
      // 新增编辑界面数据
      dataForm: {
        artid: '',
        wareno:'',
        userno: '',
        cardno: '',
        cardname: '',
        cardsex: '',
        cardrace: '',
        cardstatus: '',
        chkhash:'',
        spath:'',
        date: '',
        mem: '',
        blocknum: '',
        txhash: '',
        perhash: '',
        num:'',
        orgartid:'',
        orguserno:'',
        bustype:''
      },
      cardstatuss: [
        {key:'0',val:'未发行'},
        {key:'1',val:'发行'},
        {key:'2',val:'持有'},
        {key:'3',val:'终止'},
        {key:'4',val:'冻结'},
        {key:'5',val:'锁定'},
      ],
      bustypes: [
        {key:'1',val:'销售'},
        {key:'2',val:'抽奖奖励'},
        {key:'3',val:'实名奖励'},
        {key:'4',val:'空投奖励'},
        {key:'5',val:'合成资产'}
      ],
      cardraces:[
        "金卡","银卡","铜卡","普通卡"
      ],
      sexs:[
        {key:'0',val:'男'},
        {key:'1',val:'女'}
      ],
      cardgrades:[
        {key:'1',val:'唯一'},
        {key:'2',val:'传奇'},
        {key:'3',val:'传说'},
        {key:'4',val:'史诗'},
        {key:'5',val:'罕见'},
        {key:'6',val:'稀有'},
        {key:'7',val:'常见'},
        {key:'8',val:'普通'}
      ],
      arttypes:[
        {key:'A01',val:'图片'},
        {key:'A02',val:'3D模型'},
        {key:'A03',val:'声音'}
      ],
      upstatuss:[
        {key:'0',val:'未上链'},
        {key:'1',val:'上链中'},
        {key:'2',val:'已上链'},
        {key:'3',val:'上链失败'}
      ],
      warenos:[],
      selections:[],
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'数字资产信息.xlsx'
      }
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
			this.$api.userdigitart.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params =  Object.assign({},this.filters)
      this.$api.userdigitart.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    handleFillpath(ret){
    this.dataForm.spath  = ret.url
      this.dataForm.chkhash = ret.code
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userdigitart.batchDelete(data.params).then(data!=null?data.callback:'')
		},
    selectionChange(selections){
      this.selections = selections.map(item=>{
        return {artid:item.artid}
      })
    },
    handleBatchIssue:function(){
      if(this.selections.length <= 0){
        this.$message.error('请选择发行数据！')
        return
      }
      this.$confirm('确认发行吗？', '提示', {}).then(() => {
        let para = this.selections
        this.$api.userdigitart.batchIssue(para).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleIssue: function(data){
	    this.$confirm('确认发行吗？', '提示', {}).then(() => {
        let para = data.row
        this.$api.userdigitart.issue(para).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleStop: function(data){
       this.$confirm('确认要终止吗？', '提示', {}).then(() => {
          let para = data.row
          this.$api.userdigitart.termination(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
    handleUp:function(data){
      this.$confirm('确认要上链吗？', '提示', {}).then(() => {
          let para = data.row
          this.$api.userdigitart.update(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
         artid: '',
        userno: '',
        cardno: '',
        cardname: '',
        cardsex: '',
        cardrace: '',
        cardstatus: '',
        chkhash:'',
        spath:'',
        date: '',
        mem: '',
        blocknum: '',
        txhash: '',
        perhash: '',
        cardgrade:'',
        arttype:'',
        num:'',
        wareno:'',
        orgartid:'',
        orguserno:'',
        bustype:''
      }
		},
    handleBatchAdd: function () {
			this.batchDialogVisible = true
			this.operation = true
			this.dataForm= {
         artid: '',
        userno: '',
        cardno: '',
        cardname: '',
        cardsex: '',
        cardrace: '',
        cardstatus: '',
        chkhash:'',
        spath:'',
        date: '',
        mem: '',
        blocknum: '',
        txhash: '',
        perhash: '',
        cardgrade:'',
        arttype:'',
        num:'',
        wareno:'',
        orgartid:'',
        orguserno:'',
        bustype:'',
        cardpre:'',
        cardlength:''
      }
		},
    handleSend: function () {
      if(this.selections.length <= 0){
        this.$message.error('请选择数据！')
        return
      }
			this.sendDialogVisible = true
			this.dataForm= {
        userno: '',
        username:'',
        optType:''
      }
		},
    handleClear(){
      this.$confirm('确认清理未发行资产吗？', '提示', {}).then(() => {
        this.$api.userdigitart.clear({}).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleBatchDel(){
      if(this.selections.length <= 0){
        this.$message.error('请选择删除数据！')
        return
      }
      this.$confirm('确认删除吗？', '提示', {}).then(() => {
        let para = this.selections
        this.$api.userdigitart.batchDel(para).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleBatchTermination(){
      if(this.selections.length <= 0){
        this.$message.error('请选择终止数据！')
        return
      }
      this.$confirm('确认要终止吗？', '提示', {}).then(() => {
        let para = this.selections
        this.$api.userdigitart.batchTermination(para).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleBatchlock(){
        if(this.selections.length <= 0){
        this.$message.error('请选择锁定数据！')
          return
        }
        this.$confirm('确认要锁定吗？', '提示', {}).then(() => {
          let para = this.selections
          this.$api.userdigitart.lock(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
    handleBatchUnlock(){
        if(this.selections.length <= 0){
        this.$message.error('请选择解锁数据！')
          return
        }
        this.$confirm('确认要解锁吗？', '提示', {}).then(() => {
          let para = this.selections
          this.$api.userdigitart.unlock(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
    getUserName(){
      if(this.dataForm.userno){
        let para = {
          userno: this.dataForm.userno
        }
        this.$api.firm.getById(para).then(res => {
          this.dataForm.username = res.data.username
        })
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
              this.$api.userdigitart.save(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                  this.findPage(null)
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
              })
            }else{
              this.$api.userdigitart.edit(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                  this.findPage(null)
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
              })
            }

					})
				}
			})
		},
    submitFormSend: function () {
			this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let artid = this.selections.map(item=>{
              return item.artid
            });
						let params = {
              USERNO:this.dataForm.userno,
              OPTTYPE:this.dataForm.optType,
              ARTID:artid.join(",")
            }
            this.$api.userdigitart.send(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.sendDialogVisible = false
                  this.$refs['dataForm'].resetFields()
                  this.findPage(null)
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
              })

					})
		},
submitFormBatch: function () {
      let datas = this.$refs.uploadDir.getData();
      if(datas.length <= 0 ){
        this.$message.error('请选择上传图片文件夹')
        return false
      }
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            params.spath = datas.join(',')
            this.$api.userdigitart.batchSave(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '操作成功', type: 'success' })
                this.$refs.uploadDir.clearData()
                this.batchDialogVisible = false
                this.$refs['dataForm'].resetFields()
                this.findPage(null)
              } else {
                this.$message({message: '操作失败, ' + res.msg, type: 'error'})
              }
            })
					})
				}
			})
		},
		// 是否有效格式化
  sexFormat: function (row, column, cellValue, index){
    if(cellValue==='0') {
      return "男"
    } else {
      return "女"
    }
	},
  statusFormat: function (row, column, cellValue, index){
    let item = this.cardstatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  gradeFormat: function (row, column, cellValue, index){
    let item = this.cardgrades.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  arttypeFormat: function (row, column, cellValue, index){
    let item = this.arttypes.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
   upstatusFormat: function (row, column, cellValue, index){
    let item = this.upstatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  bustypeFormat: function (row, column, cellValue, index){
    let item = this.bustypes.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
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
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"artid", label:"资产编号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
        {prop:"orgartid", label:"原始资产编号", minWidth:120},
        {prop:"orguserno", label:"原始发行方编号", minWidth:120},
				{prop:"orgusername", label:"原始发行方名称", minWidth:120},
        {prop:"wareno", label:"商品编号", minWidth:120},
        {prop:"warename", label:"商品名称", minWidth:120},
        {prop:"bustype", label:"业务分类", minWidth:100,formatter:this.bustypeFormat},
				{prop:"cardno", label:"卡券编号", minWidth:100},
				{prop:"cardname", label:"卡券名称", minWidth:120},
				{prop:"cardsex", label:"性别", minWidth:100,formatter:this.sexFormat},
				{prop:"cardrace", label:"材质", minWidth:100},
				{prop:"cardgrade", label:"等级", minWidth:100,formatter:this.gradeFormat},
				{prop:"arttype", label:"类型", minWidth:100,formatter:this.arttypeFormat},
				{prop:"cardstatus", label:"状态", minWidth:100,formatter:this.statusFormat},
				{prop:"date", label:"时间", minWidth:150},
				{prop:"mem", label:"属性", minWidth:100},
        {prop:"chkhash", label:"资产校验码", minWidth:150},
				{prop:"blocknum", label:"所在区块", minWidth:100},
				{prop:"txhash", label:"区块HASH", minWidth:150},
        {prop:"upstatus", label:"上链状态", minWidth:100,formatter:this.upstatusFormat},
				{prop:"errmg", label:"上链反馈", minWidth:150},
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
     this.buttons=[
		    {icon:'fa fa-edit',label:'发行',perms:'trade:digitart:issue',type:'success',callback:'handleIssue'},
		    {icon:'fa fa-edit',label:'终止',perms:'trade:digitart:issue',type:'warning',callback:'handleStop'},
        {icon:'fa fa-edit',label:'手工上链',perms:'trade:digitart:update',type:'danger',callback:'handleUp'},
      ];
  },
  initDict(){
    this.$api.ware.findList({}).then(res => {
      this.warenos = res.data
    })
  }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
    this.initDict()
	}
}
</script>

<style scoped>

</style>
