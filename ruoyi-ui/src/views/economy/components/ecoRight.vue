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
        title: {
          text: "路口指标分析",
        },
        legend: {
          data: ["预估指标", "实际指标"],
        },
        radar: {
          // shape: 'circle',
          indicator: [
            { name: "交通流量", max: 6500 },
            { name: "车均延误", max: 16000 },
            { name: "车距停车次数", max: 30000 },
            { name: "路口饱和度", max: 38000 },
            { name: "排队长度", max: 52000 },
          ],
        },
        series: [
          {
            name: "预算分配 vs 实际指标",
            type: "radar",
            data: [
              {
                value: [4200, 3000, 20000, 35000, 50000],
                name: "预估指标",
              },
              {
                value: [5000, 14000, 28000, 26000, 42000],
                name: "实际指标",
              },
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
