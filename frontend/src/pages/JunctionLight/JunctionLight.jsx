import React from 'react'
import FunctionBar from 'components/rights/FunctionBar/FunctionBar'
import Amap from 'components/AMap/Amap'
import { useSelector } from 'react-redux'
import styles from './index.module.scss'
import RightTopBtn from 'components/rights/RightTopBtn/RightTopBtn'
import JunctionTimeprogress from 'components/rights/JunctionTimeprogress/JunctionTimeprogress'
import JunctionOptResult from 'components/rights/JunctionOptResult/JunctionOptResult'
import JunctionOptstrategy from 'components/rights/JunctionOptstrategy/JunctionOptstrategy'
import JunctionOpt from 'components/bottoms/JunctionOpt/JunctionOpt'
import JunctionGreenFlow from 'components/bottoms/JunctionGreenFLow/JunctionGreenFlow'
import video1 from 'assets/videos/junction.mp4'
export default function JunctionLight() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <div className={styles.junctionLight}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        <video
          src={video1}
          className={styles.junctionVideo}
          loop
          autoPlay
        ></video>
        {bigMapShow && <Amap />}
        <div className={styles.bottomContent}>
          <JunctionOpt />
          <JunctionGreenFlow />
        </div>
        <div className={styles.rightContent}>
          <FunctionBar class={styles.functionBar} />
          {/* <RightTopBtn headerTitle='信控模式管理' /> */}
          <JunctionTimeprogress />
          <JunctionOptResult />
          {/* <JunctionOptstrategy /> */}
        </div>
      </div>
    </div>
  )
}
