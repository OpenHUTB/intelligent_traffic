import React, { useEffect } from 'react'
import styles from './index.module.scss'
import * as echarts from 'echarts'
import { useDispatch, useSelector } from 'react-redux'
import { setFlowAnalysisSeries } from 'stores/storesNewUI/flowAnalysisSlice'

export default function FlowAnalysis() {
  const dispatch = useDispatch()
  const { current, weekly, timeLabels } = useSelector(
    (state) => state.flowAnalysis
  )

  // 监听全局事件 flowAnalysisChanged
  useEffect(() => {
    const handler = (event) => {
      const key = Object.keys(event.detail)[0]
      const payload = event.detail[key] || {}
      dispatch(setFlowAnalysisSeries(payload))
    }
    window.addEventListener('flowAnalysisChanged', handler)
    return () => window.removeEventListener('flowAnalysisChanged', handler)
  }, [dispatch])

  useEffect(() => {
    const chartEl = document.getElementById('trafficvolumelinechart')
    if (!chartEl) return
    const trafficFlowLineChart = echarts.init(chartEl)
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
        data: timeLabels,
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
          data: current,
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
          data: weekly,
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
    return () => trafficFlowLineChart.dispose()
  }, [current, weekly, timeLabels])

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
