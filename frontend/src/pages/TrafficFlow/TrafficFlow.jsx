import React from 'react'
import FunctionBar from 'components/rights/FunctionBar/FunctionBar'
import Amap from 'components/AMap/Amap'
import { useSelector } from 'react-redux'
import styles from './index.module.scss'
import RightTopBtn from 'components/rights/RightTopBtn/RightTopBtn'
import FlowAnalysis from 'components/rights/FlowAnalysis/FlowAnalysis'
import FLowRuntime from 'components/rights/FlowRuntime/FLowRuntime'
import FlowTrend from 'components/rights/FlowTrend/FlowTrend'
export default function TrafficFlow() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <div className={styles.trafficFlowContainer}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        {bigMapShow && <Amap />}
        <div className={styles.bottomContent}></div>
        <div className={styles.rightContent}>
          <FunctionBar class={styles.functionBar} />
          <RightTopBtn headerTitle='交通流量智能分析设置' />

          <FlowAnalysis />
          <FLowRuntime />
          <FlowTrend />
        </div>
      </div>
    </div>
  )
}
