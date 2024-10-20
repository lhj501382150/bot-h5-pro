<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.optuser" placeholder="操作用户"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.method" placeholder="操作方法"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.params" placeholder="参数信息"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:log:view" type="primary" @click="findPage(null)"/>
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
        optuser: '',
        method: '',
        params:''

			},
			columns: [
				//{prop:"logId", label:"ID", minWidth:60},
				{prop:"optuser", label:"操作用户", minWidth:100},
				// {prop:"operation", label:"操作", minWidth:120},
				{prop:"method", label:"方法", minWidth:180},
				{prop:"params", label:"参数", minWidth:220},
				{prop:"ip", label:"IP", minWidth:120},
				{prop:"time", label:"耗时", minWidth:80},
				{prop:"lastUpdate", label:"创建时间", minWidth:120, formatter:this.dateFormat}
				// {prop:"lastUpdateBy", label:"更新人", minWidth:100},
				// {prop:"lastUpdateTime", label:"更新时间", minWidth:120, formatter:this.dateFormat}
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
			this.pageRequest.params = {'optuser@like':this.filters.optuser,'method@like':this.filters.method,'params@like':this.filters.params}
			this.$api.log.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
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
