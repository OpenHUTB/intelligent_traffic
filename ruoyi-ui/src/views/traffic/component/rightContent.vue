<template>
  <div class="content">
    <dv-border-box-8 style="padding: 8px">
      <h2>视频监控</h2>
      <div style="width: 29vw; height: 220px">
          <video autoplay="autoplay" width="100%" height="100%">
              <source src="../../../assets/webm/CAMERAV.webm" type="video/mp4"/>
          </video>
      </div>
      <h2>路段统计</h2>
      <dv-scroll-board :config="config" style="width: 29vw; height: 220px" />
    </dv-border-box-8>
  </div>
</template>

<script>
export default {
  data() {
    return {
      config: {
        header: ["区县", "路段", "情况"],
        data: [
          ["岳麓区", "旺龙路", "严重"],
          ["望城区", "普瑞大道", "严重"],
          ["望城区", "湘江北路", "中等"],
          ["岳麓区", "桐梓坡路", "正常"],
          ["天心区", "五一路", "中等"],
          ["雨花区", "韶山北路", "正常"],
          ["雨花区", "友谊路", "正常"],
          ["开福区", "湘雅路", "严重"],
          ["开福区", "中山路", "正常"],
          ["岳麓区", "银杉路", "严重"],
        ],
      },
    };
  },
};
</script>

<style scoped>
.content {
  width: 30vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
h2{
    margin: 10px;
}
</style>
