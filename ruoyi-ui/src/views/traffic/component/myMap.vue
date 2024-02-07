<template>
  <div style="position: relative;" class="content">
    <div style="position: absolute; right: 10px; top: 10px; z-index: 1;" >
      <el-input v-model="busNumber" placeholder="请输入车牌号" style="width: 200px; margin: 10px;"></el-input>
      <el-button @click="queryBus">查询</el-button>
      <el-button @click="startMove">开始回放</el-button>
      <el-button @click="pauseAnimation">暂停回放</el-button>
      <el-button @click="resumeAnimation">继续回放</el-button>
      <el-select v-model="speed" style="width: 100px; margin-left: 10px;" placeholder="选择倍速"
                 @change="handleSelect($event)">
        <el-option :value="'1倍速'">1倍速</el-option>
        <el-option :value="'2倍速'">2倍速</el-option>
        <el-option :value="'3倍速'">3倍速</el-option>
        <el-option :value="'4倍速'">4倍速</el-option>
      </el-select>
    </div>
    <!-- <div style="position:absolute; left: 50px; bottom: 30px; z-index: 1; width: 95%; height: 20px;">
            <el-progress :percentage="percentage"></el-progress>
        </div> -->
    <div id="amapcontainer" style="width: 100%; height: 580px"></div>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'
import axios from 'axios'
import {getBusGpsData} from "@/api/busGpsData/busGpsData";
// import AMap from 'vue-amap'
window._AMapSecurityConfig = {
  securityJsCode: '57e15eab41bec442bfff4b98143c15a4' // '「申请的安全密钥」',
}
export default {
  data() {
    return {
      map: null, // 高德地图实例
      lineArr: [[112.916737, 28.215764], [112.916737, 28.215764]], // 轨迹
      marker: null,
      polyline: null,
      speed: '1倍速',
      duration: 1000,  // 轨迹回放时间
      percentage: 50, // 进度条进度
      busNumber: ''
    }
  },
  methods: {
    // 地图初始化
    initAMap() {
      AMapLoader.load({
        key: "81d2d99a2b6cbb08e76d8cfec965d34b", // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ["AMap.Scale", "AMap.ToolBar", "AMap.ControlBar", 'AMap.Geocoder', 'AMap.Marker',
          'AMap.CitySearch', 'AMap.Geolocation', 'AMap.AutoComplete', 'AMap.InfoWindow'], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      }).then((AMap) => {
        // 获取到作为地图容器的DOM元素，创建地图实例
        this.map = new AMap.Map("amapcontainer", { //设置地图容器id
          resizeEnable: true,
          viewMode: "3D", // 使用3D视图
          zoomEnable: true, // 地图是否可缩放，默认值为true
          dragEnable: true, // 地图是否可通过鼠标拖拽平移，默认为true
          doubleClickZoom: true, // 地图是否可通过双击鼠标放大地图，默认为true
          zoom: 1, //初始化地图级别
          center: [112.916737, 28.215764], // 初始化中心点坐标 长沙
          mapStyle: "amap://styles/darkblue", // 设置颜色底层
        })

        this.marker = new AMap.Marker({
          position: this.lineArr[0],
          icon: "https://a.amap.com/jsapi_demos/static/demo-center-v2/car.png",
          offset: new AMap.Pixel(-13, -26),
        });
        this.map.add(this.marker)

        // 绘制轨迹
        this.polyline = new AMap.Polyline({
          path: this.lineArr,
          showDir: true,
          strokeColor: "#28F",  //线颜色
          strokeOpacity: 1,     //线透明度
          strokeWeight: 1,      //线宽
          // strokeStyle: "solid"  //线样式
          lineJoin: 'round',

        });
        this.map.add(this.polyline)

        // 走过的路径
        this.passedPolyline = new AMap.Polyline({
          strokeColor: "#AF5",  //线颜色
          strokeWeight: 1,      //线宽
          strokeOpacity: 0
        });
        this.map.add(this.passedPolyline)

        // 监听marker移动
        this.marker.on('moving', (e) => {
          console.log('marker动了', e)
          this.passedPolyline.setPath(e.passedPath); // 设置路径样式
          this.map.setCenter(e.target.getPosition(), true) // 设置地图中心点
        });

        this.map.setFitView(); // 根据覆盖物自适应展示地图

      }).catch(e => {
        console.log(e)
      })
    },
    // 开始回放
    startMove() {
      this.map.plugin('AMap.MoveAnimation', () => {
        console.log('开始回放')
        this.marker.moveAlong(this.lineArr, {
          // 每一段的时长
          duration: this.duration,//可根据实际采集时间间隔设置
          // JSAPI2.0 是否延道路自动设置角度在 moveAlong 里设置
          autoRotation: true,
        });
      })

    },
    // 暂停回放
    pauseAnimation() {
      this.marker.pauseMove();
    },
    // 继续回放
    resumeAnimation() {
      this.marker.resumeMove();
    },
    // 倍速控制
    handleSelect(e) {
      console.log('e', parseInt(e.charAt(0)))
      this.duration = 500 / parseInt(e.charAt(0))
    },
    // 根据车牌号查询车辆
    queryBus () {
      if (this.busNumber === '') {
        return;
      }
      getBusGpsData(this.busNumber).then(response => {
        this.lineArr = response.data.map(item => {
          return Object.values(item)
        })
      }).finally(() => {
        // console.log(this.lineArr)
        this.initAMap()
      })

    }

  },
  mounted() {
    //DOM初始化完成进行地图初始化
    this.initAMap()
  }
}
</script>

<style scoped>
  .content {
    width: 52%;
    height: 65h;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .anchorBL {
    display: none!important;
  }
</style>
