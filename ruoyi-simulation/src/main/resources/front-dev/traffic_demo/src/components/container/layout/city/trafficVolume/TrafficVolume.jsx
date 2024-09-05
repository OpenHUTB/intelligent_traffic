import React, { useEffect, useState } from 'react'
import styles from '../css/trafficVolume.module.scss';
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { Button } from 'antd';
import * as echarts from 'echarts';

export default function TrafficVolume() {
    const generateRandomArray = (length, min, max) => {
        return Array.from({ length }, () => Math.floor(Math.random() * (max - min + 1)) + min);
    };

    const [randomCurrent, setRandomCurrent] = useState(generateRandomArray(12, 0, 100));
    const [randomToday, setRandomToday] = useState(generateRandomArray(12, 0, 80));
    useEffect(() => {
        window.addEventListener('lightTimerChanged', (event) => {
            const key = Object.keys(event.detail)[0];
            const isGreen = event.detail[key].isGreen;
            const index = parseInt(key.match(/\d+/)[0]) - 1;
            setActiveIndex(index);

            if (isGreen) {
                setRandomCurrent(generateRandomArray(12, 0, 70));
                setRandomToday(generateRandomArray(12, 0, 50));
            } else {
                setRandomCurrent(generateRandomArray(12, 0, 120));
                setRandomToday(generateRandomArray(12, 0, 90));
            }
        })
    }, [randomCurrent, randomToday]);
    useEffect(() => {

        // Initialize the line chart
        const trafficFlowLineChart = echarts.init(document.getElementById('trafficvolumelinechart'));
        // console.log('rendering');
        const lineGraphOption = {
            title: {
                text: '车道流量（辆）',
                textStyle: { color: '#FFF', fontSize: 13, fontWeight: 'normal' }
            },
            legend: {
                show: true,
                data: ['直行', '左转'],
                textStyle: { color: '#FFF' },
                top: '0',
                right: '0'
            },
            xAxis: {
                type: 'category',
                data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00',],
                axisLine: { lineStyle: { color: '#ccc' } },
                splitNumber: 4,
                splitLine: {
                    lineStyle: {
                        color: '#ccc'  // Color of the horizontal grid lines
                    }
                },
            },
            yAxis: {
                type: 'value',
                axisLine: { lineStyle: { color: '#ccc' } },
                axisTick: {
                    show: true,
                    length: 5,
                },
                splitNumber: 4,
                splitLine: {
                    lineStyle: {
                        color: '#777'  // Color of the horizontal grid lines
                    }
                },
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            series: [
                {
                    name: '直行',
                    data: randomCurrent,
                    type: 'line',
                    smooth: true,
                    showSymbol: false,
                    color: '#3ae7e6'
                },
                {
                    name: '左转',
                    data: randomToday,
                    type: 'line',
                    smooth: true,
                    color: '#2158ba',
                    showSymbol: false,
                }]
        };
        trafficFlowLineChart.setOption(lineGraphOption);
    }, [randomCurrent, randomToday]);

    // active button 
    const [activeIndex, setActiveIndex] = useState(null);
    const buttons = ['东进口', '西进口', '南进口', '北进口'];
    const handleButtonClick = (index) => {
        setActiveIndex(index);
        // Generate and set the random arrays
        setRandomCurrent(generateRandomArray(12, 0, 300));
        setRandomToday(generateRandomArray(12, 0, 250));
    };
    return (
        <div className={styles.trafficVolume}>
            <div className={styles.title}>路口交通流量</div>
            <div className={styles.contentContainer}>
                <div className={styles.controlButtons}>

                    {buttons.map((label, index) => (
                        <button
                            key={index}
                            className={activeIndex === index ? 'activeBtn' : ''}
                            onClick={() => handleButtonClick(index)}
                        >
                            {label}
                        </button>
                    ))}
                </div>
                <div id="trafficvolumelinechart" style={{ "width": "100%", "height": "100%" }} ></div>
            </div>
        </div>

    )
}



