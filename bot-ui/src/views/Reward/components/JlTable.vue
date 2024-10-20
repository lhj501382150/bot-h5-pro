<template>
  <div>

    <!--表格栏-->
    <el-table :data="data.content" :highlight-current-row="highlightCurrentRow" @selection-change="selectionChange"
              :show-summary="data.sum!=null" :summary-method="getSum"
          @current-change="handleCurrentChange" v-loading="loading" :element-loading-text="$t('action.loading')" :border="border" :stripe="stripe"
          :show-overflow-tooltip="showOverflowTooltip" :max-height="maxHeight" :size="size" :align="align" style="width:100%;">
      <slot></slot>
      <el-table-column type="selection" width="55"></el-table-column>
     <el-table-column :type="showSeqColumn" label="序号" min-width="60" width="60"></el-table-column>
      <el-table-column v-for="column in columns" header-align="center" align="center"
        :prop="column.prop" :label="column.label" :width="column.width" :min-width="column.minWidth"
        :fixed="column.fixed" :key="column.prop" :type="column.type" :formatter="column.formatter"
        :sortable="column.sortable==null?false:column.sortable" show-overflow-tooltip>
        <template #default="scope" v-if="!column.formatter">
          <slot :name="column.prop" v-bind="scope">
            {{scope.row[column.prop]}}
          </slot>
        </template>
      </el-table-column>
      </el-table-column>
      <el-table-column :label="$t('action.operation')" :width="btnWidth"  v-if="showOperation" header-align="center" align="center">
        <template slot-scope="scope">
          <kt-button v-for="(btn,index) in buttons" :key="index" v-show="scope.row.status==btn.show || scope.row.pstatus==btn.show" :icon="btn.icon" :label="btn.label" :perms="btn.perms" :size="size" :type="btn.type" @click="handleClick(scope.$index, scope.row,btn.callback)" />
        </template>


      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="toolbar" style="padding:10px;">
      <kt-button :label="batchlabel" :perms="permsEdit" :size="size" type="danger" @click="handleBatch()"
                 :disabled="this.selections.length===0" style="float:left;" v-if="showBatch & showOperation"/>
      <el-pagination layout="total, prev, pager, next, jumper" @current-change="refreshPageRequest"
        :current-page="pageRequest.pageNum" :page-size="pageRequest.pageSize" :total="data.totalSize" style="float:right;">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import KtButton from "@/views/Core/KtButton"
export default {
  name: 'JlTable',
  components:{
			KtButton
	},
  props: {
    columns: Array, // 表格列配置
    data: Object, // 表格分页数据
    permsEdit: String,  // 编辑权限标识
    permsDelete: String,  // 删除权限标识
    batchlabel: String,  // 批量标识
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
    showBatch: {  // 是否显示操作组件
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
      showOperationSon: false
    }
  },
  methods: {
    initBtnWidth: function(){

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
      this.$emit(callback, [row])
    },

		// 批量操作
		handleBatch: function () {
			let ids = this.selections
      this.$emit('handleBatch', ids)
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
          if(val !=0 ){
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
    this.refreshPageRequest(1)
  },
  beforeUpdate() {

  }
}
</script>

<style scoped>

</style>
