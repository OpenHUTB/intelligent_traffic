import React from 'react'
import styles from './index.module.scss'
import video1 from 'assets/videos/twin.mp4'
import live from 'assets/videos/live.mp4'
import mock from 'assets/videos/mock.mp4'
import combine from 'assets/videos/combine.mp4'

export default function TraffiLive() {
  const liveVideoUrl = [
    {
      title: '交通实时画面',
      url: live,
    },
    {
      title: '交通全景视频融合',
      url: combine,
    },
    {
      title: '通行车辆动态提取',
      url: mock,
    },
  ]

  const rightContent = liveVideoUrl.map((item, index) => (
    <div key={index} className={styles.container}>
      <div className={styles.title}>
        <span>{item.title}</span>
      </div>
      <div className={styles.videoContainer}>
        <video
          className={styles.video}
          // controls
          src={item.url}
          autoPlay
          loop
        ></video>
      </div>
    </div>
  ))

  return <div className={styles.trafficLiveContainer}>{rightContent}</div>
}
