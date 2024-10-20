<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.loginno" placeholder="登陆编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.issuccess" placeholder="成功/失败" style="width: 100%;" clearable>
          <el-option label="成功" :value="1"></el-option>
          <el-option label="失败" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.inorout" placeholder="登陆/退出" style="width: 100%;" clearable>
          <el-option label="登陆" :value="1"></el-option>
          <el-option label="退出" :value="2"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:loginLog:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table
    :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format } from "@/utils/datetime"
export default {
	components: {
		KtTable,
		KtButton
	},
	data() {
		return {
			size: 'small',
			filters: {
				name: ''
			},
			columns: [
				{prop:"waterid", label:"流水号", minWidth:60},
				{prop:"loginno", label:"登陆编号", minWidth:100},
				{prop:"userno", label:"用户编号", minWidth:120},
				{prop:"detype", label:"终端类型", minWidth:120,formatter: this.detypeformat},
				{prop:"ipaddr", label:"IP", minWidth:120},
				{prop:"logtime", label:"登陆时间", minWidth:80},
        {prop:"inorout", label:"登陆状态", minWidth:100,formatter: this.inoroutformat},
        {prop:"issuccess", label:"状态", minWidth:100,formatter: this.issuccessformat},
				{prop:"memo", label:"备注", minWidth:120}
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false
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
			  'loginno':this.filters.loginno,
        'inorout':this.filters.inorout,
        'issuccess':this.filters.issuccess
			}
			this.$api.loginlog.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},

    issuccessformat:function (row, column, cellValue, index){
		 switch (cellValue) {
      case 1: return "成功";
      case 2: return "失败";
       default: return cellValue;
     }

    },
    inoroutformat:function (row, column, cellValue, index){
      switch (cellValue) {
        case 1: return "登陆";
        case 2: return "退出";
        default: return cellValue;
      }

    },
    detypeformat:function (row, column, cellValue, index){
      switch (cellValue) {
        case 1: return "PC";
        case 2: return "安卓";
        case 3: return "苹果";
        case 5: return "管理端";
        default: return cellValue;
      }

    },
    // 时间格式化
	dateFormat: function (row, column, cellValue, index){
	  return format(row[column.property])
	  }
	},
	mounted() {
	}
}
</script>

<style scoped>

</style>
