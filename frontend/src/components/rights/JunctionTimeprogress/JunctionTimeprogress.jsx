import React, { useEffect, useRef } from 'react'
import * as echarts from 'echarts'
import styles from './index.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import { setLight } from 'stores/junctionLight/lightControlSlice'

export default function JunctionTimeprogress() {
  const chartRef = useRef(null)
  const chartInstance = useRef(null)
  const trafficLights = useSelector((state) => state.lightControl)

  console.log('trafficlights:', trafficLights)
  console.log('trafficlights map:', trafficLights instanceof Map)
  console.log('trafficlights keys:', Object.keys(trafficLights))

  // 方向映射
  const directionMap = {
    north: '北向',
    east: '东向',
    south: '南向',
    west: '西向',
  }

  const movementMap = {
    left: '左转',
    forward: '直行',
  }

  // 转换数据为echarts格式
  const prepareChartData = () => {
    const categories = []
    const redData = []
    const yellowData = []
    const greenData = []

    Object.entries(trafficLights).forEach(([direction, movements]) => {
      Object.entries(movements).forEach(([movement, light]) => {
        const { redDurationTime, greenDurationTime } = light
        const yellowDuration = 3

        const directionName = directionMap[direction] || direction
        const movementName = movementMap[movement] || movement
        const categoryName = `${directionName}${movementName}`

        categories.push(categoryName)
        redData.push(redDurationTime)
        yellowData.push(yellowDuration)
        greenData.push(greenDurationTime)
      })
    })

    return { categories, redData, yellowData, greenData }
  }

  useEffect(() => {
    if (!chartRef.current) return

    // 初始化echarts实例
    if (!chartInstance.current) {
      chartInstance.current = echarts.init(chartRef.current)
    }

    const { categories, redData, yellowData, greenData } = prepareChartData()

    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
        formatter: function (params) {
          const categoryName = params[0].name
          let result = `${categoryName}<br/>`
          params.forEach((param) => {
            const color = param.color
            const seriesName = param.seriesName
            const value = param.value
            result += `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color};"></span>${seriesName}: ${value}秒<br/>`
          })
          return result
        },
      },
      title: {
        text: '时间进度',
        left: '2%',
        textStyle: {
          color: '#ffffff',
          fontSize: 16,
          fontWeight: 'bold',
        },
      },
      grid: {
        left: '15%',
        right: '5%',
        top: '10%',
        bottom: '5%',
        containLabel: false,
      },
      xAxis: {
        type: 'value',
        show: true,
        splitLine: {
          show: true,
          lineStyle: {
            color: '#ffffff50',
            type: 'dashed',
          },
        },
        axisLine: {
          show: false,
          lineStyle: {
            color: '#ffffff',
          },
        },
        axisLabel: {
          show: false,
          color: '#ffffff',
          fontSize: 12,
        },
      },
      yAxis: {
        type: 'category',
        data: categories,
        axisLabel: {
          color: '#ffffff',
          fontSize: 12,
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#ffffff',
          },
        },
        axisTick: {
          show: false,
        },
      },
      series: [
        {
          name: '红灯',
          type: 'bar',
          stack: 'total',
          data: redData,
          itemStyle: {
            color: '#FF483E',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
          barWidth: '10px',
        },
        {
          name: '黄灯',
          type: 'bar',
          stack: 'total',
          data: yellowData,
          itemStyle: {
            color: '#FFCA45',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
        },
        {
          name: '绿灯',
          type: 'bar',
          stack: 'total',
          data: greenData,
          itemStyle: {
            color: '#0EFFB0',
          },
          label: {
            show: false,
            position: 'inside',
            color: '#ffffff',
            fontSize: 12,
            fontWeight: 'bold',
          },
          barHeight: 10,
        },
      ],
    }

    chartInstance.current.setOption(option)

    // 处理窗口大小变化
    const handleResize = () => {
      if (chartInstance.current) {
        chartInstance.current.resize()
      }
    }

    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [trafficLights])

  // 组件卸载时销毁echarts实例
  useEffect(() => {
    return () => {
      if (chartInstance.current) {
        chartInstance.current.dispose()
        chartInstance.current = null
      }
    }
  }, [])

  return (
    <div className={styles.junctionTimeprogress}>
      <div className={styles.title}>
        <span>信控模式管理</span>
      </div>
      <main className={styles.progressMain}>
        <div className={styles.modeContainer}></div>
        <div
          ref={chartRef}
          className={styles.chartContainer}
          style={{ width: '100%', height: '100%' }}
        />
      </main>
    </div>
  )
}
