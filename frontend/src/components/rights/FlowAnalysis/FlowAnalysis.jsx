import React, { useEffect, useState } from 'react'
import styles from './index.module.scss'

import * as echarts from 'echarts'

export default function FlowAnalysis() {
  const generateRandomArray = (length, min, max) => {
    return Array.from(
      { length },
      () => Math.floor(Math.random() * (max - min + 1)) + min
    )
  }

  const [randomCurrent, setRandomCurrent] = useState(
    generateRandomArray(12, 30, 90)
  )
  const [randomToday, setRandomToday] = useState(generateRandomArray(12, 0, 60))
  useEffect(() => {
    window.addEventListener('lightTimerChanged', (event) => {
      const key = Object.keys(event.detail)[0]
      const isGreen = event.detail[key].isGreen
      //   const index = parseInt(key.match(/\d+/)[0]) - 1
      //   setActiveIndex(index)

      if (isGreen) {
        setRandomCurrent(generateRandomArray(12, 0, 70))
        setRandomToday(generateRandomArray(12, 0, 50))
      } else {
        setRandomCurrent(generateRandomArray(12, 0, 120))
        setRandomToday(generateRandomArray(12, 0, 90))
      }
    })
  }, [randomCurrent, randomToday])
  useEffect(() => {
    // Initialize the line chart
    const trafficFlowLineChart = echarts.init(
      document.getElementById('trafficvolumelinechart')
    )
    // console.log('rendering');
    const lineGraphOption = {
      title: {
        text: '单位：分钟',
        textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'normal' },
      },
      legend: {
        show: true,
        data: ['当前流量', '周均流量'],
        textStyle: { color: '#FFF' },
        top: '0',
        right: '0',
      },
      xAxis: {
        type: 'category',
        data: [
          '00:00',
          '01:00',
          '02:00',
          '03:00',
          '04:00',
          '05:00',
          '06:00',
          '07:00',
          '08:00',
          '09:00',
          '10:00',
          '11:00',
          '12:00',
        ],
        axisLine: { lineStyle: { color: '#ccc' } },
        splitNumber: 4,
        splitLine: {
          lineStyle: {
            color: '#ccc', // Color of the horizontal grid lines
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: '#ccc' } },
        axisTick: {
          show: true,
          length: 5,
        },
        splitNumber: 3,
        splitLine: {
          lineStyle: {
            color: '#777', // Color of the horizontal grid lines
          },
        },
        splitArea: {
          show: true,
          areaStyle: {
            color: ['rgba(56, 67, 87,0.1)', 'rgba(56, 67, 87,0.1)'],
          },
        },
      },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      series: [
        {
          name: '当前流量',
          data: randomCurrent,
          type: 'line',
          smooth: true,
          showSymbol: false,
          color: '#00F2FF',
          areaStyle: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1, // x1, y1, x2, y2 - defines the direction of the gradient
              [
                { offset: 0, color: '#37F2FF6B' }, // starting color
                { offset: 1, color: '#4A83CE00' },
                // ending color
              ]
            ),
          },
        },
        {
          name: '周均流量',
          data: randomToday,
          type: 'line',
          smooth: true,
          color: '#0EFFB0',
          showSymbol: false,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1, // x1, y1, x2, y2 - defines the direction of the gradient
              [
                { offset: 0, color: '#0EFFB0' }, // starting color
                { offset: 1, color: '#90FF8610' },
                // ending color
              ]
            ),
          },
        },
      ],
    }
    trafficFlowLineChart.setOption(lineGraphOption)
  }, [randomCurrent, randomToday])

  return (
    <div className={styles.trafficVolume}>
      <div className={styles.title}>
        <span>路口交通流量</span>
      </div>
      <div className={styles.contentContainer}>
        <div
          id='trafficvolumelinechart'
          style={{ width: '100%', height: '100%' }}
        ></div>
      </div>
    </div>
  )
}
