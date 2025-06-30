import React from 'react'
import styles from './index.module.scss'
import BTNICON from 'assets/image/Frame.png'

export default function RightTopBtn({ headerTitle }) {
  return (
    <div className={styles.RightTopBtn}>
      <img src={BTNICON} alt='' />
      <span className={styles.title}>{headerTitle}</span>
    </div>
  )
}
