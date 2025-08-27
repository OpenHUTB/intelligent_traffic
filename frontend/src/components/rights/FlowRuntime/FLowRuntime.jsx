import React, { useEffect } from 'react'
import styles from './index.module.scss'
import { useDispatch, useSelector } from 'react-redux'
import { setFlowRuntimeData } from 'stores/storesNewUI/flowRuntimeSlice'

export default function FLowRuntime() {
  const dispatch = useDispatch()
  const { runtimeData } = useSelector((state) => state.flowRuntime)

  useEffect(() => {
    const handler = (event) => {
      const payload = event.detail
      if (Array.isArray(payload)) {
        dispatch(setFlowRuntimeData(payload))
        console.log('flowRuntimeChanged array payload =>', payload)
      } else {
        console.warn(
          'flowRuntimeChanged 需要直接数组格式: [{location,totalvolume}]'
        )
      }
    }
    window.addEventListener('flowRuntimeChanged', handler)
    return () => window.removeEventListener('flowRuntimeChanged', handler)
  }, [dispatch])

  const maxVolume =
    runtimeData && runtimeData.length > 0
      ? Math.max(...runtimeData.map((item) => item.totalvolume))
      : 1

  return (
    <div className={styles.FlowRuntimeContainer}>
      <div className={styles.title}>
        <span>实时交通流量监测</span>
      </div>
      <div className={styles.contentContainer}>
        <div className={styles.dataList}>
          {runtimeData &&
            runtimeData.map((item, index) => (
              <div key={index} className={styles.dataItem}>
                <div className={styles.location}>{item.location}</div>
                <div className={styles.progressContainer}>
                  <div
                    className={styles.progressBar}
                    style={{
                      width: `${(item.totalvolume / maxVolume) * 100}%`,
                    }}
                  ></div>
                </div>
                <div className={styles.volume}>{item.totalvolume}</div>
              </div>
            ))}
        </div>
      </div>
    </div>
  )
}
