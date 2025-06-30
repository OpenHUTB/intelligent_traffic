import React from 'react'
import styles from './index.module.scss'
import FunctionBar from 'components/rights/FunctionBar/FunctionBar'
import TrafficLive from 'components/rights/TrafficLive/TrafficLive'
import GreenWave from 'components/bottoms/GreenWave/GreenWave'

import Amap from 'components/AMap/Amap'
import { useSelector } from 'react-redux'
import { setBigMapShow } from 'stores/junctionLight/mapSlice'

export default function DigitalTwin() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <div className={styles.digitalTwinContainer}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        {bigMapShow && <Amap />}
        <div className={styles.bottomContent}>
          <GreenWave />
        </div>
        <div className={styles.rightContent}>
          <FunctionBar class={styles.functionBar} />
          <TrafficLive />
        </div>
      </div>
    </div>
  )
}
