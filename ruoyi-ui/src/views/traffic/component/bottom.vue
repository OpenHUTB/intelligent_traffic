<template>
  <div>
    <div style="display: flex; justify-content: space-between; width: 100%">


      <dv-border-box-8 :reverse="true" style="padding: 10px; width: 33%">
        <div>
          <Echart :options="options1" height="350px" width="600px" />
        </div>
      </dv-border-box-8>

      <dv-border-box-6 style="padding-top: 20px; width: 33%">
        <h2 class="title">周边路段全日段拥堵指数</h2>
        <Echart :options="options2" height="300px" width="600px" />
      </dv-border-box-6>


      <dv-border-box-6 style="padding: 20px;width: 33%">
        <div class="colum_center">
          <h2>各地区平均速度展示</h2>
          <dv-scroll-ranking-board
            :config="jam"
            style="width: 600px; height: 300px"
          />
        </div>
      </dv-border-box-6>

      <!-- <div class="colum_center">
        <dv-border-box-12 style="padding: 20px; margin-left: 150px">
          <div class="air">
            <h1>今日天气</h1>
            <p>当前天气晴朗，34°C。</p>
            <p>今天日间预计有大雨。 最高气温35°。</p>
          </div>
        </dv-border-box-12>
      </div> -->
    </div>
  </div>
</template>

<script>
import { pageIntersectionData } from "@/api/intersectionData/intersectionData";
import Echart from "@/common/echart/index.vue";

export default {
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
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["拥堵率", "通畅率", "净拥堵率"],
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: [
            "6:00",

            "8:00",

            "10:00",

            "12:00",

            "14:00",

            "16:00",

            "18:00",

            "20:00",
          ],
        },
        yAxis: {
          type: "value",
          min: -4,
          max: 14,
          interval: 2,
        },
        series: [
          {
            name: "拥堵率",
            type: "line",
            data: [10.8, 11.9, 11.6, 11.5, 10.5, 7.8, 6.5, 6.0],
          },
          {
            name: "通畅率",
            type: "line",
            data: [7.3, 7.3, 7.4, 7.8, 7.9, 7.9, 8.1, 8.2],
          },
          {
            name: "净拥堵率",
            type: "line",
            data: [4, 4.4, 4, 3.8, 3.1, -1, -1.6, -2],
          },
        ],
      },
      jam: {},
    };
  },

  created() {
    this.getList();
  },

  props: {
    message: {
      type: String,
      default: "0", // 默认值为0
    },
  },

  watch: {
    message() {
      this.options1Change();
      this.options2Change();
    },
  },

  methods: {
    getList() {
      const params = { evaluationTypeId: 6 };
      pageIntersectionData(params)
        .then((response) => {
          // console.log("2")
          if (response.code === 200) {
            // console.log(response)
            const { rows } = response;
            // console.log(rows)
            let roleList = {};
            roleList.data = rows.map((item) => ({
              name: item.intersectionName,
              value: item.value,
            }));
            this.jam = roleList;
            // console.log(this.config.roleList)
            // this.config = { ...this.config }
            //  console.log(this.config)
            // console.log(this)
          } else {
            console.error("获取平均速度数据失败");
          }
        })
        .catch((error) => {
          console.error("获取平均速度数据失败:", error);
        });
    },

    options1Change() {
      let data = [
        [5000, 14000, 28000, 26000, 42000],
        [4200, 3000, 20000, 35000, 50000],
        [3100, 6000, 20000, 21000, 44600],
        [5500, 11000, 23000, 11000, 22600],
        [4400, 12000, 11000, 30000, 36100],
        [5400, 3000, 15000, 32000, 44400],
        [4758, 8790, 2222, 3333, 40000],
        [4444, 14833, 25000, 26666, 28888],
        [4900, 12300, 23000, 21000, 44600],
        [3900, 11000, 19000, 27000, 43330],
      ];
      let index0 = Math.floor(Math.random() * data.length);
      // console.log(index0);
      this.options1.series[0].data[0].value = data[index0];
      let index1 = Math.floor(Math.random() * data.length);
      // console.log(index1);
      this.options1.series[0].data[1].value = data[index1];
    },

    options2Change() {
      // console.log(222222);
      let data0 = [
        [6.8, 9.9, 8.6, 11.5, 11.9, 8.8, 6.5, 10.0],
        [9.8, 8.9, 7.6, 6.5, 9.5, 7.8, 10.5, 11.0],
        [7.8, 8.9, 11.6, 10.5, 7.5, 9.8, 6.5, 7.0],
        [9.8, 8.9, 10.6, 7.5, 10.5, 7.8, 6.5, 9.0],
        [10.8, 8.9, 6.6, 11.5, 10.5, 7.8, 10.5, 8.0],
      ];
      let index0 = Math.floor(Math.random() * data0.length);
      this.options2.series[0].data = data0[index0];

      let data1 = [
        [7.3, 7.3, 7.4, 7.8, 7.9, 7.9, 8.1, 8.2],
        [6.3, 8.3, 8.4, 8.8, 9.9, 6.9, 6.1, 6.2],
        [8.3, 8.3, 6.4, 6.8, 6.9, 6.9, 6.1, 6.2],
        [7.9, 7.3, 7.4, 9.8, 9.9, 9.9, 9.1, 9.2],
        [6.3, 6.3, 6.4, 7.8, 8.9, 8.9, 9.1, 6.2],
      ];
      let index1 = Math.floor(Math.random() * data1.length);
      this.options2.series[1].data = data1[index1];

      let data2 = [
        [4, 5.4, 5, 5.8, 5.1, -1, -1.6, -2],
        [4, 4.4, 4, 3.8, 3.1, -1, -1.6, -2],
        [-2, -1.4, -1, -1.8, 3.1, -1, -1.6, -2],
        [2, 2.4, 2, 2.8, 2.1, -2, -2.6, -2],
        [3, 3.4, 3.9, 3.8, 4, 1, 1, 2],
      ];
      let index2 = Math.floor(Math.random() * data2.length);
      this.options2.series[2].data = data2[index2];
    },
  },

  components: {
    Echart,
  },
};
</script>

<style lang="scss" scoped>
.colum_center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.air {
  display: flex;
  height: 30vh;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;

  p {
    font-size: 30px;
    line-height: 40px;
  }
}
.title {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
</style>
