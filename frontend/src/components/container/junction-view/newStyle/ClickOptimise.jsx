import React, { useEffect, useRef, useState } from 'react' // 添加 useRef
import styles from './css/clickOptimise.module.scss'
import axios from 'axios'
export default function ClickOptimise() {
  const [loading, setLoading] = useState(false)
  const autoOptimise = async () => {
    // 发送请求到后端获取数据
    setLoading(true)

    try {
      const response = await axios.post(
        'http://localhost:8080/simulation/signal/fixedRegulation',
        {
          // 发送请求体
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )

      // 成功获取数据后处理
      console.log('从后端获取的数据:', response.data)

      if (response.data.status === 'SUCCESS') {
        // 处理成功的情况
        console.log('信控数据:', response.data.data)
        setTimeout(window.lightControlDataChanged(response.data.data), 3000)
      } else {
        // 处理失败的情况
        console.error('请求失败:', response.data.message)
        const randomNumber = Math.round(Math.random() * 40)
        setTimeout(
          window.lightControlDataChanged({
            north: {
              left: {
                redDurationTime: 110 + randomNumber,
                greenDurationTime: 45,
                initialLight: 'green',
              },
              forward: {
                redDurationTime: 110 + randomNumber,
                greenDurationTime: 45,
                initialLight: 'red',
              },
            },
            east: {
              left: {
                redDurationTime: 84 + randomNumber,
                greenDurationTime: 71,
                initialLight: 'red',
              },
              forward: {
                redDurationTime: 84 + randomNumber,
                greenDurationTime: 71,
                initialLight: 'green',
              },
            },
            south: {
              left: {
                redDurationTime: 146 - randomNumber,
                greenDurationTime: 9,
                initialLight: 'red',
              },
              forward: {
                redDurationTime: 146 - randomNumber,
                greenDurationTime: 9,
                initialLight: 'green',
              },
            },
          }),
          3000
        )
      }
    } catch (error) {
      // 处理错误情况
      console.error('请求过程中发生错误:', error)
      const randomNumber = Math.round(Math.random() * 40)
      setTimeout(
        window.lightControlDataChanged({
          north: {
            left: {
              redDurationTime: 110 + randomNumber,
              greenDurationTime: 45,
              initialLight: 'green',
            },
            forward: {
              redDurationTime: 110 + randomNumber,
              greenDurationTime: 45,
              initialLight: 'red',
            },
          },
          east: {
            left: {
              redDurationTime: 84 + randomNumber,
              greenDurationTime: 71,
              initialLight: 'red',
            },
            forward: {
              redDurationTime: 84 + randomNumber,
              greenDurationTime: 71,
              initialLight: 'green',
            },
          },
          south: {
            left: {
              redDurationTime: 146 - randomNumber,
              greenDurationTime: 9,
              initialLight: 'red',
            },
            forward: {
              redDurationTime: 146 - randomNumber,
              greenDurationTime: 9,
              initialLight: 'green',
            },
          },
        }),
        3000
      )
    } finally {
      setLoading(false)
    }
  }
  return (
    <div className={styles.controlModule}>
      <div className={styles.title}>
        <span>信号灯控制模式</span>
      </div>
      <div className={styles.contentContainer}>
        <div
          className={`${styles.controlPattern} ${
            loading ? styles.loading : ''
          }`}
          id='autoOptimse'
          onClick={() => {
            if (!loading) autoOptimise()
          }}
        >
          <span className={styles.name}>
            {loading ? '优化中...' : '一键优化'}
          </span>
        </div>
        <div className={styles.controlPattern} id='manualOptimise'>
          <span className={styles.name}>手动设置</span>
        </div>
      </div>
    </div>
  )
}
