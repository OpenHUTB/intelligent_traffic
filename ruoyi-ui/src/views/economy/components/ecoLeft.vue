<template>
  <div class="content">
    <div>
      <dv-border-box-8 :reverse="true" style="padding: 10px">
        <div>
          <h2>各时段车流量情况</h2>
          <Echart :options="options1" height="400px" />
        </div>
      </dv-border-box-8>
    </div>

    <div class="body">
      <!-- 衣食住行支出比例 -->
      <dv-border-box-6  style="padding: 10px">
        <Echart :options="options2" height="400px"
      /></dv-border-box-6>
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
        },
        legend: {
          data: ["车流量"],
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
          data: ["08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00"],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "车流量",
            type: "line",
            smooth: true,
            data: [800, 910, 900, 920, 1400, 1500, 1550],
          },
          // {
          //   name: "支出",
          //   type: "line",
          //   data: [220, 610, 600, 620, 890, 1000, 1100],
          // },
        ],
      },
      options2: {
  title: {
    text: '交通参与者类型占比',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
       radius: ['40%', '70%'],
      data: [
        { value: 12, name: '行人' },
        { value: 28, name: '非机动车' },
        { value: 60, name: '机动车' },
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}
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
</style>
