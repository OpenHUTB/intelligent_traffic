import React, { useEffect, useState, useRef } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import styles from './index.module.scss'
import { setAllTexts } from 'stores/storesNewUI/junctionOptSlice'
// 展示型组件：数据来自 junctionOpt slice
export default function JunctionOpt() {
  const { previousPlanText, currentPlanText, optimizeResultText } = useSelector(
    (state) => state.junctionOpt
  )
  const dispatch = useDispatch()

  useEffect(() => {
    const handleJunctionOptChanged = (event) => {
      console.log('junctionOptChanged', event.detail)
      dispatch(setAllTexts(event.detail))
    }

    window.addEventListener('junctionOptChanged', handleJunctionOptChanged)

    return () => {
      window.removeEventListener('junctionOptChanged', handleJunctionOptChanged)
    }
  }, [dispatch])

  return (
    <div className={styles.junctionOptstrategy}>
      <header className={styles.title}>
        <span>信控优化策略</span>
      </header>
      <main>
        <div className={styles.preStrategy}>
          <div className={styles.preStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>原有配时方案</span>
          </div>
          <div className={styles.content}>{previousPlanText}</div>
        </div>
        <div className={styles.currentStrategy}>
          <div className={styles.currentStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>当前绿波方案</span>
          </div>
          <div className={styles.content}>
            {currentPlanText.split('\n').map((line, idx) => (
              <div key={idx}>{line}</div>
            ))}
          </div>
        </div>
        <div className={styles.optResult}>
          <div className={styles.optResultTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>绿波优化效果</span>
          </div>
          <div className={styles.content}>{optimizeResultText}</div>
        </div>
      </main>
    </div>
  )
}
