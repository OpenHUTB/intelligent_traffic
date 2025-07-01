// WholeIndex.js

import React, { useEffect, useRef } from 'react';
import styles from '../css/wholeIndex.module.scss';
import * as echarts from 'echarts';
import { useSelector, useDispatch } from 'react-redux';
import { setWholeIndex } from 'stores/digitalTwin/wholeIndexSlice';

export default function WholeIndex() {
    const dispatch = useDispatch();
    const congestionIndex = useSelector((state) => state.wholeIndex.congestionIndex);
    const speed = useSelector((state) => state.wholeIndex.speed);

    const indexGuage1Ref = useRef(null);
    const indexGuage2Ref = useRef(null);

    // Initialize charts only once
    useEffect(() => {
        if (!indexGuage1Ref.current) {
            indexGuage1Ref.current = echarts.init(document.getElementById('indexGuage1'));
        }
        if (!indexGuage2Ref.current) {
            indexGuage2Ref.current = echarts.init(document.getElementById('indexGuage2'));
        }
    }, []);

    // Update charts when congestionIndex or speed changes
    useEffect(() => {
        const indexGuage1 = indexGuage1Ref.current;
        const indexGuage2 = indexGuage2Ref.current;

        if (indexGuage1) {
            const option1 = {
                tooltip: {
                    formatter: '{a} <br/>{b} : {c}',
                },
                series: [
                    {
                        name: 'Traffic Index',
                        type: 'gauge',
                        min: 0,
                        max: 10,
                        startAngle: 220,
                        endAngle: -40,
                        splitNumber: 5,
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                                { offset: 0, color: 'green' },
                                { offset: 0.5, color: 'yellow' },
                                { offset: 1, color: '#5cac5f' },
                            ]),
                        },
                        radius: '75%',
                        center: ['51%', '51%'],
                        detail: {
                            formatter: '{value}',
                        },
                        progress: {
                            show: true,
                            roundCap: true,
                            width: 9,
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
                                    offsetCenter: ['-5%', '50%'],
                                    fontSize: 25,
                                    fontWeight: 500,
                                    fontFamily: 'Arial',
                                    color: '#37d7de',
                                    show: true,
                                },
                            },
                        ],
                        axisLine: {
                            lineStyle: {
                                color: [[1, '#aaa']],
                                width: 9,
                            },
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
                                width: 2,
                            },
                        },
                        axisLabel: {
                            distance: 14,
                            fontSize: 14,
                            fontWeight: 300,
                            fontFamily: 'Arial',
                            color: '#fff',
                        },
                        pointer: {
                            width: 3,
                            length: '60%',
                            offsetCenter: [0, '-10%'],
                            itemStyle: {
                                color: '#ccc',
                            },
                        },
                    },
                ],
            };
            indexGuage1.setOption(option1);
        }

        if (indexGuage2) {
            const option2 = {
                grid: {
                    left: '3%',
                    right: '3%',
                    bottom: '3%',
                    top: '10%',
                    containLabel: true,
                },
                tooltip: {
                    formatter: '{a} <br/>{b} : {c}',
                },
                series: [
                    {
                        name: 'Average Speed',
                        type: 'gauge',
                        min: 0,
                        max: 100,
                        startAngle: 220,
                        endAngle: -40,
                        splitNumber: 5,
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                                { offset: 0, color: '#37d7de' },
                                { offset: 0.5, color: '#1c78c2' },
                                { offset: 1, color: 'blue' },
                            ]),
                        },
                        radius: '75%',
                        center: ['51%', '51%'],
                        detail: {
                            formatter: '{value}',
                        },
                        progress: {
                            show: true,
                            roundCap: true,
                            width: 9,
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
                                    offsetCenter: ['-5%', '50%'],
                                    fontSize: 25,
                                    fontWeight: 500,
                                    fontFamily: 'Arial',
                                    color: '#37d7de',
                                    show: true,
                                },
                            },
                        ],
                        axisLine: {
                            lineStyle: {
                                color: [[1, '#aaa']],
                                width: 9,
                            },
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
                                width: 2,
                            },
                        },
                        axisLabel: {
                            distance: 14,
                            fontSize: 14,
                            fontWeight: 300,
                            fontFamily: 'Arial',
                            color: '#fff',
                        },
                        pointer: {
                            width: 3,
                            length: '60%',
                            offsetCenter: [0, '-10%'],
                            itemStyle: {
                                color: '#ccc',
                            },
                        },
                    },
                ],
            };
            indexGuage2.setOption(option2);
        }
    }, [congestionIndex, speed]);

    // Set up event listener for trafficDataChanged event
    useEffect(() => {
        const handleTrafficDataChanged = (event) => {
            console.log('whole index Changed:', event.detail);
            dispatch(setWholeIndex(event.detail));
        };

        window.addEventListener('wholeIndexChanged', handleTrafficDataChanged);

        return () => {
            window.removeEventListener('wholeIndexChanged', handleTrafficDataChanged);
        };
    }, [dispatch]);

    return (
        <div className={styles.trafficJamContainer}>
            <div className={styles.title}>
                <span>交通运行监控</span>
            </div>
            <div className={styles.speedGuageContainer}>
                <div id="indexGuage1"></div>
                <div id="indexGuage2"></div>
            </div>
        </div>
    );
}