import React, { useEffect } from 'react'
import styles from './index.module.scss'
import * as echarts from 'echarts'
import { useDispatch, useSelector } from 'react-redux'
import { setOptSelectSeries } from 'stores/storesNewUI/optselectSlice.js'

export default function OptGraph() {
  const dispatch = useDispatch()
  const { current, weekly, timeLabels } = useSelector(
    (state) => state.optSelect
  )

  // 监听速度数据事件（新的 optSelectChanged）；兼容旧的 lightTimerChanged 仅带 series 的情况
  useEffect(() => {
    const handleSpeedChange = (event) => {
      const key = Object.keys(event.detail)[0]
      const payload = event.detail[key] || {}
      dispatch(setOptSelectSeries(payload))
    }
    window.addEventListener('optSelectChanged', handleSpeedChange)

    return () => {
      window.removeEventListener('optSelectChanged', handleSpeedChange)
    }
  }, [dispatch])

  // 绘图
  useEffect(() => {
    const barEl = document.getElementById('speedbarchart1')

    if (!barEl) return
    const barChart = echarts.init(barEl)

    const barGraphOption = {
      title: {
        text: '单位：分钟',
        textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'normal' },
      },
      legend: {
        show: true,
        data: ['优化前', '优化后'],
        textStyle: { color: '#FFF' },
        top: 0,
        right: 0,
      },
      xAxis: {
        type: 'category',
        data: timeLabels,
        axisLine: { lineStyle: { color: '#ccc' } },
        // 取消旋转，居中显示，支持自动换行（通过替换空格或-为换行）
        axisLabel: {
          rotate: 0,
          interval: 0, // 强制全部显示
          color: '#FFF',
          align: 'center',
          width: 60, // 控制换行宽度
          overflow: 'break', // ECharts v5 支持
          lineHeight: 18,
          fontSize: 14,
          formatter: (value) => {
            if (typeof value !== 'string') return value
            // 优先按空格/短横拆分，其次在过长时手动插入换行
            let v = value.replace(/[\s\-]+/g, '\n')
            if (v.length > 12 && !v.includes('\n')) {
              // 简单强制分段
              const mid = Math.floor(v.length / 2)
              v = v.slice(0, mid) + '\n' + v.slice(mid)
            }
            return v
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: '#ccc' } },
        axisTick: { show: true, length: 5 },
        splitNumber: 3,
        splitLine: { lineStyle: { color: '#777' } },
        splitArea: {
          show: true,
          areaStyle: { color: ['rgba(56,67,87,0.1)', 'rgba(56,67,87,0.1)'] },
        },
      },
      grid: { left: '8%', right: '4%', bottom: '15%', containLabel: true },
      series: [
        {
          name: '优化前',
          data: current,
          type: 'bar',
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
            { offset: 0, color: '#00F2FF20' },
            { offset: 1, color: '#00F2FF' },
          ]),
          barWidth: '15%',
        },
        {
          name: '优化后',
          data: weekly,
          type: 'bar',
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
            { offset: 0, color: '#0EFFB020' },
            { offset: 1, color: '#0EFFB0' },
          ]),
          barWidth: '15%',
        },
      ],
    }

    barChart.setOption(barGraphOption)

    return () => {
      barChart.dispose()
    }
  }, [current, weekly, timeLabels])

  return (
    <div className={styles.trafficVolume}>
      {/* <div className={styles.title}>
        <span>交通速度 </span>
      </div> */}
      <div className={styles.contentContainer}>
        <div className={styles.chartContainer}>
          <div
            id='speedbarchart1'
            style={{ width: '100%', height: '320px' }}
          ></div>
        </div>
      </div>
    </div>
  )
}
