import React, { useEffect } from 'react';
import './index.scss';
import * as echarts from 'echarts';

export default function AverageDelay() {
    const generateRandomArray = (length, min, max) => {
        return Array.from(new Array(length), () => {
            return Math.floor(Math.random() * (max - min)) + min;
        });
    };

    const randomCurrent = generateRandomArray(6, 0, 30);
    const randomToday = generateRandomArray(6, 10, 25);
    // console.log(randomCurrent, randomToday);
    useEffect(() => {

        const trafficFlowLineChart = echarts.init(document.getElementById('delay-content'));
        // console.log('rendering');
        const lineGraphOption = {
            title: {
                text: '单位：分钟',
                textStyle: { color: '#b2dbee', fontSize: 13, fontWeight: 'normal' },
                top: 10,
            },
            legend: {
                data: ['今天', '明天'],
                icon: 'pin',
                textStyle: { color: '#ccc' },
                right: 0,
                top: 10,
            },
            grid: {
                left: '1%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'],
                axisLine: { lineStyle: { color: '#ccc' } },
            },
            yAxis: {
                splitNumber: 4,
                axisLine: { lineStyle: { color: '#ccc' } },
                max: 50,
                type: 'value',
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: '#777'  // Color of the horizontal grid lines
                    }
                },
                splitArea: {
                    show: true,
                    areaStyle: {
                        color: ['rgba(56, 67, 87,0.7)', 'rgba(56, 67, 87,0.2)']
                    }
                }

            },
            series: [
                {
                    name: '今天',
                    type: 'line',
                    stack: 'total',
                    smooth: true,
                    color: 'rgb(251, 218, 115)',
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,   // x1, y1, x2, y2 - defines the direction of the gradient
                            [
                                { offset: 0, color: 'rgb(251, 218, 115, 0.8)' },   // starting color
                                { offset: 1, color: 'rgb(251, 218, 115, 0.2)' }
                                // ending color
                            ]
                        ),
                    },
                    showSymbol: true,
                    emphasis: {
                        focus: 'series'
                    },
                    data: randomCurrent
                },
                {
                    name: '明天',
                    type: 'line',
                    stack: 'total',
                    color: 'rgb(9, 121, 199)',
                    showSymbol: true,
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
                    data: randomToday
                }
            ]
        };

        trafficFlowLineChart && trafficFlowLineChart.setOption(lineGraphOption);

    });

    return (
        <div className="average-delay">
            <div className="title">平均延误</div>
            <div id="delay-content"></div>
        </div>
    )
}
