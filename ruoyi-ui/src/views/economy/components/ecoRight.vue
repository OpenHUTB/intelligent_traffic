<template>
  <div class="content">
    <div>
      <dv-border-box-8 :reverse="true" style="padding: 10px">
        <div>
          <Echart :options="options1" height="400px" />
        </div>
      </dv-border-box-8>
    </div>

    <div class="body">
      <!-- 城市农作物比例占比 -->
      <div class="body_table1">
        <dv-border-box-6 style="padding: 10px">
          <h2>主要拥堵路口分布</h2>
          <Echart
            :options="options2"
            height="400px"
            width="400px"
            style="margin: auto"
          />
        </dv-border-box-6>
      </div>
    </div>
  </div>
</template>

<script>
import Echart from "@/common/echart/index.vue";
export default {
  components: { Echart },
  data() {
    return {
    
      options1: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999",
            },
          },
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ["line", "bar"] },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ["车辆", "车辆增长率"],
        },
        xAxis: [
          {
            type: "category",
            data: [
              "6:00",
              "7:00",
              "8:00",
              "9:00",
              "10:00",
              "11:00",
              "12:00",
              "13:00",
              "14:00",
              "15:00",
              "16:00",
              "17:00",
              "18:00",
            ],
            axisPointer: {
              type: "shadow",
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "万辆",
            min: 2700,
            max: 3300,
            interval: 100,
            axisLabel: {
              formatter: "{value}",
            },
          },
          {
            type: "value",
            name: "%",
            min: 0,
            max: 70,
            interval: 10,
            axisLabel: {
              formatter: "{value}",
            },
          },
        ],
        
        series: [
          {
            name: "车辆",
            type: "bar",
            tooltip: {
              valueFormatter: function (value) {
                return value + " 万";
              },
            },
            data: [
              2920, 2950, 2970, 3010, 3040, 3070, 3110, 3150, 3160, 3190, 3210,
              3220, 3250,
            ],
          },
          {
            name: "车辆增长率",
            type: "line",
            yAxisIndex: 1,
            tooltip: {
              valueFormatter: function (value) {
                return value + " 万辆";
              },
            }, 
            data: [
              40.21, 59.81, 30.45, 36.15, 32.45, 26.54, 39.94, 33.55, 19.63,
              24.7, 21.09, 3.5, 4.21,
            ],
          },
        ],
      },
      options2: {
        legend: {},
        tooltip: {},
        dataset: {
          dimensions: ["product", "杜鹃路", "观沙路", "彩虹路", "岳麓大道", "茶子山东路", "银杉路"],
          source: [
            { product: "杜鹃路", 杜鹃路: 8 },
            { product: "观沙路", 观沙路: 6 },
            { product: "彩虹路", 彩虹路: 2 },
            { product: "岳麓大道", 岳麓大道: 9 },
            { product: "茶子山东路", 茶子山东路: 11 },
            { product: "银杉路", 银杉路: 5 },

            // { product: "2020", 小麦: 831, 玉米: 734, 高粱: 551 },
            // { product: "2021", 小麦: 864, 玉米: 652, 高粱: 825 },
            // { product: "2023", 小麦: 724, 玉米: 539, 高粱: 391 },
          ],
        },
        xAxis: { type: "category" },
        yAxis: {},
        series: [{ type: "bar" }, { type: "bar" }, { type: "bar" },{ type: "bar" }, { type: "bar" }, { type: "bar" }],
      },
    };
  },
};
</script>

<style scoped>
.content {
  width: 30%;
}
.head {
  padding: 10px;
  height: 80px;
  display: flex;
  justify-content: space-around;
}
.head_content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.body {
  margin-top: 10px;
}
.body_table1 {
  display: flex;
}
</style>
