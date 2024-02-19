import React, { useEffect, useRef } from 'react';
import './index.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import ProgressBar from 'react-bootstrap/ProgressBar';
import * as echarts from 'echarts';

export default function ViolationOverview() {

    const violationList = [
        { name: "已处理", value: 200 },
        { name: "处理中", value: 80 },
        { name: "未处理", value: 40 },
    ]
    const totalValue = violationList.reduce((sum, item) => sum + item.value, 0);
    const renderList = violationList.map((item, index) => {
        return (
            <div className="list-item" key={index}>
                <span className='index'></span>
                <span>{item.name}</span>
                <span className="number">{item.value}<span className="unit">件</span></span>
                <ProgressBar min={0} max={totalValue} now={item.value} />
            </div>
        )

    })


    // set the chart for violation overview
    const chartRef = useRef(null);
    const currentIndexRef = useRef(0);

    useEffect(() => {

        const myChart = echarts.init(chartRef.current);

        const option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                show: false,
                top: '5%',
                left: 'center'
            },
            color: ['#54ff86', '#f2524e', '#999999'],
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: ['75%', '65%'],
                    avoidLabelOverlap: false,
                    label: {
                        show: false,
                        position: 'center',
                        formatter: params => {
                            return `{labelName|${params.name}}\n{labelValue|${params.value}}`;
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
        const updateChart = () => {
            const dataLen = violationList.length;
            const dataIndex = currentIndexRef.current % dataLen;

            myChart.dispatchAction({
                type: 'pieToggleSelect',
                seriesIndex: 0,
                dataIndex: dataIndex
            });

            const newData = violationList.map((item, index) => ({
                ...item,
                label: index === dataIndex ? { show: true } : { show: false }
            }));

            myChart.setOption({
                series: [{
                    data: newData
                }]
            });

            currentIndexRef.current += 1;
        };

        const intervalId = setInterval(updateChart, 2000);

        return () => {
            clearInterval(intervalId);
            myChart.dispose();
        };

    });




    return (
        <div className="violation-overview">
            <section className="violation-data">
                <div className="title">
                    <span>智能感知事件</span>
                </div>
                <div className="data-display">
                    <div className="overview-container">
                        <div className="total">
                            <span className='title'>事件总数</span>
                            <span className="number">{233}<span className="unit">件</span></span>
                        </div>
                        <div className="last-month">
                            <span className='title'>上一周期</span>
                            <span className='number'>183</span>
                        </div>
                        <div className="compare">
                            <span className='title'>环比</span>
                            <span className='number'>1.3%</span>
                        </div>
                    </div>
                    <div className="violation-chart-container">
                        <div id="violation-chart" ref={chartRef}></div>
                        <div className="chart-data-display">
                            {
                                renderList
                            }

                        </div>
                    </div>
                </div>

            </section>

        </div>
    )
}
