import React from 'react'
import styles from './index.module.scss'
import MapIcon from 'assets/image/mapIcon.png'
import ListIcon from 'assets/image/listIcon.png'
import { useSelector, useDispatch } from 'react-redux'
import { setBigMapShow } from 'stores/junctionLight/mapSlice'

export default function FunctionBar() {
  const bigMapShow = useSelector((state) => state.map.bigMapShow)
  const dispatch = useDispatch()

  const toggleBigMapShow = () => {
    console.log('bigMapShow', bigMapShow)
    dispatch(setBigMapShow(!bigMapShow))
  }

  return (
    <div className={styles.functionBar}>
      <div className={styles.img}>
        <img
          onClick={() => toggleBigMapShow()}
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
