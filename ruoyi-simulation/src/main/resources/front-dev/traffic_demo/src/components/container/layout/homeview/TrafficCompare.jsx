import React, { useEffect, useState } from 'react';
import styles from './css/trafficCompare.module.scss';
import * as echarts from 'echarts';

export default function AverageDelay() {
    const generateRandomArray = (length, min, max) => {
        return Array.from(new Array(length), () => {
            return Math.floor(Math.random() * (max - min)) + min;
        });
    };



    // console.log(randomCurrent, randomToday);
    useEffect(() => {
        const currentHour = new Date().getHours();
        console.log(currentHour);
        const trafficFlowLineChart = echarts.init(document.getElementById('delay-content'));
        // console.log('rendering');
        const updateChart = () => {
            const randomCurrent = generateRandomArray(currentHour, 10, 30);
            const randomToday = generateRandomArray(24, 0, 20);
            console.log(randomCurrent, randomToday);
            const lineGraphOption = {
                title: {
                    text: '单位：分钟',
                    textStyle: { color: '#fff', fontSize: 13, fontWeight: 'normal' },
                    top: 10,
                },
                legend: {
                    data: ['当前流量', '周均流量'],
                    icon: 'pin',
                    textStyle: { color: '#fff' },
                    right: 0,
                    top: 10,
                },
                grid: {
                    left: '1%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    axisLabel: {
                        interval: 2,
                    },
                    type: 'category',
                    boundaryGap: false,
                    data: ['01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00', '24:00'],
                    axisLine: { lineStyle: { color: '#fff' } },
                },
                yAxis: {
                    splitNumber: 4,
                    axisLine: { lineStyle: { color: '#fff' } },
                    max: 30,
                    splitLine: {
                        show: false,
                        lineStyle: {
                            color: '#777'  // Color of the horizontal grid lines
                        }
                    },
                    splitArea: {
                        show: true,
                        areaStyle: {
                            color: ['rgba(56, 67, 87,0.1)', 'rgba(56, 67, 87,0.1)']
                        }
                    }

                },
                series: [

                    {
                        name: '当前流量',
                        type: 'line',
                        // stack: 'total',
                        color: 'rgb(9, 121, 199)',
                        showSymbol: false,
                        smooth: true,
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,   // x1, y1, x2, y2 - defines the direction of the gradient
                                [
                                    { offset: 0, color: 'rgba(16, 45, 95, 0.8)' },   // starting color
                                    { offset: 1, color: 'rgb(178, 219, 238, 0.3)' }
                                    // ending color
                                ]
                            ),
                        },
                        emphasis: {
                            focus: 'series'
                        },
                        data: randomCurrent
                    },
                    {
                        name: '周均流量',
                        type: 'line',
                        stack: 'total',
                        smooth: true,
                        color: 'rgb(251, 218, 115)',
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,   // x1, y1, x2, y2 - defines the direction of the gradient
                                [
                                    { offset: 0, color: 'rgba(251, 218, 115, 0.8)' },   // starting color
                                    { offset: 1, color: 'rgba(251, 218, 115, 0.2)' }
                                    // ending color
                                ]
                            ),
                        },
                        showSymbol: false,
                        emphasis: {
                            focus: 'series'
                        },
                        data: randomToday
                    },
                ]
            };

            trafficFlowLineChart && trafficFlowLineChart.setOption(lineGraphOption);
        }
        updateChart();
        // const intervalId = setInterval(() => {
        //     updateChart(); // 每3秒重新生成数据并更新图表
        // }, 4000);

        // return () => clearInterval(intervalId);
    }, []);

    return (
        <div className={styles.compare}>
            <div className={styles.title}>交通流速比对</div>
            <div id="delay-content" style={{ width: "100%", height: '100%' }}></div>
        </div>
    )
}