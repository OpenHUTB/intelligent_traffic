import React, { useEffect } from 'react'
import styles from '../css/violationStatistic.module.scss';
import * as echarts from 'echarts';

export default function ViolationStatistic() {
    useEffect(() => {
        let chartDom = document.getElementById('violationStatistic');
        let myChart = echarts.init(chartDom);
        let lineOption;


        lineOption = {
            title: {
                text: 'World Population',
                show: false,
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend: {
                show: true,
                data: ['已处理', '未处理', '处理中'],
                textStyle: { color: '#FFF' },
                top: '25px',
                right: '20%'

            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                boundaryGap: [0, 0.01]
            },
            yAxis: {
                type: 'category',
                data: ['重大事件', '交通灾害', '交通通行事件', '交通事故', '交通气象', '车辆故障']

            },
            series: [
                {
                    name: '已处理',
                    type: 'bar',
                    data: [1, 0, 1, 1, 1, 15],
                    itemStyle: {
                        color: '#349ec9',
                    },
                    barWidth: 10,
                },
                {
                    name: '未处理',
                    type: 'bar',
                    data: [0, 0, 0, 0, 0, 0],
                    itemStyle: {
                        color: '#f7b851',
                    },
                    barWidth: 10,
                },
                {
                    name: '处理中',
                    type: 'bar',
                    data: [0, 0, 0, 0, 0, 0],
                    itemStyle: {
                        color: '#ff0101',
                    },
                    barWidth: 10,
                },

            ]
        };

        lineOption && myChart.setOption(lineOption);
    }, []);



    return (
        <div className={styles.violationStatistic}>
            <div className={styles.title}>事件处置统计</div>
            <div id="violationStatistic" style={{ "width": "100%", "height": "100%" }}></div>
        </div>

    )
}
