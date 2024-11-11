<template>
  <div>
    <div class="echar" ref="chart"></div>
  </div>
</template>

<script>
const echarts = require('echarts');
export default {
name: "Echarts",
  props:{
    xdata: {
      type:Array,
      default:[]
    },
    ydata: {
      type:Array,
      default:[]
    }
  },
  data () {
    return {
      myChart:''
    };
  },
  watch:{
    x:function(val){
      console.log(val)
    }
  },
  methods:{
    changeChar(x,y){
      var opts = this.myChart.getOption();
      opts.xAxis[0].data = x
      opts.series[0].data = y
      this.myChart.setOption(opts)

        var that = this;
        setTimeout(function () {
          that.changeChar(that.xdata,that.ydata)
      }, 5000)
    },
    initCharts () {
      this.myChart = echarts.init(this.$refs.chart);
// 绘制图表
      this.myChart.setOption({
        tooltip: {              //设置tip提示
          trigger: 'axis',
        },
        title:{
          text:'近两周交易',
          x:'center', //水平安放位置，默认为'left'，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
          y: 'top', //垂直安放位置，默认为top，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
          textStyle: { //主标题文本样式{"fontSize": 18,"fontWeight": "bolder","color": "#333"}
              color: '#FA6F53',
              fontWeight:'normal'
          }
        },
        color: [ '#00BFFF'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
        xAxis: {                //设置x轴
          type: 'category',
          boundaryGap: false,     //坐标轴两边不留白
          data: [],
          name: '日期',           //X轴 name
          nameTextStyle: {        //坐标轴名称的文字样式
            color: '#FA6F53',
            fontSize: 16,
            padding: [0, 0, 0, 20]
          },
          axisLine: {             //坐标轴轴线相关设置。
            lineStyle: {
              color: '#FA6F53',
            }
          }
        },
        yAxis: {
          name: '交易数量',
          nameTextStyle: {
            color: '#FA6F53',
            fontSize: 16,
            padding: [0, 0, 10, 0]
          },
          axisLine: {
            lineStyle: {
              color: '#FA6F53',
            }
          },
          type: 'value'
        },
        series: [
          {
            name: '交易数量',
            data:  [],
            type: 'line',               // 类型为折线图
            lineStyle: {                // 线条样式 => 必须使用normal属性
              normal: {
                color: '#00BFFF',
              }
            },
          }
        ]
      })
      let that = this
      window.onresize = function(){
        that.myChart.resize();
      }
    }
  },
  mounted() {
    this.initCharts()
    this.changeChar(this.xdata,this.ydata)
  }
}
</script>

<style scoped>
.echar{
  width:100%;
  height:250px
}
</style>
