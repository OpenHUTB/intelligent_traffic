import React, { useEffect, useRef } from 'react'
import styles from './index.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import {
  setTrafficCongestionList,
  updateTrafficCongestionItem,
  addTrafficCongestionItem,
} from 'stores/storesNewUI/trafficCongestionSlice'

export default function TrafficCongestionData() {
  const dispatch = useDispatch()
  const listItems = useSelector(
    (state) => state.trafficCongestion?.listItems || []
  )

  const scrollContainer = useRef(null)

  // 自动滚动
  useEffect(() => {
    const scrollAmount = 2.5
    const interval = setInterval(() => {
      const container = scrollContainer.current
      if (container) {
        if (container.scrollTop >= container.scrollHeight / 2) {
          container.scrollTop = 0
        } else {
          container.scrollTop += scrollAmount
        }
      }
    }, 300)
    return () => clearInterval(interval)
  }, [listItems])

  // 监听 window 事件
  useEffect(() => {
    const handleUpdate = (event) => {
      const detail = event.detail
      console.log('signalRoadDataChanged detail =>', detail)
      if (Array.isArray(detail)) {
        dispatch(setTrafficCongestionList(detail))
      } else if (detail && detail.index != null && detail.data) {
        dispatch(updateTrafficCongestionItem(detail))
      } else if (detail && typeof detail === 'object') {
        dispatch(addTrafficCongestionItem(detail))
      } else {
        console.warn('Unsupported trafficCongestion payload:', detail)
      }
    }
    window.addEventListener('signalRoadDataChanged', handleUpdate)
    return () =>
      window.removeEventListener('signalRoadDataChanged', handleUpdate)
  }, [dispatch])

  const renderList = listItems.map((item, index) => (
    <div className={styles.listItem} key={index}>
      <span className={styles.name}>{item.name}</span>
      <span className={styles.positionText}>{item.index}</span>
      <span className={styles.number}>{item.speed}</span>
      {/* <span className={styles.time}>{item.distance}</span> */}
      <span className={styles.speed}>{item.trend}</span>
      <span
        className={`${styles.status} ${
          item.status && item.status.includes('异常')
            ? styles.red
            : styles.green
        }`}
      >
        {item.status}
      </span>
    </div>
  ))

  return (
    <div className={styles.trafficViolationContainer}>
      <div className={styles.title}>
        <span>道路拥堵情况</span>
      </div>
      <div className={styles.mainContent}>
        <div className={styles.rankContianer}>
          <span className={styles.trafficName}>{`街道\n名称`}</span>
          <span className={styles.trafficIndex}>{`交通\n指数`}</span>
          <span className={styles.trafficSpeed}>{`平均\n速度`}</span>
          <span className={styles.trafficTrend}>{`拥堵\n趋势`}</span>
          <span>{`告警\n级别`}</span>
        </div>
        <div className={styles.listContainer} ref={scrollContainer}>
          {renderList}
        </div>
      </div>
    </div>
  )
}
