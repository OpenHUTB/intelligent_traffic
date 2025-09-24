import React, { useEffect } from 'react'
import styles from './index.module.scss'
import * as echarts from 'echarts'
import { useDispatch, useSelector } from 'react-redux'
import { applyFlowSpeedUpdate } from 'stores/storesNewUI/flowSpeedSlice'

export default function FlowSpeed() {
  const dispatch = useDispatch()
  const { current, weekly, timeLabels } = useSelector(
    (state) => state.flowSpeed
  )

  // 监听速度数据事件（新的 flowSpeedChanged）；兼容旧的 lightTimerChanged 仅带 series 的情况
  useEffect(() => {
    const handleSpeedChange = (event) => {
      const key = Object.keys(event.detail)[0]
      const payload = event.detail[key] || {}
      dispatch(applyFlowSpeedUpdate(payload))
    }
    window.addEventListener('flowSpeedChanged', handleSpeedChange)
    // 兼容旧事件名（如果仍被调用）
    window.addEventListener('lightTimerChanged', handleSpeedChange)
    return () => {
      window.removeEventListener('flowSpeedChanged', handleSpeedChange)
      window.removeEventListener('lightTimerChanged', handleSpeedChange)
    }
  }, [dispatch])

  // 绘图
  useEffect(() => {
    const barEl = document.getElementById('speedbarchart')
    const lineEl = document.getElementById('speedlinechart')
    if (!barEl || !lineEl) return
    const barChart = echarts.init(barEl)
    const lineChart = echarts.init(lineEl)

    const barGraphOption = {
      title: {
        text: '单位：分钟',
        textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'normal' },
      },
      legend: {
        show: true,
        data: ['当前速度', '周均速度'],
        textStyle: { color: '#FFF' },
        top: 0,
        right: 0,
      },
      xAxis: {
        type: 'category',
        data: timeLabels,
        axisLine: { lineStyle: { color: '#ccc' } },
        axisLabel: { rotate: 45, textStyle: { color: '#ccc' } },
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
          name: '当前速度',
          data: current,
          type: 'bar',
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
            { offset: 0, color: '#00F2FF20' },
            { offset: 1, color: '#00F2FF' },
          ]),
          barWidth: '35%',
        },
        {
          name: '周均速度',
          data: weekly,
          type: 'bar',
          color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
            { offset: 0, color: '#0EFFB020' },
            { offset: 1, color: '#0EFFB0' },
          ]),
          barWidth: '35%',
        },
      ],
    }

    const lineGraphOption = {
      title: {
        text: '单位：分钟',
        textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'normal' },
      },
      legend: {
        show: true,
        data: ['当前速度', '周均速度'],
        textStyle: { color: '#FFF' },
      },
      xAxis: {
        type: 'category',
        data: timeLabels,
        axisLine: { lineStyle: { color: '#ccc' } },
        splitNumber: 4,
        splitLine: { lineStyle: { color: '#ccc' } },
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
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      series: [
        {
          name: '当前速度',
          data: current,
          type: 'line',
          smooth: true,
          showSymbol: false,
          color: '#00F2FF',
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#37F2FF6B' },
              { offset: 1, color: '#4A83CE00' },
            ]),
          },
        },
        {
          name: '周均速度',
          data: weekly,
          type: 'line',
          smooth: true,
          color: '#0EFFB0',
          showSymbol: false,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#0EFFB0' },
              { offset: 1, color: '#90FF8610' },
            ]),
          },
        },
      ],
    }

    barChart.setOption(barGraphOption)
    lineChart.setOption(lineGraphOption)

    return () => {
      barChart.dispose()
      lineChart.dispose()
    }
  }, [current, weekly, timeLabels])

  return (
    <div className={styles.trafficVolume}>
      <div className={styles.title}>
        <span>交通速度 </span>
      </div>
      <div className={styles.contentContainer}>
        <div className={styles.chartContainer}>
          <div
            id='speedbarchart'
            style={{ width: '100%', height: '100%' }}
          ></div>
        </div>
        <div className={styles.chartContainer}>
          <div
            id='speedlinechart'
            style={{ width: '100%', height: '100%' }}
          ></div>
        </div>
      </div>
    </div>
  )
}
