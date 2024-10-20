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
        <el-input v-model="filters.username" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.nickname" placeholder="昵称" clearable></el-input>
      </el-form-item>
			<el-form-item>
				<el-input v-model="filters.orderno" placeholder="订单编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="USERID"></el-input>
      </el-form-item>
      <!-- <el-form-item>
        <el-input v-model="filters.wareno" placeholder="群号"></el-input>
      </el-form-item> -->
      <el-form-item>
        <el-input v-model="filters.artid" placeholder="注单"></el-input>
      </el-form-item>
      <!-- <el-form-item>
        <el-input v-model="filters.contno" placeholder="邀请人ID"></el-input>
      </el-form-item> -->
      <el-form-item>
        <el-select v-model="filters.mode" placeholder="类型" clearable>
          <el-option label="宝斗" value="0"></el-option>
          <el-option label="牛牛" value="1"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="order:order:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
    <template #mode="scope">
      <el-tag type="primary" v-if="scope.row.mode==0">宝斗</el-tag>
      <el-tag type="success" v-else-if="scope.row.mode==1">牛牛</el-tag>
    </template>
    <template #status="scope">
      <el-tag type="success" v-if="scope.row.status==0">正常</el-tag>
      <el-tag type="danger" v-else-if="scope.row.status==1">注销</el-tag>
    </template>
    <template #oper="scope">
        <div v-if="scope.row.buyorsal=='B' && scope.row.status=='0'">
          <el-button type="danger" size="mini" @click="logoff(scope.row)" v-if="scope.row.comm === 0 && scope.row.loss==0">注销</el-button>
        </div>
       
    </template>
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format,formatDate ,getCurTime ,getWeekStartEndDates } from "@/utils/datetime"
export default {
	components: {
		KtTable,
		KtButton
	},
	data() {
		return {
			size: 'small',
			filters: {
        fdate:[this.getStartDate(),this.getEndDate()],
				orderno: '',
				userno: '',
				wareno: '',
        buyorsal: '',
        artid:'',
        status:'',
        username:'',
        nickname:'',
        contno:'',
        mode:''
			},
			columns: [
				{prop:"fdate", label:"订单日期", width:100},
				{prop:"orderno", label:"订单编号", width:100},
        {prop:"userno", label:"USERID", minWidth:100},
        {prop:"username", label:"客户用户名", minWidth:120},
        {prop:"nickname", label:"客户昵称", minWidth:120},
        {prop:"artid", label:"注单", minWidth:80},
        {prop:"price", label:"金额", minWidth:100},
        {prop:"loss", label:"本局输赢", minWidth:100},
        {prop:"cpright", label:"倍数", minWidth:80},
        {prop:"comm", label:"手续费", minWidth:100},
        {prop:"transcomm", label:"返佣", minWidth:80},
        // {prop:"contno", label:"邀请人", minWidth:80},
        {prop:"mode", label:"类型", minWidth:80},
        {prop:"ordtime", label:"下单时间", minWidth:150},
        {prop:"status", label:"状态", minWidth:100},
        {prop:"cantime", label:"注销时间", minWidth:150},
        {prop:"oper", label:"操作", minWidth:100}
      ],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false
    }
	},
	methods: {
    logoff(row){
      this.$confirm('确认注销吗？', '提示', {}).then(() => {
        let para = {
          orderno:row.orderno,
          status:1
        }
         this.$api.order.edit(para).then(res=>{
            if(res.code == 200) {
              this.$message({ message: '操作成功', type: 'success' })
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.findPage(null)
         })
      })
    },
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
			this.pageRequest.params = {
        'ordtime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'ordtime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
			  'orderno':this.filters.orderno,
        'userno':this.filters.userno,
        'wareno':this.filters.wareno,
        'contno':this.filters.contno,
        'buyorsal':this.filters.buyorsal,
        'artid':this.filters.artid,
        'status':this.filters.status,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname,
        'mode':this.filters.mode
			}
			this.$api.order.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
//0未成交1已成交2部分成交3已撤单
    statusformat:function (row, column, cellValue, index){
		 switch (cellValue) {
      case '0': return "未成交";
      case '1': return "已成交";
      case '2': return "部分成交";
      case '3': return "已撤单";
      case '4': return "锁单";
       default: return cellValue;
     }
    },
    // 时间格式化
	dateFormat: function (row, column, cellValue, index){
	  return format(row[column.property])
	  },
    getStartDate(){
      var time = new Date().getTime();
      const curTime = getCurTime()
      if(curTime<'070000'){
        return formatDate(time - 1000 * 60 * 60 * 24 * 1,2) + ' 07:00:00'
      }else{
        return formatDate(time,2) + ' 07:00:00'
      }
    },
    getEndDate(){
        var time = new Date().getTime();
				const curTime = getCurTime()
				if(curTime<'070000'){
					return formatDate(time,2) + ' 06:00:00'
				}else{
					return formatDate(time + 1000 * 60 * 60 * 24 * 1,2) + ' 06:00:00'
				}

    }
	},
	mounted() {
	}
}
</script>

<style scoped>

</style>
