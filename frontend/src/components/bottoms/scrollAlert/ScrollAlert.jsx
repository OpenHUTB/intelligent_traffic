import React, { useEffect, useRef, useState } from 'react'
import styles from './index.module.scss'
import alert from 'assets/image/alert.png'
import { useSelector, useDispatch } from 'react-redux'
import {
  setHighwayScrollAlertList,
  addHighwayScrollAlertItem,
  updateHighwayScrollAlertItem,
} from 'stores/storesNewUI/scrollAlertSlice'

export default function TrafficRank() {
  const dispatch = useDispatch()
  const listItems = useSelector(
    (state) => state.highwayScrollAlert?.listItems || []
  )

  const scrollContainer = useRef(null)
  const [currentItem, setCurrentItem] = useState(null)

  useEffect(() => {
    const startAutoScroll = () => {
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
      return interval
    }

    const intervalId = startAutoScroll()

    const highStatusItems = listItems.filter(
      (item) => item.status === '极高' || item.status === '高'
    )

    const getRandomItem = () => {
      if (highStatusItems.length === 0) return null
      const randomIndex = Math.floor(Math.random() * highStatusItems.length)
      return highStatusItems[randomIndex]
    }

    setCurrentItem(getRandomItem())

    const randomItemIntervalId = setInterval(() => {
      setCurrentItem(getRandomItem())
    }, 3000)

    return () => {
      clearInterval(intervalId)
      clearInterval(randomItemIntervalId)
    }
  }, [listItems])

  useEffect(() => {
    const handleUpdate = (event) => {
      const detail = event.detail
      console.log('scrollAlertDataChanged detail =>', detail)
      if (Array.isArray(detail)) {
        // Replace entire list
        dispatch(setHighwayScrollAlertList(detail))
      } else if (detail && detail.index != null && detail.data) {
        // Update an indexed item
        dispatch(updateHighwayScrollAlertItem(detail))
      } else if (detail && typeof detail === 'object') {
        // Treat as a single new item to prepend
        dispatch(addHighwayScrollAlertItem(detail))
      } else {
        console.warn('Unsupported scrollAlert payload:', detail)
      }
    }

    window.addEventListener('scrollAlertDataChanged', handleUpdate)

    return () => {
      window.removeEventListener('scrollAlertDataChanged', handleUpdate)
    }
  }, [dispatch])

  const renderList = listItems.map((item, index) => (
    <div className={styles.listItem} key={index}>
      <span className={styles.rank}>{index + 1}</span>
      <span className={styles.name}>{item.name}</span>
      <span className={styles.positionText}>{item.position}</span>
      <span className={styles.number}>{item.number}</span>
      <span
        className={`${styles.isAlert} ${
          item.isAlert === '是' ? styles.alertYes : styles.alertNo
        }`}
      >
        {item.isAlert}
      </span>
      <span
        className={`${styles.isDeal} ${
          item.isDeal === '是' ? styles.dealYes : styles.dealNo
        }`}
      >
        {item.isDeal}
      </span>
      <span className={styles.speed}>{item.speed} km/h</span>
      <span className={styles.time}>{item.time}</span>
      <span
        className={`${styles.status} ${
          item.status.includes('极')
            ? styles.red
            : item.status.includes('高')
            ? styles.yellow
            : styles.green
        }`}
      >
        {item.status}
      </span>
    </div>
  ))

  if (!currentItem) {
    return (
      <div className={styles.trafficViolationContainer}>
        <div className={styles.title}>
          <span>实时告警信息</span>
        </div>
        <div className={styles.mainContent}>
          <div className={styles.rankContianer}>
            <span>序号</span>
            <span className={styles.violationName}>类型</span>
            <span className={styles.violationPosition}>位置</span>
            <span className={styles.violationNumber}>车牌号</span>
            <span className={styles.violationAlert}>报警</span>
            <span className={styles.violationDeal}>处理</span>
            <span className={styles.violationSpeed}>车速</span>
            <span className={styles.violationTime}>发生时间</span>
            <span>告警级别</span>
          </div>
          <div className={styles.empty} ref={scrollContainer}>
            当前暂无告警信息
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className={styles.trafficViolationContainer}>
      <div className={styles.title}>
        <span>实时告警信息</span>
      </div>
      <div className={styles.emergency}>
        <img className={styles.gif} src={alert} alt='' />
        <span
          className={styles.alertText}
        >{`${currentItem.name}   ${currentItem.position}   ${currentItem.status} `}</span>
      </div>
      <div className={styles.mainContent}>
        <div className={styles.rankContianer}>
          <span>序号</span>
          <span className={styles.violationName}>类型</span>
          <span className={styles.violationPosition}>位置</span>
          <span className={styles.violationNumber}>车牌号</span>
          <span className={styles.violationAlert}>报警</span>
          <span className={styles.violationDeal}>处理</span>
          <span className={styles.violationSpeed}>车速</span>
          <span className={styles.violationTime}>发生时间</span>
          <span>告警级别</span>
        </div>
        <div className={styles.listContainer} ref={scrollContainer}>
          {renderList}
        </div>
      </div>
    </div>
  )
}
