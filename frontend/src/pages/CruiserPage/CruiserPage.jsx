import React from 'react'
import styles from './index.module.scss'
import FunctionBar from 'components/rights/FunctionBar/FunctionBar'

import Amap from 'components/AMap/Amap'
import { useSelector } from 'react-redux'
import { setBigMapShow } from 'stores/junctionLight/mapSlice'
import CruiserSetting from 'components/bottoms/CruiserSetting/CruiserSetting'
export default function CruiserPage() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  return (
    <div className={styles.CruiserContainer}>
      <div className={styles.leftBar}></div>
      <div className={styles.bottomBar}></div>
      <div className={styles.mainContent}>
        {bigMapShow && <Amap />}
        <div className={styles.bottomContent}>
          <CruiserSetting />
        </div>
        <div className={styles.rightContent}>
          <FunctionBar class={styles.functionBar} />
        </div>
      </div>
    </div>
  )
}
