import React, { useEffect } from 'react';
import './index.scss';
import * as echarts from 'echarts';

export default function TrafficJam() {
    useEffect(() => {
        const chartDom = document.getElementById('jam-guage');
        const myChart = echarts.init(chartDom);

        let option = {
            series: [
                {
                    type: 'gauge',
                    startAngle: 180,
                    endAngle: 0,
                    min: 0,
                    max: 7,
                    splitNumber: 7,
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
                    progress: {
                        show: true,
                        roundCap: true,
                        width: 8
                    },
                    pointer: {
                        width: 5,
                        length: '70%',
                        offsetCenter: [0, '-5%'],
                        itemStyle: {
                            color: '#ccc'
                        }
                    },
                    axisLine: {
                        roundCap: true,
                        lineStyle: {
                            width: 8
                        }
                    },
                    splitLine: {
                        show: false,
                        length: 12,
                        lineStyle: {
                            width: 2,
                            color: '#999'
                        }
                    },
                    axisLabel: {
                        show: false,
                        distance: 30,
                        color: 'red',
                        fontSize: 20
                    },
                    title: {
                        show: false,
                    },
                    detail: {
                        width: '60%',
                        lineHeight: 40,
                        height: 40,
                        borderRadius: 8,
                        offsetCenter: [0, '25%'],
                        valueAnimation: true,
                        formatter: function (value) {
                            if (value < 2) return "拥堵";
                            if (value >= 2 && value <= 4) return "正常";
                            return "畅通";
                        },
                    },
                    data: [
                        {
                            value: 4.3
                        }
                    ]
                }
            ]
        };

        option && myChart.setOption(option);



    }, []);


    return (
        <div className="traffic-jam-container" >
            <div id="jam-guage"></div>
            <div className="text"></div>
        </div>
    )
}
