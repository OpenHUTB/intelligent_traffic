<template>
    <div class="content">
        <div>
            <dv-border-box-8 :reverse="true" style="padding: 10px;height: 450px;width: 590px">
<!--                <div>-->
<!--                    <h2>原始视频数据</h2>-->

<!--                      <video ref="videoElement" controls style="padding:  10px;width: 430px;height: 450px"></video>-->

<!--                </div>-->
               <div>
                                  <h2>原始视频数据</h2>
                                  <video-player class="video-player vjs-custom-skin" ref="videoPlayer" :playsinline="true"
                                                                        :options="playerOptions1" @play="onPlayerPlay($event)" @pause="onPlayerPause($event)"
                                                                        :events="events" @fullscreenchange="handlefullscreenchange">
                                  </video-player>
                              </div>
            </dv-border-box-8>
        </div>

        <div class="body">
            <dv-border-Box-8>
                <div style="padding: 10px 10px 10px 10px; ">
                    <!-- 城市农作物比例占比 -->
                    <h2 style="padding-top: 10px;">运动轨迹</h2>
                    <video-player class="video-player vjs-custom-skin" ref="videoPlayer" :playsinline="true"
                        :options="playerOptions2" @play="onPlayerPlay($event)" @pause="onPlayerPause($event)"
                        :events="events" @fullscreenchange="handlefullscreenchange">
                    </video-player>
                </div>
            </dv-border-Box-8>
        </div>
    </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/hls.js@latest"></script>
<script>
import Echart from "@/common/echart/index.vue";

export default {
    components: { Echart },
    mounted(){
      console
      const video = this.$refs.videoElement;
      if (Hls.isSupported()) {
        const hls = new Hls();
        console.log("supported")
        hls.loadSource('http://127.0.0.1:8081/stream.m3u8');
        hls.attachMedia(video);
        hls.on(Hls.Events.MANIFEST_PARSED, function () {
          video.play();
        });
      } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = 'http://127.0.0.1:8081/stream.m3u8';
        video.addEventListener('loadedmetadata', function () {
          video.play();
          console.log("no hls supported")
        });
      }
    },
    data() {
        return {
            playerOptions1: {
                playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
                autoplay: true, // 如果为true,浏览器准备好时开始回放。
                muted: true, // 默认情况下将会消除任何音频。
                loop: true, // 是否视频一结束就重新开始。
                preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
                language: 'zh-CN',
                aspectRatio: '4:3', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
                fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
                sources: [{
                    type: "video/mp4", // 类型
                    src: '/video/chuf.webm' // url地址
                    //src: '/video/CAMERAV.webm' // url地址
                }],
                poster: '', // 封面地址
                notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
                controlBar: {
                    timeDivider: true, // 当前时间和持续时间的分隔符
                    durationDisplay: true, // 显示持续时间
                    remainingTimeDisplay: false, // 是否显示剩余时间功能
                    fullscreenToggle: true // 是否显示全屏按钮
                }
            },
            playerOptions2: {
                playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
                autoplay: true, // 如果为true,浏览器准备好时开始回放。
                muted: true, // 默认情况下将会消除任何音频。
                loop: true, // 是否视频一结束就重新开始。
                preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
                language: 'zh-CN',
                aspectRatio: '4:3', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
                fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
                sources: [{
                    type: "video/mp4", // 类型
                    src: '/video/path.webm' // url地址
                    //src: '/video/CAMERAV.webm' // url地址
                }],
                poster: '', // 封面地址
                notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
                controlBar: {
                    timeDivider: true, // 当前时间和持续时间的分隔符
                    durationDisplay: true, // 显示持续时间
                    remainingTimeDisplay: false, // 是否显示剩余时间功能
                    fullscreenToggle: true // 是否显示全屏按钮
                }
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
