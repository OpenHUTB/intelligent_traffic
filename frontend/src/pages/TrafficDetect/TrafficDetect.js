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
import { useDispatch } from 'react-redux'

export default function TrafficDetect() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  const dispatch = useDispatch()
  // 监听大地图显示状态变化
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
          <iframe
            title="traffic-video"
            src="http://172.21.108.47:3000/"
            height="100%"
          ></iframe>
        </div>
      </div>
    </div>
  )
}
