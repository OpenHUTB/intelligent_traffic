import React from 'react'
import styles from './index.module.scss'
import ScrollAllert from 'components/bottoms/scrollAlert/ScrollAlert'
import RightTopBtn from 'components/rights/RightTopBtn/RightTopBtn'
import TotalTrafficMonitor from 'components/rights/TotalTrafficMonitor/TotalTrafficMonitor'
export default function TrafficDetect() {
  return (
    <div className={styles.trafficDetectContainer}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        <div className={styles.bottomContent}>
          <ScrollAllert />
        </div>
        <div className={styles.rightContent}>
          <RightTopBtn headerTitle='监控指数设置' />
          <TotalTrafficMonitor />
        </div>
      </div>
    </div>
  )
}
