import React, { useEffect, useState } from 'react'
import styles from './index.module.scss'
import * as echarts from 'echarts'
import Pointer from 'assets/image/pointer.png'

export default function TotalTrafficMonitor() {
  const [congestionDistance, setCongestionDistance] = useState(2)
  const [congestionIndex, setCongestionIndex] = useState(3)
  const [speed, setSpeed] = useState(10)

  const updateData = () => {
    setCongestionDistance((prev) => prev + 0.5)
    setCongestionIndex((prev) => prev + 0.5)
    setSpeed((prev) => prev - 3)
  }

  useEffect(() => {
    const speedGuage1 = echarts.init(document.getElementById('trafficIndex'))
    const speedGuage2 = echarts.init(document.getElementById('trafficSpeed'))

    const option1 = {
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
              value: 5,
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
            distance: 1,
            length: 10,
            lineStyle: {
              color: '#fff',
              width: 2,
            },
            show: false, // 显示刻度线
          },
          minorSplitLine: {
            show: true, // 显示小刻度线
            length: 6, // 小刻度线长度
            distance: 111, // 小刻度线距离
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
            length: 160, // 可以根据图片大小调整
            width: 160, // 可以根据图片大小调整
            // height: 100, // 可以根据图片大小调整
            offsetCenter: ['-00%', '50%'],
            keepAspect: true, // 保持图片比例
          },
        },
      ],
    }

    const option2 = {
      grid: {
        left: '3%',
        right: '3%',
        bottom: '1%', // 统一为第一个图表的值
        top: '10%', // 统一为第一个图表的值
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
            width: 12, // 统一为第一个图表的值
          },
          data: [
            {
              value: 70,
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
                fontSize: 18, // 统一为第一个图表的值
                fontWeight: 800, // 统一为第一个图表的值
                fontFamily: 'Arial',
                color: '#FFFFFF', // 统一为第一个图表的颜色
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
            distance: 1, // 统一为第一个图表的值
            length: 10,
            lineStyle: {
              color: '#fff',
              width: 2,
            },
            show: false, // 统一为第一个图表的设置
          },
          minorSplitLine: {
            show: true, // 添加小刻度线配置与第一个图表一致
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
              color: '#fff', // 统一为第一个图表的颜色
              width: 1,
            },
          },
          axisLabel: {
            distance: -25, // 统一为第一个图表的值
            fontSize: 12, // 统一为第一个图表的值
            fontWeight: 500, // 统一为第一个图表的值
            fontFamily: 'Arial',
            color: '#fff',
          },
          pointer: {
            icon: `image://${Pointer}`,
            length: 160, // 可以根据图片大小调整
            width: 160, // 可以根据图片大小调整
            // height: 100, // 可以根据图片大小调整
            offsetCenter: ['-00%', '50%'],
            keepAspect: true, // 保持图片比例
          },
        },
      ],
    }

    speedGuage1.setOption(option1)
    speedGuage2.setOption(option2)
  }, [])

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
