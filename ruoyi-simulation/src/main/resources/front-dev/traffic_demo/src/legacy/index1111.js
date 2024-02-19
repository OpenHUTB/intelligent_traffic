// Create the echarts instance
import * as echarts from 'echarts';
import './css/style.css';

let words = `Hello 小轩`;
let statisticsData = {
    roadData: {
        '机动车': 40,
        '非机动车': 7,
    },
    carCategories: {
        '黄牌车': 5,
        '非黄牌车': 24,
    },
    carSpeed1: 0.75
}

//select all the charts and draw them
let leftTopChart = document.querySelector('.leftTop');
let pieChart = echarts.init(leftTopChart);

//data processing
let roadData = Object.keys(statisticsData.roadData).map((key) => ({
    name: key,
    value: statisticsData.roadData[key]
}))
let carData = Object.keys(statisticsData.carCategories).map((key) => ({
    name: key,
    value: statisticsData.carCategories[key]
}))
// draw the chart
let drawChart = (element, option) => {
    element.setOption(option);
};

function setOption() {
    let piechartOption = {
        color: ['#3960F4', '#41B297', '#A465DE', '#1760D5'],
        title: {
            text: '出行车辆构成',
            subtext: '(5分钟更新)',
            left: 'center',
            textStyle: {
                color: '#ffffff'
            }
        },
        legend: {
            data: [...Object.keys(statisticsData.roadData), ...Object.keys(statisticsData.carCategories)],
            orient: 'horizontal',
            bottom: '5%',
            textStyle: {
                color: '#ffffff'
            }
        },
        series: [
            {
                name: 'Left Pie',
                type: 'pie',
                radius: ['30%', '15%'],
                center: ['25%', '50%'],
                data: [
                    ...roadData
                ],

                label: {
                    position: 'outside',
                    formatter: '{percentage|{d}%}',
                    rich: {
                        name: {
                            color: '#fff'
                        },
                        percentage: {
                            color: '#fff'
                        }
                    },
                },
            },
            {
                name: 'Right Pie',
                type: 'pie',
                radius: ['30%', '15%'],
                center: ['70%', '50%'],
                data: [
                    ...carData
                ],
                label: {
                    position: 'outside',
                    formatter: '{percentage|{d}%}',
                    rich: {
                        name: {
                            color: '#fff'
                        },
                        percentage: {
                            color: '#fff'
                        }
                    }
                },
            },
        ]
    };
    return piechartOption;
}
drawChart(pieChart, setOption());
const textarea = document.getElementById('tips');

// Modify the typeText function to change wave properties after typing
function typeText(text) {
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
        }
    }
    type();
}
typeText(words);