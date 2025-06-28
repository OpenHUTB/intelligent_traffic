import React from 'react'
import styles from './index.module.scss'
import MapIcon from 'assets/image/mapIcon.png'
import ListIcon from 'assets/image/listIcon.png'
export default function FunctionBar() {
  return (
    <div className={styles.functionBar}>
      <div className={styles.img}>
        <img
          className={styles.functionBarImage}
          src={MapIcon}
          alt='Traffic Jam Icon'
        />
      </div>
      <div className={styles.img}>
        <img
          className={styles.functionBarImage}
          src={ListIcon}
          alt='Traffic List Icon'
        ></img>
      </div>
    </div>
  )
}
