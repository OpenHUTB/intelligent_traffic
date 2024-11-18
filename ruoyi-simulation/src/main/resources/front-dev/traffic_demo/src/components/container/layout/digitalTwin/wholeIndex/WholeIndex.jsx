import React, { useEffect, useState } from 'react';
import styles from '../css/wholeIndex.module.scss';
import * as echarts from 'echarts';

export default function WholeIndex() {

    const [congestionIndex, setCongestionIndex] = useState(3);
    const [speed, setSpeed] = useState(50);
    const updateData = () => {
        setCongestionIndex(prev => Number((prev + 0.2).toFixed(1)));
        setSpeed(prev => prev - 3);
    };
    useEffect(() => {

        const interval = setInterval(updateData, 3000);

        // 设置定时器5秒后清除
        const timeout = setTimeout(() => {
            clearInterval(interval);
        }, 5000);
        const indexGuage1 = echarts.init(document.getElementById('indexGuage1'));
        const indexGuage2 = echarts.init(document.getElementById('indexGuage2'));


        const option1 = {

            tooltip: {
                formatter: '{a} <br/>{b} : {c}%'
            },
            series: [
                {
                    name: 'Pressure',
                    type: 'gauge',
                    min: 0,
                    max: 10,
                    startAngle: 220,
                    endAngle: -40,
                    splitNumber: 5,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 1, 0,   // x1, y1, x2, y2 - defines the direction of the gradient
                            [
                                { offset: 0, color: 'green' },   // starting color
                                { offset: 0.5, color: 'yellow' },
                                { offset: 1, color: '#5cac5f' }
                                // ending color
                            ]
                        ),
                    },
                    radius: '75%',
                    center: ['51%', '51%'],
                    detail: {
                        formatter: '{value}'
                    },
                    progress: {
                        show: true,
                        roundCap: true,
                        width: 9
                    },
                    data: [
                        {
                            value: congestionIndex,
                            name: '交通指数',
                            title: {
                                offsetCenter: ['-5%', '80%'],
                                fontSize: 14,
                                fontWeight: 300,
                                fontFamily: 'Arial',
                                color: '#fff',
                                show: true,
                            },
                            detail: {
                                offsetCenter: ["-5%", '50%'],
                                fontSize: 25,
                                fontWeight: 500,
                                fontFamily: 'Arial',
                                color: '#37d7de',
                                show: true,
                            }
                        }
                    ],

                    axisLine: {
                        lineStyle: {
                            color: [[1, '#aaa']],
                            width: 9,
                        }
                    },
                    splitLine: {

                        distance: -7,
                        length: 10,
                        lineStyle: {
                            color: '#fff',
                            width: 2,

                        },

                    },
                    axisTick: {
                        distance: -8,
                        length: 8,
                        lineStyle: {
                            color: '#fff',
                            width: 2
                        },
                    },
                    axisLabel: {
                        distance: 14,
                        fontSize: 14,
                        fontWeight: 300,
                        fontFamily: 'Arial',
                        color: '#fff'
                    },
                    pointer: {
                        width: 3,
                        length: '60%',
                        offsetCenter: [0, '-10%'],
                        itemStyle: {
                            color: '#ccc'
                        }
                    },
                }
            ]
        };
        const option2 = {

            grid: {
                left: '3%',
                right: '3%',
                bottom: '3%',
                top: '10%',
                containLabel: true
            },
            tooltip: {
                formatter: '{a} <br/>{b} : {c}%'
            },
            series: [
                {
                    name: 'Pressure',
                    type: 'gauge',
                    min: 0,
                    max: 100,
                    startAngle: 220,
                    endAngle: -40,
                    splitNumber: 5,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 1, 0,   // x1, y1, x2, y2 - defines the direction of the gradient
                            [
                                { offset: 0, color: '#37d7de' },   // starting color
                                { offset: 0.5, color: '#1c78c2' },
                                { offset: 1, color: 'blue' }
                                // ending color
                            ]
                        ),
                    },
                    radius: '75%',
                    center: ['51%', '51%'],
                    detail: {
                        formatter: '{value}'
                    },
                    progress: {
                        show: true,
                        roundCap: true,
                        width: 9
                    },
                    data: [
                        {
                            value: speed,
                            name: '平均速度',
                            title: {
                                offsetCenter: ['-5%', '80%'],
                                fontSize: 14,
                                fontWeight: 300,
                                fontFamily: 'Arial',
                                color: '#fff',
                                show: true,
                            },
                            detail: {
                                offsetCenter: ["-5%", '50%'],
                                fontSize: 25,
                                fontWeight: 500,
                                fontFamily: 'Arial',
                                color: '#37d7de',
                                show: true,
                            }
                        }
                    ],

                    axisLine: {
                        lineStyle: {
                            color: [[1, '#aaa']],
                            width: 9,
                        }
                    },
                    splitLine: {

                        distance: -7,
                        length: 10,
                        lineStyle: {
                            color: '#fff',
                            width: 2,

                        },

                    },
                    axisTick: {
                        distance: -8,
                        length: 8,
                        lineStyle: {
                            color: '#fff',
                            width: 2
                        },
                    },
                    axisLabel: {
                        distance: 14,
                        fontSize: 14,
                        fontWeight: 300,
                        fontFamily: 'Arial',
                        color: '#fff'
                    },
                    pointer: {
                        width: 3,
                        length: '60%',
                        offsetCenter: [0, '-10%'],
                        itemStyle: {
                            color: '#ccc'
                        }
                    },
                }
            ]
        };


        indexGuage1.setOption(option1);
        indexGuage2.setOption(option2);
        return () => {
            clearInterval(interval);
            clearTimeout(timeout);
        };


    }, [speed, congestionIndex]);


    return (
        <div className={styles.trafficJamContainer} >
            <div className={styles.title}>
                <span>交通运行监控</span>
            </div>
            <div className={styles.speedGuageContainer}>
                <div id="indexGuage1"></div>
                <div id="indexGuage2"></div>

            </div>
        </div >
    )
}
