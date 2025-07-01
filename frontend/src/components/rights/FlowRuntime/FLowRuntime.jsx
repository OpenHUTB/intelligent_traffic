import React from 'react'
import styles from './index.module.scss'

export default function FLowRuntime() {
  const runtimeData = [
    {
      location: '雨花区',
      trafficVolume: 120,
    },
    {
      location: '芙蓉区',
      trafficVolume: 150,
    },
    {
      location: '天心区',
      trafficVolume: 180,
    },
    {
      location: '岳麓区',
      trafficVolume: 200,
    },
    {
      location: '开福区',
      trafficVolume: 170,
    },
    {
      location: '望城区',
      trafficVolume: 160,
    },
  ]

  // 计算最大值用于进度条比例
  const maxVolume = Math.max(...runtimeData.map((item) => item.trafficVolume))

  return (
    <div className={styles.FlowRuntimeContainer}>
      <div className={styles.title}>
        <span>实时交通流量监测</span>
      </div>

      <div className={styles.contentContainer}>
        <div className={styles.dataList}>
          {runtimeData.map((item, index) => (
            <div key={index} className={styles.dataItem}>
              <div className={styles.location}>{item.location}</div>

              <div className={styles.progressContainer}>
                <div
                  className={styles.progressBar}
                  style={{
                    width: `${(item.trafficVolume / maxVolume) * 100}%`,
                  }}
                ></div>
              </div>

              <div className={styles.volume}>{item.trafficVolume}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
