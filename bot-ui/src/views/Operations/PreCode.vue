<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.drawIssue" placeholder="期数"></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="filters.rkey" placeholder="模式"  clearable>
					<el-option v-for="(item,index) in modes" :key="index" :label="item.val" :value="item.key"></el-option>
				</el-select>
			</el-form-item>
      <el-form-item>
        
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="operations:preCode:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="operations:preCode:add" type="primary" @click="handleAdd" />
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table  permsDelete="operations:preCode:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :cL-on-click-modal="false" :destroy-on-cL="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" :size="size" 
			label-position="right">
			<el-form-item label="期数" prop="drawIssue" v-if="!operation">
				<el-input v-model="dataForm.drawIssue" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="模式" prop="rkey" >
				<el-select v-model="dataForm.rkey" :disabled="!operation" style="width: 98%;" @change="changeMode">
					<el-option v-for="(item,index) in modes" :key="index" :label="item.val" :value="item.key"></el-option>
				</el-select>
			</el-form-item>
			<div v-if="hq">
				<el-form-item label="下期开奖时间" >
						{{dataForm.drawTime}} <el-button type="primary" size="mini" @click="refresh">刷新</el-button>
				</el-form-item>
				<el-form-item label="宝斗下单明细">
					<div class="order-detail">
						<div v-for="(item,index) in hq.orders" :key="index" class="order-item">
							<span>{{ item.artid }}</span>
							<span>{{ item.bailmoney }}</span>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="宝斗汇总详情">
					<el-radio-group v-model="drawName">
						<el-radio v-for="(item,index) in hq.count" :key="index" :label="item.bNo" border>
								<span>{{ getDrawName(item.bNo) }}</span>
								<span style="margin-left:10px;color:red;">{{item.loss}}</span> / <span style="color:green;">{{item.bailmoney}}</span> 
						</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="牛牛平投明细">
					<div class="order-detail">
						<div v-for="(item,index) in hq.porder" :key="index" class="order-item">
							<span>闲{{ item.artid }}</span>
							<span>{{ item.bailmoney }}</span>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="牛牛倍投明细" >
					<div class="order-detail">
						<div v-for="(item,index) in hq.border" :key="index" class="order-item">
							<span>闲{{ item.artid }}</span>
							<span>{{ item.bailmoney }}</span>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="输赢结果(绿色赢)" >
					<el-table :data="niuDraw" style="width: 100%">
						<el-table-column prop="x1" label="闲一" >
							<template slot-scope="scope">
								<el-switch  v-model="scope.row.x1"   active-color="#13ce66" > </el-switch>
							</template>
						</el-table-column>
						<el-table-column prop="x2" label="闲二" >
							<template slot-scope="scope">
								<el-switch  v-model="scope.row.x2"   active-color="#13ce66" > </el-switch>
							</template>
						</el-table-column>
						<el-table-column prop="x3" label="闲三" >
							<template slot-scope="scope">
								<el-switch  v-model="scope.row.x3"   active-color="#13ce66" > </el-switch>
							</template>
						</el-table-column>
						<el-table-column prop="x4" label="闲四">
							<template slot-scope="scope">
								<el-switch  v-model="scope.row.x4"   active-color="#13ce66" > </el-switch>
							</template>
						</el-table-column>
						<el-table-column prop="x5" label="闲五" >
							<template slot-scope="scope">
								<el-switch  v-model="scope.row.x5"   active-color="#13ce66" > </el-switch>
							</template>
						</el-table-column>
						<!-- <el-table-column prop="oprea" label="操作" >
							<template slot-scope="scope">
								<el-button type="primary" size="mini" @click="getNiuDraw(scope.row)">生成结果</el-button>
							</template>
						</el-table-column> -->
					</el-table>
				</el-form-item>

				<el-form-item>
					<el-button type="primary" size="small" @click="tryResult">试算结果</el-button>
				</el-form-item>
				<el-form-item label="试算结果">
					<div class="order-detail">
						<div v-for="(item,index) in niuData" :key="index" class="order-item">
							<span v-if="index==0">庄：牛{{item}}</span>
							<span v-else>
								闲{{index}}:
								<span v-if="item > 0">牛{{item}}</span>
								<span v-else>无牛</span>
							</span>
						</div>
					</div>
				</el-form-item>
			</div>

			<el-form-item label="行情结果" prop="code"  >
				<el-input v-model="dataForm.code" auto-complete="off" maxlength="30"></el-input>
				<span style="color: red;">*数字之间以，分割 例：07,10,06,08,09,04,03,05,01,02</span>
			</el-form-item>
			<el-form-item label="备注" prop="memo">
				<el-input v-model="dataForm.memo" auto-complete="off" maxlength="64"></el-input>
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
				drawIssue: '',
				mode: ''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				drawIssue: [
					{ required: true, message: '请输入期数', trigger: 'blur' }
				],
				code: [
					{ required: true, message: '请输入行情结果', trigger: 'blur' }
				],
				rkey: [
					{ required: true, message: '请选择模式', trigger: 'blur' }
				]
			},
			// 新增编辑界面数据
			dataForm: {
				id:'',
				dataId:'',
				drawIssue: '',
				code: '',
				memo: ''
			},
			modes:[
				{key:'HXBD1',val:'哈希1分',mode:2},
				{key:'HXBD3',val:'哈希3分',mode:3},
				{key:'HXBD5',val:'哈希5分',mode:4},
				// {key:'HXNN1',val:'哈希1分牛牛',mode:5},
				// {key:'HXNN3',val:'哈希3分牛牛',mode:6},
				// {key:'HXNN5',val:'哈希5分牛牛',mode:7}
			],
			hq:'',
			drawName:'',
			niuDraw:[
				{x1:false,x2:false,x3:false,x4:false,x5:false}
			],
			niuData:[]
		}
	},
	methods: {
		tryResult(){
			if(!this.drawName){
				this.$message.error('请选择宝斗结果')
				return
			}
			let num = this.changeDraw(this.drawName)
			let row = this.niuDraw[0]
			this.getNiuDraw(row,num)
		},
		getNiuDraw(row,num){
			let sum = 0;
			let index = 0;
			let ret = []
			while(sum != 55 && index < 10){
				ret = this.getRandomHq(row,num)
				sum = ret.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
				index++
			}
			if(sum == 55){
				this.dataForm.code = ret.map(item=>{
				if(item < 10){
					item = '0' + item
				}
				return item
			}).join(',')
			}else{
				this.$message.error('未发现合适结果，请再次点击重试')
			}
		},
		getRandomHq(row,num){
			if(this.niuDraw.length > 0){
				this.niuDraw.splice(1,1)
			}
			this.dataForm.code = ''
			let winArr = Object.keys(this.niuDraw[0]).filter(key=>this.niuDraw[0][key] === true)
			let size = winArr.length
			let ret = []
			let zjRet = 9
			let flag = false
			if(size <= 1){
				zjRet = this.getRandomNumber(7,9)
			}else if(size == 2 || size ==3){
				zjRet = this.getRandomNumber(6,8)
			}else if(size == 3 || size ==4){
				zjRet = this.getRandomNumber(1,4)
			}else if(size == 5){
				let arr1 = this.shuffleArray([2,3,4,6,7])
				let arr2 = this.shuffleArray([8,9,10])
				let arr3 = this.shuffleArray([1,5])
				ret = [...arr1,...arr2,...arr3]
				flag = true
			}
			let index = 0
			while(!flag && index < 5){
				try {
					ret = this.getResult1(zjRet,num)
					flag = true
				} catch (error) {
					// this.$message.error('行情结果计算失败，请再次点击重试')
					flag = false
				}
				index++
			}
			for(var i=0;i<6;i++){
				let temp = [ret[i],ret[i+1],ret[i+2],ret[i+3],ret[i+4]]
				this.niuData[i] = this.countResult(temp)
			}
			return ret
		},
		getResult1(zjRet,num){
			let arr = [1,2,3,4,5,6,7,8,9,10]
			let ret = []
			ret[0] = this.getRandomNum(arr)
			ret[1] =  this.getRandomNum(arr)
			ret[2] =  this.getRandomNum(arr)
			this.replaceNum(ret,num,arr) 

			let flag = this.countNiuResult(ret,arr,zjRet)
			let index = 0
			while(!flag && index <  5){
				ret = []
				arr = [1,2,3,4,5,6,7,8,9,10]
				ret[0] = this.getRandomNum(arr)
				ret[1] =  this.getRandomNum(arr)
				ret[2] =  this.getRandomNum(arr)
				this.replaceNum(ret,num,arr) 

				flag = this.countNiuResult(ret,arr,zjRet)
				index++
			} 
			Object.keys(this.niuDraw[0]).forEach((key,index)=>{
				let legnth = ret.length
				let temp = [ret[legnth-4],ret[legnth-3],ret[legnth-2],ret[legnth-1]]
				if(this.niuDraw[0][key]){//比结果大
					let maxNum = this.getMaxNum(temp,arr,zjRet)
					index = 1
					while(maxNum == 0 && index < 5){
						temp = this.shuffleArray(temp)
						maxNum = this.getMaxNum(temp,arr,zjRet)
						index++
					}
					if(maxNum == 0){
						throw new Error('refresh is now')
					}else{
						temp[4] = maxNum
					}
				}else{//比结果小
					let minNum = this.getMinNum(temp,arr,zjRet)
					index = 1
					while(minNum == 0 && index < 5){
						temp = this.shuffleArray(temp)
						minNum = this.getMinNum(temp,arr,zjRet)
						index++
					}
					if(minNum == 0){
						throw new Error('refresh is now')
					}else{
						temp[4] = minNum
					}
				}

				ret.splice(ret.length - 4,4)
				ret = [...ret,...temp]
			})

			return ret
		},
		replaceNum(ret,num,arr){
			let index = 0
			if(!ret.includes(num)){
				index = this.getRandomNumber(0, 2) 
				let tempNum = ret[index]
				ret[index] = num
				let arrIndex = arr.findIndex(item=>item===num)
				arr[arrIndex] = tempNum
			}else{
				index = ret.findIndex(item=>item===num)
			}
			
			let temp = [1,2,3,4].filter(item=> item !== num)
			for(var i =0 ;i< index;i++){
				if(temp.includes(ret[i])){
					 ret[index] = ret[i]
					 ret[i] = num
				}
			} 
		},
		getMaxNum(ret,arr,zjRet){
			let minRet = 0
			if(arr.length == 1){
				minRet = arr[0]
				arr = []
			}else{
				for(var i=0;i<arr.length;i++){
					ret[4] = arr[i]
					let num = this.countResult(ret)
					if(num > zjRet){
						minRet = arr[i]
						arr.splice(i,1)
						break
					}
				} 
			}
			return minRet
		},
		getMinNum(ret,arr,zjRet){
			let minRet = 0
			if(arr.length == 1){
				minRet = arr[0]
				arr = []
			}else{
				for(var i=0;i<arr.length;i++){
					ret[4] = arr[i]
					let num = this.countResult(ret)
					if(num < zjRet){
						minRet = arr[i]
						arr.splice(i,1)
						break
					}
				} 
			}
			return minRet
		},
		//洗牌算法
		shuffleArray(array) {
			for (let i = array.length - 1; i > 0; i--) {
				let j = Math.floor(Math.random() * (i + 1));
				[array[i], array[j]] = [array[j], array[i]];
			}
			return array;
		},
		//计算Z需要结果数字
		countNiuResult(ret,arr,result){
			var flag = false
			if(arr.length > 1){
				for(var i=0;i<arr.length;i++){
					for(var j=i;j<arr.length;j++){
						ret[3] = arr[i]
						ret[4] = arr[j]
						let maxNum = this.countResult(ret)
						if(maxNum == result){
							arr.splice(j,1)
							arr.splice(i,1)
							flag = true
							break;
						}
					}
					break;
				}
				return flag;
			}else{
				throw new Error('arr length is error')
			}
			
		},
		//计算5个数字中最大组合结果
		countResult(arr){
			if(arr.length != 5){
				throw new Error('arr is error')
			}
			const sum = (nums) => nums.reduce((acc, num) => acc + num, 0);
			let maxNum = 0;
			for(var i = 0;i<arr.length;i++){
				for(var j=i+1;j<arr.length;j++){
					for(var m = j+1;m<arr.length;m++){
						let ret = (arr[i]+arr[j]+ arr[m]) % 10 == 0
						if(ret){
							const nums = arr.filter((n,index)=>![i,j,m].includes(index))
							let num = sum(nums) % 10
							if(num > maxNum){
								maxNum = num
							}
						}
					}
				}
			}
			return maxNum
		},
		//随机获取数字并删除
		getRandomNum(arr){
			let index = Math.floor(Math.random() * arr.length)
			let num = arr[index]
			arr.splice(index,1)
			return num
		},
		getDrawName(val){
			let ret = val
			if(val==1){
				ret = '入'
			}else if(val ==2){
				ret = '龙'
			}else if(val ==3){
				ret = '出'
			}else if(val ==4){
				ret = '虎'
			}
			return ret
		},
		changeDraw(val){
			let ret = val
			// if(val=='入'){
			// 	ret = 1
			// }else if(val =='龙'){
			// 	ret = 2
			// }else if(val =='出'){
			// 	ret = 3
			// }else if(val =='虎'){
			// 	ret = 4
			// }
			return ret
			// let arr = this.geBdArr(ret) || []
			// this.dataForm.code = arr.map(item=>{
			// 	if(item < 10){
			// 		item = '0' + item
			// 	}
			// 	return item
			// }).join(',')
		},
		geBdArr(val){
			let arr = []
			let ret = []
			for(var i = 1;i <= 10;i++){
				if(i != val){
					arr.push(i)
				}
			}
			let index = this.getRandomNumber(0,4)
			for(var i = 0;i<10;i++){
				if(i < index){
					let seq = this.getRandomNumber(arr.length-4,arr.length-1)
					ret[i] = arr[seq]
					arr.splice(seq,1)
				}else if(i==index){
					ret[index] = val
				}else{
					let seq = this.getRandomNumber(0,arr.length-1)
					ret[i] = arr[seq]
					arr.splice(seq,1)
				}
			}
			return ret
		},
		getRandomNumber(min, max) {
			return Math.floor(Math.random() * (max - min + 1)) + min;
		},
		refresh(){
			this.hq =  ''
			this.dataForm.code = ''
			this.dataForm.drawIssue = ''
			this.dataForm.drawTime = ''
			this.niuData = []
			this.$nextTick(()=>{
				this.changeMode(this.dataForm.rkey)
			})
		},
		changeMode(val){
			 this.$api.preCode.getDraw({rkey:val}).then(res=>{
				this.hq = res.data
				this.dataForm.drawIssue = this.hq.draw.drawIssue
				this.dataForm.drawTime = this.hq.draw.nextTime
			 })
		},
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
				this.pageRequest = {pageNum: 1, pageSize: 50}
			}
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.preCode.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.preCode.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.hq = ''
			this.dataForm= {
				id:'',
				dataId:'',
				drawIssue: '',
				code: '',
				memo: '',
				rkey:''
			}
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
					let ret = this.dataForm.code.split(',').map(item=>parseInt(item))
					let sum = ret.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
					if(sum != 55){
						this.$message.error('开奖结果数据不正确:' + ret.sort((a,b)=>a-b))
						return
					}
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
							if(this.operation){
							this.$api.preCode.save(params).then((res) => {
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
							this.$api.preCode.edit(params).then((res) => {
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
	modeFormat: function (row, column, cellValue, index){
      let data = this.modes.find(item=>{
		return item.key == cellValue
	  })
	  if(data && data.val){
		return data.val
	  }
	  return cellValue
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"rkey", label:"模式", minWidth:120,formatter:this.modeFormat},
				{prop:"dataId", label:"DATA_ID", minWidth:120},
				{prop:"drawIssue", label:"期数", minWidth:120},
				{prop:"code", label:"行情结果", minWidth:120},
				{prop:"memo", label:"备注", minWidth:100}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
	}
}
</script>

<style scoped lang="scss">
.order-detail{
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-wrap: wrap;
	.order-item{
		width: 100px;
		display: flex;
		justify-content: space-between;
		margin-right: 20px;
		border-bottom: 1px solid #e2e2e2;
	}
}
</style>
