<template>
  <div class="content">
    <dv-border-box-8 style="padding: 8px">
      <h2>视频监控</h2>
      <div style="width: 29vw; height: 260px" @click="SeeVideo">
          <!-- <video autoplay="autoplay" width="100%" height="100%">
              <source src="../../../assets/webm/identify.mp4" type="video/mp4"/>
          </video> -->
          <video-player class="video-player vjs-custom-skin" 
              ref="videoPlayer" 
              :playsinline="true" 
              :options="playerOptions">
          </video-player>         
      </div>
      <h2>路段统计</h2>
      <dv-scroll-board :config="config" style="width: 29vw; height: 220px" />
    </dv-border-box-8>
  </div>
</template>

<script>
import { videoPlayer } from 'vue-video-player'
import 'video.js/dist/video-js.css'
export default {
  components: {
    videoPlayer
  },
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
      playerOptions: {
        playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
        autoplay: true, // 如果为true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 是否视频一结束就重新开始。
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: "video/mp4", // 类型
          src: '/video/identify.mp4' // url地址
        }],
        poster: '', // 封面地址
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true, // 当前时间和持续时间的分隔符
          durationDisplay: true, // 显示持续时间
          remainingTimeDisplay: false, // 是否显示剩余时间功能
          fullscreenToggle: true // 是否显示全屏按钮
        }
      }   
    };
  },

  methods: {
    // 选择图标
    SeeVideo() {
      console.log(2222222)
    }
  }
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
