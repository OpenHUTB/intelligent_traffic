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
                text: 'World Population'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend: {},
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
                data: ['Brazil', 'Indonesia', 'USA', 'India', 'China', 'World']
            },
            series: [
                {
                    name: '2011',
                    type: 'bar',
                    data: [18203, 23489, 29034, 104970, 131744, 630230]
                },
                {
                    name: '2012',
                    type: 'bar',
                    data: [19325, 23438, 31000, 121594, 134141, 681807]
                }
            ]
        };

        lineOption && myChart.setOption(lineOption);
    }, []);



    return (
        <div className={styles.violationStatistic}>
            <div className={styles.title}>事件处置统计</div>
            <div id="violationStatistic" style={{ "width": "100%", "height": "350px" }}></div>
        </div>

    )
}
