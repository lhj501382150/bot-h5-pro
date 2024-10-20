<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.userno" placeholder="主持人" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.actname" placeholder="赛事活动名称" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.fmttype" placeholder="赛制分类" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in fmttypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.astatus" placeholder="活动状态" style="width: 100%;" clearable>
              <el-option v-for="(item,index) in astatuss" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:activity:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:activity:add" type="primary" @click="handleAdd" />
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" label="类似新增" perms="gm:activity:addLike" type="primary" @click="handleAddLike" />
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
	<kt-table permsEdit="gm:activity:edit" permsDelete="gm:activity:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @handleAudit="handleAudit">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
			<el-form-item label="赛事活动编号" prop="actno" v-show="!operation">
				<el-input v-model="dataForm.actno" :disabled="!operation"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="主持人" prop="userno" >
        <el-input v-model="dataForm.userno" placeholder="请输入主持人编号" maxlength="16"></el-input>
      </el-form-item>
			<el-form-item label="赛事活动名称" prop="actname" >
				<el-input v-model="dataForm.actname" placeholder="请输入赛事活动名称"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="游戏分类" prop="c1cno">
        <el-select v-model="dataForm.c1cno" placeholder="游戏分类" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in c1cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="比赛类型分类" prop="c2cno">
        <el-select v-model="dataForm.c2cno" placeholder="比赛类型分类" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in c2cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="端游类型" prop="tertype">
        <el-select v-model="dataForm.tertype" placeholder="端游类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in tertypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="赛制分类" prop="fmttype">
        <el-select v-model="dataForm.fmttype" placeholder="端游类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in fmttypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="门票" prop="wareno">
        <el-select v-model="dataForm.wareno" placeholder="门票" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item label="赛事报名时间" prop="sigdtme">
        <el-date-picker v-model="dataForm.sigdtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" placeholder="选择赛事报名时间">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="赛事准备时间" prop="predtme">
        <el-date-picker v-model="dataForm.predtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"  placeholder="选择赛事准备时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="赛事开始时间" prop="sdtme">
        <el-date-picker v-model="dataForm.sdtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"  placeholder="选择赛事开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="赛道长度" prop="roomlink">
        <el-input type="textarea" v-model="dataForm.roomlink" placeholder="请输入赛道长度" maxlength="512"></el-input>
      </el-form-item>
      <el-form-item label="奖励方式" prop="rewtype">
        <el-select v-model="dataForm.rewtype" placeholder="奖励方式" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in rewtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="规则说明" prop="rule">
        <el-input type="textarea" v-model="dataForm.rule" placeholder="请输入规则说明"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="seq">
        <el-input v-model.number="dataForm.seq" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="是否免门票" prop="isdicket">
        <el-select v-model="dataForm.isdicket" placeholder="是否免门票" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in isdickets" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动状态" prop="astatus" v-show="!operation">
        <el-select v-model="dataForm.astatus" placeholder="活动状态" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in astatusss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="赛道背景图" prop="picpath" v-show="!operation">
        <upload-file :model="'activity' + dataForm.actno" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading" v-show="optbtn">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
<el-dialog title="类似新增" width="40%" :visible="true" v-if="addDialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height: 380px;overflow: auto;"
			label-position="right">
			<el-form-item label="赛事活动" prop="actno" >
				<el-select v-model="actno" placeholder="请选择活动" style="width: 98%;" @change="changeActivity">
            <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
          </el-select>
			</el-form-item>
      <el-form-item label="主持人" prop="userno" >
        <el-input v-model="dataForm.userno" placeholder="请输入主持人编号" maxlength="16"></el-input>
      </el-form-item>
			<el-form-item label="赛事活动名称" prop="actname" >
				<el-input v-model="dataForm.actname" placeholder="请输入赛事活动名称"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="游戏分类" prop="c1cno">
        <el-select v-model="dataForm.c1cno" placeholder="游戏分类" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in c1cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="比赛类型分类" prop="c2cno">
        <el-select v-model="dataForm.c2cno" placeholder="比赛类型分类" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in c2cnos" :key="index" :label="item.cname" :value="item.cno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="端游类型" prop="tertype">
        <el-select v-model="dataForm.tertype" placeholder="端游类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in tertypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="赛制分类" prop="fmttype">
        <el-select v-model="dataForm.fmttype" placeholder="端游类型" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in fmttypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="门票" prop="wareno">
        <el-select v-model="dataForm.wareno" placeholder="门票" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item label="赛事报名时间" prop="sigdtme">
        <el-date-picker v-model="dataForm.sigdtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" placeholder="选择赛事报名时间" @blur="countTime">
        </el-date-picker>
			</el-form-item>
      <el-form-item label="赛事准备时间" prop="predtme">
        <el-date-picker v-model="dataForm.predtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"  placeholder="选择赛事准备时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="赛事开始时间" prop="sdtme">
        <el-date-picker v-model="dataForm.sdtme" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"  placeholder="选择赛事开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="赛道长度" prop="roomlink">
        <el-input type="textarea" v-model="dataForm.roomlink" placeholder="请输入赛道长度" maxlength="512"></el-input>
      </el-form-item>
      <el-form-item label="奖励方式" prop="rewtype">
        <el-select v-model="dataForm.rewtype" placeholder="奖励方式" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in rewtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="规则说明" prop="rule">
        <el-input type="textarea" v-model="dataForm.rule" placeholder="请输入规则说明"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="seq">
        <el-input v-model.number="dataForm.seq" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="是否免门票" prop="isdicket">
        <el-select v-model="dataForm.isdicket" placeholder="是否免门票" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in isdickets" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动状态" prop="astatus" v-show="!operation">
        <el-select v-model="dataForm.astatus" placeholder="活动状态" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in astatusss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="赛道背景图" prop="picpath" v-show="!operation">
        <upload-file :model="'activity' + dataForm.actno" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="addDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading" v-show="optbtn">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
    <el-dialog title="赛事结果" width="50%" :visible="true" v-if="resultDialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
      <el-table :data="dtls" style="width: 98%" border height="300" align="center">
        <el-table-column prop="userno" label="参赛人员"  width="180">  </el-table-column>
        <el-table-column prop="seq" label="名次"  width="100">  </el-table-column>
        <el-table-column prop="vorf" label="胜/负"  width="100" :formatter="vorfFormat">  </el-table-column>
        <el-table-column prop="rewmn" label="奖励金额"  width="120">  </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="resultDialogVisible = false">{{$t('action.cancel')}}</el-button>
        <el-button :size="size" type="primary" @click.native="submitForm2" :loading="editLoading">{{$t('action.submit')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import UploadFile from "@/views/Trade/Compents/UploadFile";
import { format,subTime } from "@/utils/datetime"
export default {
	components:{
    UploadFile,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        actname: '',
        fmttype: '',
        astatus:''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      addDialogVisible:false,
      resultDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        userno: [
          {required: true, message: '请输入主持人编号', trigger: 'blur'}
        ],
        actname: [
          {required: true, message: '请输入赛事活动名称', trigger: 'blur'}
        ],
        c1cno: [
          {required: true, message: '请选择游戏分类', trigger: 'blur'}
        ],
        c2cno: [
          {required: true, message: '请选择比赛类型分类', trigger: 'blur'}
        ],
        tertype: [
          {required: true, message: '请选择端游类型', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请选择门票', trigger: 'blur'}
        ],
        sigdtme: [
          {required: true, message: '请输入赛事报名时间', trigger: 'blur'}
        ],
        predtme: [
          {required: true, message: '请输入赛事准备时间', trigger: 'blur'}
        ],
        sdtme: [
          {required: true, message: '请输入赛事开始时间', trigger: 'blur'}
        ],
        roomlink: [
          {required: true, message: '请输入赛道长度', trigger: 'blur'}
        ],
        rewtype: [
          {required: true, message: '请选择奖励方式', trigger: 'blur'}
        ],
        rule: [
          {required: true, message: '请输入规则说明', trigger: 'blur'}
        ],
        isdicket: [
          {required: true, message: '请选择是否免门票', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        actno: '',
        userno: '',
        actname: '',
        c1cno: '',
        c2cno: '',
        tertype: '',
        fmttype: '',
        wareno: '',
        sigdtme: '',
        predtme: '',
        sdtme: '',
        roomlink: '',
        rewtype: '',
        rule: '',
        seq: 1,
        isdicket: '',
        astatus: '',
        picpath: ''
      },
      c1cnos:[],
      c2cnos:[],
      tertypes:[
        {key:1,val:'微信区'},
        {key:2,val:'QQ区'}
      ],
      fmttypes:[
        {key:1,val:'单排'},
        {key:2,val:'双排'},
        {key:3,val:'战队赛'},
        {key:4,val:'和平精英四排'},
        {key:5,val:'五排'}
      ],
      warenos:[],
      rewtypes:[
        {key:1,val:'按人头'},
        {key:2,val:'按排名'}
      ],
      isdickets:[
        {key:0,val:'不免'},
        {key:1,val:'免'}
      ],
      astatuss:[
        {key:0,val:'正常'},
        {key:1,val:'结束'},
        {key:2,val:'已发放'}
      ],
      astatusss:[
        {key:0,val:'正常'},
        {key:1,val:'结束'},
      ],
      dtls:[],
      optbtn:true,
      acts: [],
      actno:'',
      temp:{}
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
			this.pageRequest.params = {
			  'userno':this.filters.userno,
        'actname@LIKE':this.filters.actname,
        'fmttype':this.filters.fmttype,
        'astatus':this.filters.astatus
			}
			this.$api.activity.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
        this.findWareClass1()
        this.findWareClass2()
        this.findWare()
			}).then(data!=null?data.callback:'')
		},
    handleFillpath(path){
      this.dataForm.picpath = path
    },
    //查询分类
    findWareClass1:function(){
		  let para = {sysblkno:'02',clevel:1}
		  this.$api.wareclass.findList(para).then(res=>{
          this.c1cnos = res.data
      })
    },
    findWareClass2:function(){
      let para = {sysblkno:'02',clevel:2}
      this.$api.wareclass.findList(para).then(res=>{
        this.c2cnos = res.data
      })
    },
    findWare:function(){
      let para = {sysblkno:'02'}
      this.$api.ware.findList(para).then(res=>{
        this.warenos = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.activity.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
      this.optbtn = true
			this.dataForm= {
        actno: '',
        userno: '',
        actname: '',
        c1cno: '',
        c2cno: '',
        tertype: 1,
        fmttype: 1,
        wareno: '',
        sigdtme: '',
        predtme: '',
        sdtme: '',
        roomlink: '',
        rewtype: 1,
        rule: '',
        seq: 1,
        isdicket: 0,
        astatus: 0,
        picpath: ''
      }
		},
    handleAddLike: function () {
			this.addDialogVisible = true
			this.operation = true
      this.optbtn = true
      this.actno = ''
      this.dataForm = {
        actno: '',
        userno: '',
        actname: '',
        c1cno: '',
        c2cno: '',
        tertype: 1,
        fmttype: 1,
        wareno: '',
        sigdtme: '',
        predtme: '',
        sdtme: '',
        roomlink: '',
        rewtype: 1,
        rule: '',
        seq: 1,
        isdicket: 0,
        astatus: 0,
        picpath: ''
      }
      this.$api.activity.findList().then(res=>{
	      this.acts = res.data
      })
		},
    changeActivity:function(val){
      this.temp = {}
      let arr = this.acts.filter(item => {
          return item.actno === val
        })
        if(arr && arr.length > 0){
          this.dataForm = {...arr[0]}
          this.temp.sigdtme = this.dataForm.sigdtme
          this.temp.predtme = this.dataForm.predtme
          this.temp.sdtme = this.dataForm.sdtme
        }else{
          this.$refs['dataForm'].resetFields()
        }
    },
    countTime:function(){
      var now = this.dataForm.sigdtme
      this.dataForm.predtme = subTime(this.temp.predtme,this.temp.sigdtme,now)
      this.dataForm.sdtme = subTime(this.temp.sdtme,this.temp.sigdtme,now)
    },
		// 显示编辑界面
		handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      if(this.dataForm.astatus == 2){
        this.optbtn = false
      }else{
        this.optbtn = true
      }
      this.findWare()
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.activity.save(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.addDialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }else{
              this.$api.activity.edit(params).then((res) => {
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
    handleAudit:function(params){
		  if(params.row.astatus == 0){
		    this.$message({message:'活动尚未结束！',type:'error'})
        return;
      }
      if(params.row.astatus == 2){
        this.$message({message:'活动奖励已发放！',type:'error'})
        return;
      }
      let para = {
        actno:params.row.actno
      }
      this.$api.userlist.findList(para).then(res =>{
          this.dtls = res.data
      })
      this.resultDialogVisible = true
      this.dataForm = Object.assign({},params.row)
    },
    // 审核
    submitForm2: function () {
      this.$confirm('确认提交吗？', '提示', {}).then(() => {
        this.editLoading = true
        let params = Object.assign({}, this.dataForm)
        this.$api.activity.audit(params).then((res) => {
          this.editLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
            this.resultDialogVisible = false
            this.$refs['dataForm'].resetFields()
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })

      })
    },
		//
  tertypeFormat: function (row, column, cellValue, index){
    let t = this.tertypes.filter(function(item){
      return item.key == cellValue
    });
    let val = cellValue;
    if(t) val = t[0].val
    return val
	},
    fmttypeFormat: function (row, column, cellValue, index){
      let t = this.fmttypes.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t) val = t[0].val
      return val
    },
    rewtypeFormat: function (row, column, cellValue, index){
      let t = this.rewtypes.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length > 0) val = t[0].val
      return val
    },
    isdicketFormat: function (row, column, cellValue, index){
      let t = this.isdickets.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length > 0) val = t[0].val
      return val
    },
    astatusFormat: function (row, column, cellValue, index){
      let t = this.astatuss.filter(function(item){
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
        {prop:"userno", label:"主持人编号", minWidth:120},
        {prop:"username", label:"主持人名称", minWidth:120},
				{prop:"c1name", label:"游戏分类", minWidth:100},
				{prop:"c2name", label:"比赛类型分类", minWidth:120},
				{prop:"tertype", label:"端游类型", minWidth:100,formatter:this.tertypeFormat},
				{prop:"fmttype", label:"赛制分类", minWidth:100,formatter:this.fmttypeFormat},
        {prop:"warename", label:"门票", minWidth:120},
        {prop:"sigdtme", label:"赛事报名时间", minWidth:180},
        {prop:"predtme", label:"赛事准备时间", minWidth:180},
        {prop:"sdtme", label:"赛事开始时间", minWidth:180},
        {prop:"roomlink", label:"赛道长度", minWidth:120},
        {prop:"rewtype", label:"奖励方式", minWidth:100,formatter:this.rewtypeFormat},
        {prop:"seq", label:"排序", minWidth:120},
        {prop:"isdicket", label:"是否免门票", minWidth:100,formatter:this.isdicketFormat},
				{prop:"astatus", label:"活动状态", minWidth:100,formatter:this.astatusFormat}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-check',label:'结果审核',type:'warning',perms:'gm:activity:audit',callback:'handleAudit'}
      ];
    },
    vorfFormat: function (row, column, cellValue, index){
		  let vorfs = [
        {key:0,val:'胜'},
        {key:1,val:'负'}
      ]
      let t = vorfs.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t && t.length > 0) val = t[0].val
      return val
    }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
