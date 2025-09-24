import React, { useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import styles from './index.module.scss'
import { setAllOptstrategyText } from 'stores/storesNewUI/junctionOptstrategySlice'
import OptGraph from '../OptGraph/OptGraph'
// 展示：路口信控优化策略三段文字
// 文本来自 redux: state.junctionOptstrategy.{previousStr,currentOptr,result}
export default function OptText({ onClose }) {
  const { previousStr, currentOptr, result } = useSelector(
    (state) => state.junctionOptstrategy
  )

  const dispatch = useDispatch()
  const [metric, setMetric] = useState('averageThroughput')

  const averageThroughput = {
    current: [32, 44, 25, 28, 29, 86],
    weekly: [39, 57, 30, 33, 33, 115],
  }
  const delayTime = {
    current: [203, 236, 147, 154, 190, 616],
    weekly: [157, 173, 114, 129, 154, 388],
  }
  const carLine = {
    current: [8.3, 9.9, 5.3, 6.7, 7.9, 28.6],
    weekly: [6.3, 7, 7, 4.25, 5.32, 6.15, 17.15],
  }
  //在这里监听选择框，如果选择的值发生了变化，就传入不同的数据给graph，通过window的事件

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

  // 选择框变化 -> 调用全局 window.optSelectChanged 传递对应数据
  const handleMetricChange = (e) => {
    const value = e.target.value
    setMetric(value)
    triggerOptSelectChanged(value)
  }

  const triggerOptSelectChanged = (value) => {
    const map = {
      averageThroughput,
      delayTime,
      carLine,
    }
    const data = map[value]
    if (!data) return
    // 方式一：优先调用全局函数（按需求）
    if (
      typeof window !== 'undefined' &&
      typeof window.optSelectChanged === 'function'
    ) {
      window.optSelectChanged({ [value]: data })
    } else {
      // 方式二：如果函数不存在，降级为自定义事件，方便图表监听
      try {
        const evt = new CustomEvent('optSelectChanged', {
          detail: { [value]: data },
        })
        window.dispatchEvent(evt)
      } catch (err) {
        console.warn('optSelectChanged dispatch failed', err)
      }
    }
  }

  // 初始化发送一次默认指标
  useEffect(() => {
    triggerOptSelectChanged(metric)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

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
      <header>
        信控优化策略
        <button
          type='button'
          aria-label='关闭'
          className={styles.closeBtn}
          onClick={() => onClose && onClose()}
        >
          ×
        </button>
      </header>

      <main>
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
            <div className={styles.controlItem}>
              <span className={styles.label}>交通指标选择</span>
              <select
                className={styles.selectBox}
                name='metric'
                value={metric}
                onChange={handleMetricChange}
              >
                <option value='averageThroughput'>平均每分钟吞吐量</option>
                <option value='carLine'>车辆排队长度</option>
                <option value='delayTime'>平均延误时间</option>
              </select>
            </div>
          </div>
          <div className={styles.content}>{renderMultiline(result)}</div>
        </div>
      </main>
      <footer>
        <OptGraph></OptGraph>
      </footer>
    </div>
  )
}
