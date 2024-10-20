<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-date-picker
					v-model="filters.fdate"
					type="datetimerange"
					align="right"
					unlink-panels
					range-separator="至"
					start-placeholder="开始时间"
					end-placeholder="结束时间"
					value-format="yyyy-MM-dd HH:mm:ss">
				</el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-input v-model="filters.account" placeholder="操作用户"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.requestUrl" placeholder="操作方法"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.body" placeholder="参数信息"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:h5log:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table  :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
		<template #responseBody="scope">
			<el-tag type="success" v-if="scope.row.outPara.iCode==0">成功</el-tag>
			<el-tag type="danger" v-else>{{ scope.row.outPara.sMsg }}</el-tag>
		</template>
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
				fdate:[],
				account: '',
				requestUrl: '',
				body:''

			},
			columns: [
				{prop:"logNo", label:"ID", minWidth:60},
				{prop:"account", label:"操作用户", minWidth:100},
				{prop:"requestUrl", label:"方法", minWidth:180},
				{prop:"httpType", label:"IP地址", minWidth:150},
				{prop:"body", label:"参数", minWidth:220},
				{prop:"responseBody", label:"反馈数据", minWidth:120},
				{prop:"elapsedTime", label:"耗时", minWidth:80},
				{prop:"startTime", label:"开始时间", minWidth:160},
				{prop:"endTime", label:"结束时间", minWidth:160}
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
				'startTime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        		'startTime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
				'account@like':this.filters.account.trim(),
				'requestUrl@like':this.filters.requestUrl,
				'body@like':this.filters.body
			}
			this.$api.h5log.findPage(this.pageRequest).then((res) => {
				let datas = res.data
				if(datas.content && datas.content.length > 0){
					datas.content.forEach(item => {
						try {
							if(item.body){
								item.inPara = JSON.parse(item.body)
							}
							if(item.responseBody){
								item.outPara = JSON.parse(item.responseBody)
							}
						} catch (error) {
							
						}
					});
				}
				this.pageResult = datas
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
