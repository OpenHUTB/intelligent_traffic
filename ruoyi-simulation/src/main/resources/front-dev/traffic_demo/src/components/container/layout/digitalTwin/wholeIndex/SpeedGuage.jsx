import React, { useEffect, useState } from 'react';
import styles from '../css/speedGuage.module.scss';
import * as echarts from 'echarts';

export default function SpeedGuage() {
    const [congestionDistance, setCongestionDistance] = useState(2);
    const [congestionIndex, setCongestionIndex] = useState(3);
    const [speed, setSpeed] = useState(83);
    const updateData = () => {
        setCongestionDistance(prev => prev + 0.5);
        setCongestionIndex(prev => prev + 0.5);
        setSpeed(prev => prev - 3);
    };
    useEffect(() => {
        const speedGuage1 = echarts.init(document.getElementById('speedGuage1'));
        const speedGuage2 = echarts.init(document.getElementById('speedGuage2'));
        const speedGuage3 = echarts.init(document.getElementById('speedGuage3'));
        const speedGuage4 = echarts.init(document.getElementById('speedGuage4'));

        const option1 = {

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
                                { offset: 0, color: 'green' },   // starting color
                                { offset: 0.5, color: 'yellow' },
                                { offset: 1, color: '#5cac5f' }
                                // ending color
                            ]
                        ),
                    },
                    radius: '85%',
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
                            value: 30,
                            name: '望青路',
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
                    radius: '85%',
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
                            value: 70,
                            name: '青山路',
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
        const option3 = {

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
                                { offset: 0, color: 'green' },   // starting color
                                { offset: 0.5, color: 'green' },
                                { offset: 1, color: 'green' }
                                // ending color
                            ]
                        ),
                    },
                    radius: '85%',
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
                            value: 60,
                            name: '旺龙路',
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
        const option4 = {

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
                                { offset: 0, color: 'red' },   // starting color
                                { offset: 0.5, color: 'rgb(231, 209, 9)' },
                                { offset: 1, color: '#5cac5f' }
                                // ending color
                            ]
                        ),
                    },
                    radius: '85%',
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
                            value: 20,
                            name: '岳麓西大道',
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

        speedGuage1.setOption(option1);
        speedGuage2.setOption(option2);
        speedGuage3.setOption(option3);
        speedGuage4.setOption(option4);


    }, []);


    return (
        <div className={styles.trafficJamContainer} >
            <div className={styles.title}>
                <span>区间速度</span>
            </div>
            <div className={styles.speedGuageContainer}>
                <div id="speedGuage1"></div>
                <div id="speedGuage2"></div>
                <div id="speedGuage3"></div>
                <div id="speedGuage4"></div>
            </div>
        </div >
    )
}
