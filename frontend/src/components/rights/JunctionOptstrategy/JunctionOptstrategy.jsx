import React from 'react'
import styles from './index.module.scss'
export default function JunctionOptstrategy() {
  return (
    <div className={styles.junctionOptstrategy}>
      <header>信控优化策略</header>
      <main>
        <div className={styles.preStrategy}>
          <div className={styles.preStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>原方案</span>
          </div>
          <div className={styles.content}>
            工作日场景，晴天模式，手动调控，直行与左转同级控制
          </div>
        </div>
        <div className={styles.currentStrategy}>
          <div className={styles.currentStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>当前方案</span>
          </div>
          <div className={styles.content}>
            周末晚高峰场景，夜晚雨天模式，自动调优，直行优先
          </div>
        </div>
        <div className={styles.optResult}>
          <div className={styles.optResultTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>协调效果跟踪</span>
          </div>
          <div className={styles.content}>通过区间时间与停车次数统计</div>
        </div>
      </main>
    </div>
  )
}
