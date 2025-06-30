import React from 'react'
import styles from './index.module.scss'
import video1 from 'assets/videos/twin.mp4'

export default function TraffiLive() {
  const liveVideoUrl = [
    {
      title: '交通实时画面',
      url: video1,
    },
    {
      title: '交通全景视频融合',
      url: video1,
    },
    {
      title: '通行车辆动态提取',
      url: video1,
    },
  ]

  const rightContent = liveVideoUrl.map((item, index) => (
    <div key={index} className={styles.container}>
      <div className={styles.title}>
        <span>{item.title}</span>
      </div>
      <div className={styles.videoContainer}>
        <video className={styles.video} controls src={item.url}></video>
      </div>
    </div>
  ))

  return <div className={styles.trafficLiveContainer}>{rightContent}</div>
}
