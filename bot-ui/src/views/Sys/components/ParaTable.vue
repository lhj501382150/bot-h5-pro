<template>
  <div>

    <!--表格栏-->
    <el-table :data="tabledata" :highlight-current-row="highlightCurrentRow" @selection-change="selectionChange"
          @current-change="handleCurrentChange" v-loading="loading" :element-loading-text="$t('action.loading')" :border="border" :stripe="stripe"
          default-expand-all :tree-props="{children: 'children'}" row-key="sysid" border
          :show-overflow-tooltip="showOverflowTooltip" :max-height="maxHeight" :size="size" :align="align" style="width:100%;">
     <el-table-column :type="showSeqColumn" label="序号" min-width="60" width="60"></el-table-column>
      <el-table-column prop="groupname" label="分组名称" min-width="120" width="120" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="!scope.row.sysname">{{scope.row.groupname}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sysid" label="参数编号" min-width="80" width="80" header-align="center" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.sysname">{{scope.row.sysid}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sysname" label="参数名称" min-width="150" width="150" header-align="center" align="center">
      </el-table-column>
      <el-table-column prop="sval" label="参数值"  min-width="220" show-overflow-tooltip header-align="center" align="center">
      </el-table-column>
      <el-table-column prop="isvalid" label="是否启用" width="80" min-width="80" header-align="center" align="center">
        <template slot-scope="scope">
            <span v-if="scope.row.isvalid=='Y'">启用</span>
            <span v-else>禁用</span>
        </template>
      </el-table-column>
      <el-table-column prop="memo" label="描述" min-width="180" header-align="center" align="center">
      </el-table-column>
      <el-table-column :label="$t('action.operation')" :width="120"  v-if="showOperation" header-align="center" align="center">
        <template slot-scope="scope">
          <kt-button icon="fa fa-edit" v-show="scope.row.sysname" :label="$t('action.edit')" :perms="permsEdit" :size="size" @click="handleEdit(scope.$index, scope.row)" />
        </template>


      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import KtButton from "@/views/Core/KtButton"
export default {
  name: 'TreeTable',
  components:{
			KtButton
	},
  props: {
    data: Object, // 表格分页数据
    tabledata:{
      type: Array,
      default:()=>[]
    },
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
      showOperationSon: false
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
        this.btnWidth = 0
      }else if(size==1){
        this.btnWidth = 100
      }else if(size==2){
        this.btnWidth = 185
      }else{
        this.btnWidth = 300
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
