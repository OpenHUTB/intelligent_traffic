<template>
  <div class="content">
    <!-- <div id="container" class="map">

    </div> -->
    <baidu-map
    @dragging="handleClick"
    @click="handleClick"
      class="map"
      :center="center"
      :zoom="zoom"
      @ready="handler"
      :scroll-wheel-zoom="true">
      <bm-traffic :predictDate="{weekday: 7, hour: 12}">
      </bm-traffic>
    </baidu-map>
  </div>
</template>

<script>
import mapStyle from "@/assets/mapStyle.json";
import { debounce } from 'lodash';
export default {
  components: {},
  data() {
    return {
      center: { lng: 0, lat: 0 },
      zoom: 3,
      mapStyle: {
        styleJson: mapStyle,
      },
    };
  },
  created() {
    this.handleClick = debounce(this.handleClick, 300); // 设置防抖时间间隔为300ms
  },
  mounted() {
    // this.builmap();
  },
  methods: {
    handler({ BMap, map }) {

      this.center.lng = 112.925737;
      this.center.lat = 28.225764;
      this.zoom = 19;

      map.setMapStyle({style:'midnight'});
    },

    builmap() {
      let map = new window.BMap.Map("container");
      let point = new window.BMap.Point(112.925737, 28.225764);
      map.centerAndZoom(point, 19);
      map.enableScrollWheelZoom(true);
      map.setMapStyle({style:'midnight'})
      map.addEventListener("click", function(e) {
        console.log(e.point.lng + "," + e.point.lat);
      });

    },

    handleClick() {
      let newMessage = Math.random().toString() ;
      this.$emit('update-message', newMessage)
    }
  },
};
</script>

<style scoped>
.content {
  width: 52%;
  height: 65h;
  display: flex;
  align-items: center;
  justify-content: center;
}
.map {
  width: 100%;
  height: 100%;
}
.anchorBL {
                display: none!important;
            }
</style>
