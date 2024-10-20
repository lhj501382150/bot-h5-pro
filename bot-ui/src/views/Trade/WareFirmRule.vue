<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.sname" placeholder="规则名称" maxlength="16"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.rtype"  placeholder="类型" clearable>
              <el-option v-for="(item,index) in rtypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
        </el-form-item>
         <el-form-item>
            <el-select v-model="filters.istri"  placeholder="是否开启" clearable>
              <el-option v-for="(item,index) in istris" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
        </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:wareFirmRule:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:wareFirmRule:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="trade:wareFirmRule:edit" permsDelete="trade:wareFirmRule:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" :showOverflowTooltip="false"
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
				<el-input v-model="dataForm.mem" type="textarea" :rows="4" placeholder="请输入规则说明" ></el-input>
			</el-form-item>
       <el-form-item label="类型" prop="rtype">
            <el-select v-model="dataForm.rtype"  placeholder="请选择类型" style="width:98%">
              <el-option v-for="(item,index) in rtypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
        </el-form-item>
      <el-form-item label="推荐人单位" v-if="dataForm.rtype == 2">
				<el-input v-model="tabItem.WARENO"  placeholder="请输入推荐人单位" ></el-input>
			</el-form-item>
      <el-form-item label="最大数量" v-if="dataForm.rtype == 2">
				<el-input v-model.number="tabItem.NUM"  placeholder="请输入最大数量" ></el-input>
			</el-form-item>
      <el-form-item label="触发条件" v-if="dataForm.rtype != 2">
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
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="danger"  @click="delRow(scope.row,scope.$index)" size="small">删除</el-button>
              </template>
            </el-table-column>
         </el-table>
         <el-button type="primary" @click="addRow()" size="small">添加</el-button>
			</el-form-item>
      <el-form-item label="权益商品" prop="wareinfo" >
				<el-select v-model="dataForm.wareinfo" style="width:98%" multiple>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.title" :value="item.wareno"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item v-for="(item,index) in dataForm.giveinfo" :key="index" :label="item.paraname" >
        <el-input v-if="!item.listData" v-model="item.paraval"  auto-complete="off"></el-input>
        <el-select v-else v-model="item.paraval" style="width: 98%">
          <el-option v-for="opt in item.items" :key="opt.key" :value="opt.key" :label="opt.val"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="触发时间" prop="tritime" >
          <el-date-picker v-model="dataForm.tritime" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"  placeholder="选择日期时间" style="width:100%">
          </el-date-picker>
        </el-form-item>
      <el-form-item label="是否开启" prop="istri" >
            <el-select v-model="dataForm.istri"  placeholder="请选择" style="width:98%">
              <el-option v-for="(item,index) in istris" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
        </el-form-item>
         <el-form-item style="text-align:left">
            <span style="color:red;">时间类型必须符合格式：yyyy-MM-dd HH:mm:ss</span>
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
		return {
      size: 'small',
      filters: {
        sname: '',
        rtype:'',
        istri:''
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
          {required: true, message: '请选择权益商品', trigger: 'blur'}
        ],
        sname: [
          {required: true, message: '请输入规则名称', trigger: 'blur'}
        ],
        mem: [
          {required: true, message: '请输入规则说明', trigger: 'blur'}
        ],
        rtype: [
          {required: true, message: '请选择类型', trigger: 'blur'}
        ],
        istri: [
          {required: true, message: '请选择是否开启', trigger: 'blur'}
        ],
        tritime: [
          {required: true, message: '请输入触发时间', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        wareinfo: '',
        sname: '',
        mem: '',
        giveinfo: '',
        owareinfo: '',
        rtype: '',
        tritime: '',
        istri: ''
      },
      warenos:[],
      tabList:[],
      tabItem:{},
      paras:[],
      rtypes:[
        {key:'1',val:'持有资产'},
        {key:'2',val:'推荐人数'},
        {key:'3',val:'购买送权益'},
        {key:'4',val:'导入送权益'}
      ],
      istris:[
        {key:'Y',val:'开启'},
        {key:'N',val:'未开启'}
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
			this.pageRequest.params = {
        'sname@LIKE':this.filters.sname,
        rtype: this.filters.rtype,
        istri: this.filters.istri
			}
			this.$api.wareFirmRule.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findWare:function(){
      let para = {}
      this.$api.wareFirmRule.findWare(para).then(res=>{
        this.warenos = res.data
      })
    },
    findPara(){
		  let para = {}
      this.$api.wareFirmPara.findParalist(para).then(res =>{
        this.paras = res.data
      })
    },
    addRow(){
      this.tabList.push({WARENO:'',NUM:''})
    },
    delRow(row,index){
      this.tabList.splice(index,1)
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.wareFirmRule.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
      this.tabList = []
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
         id: 0,
        wareinfo: '',
        sname: '',
        mem: '',
        giveinfo: this.paras,
        owareinfo: ''
      }
		},

		// 显示编辑界面
		handleEdit: function (params) {
      this.dialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.tabList = JSON.parse(this.dataForm.owareinfo);
      if(this.dataForm.rtype == 2 && this.tabList.length > 0){
        this.tabItem = this.tabList[0]
      }
      let wareinfo = JSON.parse(this.dataForm.wareinfo);
      this.dataForm.wareinfo = wareinfo.map(item => {
        return item.WARENO
      })
      let paras = this.paras
      if(this.dataForm.giveinfo){
        let para = JSON.parse(this.dataForm.giveinfo);
        for(var i=0;i<paras.length;i++){
          for(var j =0;j<para.length ;j++){
            if(paras[i].parano == para[j].PARANO){
              para[j] = {
                parano: paras[i].parano,
                paraname: paras[i].paraname,
                paraval: para[j].PARAVAL,
                listData: paras[i].listData,
                items: paras[i].items
              }
            }
          }
        }
        this.dataForm.giveinfo = para
      }else{
        this.dataForm.giveinfo = paras
      }
		},
		// 编辑
		submitForm: function () {

      if(this.dataForm.rtype != 2 && this.tabList.length <= 0){
        this.$message.error("请设置触发条件")
        return
      }
      if(this.dataForm.rtype == 2){
        this.tabList = []
        this.tabList.push(this.tabItem)
      }
      this.dataForm.owareinfo = JSON.stringify(this.tabList);
      let wareinfo = this.dataForm.wareinfo.map(item => {
        return {WARENO:item}
      })
      this.dataForm.wareinfo = JSON.stringify(wareinfo);
      let para = this.dataForm.giveinfo.map(item => {
        return {PARANO:item.parano,PARAVAL:item.paraval}
      })
      this.dataForm.giveinfo = JSON.stringify(para);
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.wareFirmRule.save(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                  this.tabList = []
                  this.findPage(null)
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
              })
            }else{
              this.$api.wareFirmRule.edit(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.dataForm = {}
                  this.tabList = []
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
		//
  tertypeFormat: function (row, column, cellValue, index){
    let val = ""
    try{
      let arr = JSON.parse(cellValue)
      for(var i=0;i<arr.length;i++){
        if(row.rtype == 2){
           val += "推荐人单位：" +arr[i].WARENO + "\n" + ",最大数量：" + arr[i].NUM
        }else{
          val += "商品编号：" +arr[i].WARENO + ",数量：" + arr[i].NUM + '\n'
        }
      }
    }catch(e){
      val = cellValue
    }

    return val
	},
  wareFormat: function (row, column, cellValue, index){
    let val = ""
    try{
      let arr = JSON.parse(cellValue)
      for(var i=0;i<arr.length;i++){
        val += "商品编号：" +arr[i].WARENO + '\n'
      }
    }catch(e){
      val = cellValue
    }

    return val
	},
  giveFormat: function (row, column, cellValue, index){
    let val = ""
    try{
      let arr = JSON.parse(cellValue)
      for(var i=0;i<arr.length;i++){
        let para = this.paras.filter(item => {
          return item.parano == arr[i].PARANO
        })
        if(para && para.length > 0){
          val += para[0].paraname +":"
          if(para[0].listData){

            let a = para[0].items.filter(t =>{
              return t.key == arr[i].PARAVAL
            })
            if(a && a.length > 0){
              val += a[0].val + '\n'
            }else{
              val += arr[i].PARAVAL + '\n'
            }
          }else{
            val += arr[i].PARAVAL + '\n'
          }
        }
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
  rtypeFormat: function (row, column, cellValue, index){
    let item = this.rtypes.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  istriFormat: function (row, column, cellValue, index){
    let item = this.istris.filter(function(s){
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
        {prop:"sname", label:"规则名称", minWidth:120},
        {prop:"mem", label:"规则说明", minWidth:180},
        {prop:"rtype", label:"类型", minWidth:120,formatter:this.rtypeFormat},
				{prop:"owareinfo", label:"触发条件", minWidth:160,formatter:this.tertypeFormat},
        {prop:"wareinfo", label:"权益商品", minWidth:120,formatter:this.wareFormat},
        {prop:"giveinfo", label:"权益明细", width:250,formatter:this.giveFormat},
        {prop:"tritime", label:"触发时间", minWidth:180},
        {prop:"istri", label:"是否开启", minWidth:160,formatter:this.istriFormat}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
    this.findWare()
    this.findPara()
	}
}
</script>

<style scoped>

</style>
