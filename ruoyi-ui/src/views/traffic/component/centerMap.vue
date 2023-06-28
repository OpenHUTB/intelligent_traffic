<template>
  <div class="content">
    <baidu-map
      class="map"
      :center="center"
      :zoom="zoom"
      @ready="handler"
      :scroll-wheel-zoom="true">
      <!-- <bm-traffic :predictDate="{weekday: 7, hour: 12}">
      </bm-traffic> -->
    </baidu-map>
  </div>
</template>

<script>
import mapStyle from "@/assets/mapStyle.json";
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
  created() {},
  mounted() {},
  methods: {
    handler({ BMap, map }) {

      this.center.lng = 112.91992;
      this.center.lat = 28.21903;
      this.zoom = 15;

      map.setMapStyle({style:'midnight'});    
    },
  },
};
</script>

<style scoped>
.content {
  width: 52%;
  height: 75vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.map {
  width: 100%;
  height: 100%;
}
/deep/.anchorBL {
                display: none!important;
            }
</style>
