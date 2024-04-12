import React, { useEffect } from 'react';
import styles from './css/trafficInfo3Guage.module.scss';
import * as echarts from 'echarts';

export default function TrafficInfo3Guage() {
    useEffect(() => {
        const chartDom = document.getElementById('jamGuage');
        const myChart = echarts.init(chartDom);
        let congestionDistance = 5;
        let congestionIndex = 4.3;
        let color = '#f00';
        if (congestionIndex <= 3) {
            color = '#0f0';
        } else if (congestionIndex <= 5) {
            color = '#ff0';
        } else if (congestionIndex <= 7) {
            color = '#f90';
        }
        let speed = 50;
        let option = {
            toolbox: {
                // feature: {
                //     restore: {},
                //     saveAsImage: {}
                // }
            },
            series: [
                // left
                {
                    name: 'gauge 0',
                    type: 'gauge',
                    min: 0,
                    max: 270,
                    startAngle: -43,
                    endAngle: -720,
                    splitNumber: 5,
                    radius: '55%',
                    center: ['22%', '50%'],
                    axisLine: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    anchor: {},
                    pointer: {
                        show: false
                    },
                    detail: {
                        show: false
                    },
                    title: {
                        fontSize: 5,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        color: color,
                        offsetCenter: [0, '-60%']
                    },
                    progress: {
                        show: true,
                        width: 5,
                        itemStyle: {
                            color: '#ccc'
                        }
                    },
                    data: [
                        {
                            value: 0,
                        }
                    ]
                },
                {
                    name: 'gauge 1',
                    type: 'gauge',
                    min: 0,
                    max: 10,
                    startAngle: -140,
                    endAngle: -315,
                    splitNumber: 5,
                    radius: '55%',
                    center: ['22%', '50%'],
                    axisLine: {
                        lineStyle: {
                            color: [[1, '#AE96A6']]
                        }
                    },
                    splitLine: {
                        distance: -7,
                        length: 12,
                        lineStyle: {
                            color: '#fff',
                            width: 4
                        }
                    },
                    axisTick: {
                        distance: -8,
                        length: 8,
                        lineStyle: {
                            color: '#fff',
                            width: 2
                        }
                    },
                    axisLabel: {
                        distance: 14,
                        fontSize: 14,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        color: '#fff'
                    },
                    anchor: {},
                    pointer: {
                        icon: 'path://M-36.5,23.9L-41,4.4c-0.1-0.4-0.4-0.7-0.7-0.7c-0.5-0.1-1.1,0.2-1.2,0.7l-4.5,19.5c0,0.1,0,0.1,0,0.2v92.3c0,0.6,0.4,1,1,1h9c0.6,0,1-0.4,1-1V24.1C-36.5,24-36.5,23.9-36.5,23.9z M-39.5,114.6h-5v-85h5V114.6z',
                        width: 3,
                        length: '60%',
                        offsetCenter: [0, '-15%'],
                        itemStyle: {
                            color: '#f00',
                            shadowColor: 'rgba(255, 0, 0)',
                            shadowBlur: 5,
                            shadowOffsetY: 2
                        }
                    },
                    title: {
                        color: color,
                        fontSize: 14,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        offsetCenter: [0, '25%']
                    },
                    detail: {
                        show: false
                    },
                    data: [
                        {
                            value: congestionDistance,
                            name: `拥堵里程 \n\n ${congestionDistance}`
                        }
                    ]
                },
                // middle
                {
                    name: 'gauge 2',
                    type: 'gauge',
                    min: 1,
                    max: 10,
                    z: 10,
                    startAngle: 210,
                    endAngle: -30,
                    splitNumber: 9,
                    radius: '65%',
                    center: ['50%', '50%'],
                    axisLine: {
                        show: true,
                        lineStyle: {
                            width: 5,
                            color: [
                                [0.4, '#0f0'],
                                [0.7, '00f'],
                                [1, '#f00']
                            ]
                        }
                    },
                    splitLine: {
                        distance: 20,
                        length: 5,
                        lineStyle: {
                            color: 'auto',
                            width: 4,
                            shadowColor: 'rgba(255, 255, 255, 0.5)',
                            shadowBlur: 15,
                            shadowOffsetY: -10
                        }
                    },
                    axisTick: {
                        distance: 10,
                        length: 5,
                        lineStyle: {
                            color: 'auto',
                            width: 2,
                            shadowColor: 'rgba(255, 255, 255)',
                            shadowBlur: 10,
                            shadowOffsetY: -10
                        }
                    },
                    axisLabel: {
                        distance: 10,
                        fontSize: 15,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        color: '#fff'
                    },
                    anchor: {},
                    pointer: {
                        icon: 'path://M-36.5,23.9L-41,4.4c-0.1-0.4-0.4-0.7-0.7-0.7c-0.5-0.1-1.1,0.2-1.2,0.7l-4.5,19.5c0,0.1,0,0.1,0,0.2v92.3c0,0.6,0.4,1,1,1h9c0.6,0,1-0.4,1-1V24.1C-36.5,24-36.5,23.9-36.5,23.9z M-39.5,114.6h-5v-85h5V114.6z',
                        width: 5,
                        offsetCenter: [0, '-20%'],
                        length: '45%',
                        itemStyle: {
                            color: '#f00',
                            shadowColor: 'rgba(255, 0, 0)',
                            shadowBlur: 5,
                            shadowOffsetY: 3
                        }
                    },
                    title: {
                        color: color,
                        fontSize: 18,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        offsetCenter: [0, 0]
                    },
                    data: [
                        {
                            value: congestionIndex,
                            name: '拥堵指数'
                        }
                    ],
                    detail: {
                        show: false
                    }
                },
                {
                    name: 'gauge 3',
                    type: 'gauge',
                    min: 0,
                    max: 8,
                    z: 10,
                    splitNumber: 8,
                    radius: '62%',
                    startAngle: 210,
                    endAngle: -30,
                    axisLine: {
                        lineStyle: {
                            width: 7,
                            color: [[1, '#444']]
                        }
                    },
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    anchor: {},
                    pointer: {
                        show: false
                    },
                    title: {
                        show: false
                    },
                    detail: {
                        offsetCenter: [0, '50%'],
                        formatter: '{a|{value}}',
                        rich: {
                            a: {
                                fontSize: 20,
                                fontWeight: 800,
                                fontFamily: 'Arial',
                                color: color,
                                align: 'center',
                                padding: [0, 10, 0, 0]
                            },

                        }
                    },
                    // value is speed
                    data: [
                        {
                            value: congestionIndex,
                            name: ''
                        }
                    ]
                },
                // right
                {
                    name: 'gauge 4',
                    type: 'gauge',
                    min: 0,
                    max: 120,
                    startAngle: 0,
                    endAngle: 0,
                    splitNumber: 8,
                    radius: '55%',
                    center: ['79%', '50%'],
                    axisLine: {
                        lineStyle: {
                            color: [[1, '#AE96A6']]
                        }
                    },
                    splitLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    anchor: {},
                    pointer: {
                        show: false
                    },
                    title: {},
                    detail: {
                        show: false,
                        offsetCenter: ['25%', '50%'],
                        formatter: '{a|{value}}{b|km/h}',
                        rich: {
                            a: {
                                fontSize: 15,
                                fontWeight: 800,
                                fontFamily: 'Arial',
                                color: '#fff',
                                align: 'center',
                                padding: [0, 5, 0, 0]
                            },
                            b: {
                                fontSize: 14,
                                fontWeight: 800,
                                fontFamily: 'Arial',
                                color: '#fff',
                                padding: [0, 0, 20, 0]
                            }
                        }
                    },
                    progress: {
                        show: true,
                        width: 3,
                        itemStyle: {
                            color: '#CCC'
                        }
                    },
                    data: [
                        {
                            value: 120,
                        }
                    ]
                },
                {
                    name: 'gauge 6',
                    type: 'gauge',
                    min: -120,
                    max: 0,
                    startAngle: -40,
                    endAngle: 150,
                    clockwise: false,
                    splitNumber: 4,
                    radius: '55%',
                    center: ['79%', '50%'],
                    axisLine: {
                        lineStyle: {
                            color: [
                                [1, '#AE96A6'],
                                [1.1, '#f00']
                            ]
                        }
                    },
                    splitLine: {
                        distance: -7,
                        length: 12,
                        lineStyle: {
                            color: '#fff',
                            width: 4
                        }
                    },
                    axisTick: {
                        splitNumber: 5,
                        length: 8,
                        distance: -8,
                        lineStyle: {
                            color: '#fff',
                            width: 2
                        }
                    },
                    axisLabel: {
                        distance: 14,
                        fontSize: 14,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        color: '#fff',
                        formatter: function (value) {
                            return -value + '';
                        }
                    },
                    anchor: {
                        show: false,
                        itemStyle: {},
                        offsetCenter: [0, '55%'],
                        size: 20,
                    },
                    pointer: {
                        show: true,
                        icon: 'path://M-36.5,23.9L-41,4.4c-0.1-0.4-0.4-0.7-0.7-0.7c-0.5-0.1-1.1,0.2-1.2,0.7l-4.5,19.5c0,0.1,0,0.1,0,0.2v92.3c0,0.6,0.4,1,1,1h9c0.6,0,1-0.4,1-1V24.1C-36.5,24-36.5,23.9-36.5,23.9z M-39.5,114.6h-5v-85h5V114.6z',
                        width: 3,
                        length: '60%',
                        offsetCenter: [0, '-15%'],
                        itemStyle: {
                            color: '#f00'
                        }
                    },
                    title: {
                        color: color,
                        fontSize: 14,
                        fontWeight: 800,
                        fontFamily: 'Arial',
                        offsetCenter: [0, '25%']
                    },
                    detail: {
                        show: false
                    },
                    data: [
                        {
                            value: -speed,
                            name: `平均速度 \n\n ${speed}`
                        }
                    ]
                }
            ]
        };

        option && myChart.setOption(option);



    }, []);


    return (
        <div className={styles.trafficJamContainer} >
            <div className={styles.title}>
                <span>交通运行指数</span>
            </div>
            <div id="jamGuage" style={{ width: "100%", height: "100%" }}></div>

            {/* <div className={styles.text}>
                <h3>交通拥堵指数</h3>
                <span className={styles.number}>{4.3}</span><span className={styles.unit}>分</span>
            </div> */}
        </div >
    )
}
