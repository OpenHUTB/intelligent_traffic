import React, { useEffect } from 'react'
import styles from './css/trafficSpeed.module.scss';
import * as echarts from 'echarts';
import { type } from '@testing-library/user-event/dist/type';
export default function TrafficSpeed() {
    useEffect(() => {
        let chartDom = document.getElementById('main');
        let myChart = echarts.init(chartDom);
        let lineOption;

        let series = [
            {
                barWidth: '8%',
                data: [70, 60, 50, 80],
                type: 'bar',
                stack: 'a',
                name: '平均速度',
                itemStyle: {
                    opacity: 0.7
                }
            },
            {
                barWidth: '8%',
                data: [10, 46, 64, 5],
                type: 'bar',
                stack: 'b',
                name: '平均车间距',
                itemStyle: {
                    opacity: 0.7
                }
            },
            {
                barWidth: '8%',
                data: [30, 5, 10, 20],
                type: 'bar',
                stack: 'c',
                name: '空间占有率',
                itemStyle: {
                    opacity: 0.7
                }
            },
            {
                barWidth: '8%',
                data: [30, 5, 5, 20],
                type: 'bar',
                stack: 'd',
                name: '平均车头时距',
                itemStyle: {
                    opacity: 0.7
                }
            },
        ];
        const stackInfo = {};
        for (let i = 0; i < series[0].data.length; ++i) {
            for (let j = 0; j < series.length; ++j) {
                const stackName = series[j].stack;
                if (!stackName) {
                    continue;
                }
                if (!stackInfo[stackName]) {
                    stackInfo[stackName] = {
                        stackStart: [],
                        stackEnd: []
                    };
                }
                const info = stackInfo[stackName];
                const data = series[j].data[i];
                if (data && data !== '-') {
                    if (info.stackStart[i] == null) {
                        info.stackStart[i] = j;
                    }
                    info.stackEnd[i] = j;
                }
            }
        }
        for (let i = 0; i < series.length; ++i) {
            const data = series[i].data;
            const info = stackInfo[series[i].stack];
            for (let j = 0; j < series[i].data.length; ++j) {
                const isEnd = info.stackEnd[j] === i;
                const topBorder = isEnd ? 20 : 0;
                const bottomBorder = 0;
                data[j] = {
                    value: data[j],
                    itemStyle: {
                        borderRadius: [topBorder, topBorder, bottomBorder, bottomBorder]
                    }
                };
            }
        }
        lineOption = {
            title: {
                text: 'K988+000 - K988+500',
                textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'bold' },
                top: '5px',
                left: '35%'

            },
            legend: {
                show: true,
                data: ['平均速度', '平均车间距', '空间占有率', '平均车头时距'],
                textStyle: { color: '#FFF' },
                top: '25px',
                right: '5%'
            },
            xAxis: {
                type: 'category',
                data: ['上行', '下行'],
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
        <div className={styles.trafficSpeed}>
            <div className={styles.title}>车流车速</div>
            <div id="main" style={{ "width": "100%", "height": "350px" }}></div>
        </div>

    )
}
