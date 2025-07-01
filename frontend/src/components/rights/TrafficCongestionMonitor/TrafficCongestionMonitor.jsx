import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { setInfo } from 'stores/trafficInfoSlice'
import TrafficIcon from 'assets/image/trafficIcon.png'
import styles from './index.module.scss'

export default function TrafficCongestionMonitor() {
  const info = useSelector((state) => state.trafficInfo)
  const dispatch = useDispatch()

  console.log(info)
  useEffect(() => {
    const handleTrafficInfoChange = (event) => {
      console.log(event.detail)
      const newInfo = { ...info, ...event.detail }
      dispatch(setInfo(newInfo))
    }
    window.addEventListener('TrafficInfoChanged', handleTrafficInfoChange)
  }, [info, dispatch])

  const dataItems = [
    { description: '交通拥堵指数', value: info.index, unit: '' },
    { description: '拥堵里程', value: info.road, unit: 'Km' },
    { description: '拥堵路段总数', value: info.congestion, unit: '个' },
    { description: '平均拥堵时长', value: info.time, unit: 'min' },
  ]

  return (
    <div className={styles.overview}>
      <div className={styles.title}>
        <span>交通数据感知</span>
      </div>
      <div className={styles.dataContainer}>
        {dataItems.map((item, index) => (
          <div key={index} className={styles.dataItem}>
            <img className={styles.icon} src={TrafficIcon} alt='Traffic Icon' />
            <div className={styles.descriptionContainer}>
              <span className={styles.description}>{item.description}</span>
              <span className={styles.number}>
                {item.value}
                <span className={styles.unit}>{item.unit}</span>
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
