import React from 'react'
import styles from './index.module.scss'
export default function JunctionOpt() {
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
          <div className={styles.content}>
            路线1，路线2，路线3，绿波持续时间15分钟
          </div>
        </div>
        <div className={styles.currentStrategy}>
          <div className={styles.currentStrategyTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>当前绿波方案</span>
          </div>
          <div className={styles.content}>
            路口1： 红灯40秒，绿灯12秒，黄灯3秒
            <br />
            路口2： 红灯35秒，绿灯15秒，黄灯3秒
          </div>
        </div>
        <div className={styles.optResult}>
          <div className={styles.optResultTitle}>
            <span className={styles.dot}></span>
            <span className={styles.text}>绿波优化效果</span>
          </div>
          <div className={styles.content}>通过区间时间与停车次数统计</div>
        </div>
      </main>
    </div>
  )
}
