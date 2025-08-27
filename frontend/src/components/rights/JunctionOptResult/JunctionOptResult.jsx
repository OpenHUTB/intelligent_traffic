import React, { useEffect } from 'react'
import styles from './index.module.scss'
import JunctionOptstrategy from 'components/rights/JunctionOptstrategy/JunctionOptstrategy'
import * as echarts from 'echarts'
import { useSelector, useDispatch } from 'react-redux'
import { setBothFlows } from 'stores/storesNewUI/junctionOptResultSlice'
export default function JunctionOptResult() {
  // 从 redux 获取折线图数据
  const dispatch = useDispatch()
  const { currentFlow, weekAvgFlow } = useSelector(
    (state) => state.junctionOptResult
  )

  useEffect(() => {
    const handleJunctionOptResultChanged = (event) => {
      console.log('junctionOptResultChanged', event.detail)
      dispatch(setBothFlows(event.detail))
    }

    window.addEventListener(
      'junctionOptResultChanged',
      handleJunctionOptResultChanged
    )

    return () => {
      window.removeEventListener(
        'junctionOptResultChanged',
        handleJunctionOptResultChanged
      )
    }
  }, [dispatch])

  useEffect(() => {
    // Initialize the line chart
    const trafficFlowLineChart = echarts.init(
      document.getElementById('junctionOptResult')
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
          data: currentFlow,
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
          name: '周均流量',
          data: weekAvgFlow,
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
    trafficFlowLineChart.setOption(lineGraphOption)
  }, [currentFlow, weekAvgFlow])

  return (
    <div className={styles.trafficVolume}>
      <div className={styles.title}>
        <span>信控优化效果分析</span>
      </div>
      <div className={styles.contentContainer}>
        <div
          id='junctionOptResult'
          style={{ width: '100%', height: '200px' }}
        ></div>
      </div>
      <JunctionOptstrategy />
    </div>
  )
}
