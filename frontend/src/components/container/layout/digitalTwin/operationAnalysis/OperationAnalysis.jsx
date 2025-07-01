import React, { useEffect, useRef, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import styles from '../css/optimiseAnalysis.module.scss';
import * as echarts from 'echarts';
import ProgressBar from 'react-bootstrap/ProgressBar';
import { useSelector, useDispatch } from 'react-redux';
import { setOptimiseList, setViolationList } from 'stores/digitalTwin/analysisSlice';

export default function OperationAnalysis() {
    const dispatch = useDispatch();

    // Retrieve violationList and optimiseList from Redux store
    const violationList = useSelector((state) => state.analysis.violationList);
    const optimiseList = useSelector((state) => state.analysis.optimiseList);

    const chartRef2 = useRef(null);
    const chartInstanceRef = useRef(null);

    // Calculate total value for the chart
    const totalValue = violationList.reduce((sum, item) => sum + item.value, 0);

    // Render optimiseList
    const renderList = optimiseList.map((item, index) => (
        <div className={styles.listItem} key={index}>
            <span>{item.name}</span>
            <span className={styles.number}>
                {item.value.toFixed(2)}
                <span className="unit"></span>
            </span>
            <ProgressBar className={styles.progress} min={0} max={10} now={item.value} />
        </div>
    ));

    // Initialize the chart
    useEffect(() => {
        if (chartRef2.current && !chartInstanceRef.current) {
            chartInstanceRef.current = echarts.init(chartRef2.current);

            const option = {
                tooltip: {
                    trigger: 'item',
                },
                legend: {
                    show: true,
                    bottom: '-3%',
                    left: 'center',
                    textStyle: {
                        color: '#fff',
                        fontSize: 12,
                    },
                },
                color: ['#54ff86', '#f2524e'],
                series: [
                    {
                        name: 'Access From',
                        type: 'pie',
                        radius: ['95%', '75%'],
                        avoidLabelOverlap: false,
                        label: {
                            show: true,
                            position: 'center',
                            formatter: (params) => {
                                return `{labelValue|${((params.value / totalValue) * 100).toFixed(2)}%}\n{labelName|在线率}`;
                            },
                            rich: {
                                labelName: {
                                    fontSize: '1rem',
                                    color: '#fff',
                                    height: 40,
                                },
                                labelValue: {
                                    fontSize: '1.8rem',
                                    color: '#fff',
                                    fontWeight: 'bold',
                                },
                            },
                        },
                        selectedMode: 'single',
                        selectedOffset: 13,
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '1rem',
                                fontWeight: 'bold',
                            },
                        },
                        labelLine: {
                            show: true,
                        },
                        data: violationList,
                    },
                ],
            };
            chartInstanceRef.current.setOption(option);
        }
    }, []);

    // Update the chart when violationList changes
    useEffect(() => {
        if (chartInstanceRef.current) {
            const totalValue = violationList.reduce((sum, item) => sum + item.value, 0);
            chartInstanceRef.current.setOption({
                series: [
                    {
                        data: violationList,
                        label: {
                            formatter: (params) => {
                                return `{labelValue|${((params.value / totalValue) * 100).toFixed(0)}%}\n{labelName|在线率}`;
                            },
                        },
                    },
                ],
            });
        }
    }, [violationList]);

    // Set up event listeners for violationListChanged and optimiseListChanged events
    useEffect(() => {
        const handleViolationListChanged = (event) => {
            console.log('Violation List Changed:', event.detail);
            dispatch(setViolationList(event.detail)); // Ensure event.detail is an array
        };

        const handleOptimiseListChanged = (event) => {
            console.log('Optimise List Changed:', event.detail);
            dispatch(setOptimiseList(event.detail)); // Ensure event.detail is an array
        };

        window.addEventListener('violationListChanged', handleViolationListChanged);
        window.addEventListener('optimiseListChanged', handleOptimiseListChanged);

        return () => {
            window.removeEventListener('violationListChanged', handleViolationListChanged);
            window.removeEventListener('optimiseListChanged', handleOptimiseListChanged);
        };
    }, [dispatch]);

    return (
        <div className={styles.violationOverview}>
            <div className={styles.title}>
                <span>运行分析</span>
            </div>
            <section className={styles.violationData}>
                <div id="violationChart2" ref={chartRef2} className={styles.charts}></div>
                <div className={styles.chartDataDisplay}>
                    <div className={styles.listContainer}>{renderList}</div>
                </div>
            </section>
        </div>
    );
}