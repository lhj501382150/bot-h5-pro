<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-date-picker
              v-model="filters.fdate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.orgno" placeholder="USERID"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.pstatus" placeholder="状态" style="width: 100%;" clearable>
              <el-option v-for="item in statuss" :key="item.key" :label="item.name" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="reward:orghandle:view" type="primary" @click="findPage(null)"/>
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

	<!--表格内容栏-->
	<jl-table :data="pageResult" :columns="filterColumns" permsEdit="reward:orghandle:edit" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleBatch="handleBatch" batchlabel="批量审核">
    <template #btime="scope"> 
      {{ scope.row.btime }}<br/>
      {{ scope.row.etime }}
    </template>
    <template #clevel="scope"> 
      <el-tag type="warning" v-if="scope.row.clevel==1">一级</el-tag>
      <el-tag type="success" v-if="scope.row.clevel==2">二级</el-tag>
      <el-tag type="danger" v-if="scope.row.clevel==3">三级</el-tag>
    </template>
	</jl-table>

  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import JlTable from "@/views/Reward/components/JlTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { format } from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
    JlTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        fdate: '',
        orgno: '',
        username:'',
        pstatus: ''
      },
      columns: [],
      buttons: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {

      },
      // 新增编辑界面数据
      dataForm: {

      },
      statuss: [
        {'key':'0','name':'未审核'},
        {'key':'1','name':'已审核'},
        {'key':'2','name':'审核失败'},
      ],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
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
        'pdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'pdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'pstatus':this.filters.pstatus,
        'orgno':this.filters.userno,
        'username@like': this.filters.username
			}
			this.$api.orghandle.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    selectionChange:function(params){
		  console.log(params)
    },
    handleBatch:function(ids){
      let params = []
      for(var i=0; i<ids.length; i++) {
        params.push({'waterno':ids[i].waterno})
      }
      this.handleEdit(params)
    },
		// 显示编辑界面
		handleEdit: function (para) {
      this.$confirm('确认审核选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$api.orghandle.audit(para).then(res=>{
          if(res.code == 200) {
            this.$message({ message: '审核成功', type: 'success' })
          } else {
            let msg = res.msg.split("\r\n");
            let newDatas = [];
            const h = this.$createElement;
            for (let i in msg) {
              if(msg[i]){
                newDatas.push(h('p', null, msg[i]));
              }
            }
            this.$message({
              title: '错误提示',
              message: h('div', null, newDatas),
              type: 'error'
            });
            //this.$message.error(res.msg)
          }
          this.findPage(null)
        });
      })
		},
		// 是否有效格式化
  statusFormat: function (row, column, cellValue, index){
		  let para = ['未审核','已审核','审核失败'];
    return para[cellValue]
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
        {prop:"pdate", label:"办理日期", minWidth:120},
        {prop:"btime", label:"返还时间", minWidth:150},
        {prop:"orgno", label:"用户ID", minWidth:120},
        {prop:"username", label:"用户名", minWidth:120},
        {prop:"nickname", label:"用户昵称", minWidth:120},
        {prop:"comms", label:"手续费合计", minWidth:100},
        {prop:"commbonus", label:"推荐奖励", minWidth:100},
        {prop:"clevel", label:"推荐层级", minWidth:100},
				{prop:"pstatus", label:"审核状态", minWidth:100,formatter:this.statusFormat},
        {prop:"chkuno", label:"审核人", minWidth:100},
        {prop:"chktime", label:"审核时间", minWidth:150},
        {prop:"chkip", label:"审核IP", minWidth:100},
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-check',label:'审核',perms:'reward:orghandle:edit',type:'warning',callback:'handleEdit',show:0}
      ];
    },
	},
	mounted() {
		this.initColumns(),
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
