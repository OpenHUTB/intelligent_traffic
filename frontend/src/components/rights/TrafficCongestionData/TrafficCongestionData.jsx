import React, { useEffect, useRef, useState } from 'react'
import styles from './index.module.scss'

export default function TrafficCongestionData() {
  // 生成今天内的随机时间
  const generateRandomTimeToday = () => {
    const now = new Date()
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    const randomMs = Math.floor(Math.random() * now.getTime() - today.getTime())
    const randomTime = new Date(today.getTime() + randomMs)
    return randomTime.toLocaleTimeString('zh-CN', { hour12: false }).slice(0, 5) // 格式: HH:MM
  }

  const staticListItems = [
    {
      name: '岳麓西大道',
      index: '2',
      distance: '5',
      status: '否',
      speed: '33',
      trend: '-3%',
    },
    {
      name: '旺龙路',
      index: '1',
      distance: '2',
      status: '异常',
      speed: '5',
      trend: '-3%',
    },
    {
      name: '尖山路',
      index: '5',
      distance: '5',
      status: '否',
      speed: '15',
      trend: '-3%',
    },
    {
      name: '望青路',
      index: '6',
      distance: '5',
      status: '否',
      speed: '20',
      trend: '-3%',
    },
    {
      name: '青山路',
      index: '7',
      distance: '5',
      status: '否',
      speed: '62',
      trend: '-3%',
    },
  ]

  const scrollContainer = useRef(null)
  const [currentItem, setCurrentItem] = useState(null)

  useEffect(() => {
    const startAutoScroll = () => {
      const scrollAmount = 2.5 // Adjust for faster/slower scrolling

      const interval = setInterval(() => {
        const container = scrollContainer.current
        if (container) {
          // When you've scrolled to the end of the original content, reset to the top
          if (container.scrollTop >= container.scrollHeight / 2) {
            container.scrollTop = 0 // Set to start without user noticing
          } else {
            container.scrollTop += scrollAmount
          }
        }
      }, 300) // Adjust the interval for faster/slower scrolling

      return interval
    }

    const intervalId = startAutoScroll()

    const highStatusItems = staticListItems.filter(
      (item) => item.status === '极高' || item.status === '高'
    )

    // 函数：随机选择一个对象
    const getRandomItem = () => {
      const randomIndex = Math.floor(Math.random() * highStatusItems.length)
      return highStatusItems[randomIndex]
    }

    // 设置初始显示的对象
    setCurrentItem(getRandomItem())

    // 每 3 秒钟更新一次显示的对象
    const randomItemIntervalId = setInterval(() => {
      setCurrentItem(getRandomItem())
    }, 3000)

    // 清除定时器
    return () => {
      clearInterval(intervalId)
      clearInterval(randomItemIntervalId)
    }
  }, [])

  const renderList = staticListItems.map((item, index) => {
    return (
      <div className={styles.listItem} key={index}>
        <span className={styles.name}>{item.name}</span>
        <span className={styles.positionText}>{item.index}</span>
        <span className={styles.number}>{item.speed}</span>
        <span className={styles.time}>{item.distance}</span>
        <span className={styles.speed}>{item.trend} </span>

        <span
          className={`${styles.status} ${
            item.status.includes('异常') ? styles.red : styles.green
          }`}
        >
          {item.status}
        </span>
      </div>
    )
  })

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
          <span className={styles.trafficDistance}>{`拥堵\n里程`}</span>
          <span className={styles.trafficTrend}>{`拥堵\n趋势`}</span>
          <span>{`告警\n级别`}</span>
        </div>
        <div className={styles.listContainer} ref={scrollContainer}>
          {renderList}
          {/* {renderList} */}
        </div>
      </div>
    </div>
  )
}
