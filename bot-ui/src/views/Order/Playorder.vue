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
        <el-input v-model="filters.userno" placeholder="陪玩账号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.gmstatus" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in gmstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="order:contract:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="order:contract:view" type="primary" @click="exportExcel()"/>
      <ExportExcel ref="export" :columns="filterColumns" :table-data="exportData" :info="exportInfo"></ExportExcel>
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
	<kt-table
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
			filters: {
			  fdate:'',
				userno: '',
        gmstatus:'',
			},
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
      gmstatuss:[
        {key:'0',val:'未使用'},
        {key:'1',val:'已使用'},
        {key:'2',val:'已撤销'},
        {key:'3',val:'已完成'}
      ],
      dataForm:{},
			dataFormRules: {

			},
      exportInfo:{
			  id:'record-table',
        name:'陪玩订单.xlsx'
      },
      exportData:[],
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
			this.pageRequest.params = {
        'fdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'fdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        gmstatus:this.filters.gmstatus,
        userno: this.filters.userno
      }
			this.$api.playorder.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    handleEdit: function (data){
		  if(data.row.gmstatus != 1){
        this.$message({message: '请确认订单是否已使用！', type: 'error'})
		    return ;
      }
      this.$confirm('确认提交吗？', '提示', {}).then(() => {
        let params = data.row
        this.$api.playorder.edit(params).then((res) => {
          this.editLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
            this.dataForm = {}
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })

      })
    },
    handleDelete: function (data){
		  if(data.row.gmstatus != 1){
        this.$message({message: '请确认订单是否已使用！', type: 'error'})
		    return ;
      }
      this.$confirm('确认要作废吗？', '提示', {}).then(() => {
        let params = data.row
        this.$api.playorder.del(params).then((res) => {
          this.editLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
            this.dataForm = {}
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })

      })
    },
    exportExcel(){
      let params = Object.assign({}, this.filters)
      let para = {
        pageSize:1000000,
        pageNum: 1,
        params: params
      }
      this.$api.playorder.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })

    },
		// 是否有效格式化
  statusformat: function (row, column, cellValue, index){
    switch (cellValue) {
      case "0" : return "未使用"
      case "1" : return "已使用"
      case "2" : return "已撤销"
      case "3" : return "已完成"
      case "4" : return "已作废"
       default: return cellValue
    }
  },
    sexformat: function (row, column, cellValue, index){
      switch (cellValue) {
        case "0" : return "男"
        case "1" : return "女"
        default: return cellValue
      }
    },
    gmtypeformat: function (row, column, cellValue, index){
     var  val = '';
     if(cellValue.indexOf('01')>-1){
       val += ' 王者荣耀 '
     }
      if(cellValue.indexOf('02')>-1){
        val += ' 和平精英 '
      }
      return val;
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
        {prop:"waterno", label:"订单编号", minWidth:120},
        {prop:"fdate", label:"订单日期", minWidth:120},
        {prop:"userno", label:"陪玩账号", minWidth:100},
        {prop:"username", label:"陪玩名称", minWidth:120},
        {prop:"comm", label:"服务费", minWidth:80},
        {prop:"tcomm", label:"平台服务费", minWidth:100},
        {prop:"sex", label:"性别", minWidth:60,formatter: this.sexformat},
        {prop:"age", label:"年龄", minWidth:60},
        {prop:"gmtype", label:"擅长游戏", minWidth:180,formatter: this.gmtypeformat},
        {prop:"step", label:"时长", minWidth:80},
        {prop:"title", label:"标签", minWidth:120},
        {prop:"gmstatus", label:"状态", minWidth:100,formatter: this.statusformat},
        {prop:"requserno", label:"接单账号", minWidth:120},
        {prop:"reqtime", label:"接单时间", minWidth:150},
        {prop:"reqaddr", label:"接单地址", minWidth:120}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-edit',label:'完成',type:'danger',perms:'order:playorder:edit',callback:'handleEdit'},
        {icon:'fa fa-times',label:'作废',type:'warning',perms:'order:playorder:del',callback:'handleDelete'}
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

</style>
