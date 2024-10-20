<!--  -->
<template>
  <div>
    <canvas id="tutorial" width="360" height="360" class="item_block" @mousemove="showXY($event)"></canvas>
    <p class="" v-if="X  || Y ">坐标X:{{X}}  坐标Y:{{Y}}  {{mem}}</p>
    <div class="tips" :style="pos" v-show="IS_TIP">
      X: {{X}} <br/>
      Y: {{Y}} <br/>
      {{mem}}
    </div>
  </div>
</template>

<script>
export default {
  name:'LandinfoModel',
  props: {
    items:{
      type:Array,
      default:[]
    },
    one:{}
  },
  data () {
    return {
      X:'',
      Y:'',
      mem:'',
      pos:{},
      IS_TIP: false
    };
  },

  components: {},

  computed: {},

  mounted(){
    this.draw()
  },

  methods: {
    draw(){
      var canvas = document.getElementById('tutorial');
      var ctx = canvas.getContext('2d');
      // ctx.clearRect(0, 0, canvas.width, canvas.height)
      let color = this.one.color
      ctx.fillStyle = color;
      for(var i = 0;i< 360;i++){
        for( var j = 0; j < 360;j++){
          ctx.fillRect (i, j, 1, 1);
        }
      }
      for(var i=0;i<this.items.length;i++){
        var item = this.items[i]
        ctx.fillStyle = item.color;
        var x = parseInt(item.coordx) + 180;
        var y = Math.abs(parseInt(item.coordy) - 180);
        ctx.fillRect (x, y, 1, 1);
      }
    },
    showXY(e){
      this.X = e.offsetX - 180;
      this.Y = (e.offsetY - 180) * -1;
      const that = this
      let res = this.items.filter(function(item){
          return item.coordx == that.X && item.coordy ==that.Y;
      })
      if(res && res.length>0){
        let item = res[0]
        this.mem = item.mem
      }else{
        this.mem = '普通'
      }

      if(that.X > 180 || that.X < -180 || that.Y > 180 || that.Y < -180){
        this.IS_TIP = false
      }else{
        this.IS_TIP = true
        var x = e.offsetX + 20;
        var y = e.offsetY + 20;
        if(that.X < -90 ){
          x += 100;
        }
        if(that.Y > 90 ){
          y += 80;
        }
        this.pos = {
          position:'absolute',
          top: y + 'px',
          left: x + 'px'
        }
      }

    }
  }
}

</script>
<style scoped>
  .item_block{
    border: solid 1px #e2e2e2;
    margin: 0 auto;
    box-shadow: -10px -10px 50px 5px #e2e2e2;
  }
  .tips{
    width: 80px;
    height: 50px;
    background-color: rgb(245, 249, 252);
    opacity: 0.6;
    border-radius: 15%;
    padding:5px;
    display: float;
  }
</style>
