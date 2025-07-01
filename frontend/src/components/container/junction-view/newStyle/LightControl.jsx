import React, { useEffect, useState, useRef } from 'react'
import { ReactComponent as LeftIcon } from 'assets/icon/turnleft.svg'
import { ReactComponent as ForwardIcon } from 'assets/icon/forward.svg'

import { useSelector, useDispatch } from 'react-redux'
import { setLight } from 'stores/junctionLight/lightControlSlice'
import Light from './Light'
import styles from './css/lightControl.module.scss'
import {
  setTimeProgressShow,
  setTimeChangeShow,
} from 'stores/junctionLight/timeShowSlice'
export default function LightControl() {
  // 为每个路口的灯创建状态
  const lights = useSelector((state) => state.lightControl)
  const dispatch = useDispatch()
  const timeProgressDisplay = useSelector(
    (state) => state.timeShow.timeProgressShow
  )
  const timeChangeShow = useSelector((state) => state.timeShow.timeChangeShow)
  const toggleTimeChangeShow = () => {
    console.log('timechangeShow')
    dispatch(setTimeChangeShow(!timeChangeShow))
    dispatch(setTimeProgressShow(!timeProgressDisplay))
  }
  useEffect(() => {
    const handleLightControlDataChanged = (event) => {
      console.log('Light Control Data Changed:', event.detail)
      // event.detail 应为与 initialState 结构相同的数据对象
      dispatch(setLight(event.detail))
    }

    window.addEventListener(
      'lightControlDataChanged',
      handleLightControlDataChanged
    )

    return () => {
      window.removeEventListener(
        'lightControlDataChanged',
        handleLightControlDataChanged
      )
    }
  }, [dispatch])
  const lightComponents = Object.keys(lights).map((direction) => {
    const directionLights = lights[direction]
    return (
      <div key={direction} className={styles[direction]}>
        <div className={styles.lightContainer}>
          <Light
            initialLight={directionLights.left.initialLight}
            redDuration={directionLights.left.redDurationTime * 1000}
            greenDuration={directionLights.left.greenDurationTime * 1000}
          />
          <Light
            initialLight={directionLights.forward.initialLight}
            redDuration={directionLights.forward.redDurationTime * 1000}
            greenDuration={directionLights.forward.greenDurationTime * 1000}
          />
        </div>
        <div className={styles.iconContainer}>
          <LeftIcon />
          <ForwardIcon />
        </div>
        {/* <div className={styles.iconRoadContainer}>
          <LeftIcon />
          <ForwardIcon />
        </div> */}
      </div>
    )
  })
  return (
    <div className={styles.lightControl} onClick={() => toggleTimeChangeShow()}>
      <header className={styles.title}>
        <span>信号灯控制</span>
      </header>
      <section className={styles.controlMain}>{lightComponents}</section>

      {/* <section className={styles.controlMain}>
                {lights.map((light) => (
                    <div key={light.id} className={styles[light.direction]}>
                        <div className={styles.iconContainer}>
                            <LeftIcon />
                            <ForwardIcon />
                            <Light
                                initialLight={light.initialLight}
                                redDuration={light.redDuration}
                                greenDuration={light.greenDuration}
                            />
                        </div>
}
            </section > */}
    </div>
  )
}
