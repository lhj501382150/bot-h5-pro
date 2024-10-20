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
            <el-input v-model="filters.leveno" placeholder="等级"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.levname" placeholder="等级名称"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.status" placeholder="状态" style="width: 100%;" clearable>
              <el-option v-for="acct in statuss" :key="acct.key" :label="acct.val" :value="acct.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.rtype" placeholder="类型" style="width: 100%;" clearable>
              <el-option v-for="acct in rtypes" :key="acct.key" :label="acct.val" :value="acct.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:draworder:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="spread:draworder:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="spread:draworder:edit" permsDelete="spread:draworder:del"
		:data="pageResult" :columns="filterColumns" :show-operation="false"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>

  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
      size: 'small',
      filters: {
        levname:'',
        leveno:'',
        userno: '',
        status:'',
        rtype:''
      },
      columns: [],
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
      statuss:[
        {key:'1',val:'待发货'},
        {key:'2',val:'待收货'},
        {key:'3',val:'已完成'}
      ],
      rtypes:[
        {key:'0',val:'谢谢参与'},
        {key:'1',val:'虚拟值'},
        {key:'2',val:'实物'},
        {key:'3',val:'盲盒'},
        {key:'4',val:'卡牌'},
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
        'levname@like': this.filters.lname,
        'userno':this.filters.userno,
        'leveno':this.filters.leveno,
        'status':this.filters.status,
        'rtype':this.filters.rtype

			}
			this.$api.draworder.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.draworder.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
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
              this.$api.draworder.save(params).then((res) => {
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
              this.$api.draworder.edit(params).then((res) => {
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
  statusFormat: function (row, column, cellValue, index){
    let items = this.statuss.filter(function(item){
      return item.key == cellValue
    })
    if(items && items.length > 0){
      return items[0].val
    }
    return cellValue
  },
    rtypeFormat: function (row, column, cellValue, index){
      let items = this.rtypes.filter(function(item){
        return item.key == cellValue
      })
      if(items && items.length > 0){
        return items[0].val
      }
      return cellValue
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"id", label:"编号", minwidth:120},
				{prop:"userno", label:"客户编号", minwidth:120},
				{prop:"username", label:"客户名称", minwidth:120},
        {prop:"leveno", label:"等级", minwidth:120},
        {prop:"levname", label:"等级名称", minwidth:120},
        {prop:"rewinfo", label:"奖励物品", minwidth:120},
				{prop:"status", label:"状态", minwidth:120,formatter:this.statusFormat},
        {prop:"ctime", label:"时间", minwidth:150},
        {prop:"orddev", label:"终端标识", minwidth:100},
        {prop:"chkip", label:"IP地址", minwidth:100},
        {prop:"rtype", label:"类型", minwidth:120,formatter:this.rtypeFormat}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
	}
}
</script>

<style scoped>

</style>
