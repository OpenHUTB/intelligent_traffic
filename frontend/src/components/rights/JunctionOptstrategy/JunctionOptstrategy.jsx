import React, { useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import styles from './index.module.scss'
import { setAllOptstrategyText } from 'stores/storesNewUI/junctionOptstrategySlice'
// 展示：路口信控优化策略三段文字
// 文本来自 redux: state.junctionOptstrategy.{previousStr,currentOptr,result}
export default function JunctionOptstrategy() {
  const { previousStr, currentOptr, result } = useSelector(
    (state) => state.junctionOptstrategy
  )

  const dispatch = useDispatch()

  useEffect(() => {
    const handleJunctionOptstrategyChanged = (event) => {
      console.log('junctionOptstrategyChanged', event.detail)
      dispatch(setAllOptstrategyText(event.detail))
    }

    window.addEventListener(
      'junctionOptstrategyChanged',
      handleJunctionOptstrategyChanged
    )

    return () => {
      window.removeEventListener(
        'junctionOptstrategyChanged',
        handleJunctionOptstrategyChanged
      )
    }
  }, [dispatch])

  // 支持换行显示（若字符串中包含 \n）
  const renderMultiline = (text) =>
    text.split('\n').map((line, idx) => (
      <span key={idx}>
        {line}
        {idx !== text.split('\n').length - 1 && <br />}
      </span>
    ))

  return (
    <div className={styles.junctionOptstrategy}>
      <header>信控优化策略</header>
      <main>
        <div className={styles.preStrategy}>
          <div className={styles.preStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>原方案</span>
          </div>
          <div className={styles.content}>{renderMultiline(previousStr)}</div>
        </div>
        <div className={styles.currentStrategy}>
          <div className={styles.currentStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>当前方案</span>
          </div>
          <div className={styles.content}>{renderMultiline(currentOptr)}</div>
        </div>
        <div className={styles.optResult}>
          <div className={styles.optResultTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>协调效果跟踪</span>
          </div>
          <div className={styles.content}>{renderMultiline(result)}</div>
        </div>
      </main>
    </div>
  )
}
