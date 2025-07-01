import React from 'react'
import styles from './index.module.scss'
import ScrollAllert from 'components/bottoms/scrollAlert/ScrollAlert'
import RightTopBtn from 'components/rights/RightTopBtn/RightTopBtn'
import TotalTrafficMonitor from 'components/rights/TotalTrafficMonitor/TotalTrafficMonitor'
import FunctionBar from 'components/rights/FunctionBar/FunctionBar'
import TrafficCongestionMonitor from 'components/rights/TrafficCongestionMonitor/TrafficCongestionMonitor'
import TrafficCongestionData from 'components/rights/TrafficCongestionData/TrafficCongestionData'
import Amap from 'components/AMap/Amap'
import { useSelector } from 'react-redux'
import { setBigMapShow } from 'stores/junctionLight/mapSlice'

export default function TrafficDetect() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <div className={styles.trafficDetectContainer}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        {bigMapShow && <Amap />}
        <div className={styles.bottomContent}>
          <ScrollAllert />
        </div>
        <div className={styles.rightContent}>
          <FunctionBar class={styles.functionBar} />
          <RightTopBtn headerTitle='监控指数设置' />
          <TotalTrafficMonitor />
          <TrafficCongestionMonitor />
          <TrafficCongestionData />
        </div>
      </div>
    </div>
  )
}
