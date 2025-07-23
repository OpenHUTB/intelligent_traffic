import React, { useEffect, useRef } from 'react'
import * as echarts from 'echarts'
import styles from './index.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import { setLight } from 'stores/junctionLight/lightControlSlice'
import optIcon from 'assets/image/opt-icon.png'
import manualIcon from 'assets/image/Frame.png'

export default function JunctionGreenFlow() {
  const chartRef = useRef(null)
  const chartInstance = useRef(null)
  const trafficLights = useSelector((state) => state.lightControl)

  console.log('trafficlights:', trafficLights)
  console.log('trafficlights map:', trafficLights instanceof Map)
  console.log('trafficlights keys:', Object.keys(trafficLights))

  // 路名数组，按照你要求的顺序
  const roadNames = ['尖山路', '旺龙路', '望青路', '岳麓西大道', '天顶路']

  // 转换数据为echarts格式，计算每个方向的偏移量
  const prepareChartData = () => {
    const categories = []
    const redData = []
    const yellowData = []
    const greenData = []
    const offsets = [] // 存储每个方向的x轴偏移量

    let currentOffset = 0
    let roadIndex = 0

    Object.entries(trafficLights).forEach(([direction, movements]) => {
      Object.entries(movements).forEach(([movement, light]) => {
        //只处理前五个数据
        if (roadIndex >= 5) {
          return // 跳过多余的数据
        }
        const { redDurationTime, greenDurationTime } = light
        const yellowDuration = 3

        // 使用路名替代方向和转向名称
        const roadName = roadNames[roadIndex]
        categories.push(roadName)
        roadIndex++

        offsets.push(currentOffset)

        // 保持原来的堆叠数据格式
        redData.push(redDurationTime)
        yellowData.push(yellowDuration)
        greenData.push(greenDurationTime)

        // 计算下一个方向的偏移量（当前方向的总时长）
        currentOffset += redDurationTime + yellowDuration + greenDurationTime
      })
    })

    // 反转数组，让第一个数据在y轴最高点
    categories.reverse()
    redData.reverse()
    yellowData.reverse()
    greenData.reverse()
    offsets.reverse()

    return { categories, redData, yellowData, greenData, offsets }
  }

  useEffect(() => {
    if (!chartRef.current) return

    // 初始化echarts实例
    if (!chartInstance.current) {
      chartInstance.current = echarts.init(chartRef.current)
    }

    const { categories, redData, yellowData, greenData, offsets } =
      prepareChartData()

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
            if (param.seriesName !== '偏移') {
              // 不显示偏移系列的tooltip
              const color = param.color
              const seriesName = param.seriesName
              const value = param.value
              result += `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color};"></span>${seriesName}: ${value}秒<br/>`
            }
          })
          return result
        },
      },

      grid: {
        left: '8%',
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
          show: true,
          lineStyle: {
            color: '#ffffff',
          },
        },
        axisLabel: {
          show: false,
          color: '#ffffff',
          fontSize: 12,
          formatter: '{value}s',
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
            type: 'dashed',
          },
        },
        axisTick: {
          show: false,
        },
      },
      series: [
        // 添加透明的占位系列来实现偏移效果
        {
          name: '偏移',
          type: 'bar',
          stack: 'total',
          data: offsets,
          itemStyle: {
            color: 'transparent', // 透明色
          },
          label: {
            show: false,
          },
          barHeight: 10,
          silent: true, // 不响应鼠标事件
          tooltip: {
            show: false, // 不显示tooltip
          },
        },
        {
          name: '红灯',
          type: 'bar',
          stack: 'total',
          data: redData.map((value, index) => {
            return {
              value: value,
              itemStyle: {
                color: '#FF483E',
              },
            }
          }),
          label: {
            show: true,
            position: 'inside',
            color: '#ffffff',
            fontSize: 10,
          },
          barHeight: 10,
          barWidth: '10px',
        },
        {
          name: '黄灯',
          type: 'bar',
          stack: 'total',
          data: yellowData.map((value, index) => {
            return {
              value: value,
              itemStyle: {
                color: '#FFCA45',
              },
            }
          }),
          label: {
            show: false,
          },
          barHeight: 10,
        },
        {
          name: '绿灯',
          type: 'bar',
          stack: 'total',
          data: greenData.map((value, index) => {
            return {
              value: value,
              itemStyle: {
                color: '#0EFFB0',
              },
            }
          }),
          label: {
            show: false,
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
        <span>当前绿波路段</span>
      </div>
      <main className={styles.progressMain}>
        <div
          ref={chartRef}
          className={styles.chartContainer}
          style={{ width: '100%', height: '250px' }}
        />
      </main>
    </div>
  )
}
