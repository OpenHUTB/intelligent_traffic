import React, { useEffect } from 'react'
import styles from '../css/specialTarget.module.scss';
import * as echarts from 'echarts';

export default function SpecialTarget() {
    useEffect(() => {
        let chartDom = document.getElementById('main');
        let myChart = echarts.init(chartDom);
        let lineOption;

        let series = [
            {
                barWidth: '8%',
                data: [70, 60, 50], // 假设这是今日的数据
                type: 'bar',
                stack: 'a',
                name: '今日',
                itemStyle: {
                    opacity: 0.7,
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#FFD700' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#FF4500' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    }
                }
            },
            {
                barWidth: '8%',
                data: [], // 假设这是本周的数据
                type: 'bar',
                stack: 'a',
                name: '本周',
                itemStyle: {
                    opacity: 0.7
                }
            },
            {
                barWidth: '8%',
                data: [], // 假设这是本月的数据
                type: 'bar',
                stack: 'a',
                name: '本月',
                itemStyle: {
                    opacity: 0.7
                }
            }

        ];

        lineOption = {
            title: {
                text: 'K988+000 - K988+500',
                textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'bold' },
                top: '5px',
                left: '35%',
                show: false,

            },
            legend: {
                show: true,
                data: ['今日', '本周', '本月'],
                textStyle: { color: '#FFF' },
                top: '25px',
                right: '5%'
            },
            xAxis: {
                type: 'category',
                data: ['公路客运', '旅游客运', '危化品运输'],
                axisLine: { lineStyle: { color: '#fff', opacity: 0.5 } },
            },
            yAxis: {
                type: 'value',
                axisLine: { lineStyle: { color: '#ccc' } },
                splitLine: {
                    lineStyle: { color: '#fff', opacity: 0.7, type: 'dashed' },
                },
                splitNumber: 4,
            },
            series: series
        };

        lineOption && myChart.setOption(lineOption);
    }, []);



    return (
        <div className={styles.specialTarget}>
            <div className={styles.title}>两客一危监控</div>
            <div id="main" style={{ "width": "100%", "height": "300px" }}></div>
        </div>

    )
}
