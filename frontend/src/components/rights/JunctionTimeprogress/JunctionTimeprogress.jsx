import React, { useEffect, useRef } from 'react'
import * as echarts from 'echarts'
import styles from './index.module.scss'
import { useSelector, useDispatch } from 'react-redux'
import {
  setTrafficState,
  mergeTrafficState,
} from 'stores/storesNewUI/junctionTimeprogressSlice'
import optIcon from 'assets/image/opt-icon.png'
import manualIcon from 'assets/image/Frame.png'
import download from 'assets/image/download.png'
import { downloadFiles } from 'utils/fileDownload'

export default function JunctionTimeprogress() {
  const chartRef = useRef(null)
  const chartInstance = useRef(null)
  const { directionMap, movementMap, trafficState } = useSelector(
    (state) => state.junctionTimeprogress
  )
  const dispatch = useDispatch()
  const OPTclick = () => {
    console.log('一键优化')
    // 示例：这里可以调用后端算法；当前只是再次写入（触发重绘）
    dispatch(mergeTrafficState(trafficState))
  }

  // 方案导出：使用新的下载工具
  const exportPlan = async () => {
    try {
      const results = await downloadFiles({
        fileSource: 'auto', // 自动选择最佳方式
        onProgress: (progress) => {
          console.log(
            `下载进度: ${progress.current}/${progress.total} - ${progress.filename}`
          )
          // 这里可以显示进度条或Toast提示
        },
        onError: (error) => {
          console.error('下载出错:', error)
        },
        onSuccess: (results) => {
          const message = `下载完成！成功: ${results.success}个，失败: ${results.failed}个`
          alert(message)
          if (results.errors.length > 0) {
            console.error('下载错误详情:', results.errors)
          }
        },
      })
    } catch (error) {
      console.error('下载失败:', error)
      alert(`下载失败: ${error.message}`)
    }
  }

  console.log('trafficState from slice:', trafficState)

  // directionMap & movementMap 现在来自 Redux，可在运行时调整

  // 转换数据为echarts格式
  const prepareChartData = () => {
    const categories = []
    const redData = []
    const yellowData = []
    const greenData = []

    Object.entries(trafficState).forEach(([direction, movements]) => {
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

  // WebSocket监听文件更新的useEffect
  useEffect(() => {
    let ws = null

    const connectWebSocket = () => {
      try {
        ws = new WebSocket('ws://localhost:8080/ws/files')

        ws.onmessage = (event) => {
          try {
            const data = JSON.parse(event.data)
            if (data.type === 'fileGenerated' && data.files) {
              console.log('收到新文件推送:', data.files)
              // 更新全局文件列表
              window.__OUTPUT_FILE_LIST = data.files
              // 可以在这里触发UI更新或显示提示
            }
          } catch (e) {
            console.warn('WebSocket消息解析失败:', e)
          }
        }

        ws.onerror = (error) => {
          console.warn('WebSocket连接失败:', error)
        }

        ws.onclose = () => {
          console.info('WebSocket连接已关闭')
        }
      } catch (e) {
        console.info('WebSocket不可用，使用轮询方式')
      }
    }

    // 尝试连接WebSocket
    connectWebSocket()

    return () => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close()
      }
    }
  }, [])

  // 事件监听和全局函数注册的useEffect
  useEffect(() => {
    const handleJunctionTimeprogressChanged = (event) => {
      const payload = event.detail
      console.log('junctionTimeprogressChanged event:', payload)
      dispatch(setTrafficState(payload))
    }
    window.addEventListener(
      'junctionTimeprogressChanged',
      handleJunctionTimeprogressChanged
    )

    // 暴露全局函数，允许直接传对象（无需包装 CustomEvent）
    window.junctionTimeprogressChanged = (payload, { merge = false } = {}) => {
      if (merge) {
        dispatch(mergeTrafficState(payload))
      } else {
        dispatch(setTrafficState(payload))
      }
    }

    return () => {
      window.removeEventListener(
        'junctionTimeprogressChanged',
        handleJunctionTimeprogressChanged
      )
    }
  }, [dispatch])

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
  }, [trafficState, directionMap, movementMap])

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
        <div className={styles.download} onClick={exportPlan}>
          <img src={download} alt='' />
          <span className={styles.text}>方案导出</span>
        </div>
      </div>
      <main className={styles.progressMain}>
        <div className={styles.modeContainer}>
          <div className={styles.opt} onClick={OPTclick}>
            <img src={optIcon} alt='' />
            <span>一键优化</span>
          </div>
          <div className={styles.manual}>
            <img src={manualIcon} alt='' />
            <span>手动控制</span>
          </div>
        </div>
        <div
          ref={chartRef}
          className={styles.chartContainer}
          style={{ width: '100%', height: '300px' }}
        />
      </main>
    </div>
  )
}
