// SignalJunction.js

import React, { useEffect, useState, useRef } from 'react'
import styles from './index.module.scss'
import { ReactComponent as UP } from 'assets/image/UP.svg'
import { ReactComponent as DOWN } from 'assets/image/DOWN.svg'
import { useSelector, useDispatch } from 'react-redux'
import { setListItems } from 'stores/digitalTwin/signalJunctionSlice'

export default function FlowTrend() {
  const dispatch = useDispatch()

  // Get listItems from Redux store
  const listItems = useSelector((state) => state.signalJunction.listItems)

  // State to store listItems with changes
  const [listItemsWithChanges, setListItemsWithChanges] = useState([])

  // useRef to store previous listItems for comparison
  const prevListItemsRef = useRef([])

  // Event listener to update listItems in Redux
  useEffect(() => {
    const handleSignalJunctionDataChanged = (event) => {
      console.log('SignalJunction Data Changed:', event.detail)
      dispatch(setListItems(event.detail))
    }

    window.addEventListener(
      'signalJunctionDataChanged',
      handleSignalJunctionDataChanged
    )

    return () => {
      window.removeEventListener(
        'signalJunctionDataChanged',
        handleSignalJunctionDataChanged
      )
    }
  }, [dispatch])

  const renderList = listItems.map((item, index) => {
    return (
      <div className={styles.listItem} key={index}>
        <span className={styles.street}>{item.name}</span>
        <span className={styles.name}>{item.index.toFixed(2)}</span>
        <span className={styles.time}>{item.time}</span>
        <span className={styles.trendBox}>
          <span className={styles.trend}>{item.trend.toFixed(2)}%</span>
          {item.trendDirection ? <UP /> : <DOWN />}
        </span>
      </div>
    )
  })

  return (
    <div className={styles.trafficTrendContainer}>
      <div className={styles.title}>
        <span>信控路口</span>
      </div>
      <div className={styles.mainContent}>
        <div className={styles.rankContainer}>
          <span className={styles.street}>路段名称</span>
          <span className={styles.street}>拥堵里程</span>
          <span className={styles.time}>通行时间</span>
          <span>拥堵趋势</span>
        </div>
        <div className={styles.listContainer}>{renderList}</div>
      </div>
    </div>
  )
}
