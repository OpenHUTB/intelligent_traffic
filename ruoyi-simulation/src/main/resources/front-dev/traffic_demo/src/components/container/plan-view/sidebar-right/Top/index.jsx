import React, { useEffect } from 'react';
import './style.scss';
import * as echarts from 'echarts';
import 'echarts-gl';

// Register the required components for 3D charts
export default function Index(props) {

    const { unnormal, violation, parking } = props.randomTrafficViolation;
    const { current, amount, currentCompare, amountCompare } = props.randomCompare;
    console.log(currentCompare, amountCompare);
    const violationData = {
        '异常事件': unnormal,
        '交通违法': violation,
        '违章停车': parking,
    }
    const customedData = Object.keys(violationData).map((key) => {
        return {
            name: key,
            value: violationData[key]
        }
    })
    //generate the fake traffic violation data;
    const generateRandomArray = (length, min, max) => {
        return Array.from({ length }, () => Math.floor(Math.random() * (max - min + 1)) + min);
    };

    const randomCurrent = generateRandomArray(12, 0, 300);
    const randomToday = generateRandomArray(12, 0, 250);
    useEffect(() => {

        // // Initialize the line chart
        // const lineChart = echarts.init(document.getElementById('lineChart'));
        // const lineGraphOption = {
        //     xAxis: {
        //         type: 'category',
        //         data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00',],
        //         axisLine: { color: '#FFF' }
        //     },
        //     yAxis: { type: 'value', axisLine: { color: 'red' } },
        //     series: [{
        //         data: randomCurrent,
        //         type: 'line',
        //         smooth: true,
        //         color: '#FF0000'
        //     }, {
        //         data: randomToday,
        //         type: 'line',
        //         smooth: true,
        //         color: '#00FF00'
        //     }]
        // };
        // lineChart.setOption(lineGraphOption);


        // Initialize the pie chart
        const pieChart = echarts.init(document.getElementById('pieChart'));
        let option2 = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                top: 'middle',
                textStyle: {
                    color: '#ffffff',
                    fontSize: 10
                }
            },
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: ['30%', '60%'],
                    center: ['65%', '50%'],
                    data: [
                        ...customedData
                    ],
                    label: {
                        show: true,
                        textStyle: {
                            fontSize: 10,
                            color: '#ffffff'
                        }
                    },
                    lableLine: {
                        show: false
                    },
                },

            ]
        }
        pieChart.setOption(option2);
    }, [customedData]);

    return (
        <section className="rightTop">
            <h1>事件警情</h1>
            <div className="stats">
                <div className="stat-situation">
                    <div className="stat-current">
                        <div className="stat-item-date">当前</div>
                        <div className='stat-item-number'>{current}</div>
                        <div className="stat-item decrease">
                            <span className="text">同比</span>
                            <span className={"number " + (currentCompare >= 0 ? "red" : "green")}>{currentCompare >= 0 ? ("+" + currentCompare) : currentCompare}</span>

                        </div>
                    </div>
                    <div className="stat-today">
                        <div className="stat-item-date">今日</div>
                        <div className='stat-item-number'>{amount}</div>
                        <div className="stat-item decrease">
                            <span className="text">同比</span>
                            <span className={"number " + (amountCompare >= 0 ? "red" : "green")}>{amountCompare >= 0 ? ("+" + amountCompare) : amountCompare}</span>
                        </div>
                    </div>
                </div>
                <div className="pieChart">
                    <div id="pieChart"></div>
                </div>
            </div>

            <div className="graph">
                <div id="lineChart" ></div>
            </div>

        </section>
    );
}





