// Create the echarts instance
import * as echarts from 'echarts';
import './css/style.css';

let words = "Hello 小轩";
let statisticsData = {
    roadData1: {
        '机动车': 40,
        '非机动车': 7,
        '行人': 3
    },
    carCategories1: {
        '危险车辆': 5,
        '普通车辆': 24,
        '特种车辆': 1
    },
    carSpeed1: 0.75
}

let myChart1 = echarts.init(document.getElementsByClassName('barCharts')[0]);
let myChart2 = echarts.init(document.getElementsByClassName('leftBottom')[0]);
let myChart3 = echarts.init(document.getElementsByClassName('guageChart')[0]);
let myChart4 = echarts.init(document.getElementsByClassName('rightBottom')[0]);
// clear the charts if needed,
function clearCharts() {
    myChart1.clear();
    myChart2.clear();
    myChart3.clear();
    myChart4.clear();
}
function renderAllCharts(roadData, carSpeed, carCategories) {
    let option1 = {
        xAxis: {
            type: 'category',
            data: Object.keys(roadData)
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {

                data: Object.values(roadData),
                type: 'bar'
            }
        ]
    };
    option1 && myChart1.setOption(option1);

    // pie chart
    // Initialize ECharts here

    let customedData = Object.keys(roadData).map((key) => ({
        name: key,
        value: roadData[key]
    }))
    // Draw the chart
    let option2 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            textStyle: {
                color: '#ffffff'
            }
        },
        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: [
                    ...customedData
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    option2 && myChart2.setOption(option2);

    // right top guage chart;
    let option3 = {
        series: [
            {
                type: 'gauge',
                startAngle: 180,
                endAngle: 0,
                center: ['50%', '75%'],
                radius: '90%',
                min: 0,
                max: 1,
                splitNumber: 8,
                axisLine: {
                    lineStyle: {
                        width: 6,
                        color: [
                            [0.25, '#FF6E76'],
                            [0.5, '#FDDD60'],
                            [1, '#7CFFB2']
                        ]
                    }
                },
                textStyle: {
                    fontSize: 12,
                },
                pointer: {
                    icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                    length: '12%',
                    width: 20,
                    offsetCenter: [0, '-60%'],
                    itemStyle: {
                        color: 'auto'
                    }
                },
                axisTick: {
                    length: 12,
                    lineStyle: {
                        color: 'auto',
                        width: 2
                    }
                },
                splitLine: {
                    length: 20,
                    lineStyle: {
                        color: 'auto',
                        width: 5
                    }
                },
                axisLabel: {
                    color: '#ffffff',
                    fontSize: 15,
                    distance: -40,
                    rotate: 'tangential',
                    formatter: function (value) {
                        if (value === 0.125) {
                            return '重度拥堵';
                        } else if (value === 0.375) {
                            return '轻微拥堵';
                        } else if (value === 0.750) {
                            return '畅通';
                        }
                        return '';
                    }
                },
                title: {
                    offsetCenter: [0, '-10%'],
                    fontSize: 14,
                    color: '#ffffff'
                },
                detail: {
                    fontSize: 30,
                    offsetCenter: [0, '-25%'],
                    valueAnimation: true,
                    formatter: function (value) {
                        return Math.round(value * 100) + '';
                    },
                    color: 'inherit'
                },
                data: [
                    {
                        value: carSpeed,
                        name: '平均车速',
                    }

                ]
            }
        ]
    };
    option3 && myChart3.setOption(option3);


    // pie chart right bottom
    // Initialize ECharts here

    let customedData2 = Object.keys(carCategories).map((key) => ({
        name: key,
        value: carCategories[key]
    }))
    console.log(customedData2);
    // Draw the chart
    let option4 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center',
            textStyle: {
                color: '#ffffff'
            }
        },
        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 40,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: true
                },
                data: [
                    ...customedData2
                ]
            }
        ]
    };
    option4 && myChart4.setOption(option4);

}


// play the video and display words with typing effect.
document.addEventListener('DOMContentLoaded', () => {
    const textarea = document.getElementById('tips');
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');


    let offset = 0; // global variable to shift the wave drawing to simulate progression
    let currentDrawPosition = 0; // global variable to track drawing position for animation effect

    let currentWaveProperties = getRandomWaveProperties();

    function getRandomWaveProperties() {
        return {
            baseAmplitude: 20 + Math.random() * 40,  // Amplitude between 30 and 70
            frequency1: 1 + Math.random() * 3,  // Frequency between 1 and 4
            frequency2: 1 + Math.random() * 3,  // Frequency between 1 and 4
            frequency3: 1 + Math.random() * 3,  // Frequency between 1 and 4
            gradientColors: [
                '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0'),
                '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0'),
                '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0')
            ]
        };
    }

    function drawWaveSegment(endX) {
        const canvasWidth = canvas.width;
        const canvasHeight = canvas.height;
        const centerY = canvasHeight / 2;
        currentWaveProperties.baseAmplitude *= 0.95;  // Reduce the amplitude over time
        const gradient = ctx.createLinearGradient(0, centerY - currentWaveProperties.baseAmplitude, 0, centerY + currentWaveProperties.baseAmplitude);
        gradient.addColorStop(0, currentWaveProperties.gradientColors[0]);
        gradient.addColorStop(0.5, currentWaveProperties.gradientColors[1]);
        gradient.addColorStop(1, currentWaveProperties.gradientColors[2]);

        ctx.beginPath();
        ctx.moveTo(currentDrawPosition, centerY);

        for (let i = currentDrawPosition; i < endX; i++) {
            const y = centerY +
                currentWaveProperties.baseAmplitude * Math.sin((i + offset) * currentWaveProperties.frequency1 / canvasWidth * (2 * Math.PI)) +
                (currentWaveProperties.baseAmplitude / 2) * Math.sin((i + offset * 1.5) * currentWaveProperties.frequency2 / canvasWidth * (2 * Math.PI)) +
                (currentWaveProperties.baseAmplitude / 3) * Math.sin((i - offset * 0.5) * currentWaveProperties.frequency3 / canvasWidth * (2 * Math.PI));

            ctx.lineTo(i, y);
        }

        ctx.strokeStyle = gradient;
        ctx.lineWidth = 3;
        ctx.stroke();

        currentDrawPosition = endX;
    }

    // Modify the typeText function to change wave properties after typing
    function typeText(text, callback, updateWave = true) {
        // If the textarea already contains text, append a newline
        if (textarea.value.length > 0) {
            textarea.value += '\n';
        }

        let index = 0;

        function type() {
            if (index < text.length) {
                textarea.value += text[index];
                index++;

                // Adjust this line to auto-scroll to the bottom of the textarea content
                textarea.scrollTop = textarea.scrollHeight;

                setTimeout(type, 20); // typing speed
            } else {
                if (updateWave) {
                    currentWaveProperties = getRandomWaveProperties();  // Update wave properties only if updateWave is true
                }
                callback && callback();
            }
        }

        type();
    }

    function animateWave() {
        if (currentDrawPosition < canvas.width) {
            drawWaveSegment(currentDrawPosition + 5);  // Adjust '5' to increase/decrease speed of drawing effect
            requestAnimationFrame(animateWave);
        } else {
            offset += 10;
            currentDrawPosition = 0; // Reset drawing position after reaching canvas end
        }
    }

    // Modify the original drawWave function to initiate the animation:
    function drawWave() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        animateWave();
    }

    typeText(words, drawWave, true);
    renderAllCharts(statisticsData.roadData1, statisticsData.carSpeed1, statisticsData.carCategories1);
});