import React, { useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import styles from '../css/optimiseAnalysis.module.scss';
import * as echarts from 'echarts';
import ProgressBar from 'react-bootstrap/ProgressBar';
export default function OperationAnalysis() {

    const violationList = [
        { name: "在线路口", value: 185 },
        { name: "离线路口", value: 15 },
    ]
    const totalValue = violationList.reduce((sum, item) => sum + item.value, 0);

    // set the chart for violation overview
    const chartRef2 = useRef(null);
    const currentIndexRef = useRef(0);
    const optimiseList = [
        { name: "优化路口占比(%)", value: 76 },
        { name: "优化次数(次)", value: 8 },
        { name: "服务车次（万辆）", value: 0.3 },
        { name: "路网车辆 (万辆)", value: 1 },
    ]

    const renderList = optimiseList.map((item, index) => {
        return (
            <div className={styles.listItem} key={index}>
                <span>{item.name}</span>
                <span className={styles.number}>{item.value}<span className="unit"></span></span>
                <ProgressBar className={styles.progress} min={0} max={10} now={item.value} />
            </div>
        )

    })
    useEffect(() => {

        const myChart = echarts.init(chartRef2.current);

        const option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                show: true,
                bottom: '-3%',
                left: 'center',
                textStyle: {
                    color: '#fff',
                    fontSize: 12
                },
            },
            color: ['#54ff86', '#f2524e', '#999999'],
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: ['95%', '75%'],
                    avoidLabelOverlap: false,
                    label: {
                        show: true,
                        position: 'center',
                        formatter: params => {
                            return `{labelValue|${params.value / totalValue * 100}%}\n{labelName|${"在线率"}}`;
                        },
                        rich: {
                            labelName: {
                                fontSize: '1rem', // Font size for the label
                                color: '#fff', // Customize the color as well
                                height: 40,
                            },
                            labelValue: {
                                fontSize: '1.8rem', // Font size for the value
                                color: '#fff',
                                fontWeight: 'bold',

                            }
                        }
                    },
                    selectedMode: 'single',
                    selectedOffset: 13,
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: '1rem',
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: true
                    },
                    data: violationList
                }
            ]
        };
        myChart.setOption(option);



    });

    return (
        <div className={styles.violationOverview}>
            <div className={styles.title}>
                <span>运行分析</span>
            </div>
            <section className={styles.violationData}>

                <div id="violationChart2" ref={chartRef2} className={styles.charts} ></div>
                <div className={styles.chartDataDisplay}>

                    <div className={styles.listContainer}>
                        {renderList}
                    </div>
                </div>
            </section>
        </div>
    )
}
