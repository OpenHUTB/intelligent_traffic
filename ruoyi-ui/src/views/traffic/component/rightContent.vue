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
          ["渝北区", "行1列2", "严重"],
          ["江北区", "行2列2", "严重"],
          ["渝北区", "行3列2", "中等"],
          ["合川区", "行4列2", "正常"],
          ["万州区", "行5列2", "中等"],
          ["万州区", "行6列2", "正常"],
          ["万州区", "行7列2", "正常"],
          ["巴南区", "行8列2", "严重"],
          ["巴南区", "行9列2", "正常"],
          ["巴南区", "行10列2", "严重"],
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
