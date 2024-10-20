<template>
  <div>

    <!--表格栏-->
    <el-table :data="data.content" :highlight-current-row="highlightCurrentRow" @selection-change="selectionChange"
          :show-summary="data.sum!=null" :summary-method="getSum"
          @current-change="handleCurrentChange" v-loading="loading" :element-loading-text="$t('action.loading')" :border="border" :stripe="stripe"
          :show-overflow-tooltip="showOverflowTooltip" :max-height="maxHeight" :size="size" :align="align" style="width:100%;">
     <!-- <el-table-column :type="showSeqColumn" width="40" v-if="showBatchDelete & showOperation"></el-table-column> -->
     <el-table-column :type="showSeqColumn" label="序号" min-width="60" width="60"></el-table-column>
      <el-table-column  prop="artid" label="资产编号"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="userno" label="客户编号"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="username" label="客户名称"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="coordx" label="地块坐标X"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="coordy" label="地块坐标Y"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="area" label="面积"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="atype" label="类型"  min-width="120" :formatter="atypeFormat"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="astatus" label="状态"   min-width="120" :formatter="astatusFormat"   show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="color" label="颜色"  min-width="120"  show-overflow-tooltip header-align="center" align="center">
         <template slot-scope="scope">
            <div :style="'background-color:'+scope.row.color+';color:#fff;'" >
              {{scope.row.color}}
            </div>
        </template>
      </el-table-column>
      <el-table-column  prop="date" label="时间"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="mem" label="属性"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>
      <el-table-column  prop="blocknum" label="所在区块"  min-width="120"  show-overflow-tooltip header-align="center" align="center"> </el-table-column>

      <el-table-column :label="$t('action.operation')" :width="btnWidth"  v-if="showOperation" header-align="center" align="center">
        <template slot-scope="scope">
          <kt-button icon="fa fa-edit" :label="$t('action.edit')" :perms="permsEdit" :size="size" @click="handleEdit(scope.$index, scope.row)" />
          <kt-button icon="fa fa-trash" :label="$t('action.delete')" :perms="permsDelete" :size="size" type="danger" @click="handleDelete(scope.$index, scope.row)" />
          <kt-button v-for="(btn,index) in buttons" :key="index" :icon="btn.icon" :label="btn.label" :perms="btn.perms" :size="size" :type="btn.type" @click="handleClick(scope.$index, scope.row,btn.callback)" />
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="" style="padding:10px;">
      <kt-button :label="$t('action.batchDelete')" :perms="permsDelete" :size="size" type="danger" @click="handleBatchDelete()"
        :disabled="this.selections.length===0" style="float:left;" v-if="showBatchDelete & showOperation"/>
      <el-pagination layout="total, prev, pager, next, jumper" @current-change="refreshPageRequest"
        :current-page.sync="data.pageNum" :page-size.sync="data.pageSize" :total="data.totalSize" style="float:right;">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import KtButton from "@/views/Core/KtButton"
export default {
  name: 'KtTable',
  components:{
			KtButton
	},
  props: {
    columns: Array, // 表格列配置
    data: Object, // 表格分页数据
    permsEdit: String,  // 编辑权限标识
    permsDelete: String,  // 删除权限标识
    buttons: {
      type:Array,
      default:()=>[]
    },
    size: { // 尺寸样式
      type: String,
      default: 'mini'
    },
    align: {  // 文本对齐方式
      type: String,
      default: 'left'
    },
    maxHeight: {  // 表格最大高度
      type: Number,
      default: 700
    },
    showOperation: {  // 是否显示操作组件
      type: Boolean,
      default: true
    },
    border: {  // 是否显示边框
      type: Boolean,
      default: false
    },
    stripe: {  // 是否显示斑马线
      type: Boolean,
      default: true
    },
    highlightCurrentRow: {  // // 是否高亮当前行
      type: Boolean,
      default: true
    },
    showOverflowTooltip: {  // 是否单行显示
      type: Boolean,
      default: true
    },
    showBatchDelete: {  // 是否显示操作组件
      type: Boolean,
      default: true
    },
    showSeqColumn:{
      type: String,
      default:'index'
    }
  },
  data() {
    return {
      // 分页信息
			pageRequest: {
				pageNum: 1,
        pageSize: 50
      },
      loading: false,  // 加载标识
      selections: [],  // 列表选中列
      btnWidth: 185,
      showOperationSon: false,
       astatuss: [
        {key:'0',val:'未发行'},
        {key:'1',val:'发行'},
        {key:'2',val:'持有'},
        {key:'3',val:'终止'},
        {key:'4',val:'冻结'},
        {key:'5',val:'持有未上链'},
      ],
      atypes:[
        {key:'1',val:'道路'},
        {key:'2',val:'广场'},
        {key:'3',val:'普通'}
      ]
    }
  },
  methods: {
    initBtnWidth: function(){
      let size = this.buttons.length
      if(this.permsEdit){
        size++
      }
      if(this.permsDelete){
        size++
      }
      if(size ==0){
        this.btnWidth = 100
      }else if(size==1){
        this.btnWidth = 100
      }else if(size==2){
        this.btnWidth = 185
      }else if(size > 2 && size <= 4) {
        this.btnWidth = 400
      }else{
        this.btnWidth = 500
      }
    },
    		// 是否有效格式化
  atypeFormat: function (row, column, cellValue, index){
    let t = this.atypes.filter(function(item){
      return item.key == cellValue
    });
    let val = cellValue;
    if(t && t.length > 0) val = t[0].val
    return val
	},
  astatusFormat: function (row, column, cellValue, index){
    let item = this.astatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
    // 分页查询
    findPage: function () {
        this.loading = true
        let callback = res => {
          this.loading = false
        }
      this.$emit('findPage', {pageRequest:this.pageRequest, callback:callback})
    },
    // 选择切换
    selectionChange: function (selections) {
      this.selections = selections
      this.$emit('selectionChange', {selections:selections})
    },
    // 选择切换
    handleCurrentChange: function (val) {
      this.$emit('handleCurrentChange', {val:val})
    },
    // 换页刷新
		refreshPageRequest: function (pageNum) {
      this.pageRequest.pageNum = pageNum
      this.findPage()
    },
    // 操作
    handleClick: function (index, row,callback) {
      this.$emit(callback, {index:index, row:row})
    },
    // 编辑
		handleEdit: function (index, row) {
      this.$emit('handleEdit', {index:index, row:row})
		},
    // 删除
		handleDelete: function (index, row) {
      this.$confirm('确认删除该记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.loading = true
        let callback = res => {
          if(res.code == 200) {
            this.$message({message: '删除成功', type: 'success'})
            this.findPage()
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.loading = false
        }
        this.$emit('handleDelete', {params:row, callback:callback})
      }).catch(() => {
      })
		},
		// 批量删除
		handleBatchDelete: function () {
			let ids = this.selections.map(item => item.id).toString()
			this.delete(ids)
		},
		// 删除操作
		delete: function (ids) {
			this.$confirm('确认删除选中记录吗？', '提示', {
				type: 'warning'
			}).then(() => {
				let params = []
				let idArray = (ids+'').split(',')
        if(idArray.length>1){
          for(var i=0; i<idArray.length; i++) {
          	params.push({'id':idArray[i]})
          }
        }else{
          params={'id':ids}
        }

        this.loading = true
        let callback = res => {
          if(res.code == 200) {
            this.$message({message: '删除成功', type: 'success'})
            this.findPage()
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.loading = false
        }
        this.$emit('handleDelete', {params:params, callback:callback})
			}).catch(() => {
			})
		},
    getSum(param){
      const { columns, data } = param;
      let count = this.data.sum
      let sums = []
      columns.forEach(function(col, index) {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }else{
          let val = count[col.property];
          if(val !=null ){
            sums[index] = val
          }else{
            sums[index] = '--'
          }

        }
      })
      return sums;
    }
  },
  mounted() {
   //this.showOperationSon = this.showOperation
    this.refreshPageRequest(1)
  },
  beforeUpdate() {
    this.initBtnWidth()
  }

}
</script>

<style scoped>

</style>
