import React, { useEffect, useRef } from 'react' // 添加 useRef
import styles from './css/rightSide.module.scss'
import LightControl from './LightControl'
import JunctionInfo from './JunctionInfo'
import ControlStrategy from './ControlStrategy'
import TimeProgress from './TimeProgress'
import ClickOptimise from './ClickOptimise'
import { useSelector, useDispatch } from 'react-redux'

import Chat from './Chat'
// import Map from 'components/container/layout/homeview/Map/Map'

export default function RightSide() {
  const timeProgressDisplay = useSelector(
    (state) => state.timeShow.timeProgressShow
  )
  const timeChangeShow = useSelector((state) => state.timeShow.timeChangeShow)
  const dispatch = useDispatch()
  return (
    <section className={styles.rightSide}>
      <Chat />
      {/* <JunctionInfo />
            <ControlStrategy /> */}
      {/* <Map /> */}
      <LightControl />
      {timeProgressDisplay && <TimeProgress />}
      {timeChangeShow && <ClickOptimise></ClickOptimise>}
    </section>
  )
}
