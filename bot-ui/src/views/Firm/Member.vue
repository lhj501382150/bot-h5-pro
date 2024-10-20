<template>
  <div class="page-container">
	<!--工具栏-->
  <el-row>
    <el-col class="toolbar" :span="20">
      <el-form :inline="true" :model="filters" :size="size">
        <el-form-item>
          <el-input v-model="filters.userno" placeholder="用户信息" clearable></el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-input v-model="filters.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="filters.nickname" placeholder="用户昵称"></el-input>
        </el-form-item> -->
        <el-form-item>
          <el-input v-model="filters.openid" placeholder="邀请人信息" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="filters.balance" placeholder="余额(≥)" clearable ></el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-input v-model="filters.tjusername" placeholder="邀请人用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="filters.tjnickname" placeholder="邀请人用户昵称"></el-input>
        </el-form-item> -->
        <el-form-item>
          <el-select v-model="filters.sex" placeholder="状态" clearable>
            <el-option label="正常" value="Y"></el-option>
            <el-option label="冻结" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
        <el-select v-model="filters.orgtype" placeholder="类型" clearable>
          <el-option v-for="(item,index) in orgtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
        <el-select v-model="filters.clevel" placeholder="级别" clearable>
          <el-option v-for="(item,index) in clevels" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
			</el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:member:view" type="primary" @click="findPage(null)"/>
        </el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="firm:member:add" type="primary" @click="handleAdd" />
        </el-form-item>
         <el-form-item>
          <kt-button icon="fa fa-fa-trash" label="一键清空" perms="firm:member:clear" type="danger" @click="clean" />
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
      <table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns"
                                  @handleFilterColumns="handleFilterColumns">
      </table-column-filter-dialog>
    </el-col>
  </el-row>
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">

	</div>
	<div class="toolbar" style="float:right;padding-top:10px;padding-right:15px;">

	</div>
	<!--表格内容栏-->
	<kt-table permsEdit="firm:member:edit"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" @addMoney="addMoney" @subMoney="subMoney" @editPwd="editPwd"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete"  >
    <template #sex="scope">
          <div>
            <div class="title">
                <el-tag type="success" v-if="scope.row.sex==0">正常</el-tag>
                <el-tag type="warning" v-if="scope.row.sex==1">冻结</el-tag>
                <el-tag type="danger" v-if="scope.row.sex==2">停用</el-tag>
            </div>
            <div class="btn">
              <el-button type="text" size="mini" @click="changeEnable(scope.row,0)" v-if="scope.row.sex!=0">启用</el-button>
              <el-button type="text" size="mini" @click="changeEnable(scope.row,1)" v-if="scope.row.sex!=1">冻结</el-button>
              <el-button type="text" size="mini" @click="changeEnable(scope.row,2)" v-if="scope.row.sex!=2">停用</el-button>
            </div>
          </div>


    </template>
    <template #chmoney="scope">
          <el-input v-model="scope.row.chmoney" type="number" style="width: 98%;height:23px;"></el-input>
    </template>
    <template #userno="scope">
        <div style="text-align:left;">
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.userno }}</p>
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.username }}</p>
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.nickname }}</p>
        </div>
    </template>
    <template #openid="scope">
        <div style="text-align:left;" v-if="scope.row.openid">
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.openid }}</p>
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.tjusername }}</p>
          <p style="padding: 0;margin:0;height:18px;">{{ scope.row.tjnickname }}</p>
        </div>
    </template>
    <template #usertype="scope">
        <el-tag type="success" v-if="scope.row.usertype==1">开启</el-tag>
        <el-tag type="danger" v-else>关闭</el-tag>
    </template>
    <template #clevel="scope">
        <el-tag type="danger" v-if="scope.row.orgtype==1">
          {{ getClevel(scope.row.clevel) }}
        </el-tag>
        <el-tag v-if="scope.row.orgtype==2">普通会员</el-tag>
    </template>
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog_height"
			label-position="right">
			<el-form-item label="USERID" prop="userno">
				<el-input v-model="dataForm.userno" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="用户名称" prop="username">
				<el-input v-model="dataForm.username" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="昵称" prop="nickname">
				<el-input v-model="dataForm.nickname" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="类型" prop="orgtype">
        <el-select v-model="dataForm.orgtype" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in orgtypes" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item :label="dataForm.orgtype==2?'所属上级类型':'当前级别'" prop="clevel">
        <el-select v-model="dataForm.clevel" placeholder="请选择" style="width: 98%;" @change="findParent">
          <el-option v-for="(item,index) in clevels" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="dataForm.clevel > 1 || dataForm.orgtype==2" label="所属上级" prop="parentno">
        <el-select  v-model="dataForm.parentno" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in parentnos" :key="index" :label="item.username" :value="item.userno"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="邀请人USERID" prop="openid">
				<el-input v-model="dataForm.openid" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <!-- <el-form-item label="邀请权限" prop="usertype">
          <el-select v-model="dataForm.usertype" placeholder="邀请权限" style="width:98%">
            <el-option label="关闭" :value="0"></el-option>
            <el-option label="开启" :value="1"></el-option>
          </el-select>
        </el-form-item> -->
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  <el-dialog title="密码修改" width="40%" :visible.sync="updatePwdDialogVisible" :close-on-click-modal="false" :destroy-on-close="true"  v-dialogDrag>
		<el-form :model="updatePwdDataForm" label-width="120px" :rules="updatePwdDataFormRules" ref="updatePwdDataForm" :size="size" class="dialog_height"
			label-position="right">
			<el-form-item label="用户编号" prop="userno">
				<el-input v-model="updatePwdDataForm.userno" :disabled="true" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="新密码" prop="paypwd">
				<el-input type="password" v-model="updatePwdDataForm.paypwd" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="确认密码" prop="comfirmPwd">
				<el-input type="password" v-model="updatePwdDataForm.comfirmPwd" auto-complete="off"></el-input>
			</el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="updatePwdDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="updatePassword" :loading="updatePwdLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import {mapState} from 'vuex'
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
			filters: {
        fdate: '',
				userno: '',
        sex:'',
        openid: '',
        balance:'',
        orgtype:''
			},
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				userno: [
					{ required: true, message: '请输入用户编号', trigger: 'blur' }
				],
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        orgtype:[
          { required: true, message: '请选择类型', trigger: 'blur' }
        ],

			},
			// 新增编辑界面数据
			dataForm: {},
			roles: [],
      parentnos:[],
      orgtypes:[
        {key:1,val:'非会员'},
        {key:2,val:'普通会员'}
      ],
      clevels:[
        {key:1,val:'分公司'},
        {key:2,val:'股东'},
        {key:3,val:'总代理'},
        {key:4,val:'代理'}
      ],
      updatePwdDialogVisible: false,
      updatePwdLoading: false,
      updatePwdDataForm: {
			  userno: '',
        paypwd: '',
        comfirmPwd: ''
      },
      updatePwdDataFormRules: {
        userno: [
					{ required: true, message: '请输入用户编号', trigger: 'blur' }
				],
        paypwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        comfirmPwd: [
          { required: true, message: '请输入确认密码', trigger: 'blur' },
          {validator:(rule,value,callback)=>{
            if(value != this.updatePwdDataForm.paypwd){
              callback(new Error("两次密码输入不一致"))
            }else{
              callback()
            }
          },trigger:'blur'}
        ]
      },
      exportColumns:[],
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'客户信息.xlsx'
      },
      roles: [],
      pickerOptions: {
      }
		}
	},
	methods: {
    getClevel(level){
      return this.clevels.find(item=>item.key==level).val
    },
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
			let params = Object.assign({}, this.filters)
      if(params.clevel){
        params.orgtype =  1
      }
      delete params.fdate
      this.pageRequest.params = params
			this.$api.firm.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    clean(){
      this.$confirm('确认要一键清空吗？', '提示', {
              type: 'warning'
            }).then(() => {
              this.loading = true
              this.$api.firm.clean({}).then(res => {
                if(res.code == 200) {
                  this.$message({message: '操作成功', type:'success'})
                } else {
                  this.$message({message: '操作失败,'+ res.msg, type: 'error'})
                }
                this.loading = false
                this.findPage(null)
              })
            }).catch(() => {
            })
    },
    addMoney(data){
      if(data.row.chmoney <= 0){
        this.$message.error('请输入大于0的金额');
        return;
      }
      this.$confirm('确认要上分吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
         let para = {
          userno:data.row.userno,
          chmoney:data.row.chmoney
        }
        this.$api.firm.addMoney(para).then(res => {
          if(res.code == 200) {
            this.$message({message: '上分成功', type:'success'})
          } else {
            this.$message({message: '操作失败,'+ res.msg, type: 'error'})
          }
          this.loading = false
          setTimeout(() => {
            this.findPage(null)
          }, 2000);
        })
      }).catch(() => {
      })


    },
    subMoney(data){
      if(data.row.chmoney <= 0){
        this.$message.error('请输入大于0的金额');
        return;
      }
      this.$confirm('确认要下分吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
         let para = {
          userno:data.row.userno,
          chmoney:data.row.chmoney
        }
        this.$api.firm.subMoney(para).then(res => {
          if(res.code == 200) {
            this.$message({message: '下分成功', type:'success'})
          } else {
            this.$message({message: '操作失败,'+ res.msg, type: 'error'})
          }
          this.loading = false
          setTimeout(() => {
            this.findPage(null)
          }, 2000);
        })
      }).catch(() => {
      })
    },
    changeEnable(row,sex){
				let status = sex == 0 ? '启用':sex == 1 ? '冻结':'停用'
				 this.$confirm(`是否${status}?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(()=> {
					let para = {
						userno:row.userno,
            sex:sex
					}
          this.loading = true
					this.$api.firm.changeState(para).then(res => {
						if(res.code == 200) {
              this.$message({message: '操作成功', type: 'success'})
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.loading = false
            this.findPage(null)
					})
				})
    },
    editPwd(data){
      this.updatePwdDataForm.userno = data.row.userno
      this.updatePwdDialogVisible = true
    },
    exportExcel(){
    },
    //查询上级信息
    findParent(){
      this.$data.dataForm.parentno = ''
		  let level = this.dataForm.clevel
      if(level == '') return
      if(level > 14 || level < 1){
        this.$message.error("请输入正确的层级")
        return
      }
      if(!this.dataForm.orgtype){
        this.$message.error("请选择类型")
        return
      }
      let para = {}
        if(this.dataForm.orgtype==2){
          para = {clevel:level}
        }else{
          if(level > 1){
            para = {clevel:level-1}
          }else{
            return
          }
        }
        this.$api.firm.findParent(para).then(res =>{
            this.parentnos = res.data
        })
    },
    // 加载用户角色信息
		findUserRoles: function () {
		},
		// 批量删除
		handleDelete: function (data) {
      this.$confirm('确认删除该记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        let callback = res => {
          if(res.code == 200) {
            this.$message({message: '注销成功', type: 'success'})
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.loading = false
          this.findPage(null)
        }
        this.$api.firm.batchDelete(data.row).then(data!=null?callback:'')
      }).catch(() => {
      })

		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.initFormData()
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.firm.save(params).then((res) => {
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
              this.$api.firm.edit(params).then((res) => {
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
    // 修改密码
    updatePassword: function () {
      this.$refs.updatePwdDataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.updatePwdLoading = true
            let params = Object.assign({}, this.updatePwdDataForm)
            this.$api.firm.editPwd(params).then((res) => {
                this.updatePwdLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.updatePwdDialogVisible = false
                  this.$refs['updatePwdDataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
					})
				}
			})
    },
		// 是否有效格式化
    sexFormat: function (row, column, cellValue, index){
    if(cellValue==='0') {
      return '正常'
    } else {
      return "冻结"
    }
	},
    // 是否有效格式化
    orgtypeFormat: function (row, column, cellValue, index){
      switch (cellValue) {
        case 1: return "代理";
        case 2: return "普通用户";
        default: return cellValue;
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
    initFormData(){
		  this.dataForm = {
        userno: '',
        username: '',
        orgtype:'',
        clevel:'',
        parentno:''

      }
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"userno", label:"用户信息", width:120},
        // {prop:"orgtype", label:"类型", width:120,formatter:this.orgtypeFormat},
        {prop:"clevel", label:"类型", width:120},
        // {prop:"openid", label:"邀请人信息", width:120},
        {prop:"balance", label:"当前余额", width:100},
        {prop:"enable", label:"可用余额", width:100},
        {prop:"freeze", label:"冻结金额", width:100},
         {prop:"limtout", label:"已使用金额", width:100},
        {prop:"sex", label:"状态", width:100},
        // {prop:"usertype", label:"邀请权限", width:100},
        {prop:"chmoney", label:"变动金额", width:120},
        // {prop:"uno1", label:"所属代理", width:120},
        {prop:"parentno", label:"所属代理", width:120},

			]
			this.filterColumns = this.columns;

	},
    initButtons(){
		  this.buttons=[
         {icon:'fa fa-edit',label:'上分',perms:'firm:member:addMoney',type:'primary',callback:'addMoney'},
         {icon:'fa fa-edit',label:'下分',perms:'firm:member:subMoney',type:'danger',callback:'subMoney'},
         {icon:'fa fa-edit',label:'修改密码',perms:'firm:member:edit',type:'warning',callback:'editPwd'}
      ];
    },
    initData(){}
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	},
  computed:{
	  ...mapState({
      curUser: state=>state.user.curUser,
    })
  }
}
</script>

<style scoped>
.dialog_height{
  height: 400px;
  overflow: auto;
}
</style>
