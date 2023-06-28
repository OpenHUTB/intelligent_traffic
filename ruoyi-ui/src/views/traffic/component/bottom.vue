<template>
  <div>
    <div style="display: flex; justify-content: space-between; width: 100vw">
      <div style="display: flex">
        <div class="colum_center">
          <h2>拥堵里程</h2>
          <div>
            <dv-charts
              :option="carsNum"
              style="width: 300px; height: 300px"
            />
          </div>
        </div>
        <div class="colum_center">
          <h2>拥堵指数</h2>
          <div>
            <dv-charts
              :option="accidentNum"
              style="width: 300px; height: 300px"
            />
          </div>
        </div>
        <div class="colum_center">
          <h2>平均速度</h2>
          <div>
            <dv-charts
              :option="accidentNum2"
              style="width: 300px; height: 300px"
            />
          </div>
        </div>
      </div>
      <dv-border-box-6  style="padding: 20px">
        <div class="colum_center">
          <h2>各地区交通拥堵指数</h2>
          <dv-scroll-ranking-board
            :config="jam"
            style="width: 600px; height: 250px"
          />
        </div>
      </dv-border-box-6>

      <div class="colum_center">
        <dv-border-box-12 style="padding: 20px; margin-left: 150px">
          <div class="air">
            <h1>今日天气</h1>
            <p>当前天气晴朗，24°C。</p>
            <p>今天日间预计有大雨。 最高气温25°。</p>
          </div>
        </dv-border-box-12>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      
      carsNum: {
        series: [
          {
            type: 'gauge',
            data: [ { name: 'itemA', value: 55 } ],
            axisLabel: {
              formatter: '{value}%'
            },
            details: {
              show: true,
              offset: [0, 40],
              formatter: '{value}Km'
            },
            animationCurve: 'easeOutBack'
          }
        ]        
      },


      accidentNum: {
        series: [
          {
            type: 'gauge',
            data: [ { name: 'itemA', value: '30' } ],
            axisLabel: {
              formatter: '{value}%'
            },
            details: {
              show: true,
              offset: [0, 40],
              formatter: '畅通'
            },
            animationCurve: 'easeOutBack'
          }
        ] 
      },
      accidentNum2: {
        series: [
          {
            type: 'gauge',
            data: [ { name: 'itemA', value: 44 } ],
            axisLabel: {
              formatter: '{value}%'
            },
            details: {
              show: true,
              offset: [0, 40],
              formatter: '{value}Km/h'
            },
            animationCurve: 'easeOutBack'
          }
        ] 
      },
      jam: {
        data: [
          {
            name: "岳麓区",
            value: 55,
          },
          {
            name: "望城区",
            value: 120,
          },
          {
            name: "芙蓉区",
            value: 78,
          },
          {
            name: "开福区",
            value: 66,
          },
          {
            name: "天心区",
            value: 80,
          },
          {
            name: "浏阳市",
            value: 45,
          },
          {
            name: "长沙县",
            value: 29,
          },
        ],
      },
    };
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
</style>
