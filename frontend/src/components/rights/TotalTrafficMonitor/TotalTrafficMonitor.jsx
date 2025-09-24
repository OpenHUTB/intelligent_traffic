import React, { useEffect, useState } from 'react'
import styles from './index.module.scss'
import * as echarts from 'echarts'
import Pointer from 'assets/image/pointer.png'
import { useSelector, useDispatch } from 'react-redux'
import { setDistrictSpeed } from 'stores/storesNewUI/districtSpeedSlice'

export default function TotalTrafficMonitor() {
  const dispatch = useDispatch()
  const { congestionIndex, speed } = useSelector((state) => state.districtSpeed)
  //监听updateDistrctSpeed事件

  useEffect(() => {
    const handleUpdate = (event) => {
      console.log('Update District Speed:', event.detail)
      dispatch(setDistrictSpeed(event.detail))
    }

    window.addEventListener('updateDistrictSpeed', handleUpdate)

    return () => {
      window.removeEventListener('updateDistrictSpeed', handleUpdate)
    }
  }, [dispatch])

  useEffect(() => {
    const speedGuage1 = echarts.init(document.getElementById('trafficIndex'))
    const speedGuage2 = echarts.init(document.getElementById('trafficSpeed'))

    const option1 = {
      grid: {
        left: '5%',
        right: '3%',
        bottom: '-1%',
        top: '15%',
        containLabel: false,
      },
      tooltip: {
        formatter: '{a} <br/>{b} : {c}%',
      },
      series: [
        {
          name: 'Pressure',
          type: 'gauge',
          min: 0,
          max: 12,
          startAngle: 220,
          endAngle: -40,
          splitNumber: 8,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#00BEDF' },
              { offset: 1, color: '#0EFFB0' },
            ]),
          },
          radius: '85%',
          center: ['51%', '51%'],
          detail: {
            formatter: '{value}',
          },
          progress: {
            show: true,
            roundCap: true,
            width: 12,
          },
          data: [
            {
              value: congestionIndex,
              name: '交通指数',
              title: {
                offsetCenter: ['5%', '80%'],
                fontSize: 14,
                fontWeight: 300,
                fontFamily: 'Arial',
                color: '#fff',
                show: true,
              },
              detail: {
                offsetCenter: ['5%', '50%'],
                fontSize: 18,
                fontWeight: 800,
                fontFamily: 'Arial',
                color: '#FFFFFF',
                show: true,
              },
            },
          ],
          axisLine: {
            lineStyle: {
              color: [[1, '#aaa8']],
              width: 12,
            },
          },
          splitLine: {
            distance: -5,
            length: 10,
            lineStyle: {
              color: '#fff',
              width: 2,
            },
            show: false,
          },
          minorSplitLine: {
            show: true,
            length: 6,
            distance: 111,
            lineStyle: {
              color: '#aaa',
              width: 1,
            },
          },
          axisTick: {
            distance: 8,
            length: 5,
            lineStyle: {
              color: '#fff',
              width: 1,
            },
          },
          axisLabel: {
            distance: -25,
            fontSize: 12,
            fontWeight: 500,
            fontFamily: 'Arial',
            color: '#fff',
          },
          pointer: {
            icon: `image://${Pointer}`,
            length: 160,
            width: 160,
            offsetCenter: ['-00%', '50%'],
            keepAspect: true,
          },
        },
      ],
    }

    const option2 = {
      grid: {
        left: '3%',
        right: '3%',
        bottom: '1%',
        top: '10%',
        containLabel: false,
      },
      tooltip: {
        formatter: '{a} <br/>{b} : {c}%',
      },
      series: [
        {
          name: 'Pressure',
          type: 'gauge',
          min: 0,
          max: 120,
          startAngle: 220,
          endAngle: -40,
          splitNumber: 8,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#00BEDF' },
              { offset: 0.5, color: '#0EFFB0' },
              { offset: 1, color: '#DEAC07' },
            ]),
          },
          radius: '85%',
          center: ['51%', '51%'],
          detail: {
            formatter: '{value}',
          },
          progress: {
            show: true,
            roundCap: true,
            width: 12,
          },
          data: [
            {
              value: speed,
              name: '平均速度',
              title: {
                offsetCenter: ['5%', '80%'],
                fontSize: 14,
                fontWeight: 300,
                fontFamily: 'Arial',
                color: '#fff',
                show: true,
              },
              detail: {
                offsetCenter: ['-5%', '50%'],
                fontSize: 18,
                fontWeight: 800,
                fontFamily: 'Arial',
                color: '#FFFFFF',
                show: true,
              },
            },
          ],
          axisLine: {
            lineStyle: {
              color: [[1, '#aaa8']],
              width: 12,
            },
          },
          splitLine: {
            distance: 1,
            length: 10,
            lineStyle: {
              color: '#fff',
              width: 2,
            },
            show: false,
          },
          minorSplitLine: {
            show: true,
            length: 6,
            distance: 111,
            lineStyle: {
              color: '#aaa',
              width: 1,
            },
          },
          axisTick: {
            distance: 8,
            length: 5,
            lineStyle: {
              color: '#fff',
              width: 1,
            },
          },
          axisLabel: {
            distance: -25,
            fontSize: 12,
            fontWeight: 500,
            fontFamily: 'Arial',
            color: '#fff',
          },
          pointer: {
            icon: `image://${Pointer}`,
            length: 160,
            width: 160,
            offsetCenter: ['-00%', '50%'],
            keepAspect: true,
          },
        },
      ],
    }

    speedGuage1.setOption(option1)
    speedGuage2.setOption(option2)

    // // 设置5秒定时器更新数据
    // const interval = setInterval(() => {
    //   updateData()
    // }, 5000)

    // 清理定时器
    return () => {
      // clearInterval(interval)
      // speedGuage1.dispose()
      // speedGuage2.dispose()
    }
  }, [])

  // 当状态更新时，更新图表数据
  useEffect(() => {
    const speedGuage1 = echarts.getInstanceByDom(
      document.getElementById('trafficIndex')
    )
    const speedGuage2 = echarts.getInstanceByDom(
      document.getElementById('trafficSpeed')
    )

    if (speedGuage1) {
      speedGuage1.setOption(
        {
          series: [
            {
              data: [
                {
                  value: congestionIndex,
                  name: '交通指数',
                  title: {
                    offsetCenter: ['5%', '80%'],
                    fontSize: 14,
                    fontWeight: 300,
                    fontFamily: 'Arial',
                    color: '#fff',
                    show: true,
                  },
                  detail: {
                    offsetCenter: ['5%', '50%'],
                    fontSize: 18,
                    fontWeight: 800,
                    fontFamily: 'Arial',
                    color: '#FFFFFF',
                    show: true,
                  },
                },
              ],
            },
          ],
        },
        false
      )
    }

    if (speedGuage2) {
      speedGuage2.setOption(
        {
          series: [
            {
              data: [
                {
                  value: speed,
                  name: '平均速度',
                  title: {
                    offsetCenter: ['5%', '80%'],
                    fontSize: 14,
                    fontWeight: 300,
                    fontFamily: 'Arial',
                    color: '#fff',
                    show: true,
                  },
                  detail: {
                    offsetCenter: ['-5%', '50%'],
                    fontSize: 18,
                    fontWeight: 800,
                    fontFamily: 'Arial',
                    color: '#FFFFFF',
                    show: true,
                  },
                },
              ],
            },
          ],
        },
        false
      )
    }
  }, [congestionIndex, speed])

  return (
    <div className={styles.trafficJamContainer}>
      <div className={styles.title}>
        <span>区间速度</span>
      </div>
      <div className={styles.speedGuageContainer}>
        <div id='trafficIndex'></div>
        <div id='trafficSpeed'></div>
      </div>
    </div>
  )
}
