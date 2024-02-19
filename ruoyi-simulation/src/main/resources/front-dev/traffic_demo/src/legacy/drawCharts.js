// Create the echarts instance
let roadData1 = {
    '机动车': 40,
    '非机动车': 7,
    '行人': 3
}
let roadData2 = {
    '非机动车': 40,
    '机动车': 100,
    '行人': 10
}
let roadData3 = {
    '非机动车': 40,
    '行人': 10,
    '机动车': 100,
}
let roadData4 = {
    '非机动车': 10,
    '行人': 20,
    '机动车': 180,
}
let carCategories1 = {
    '危险车辆': 5,
    '普通车辆': 24,
    '特种车辆': 1
}

let carCategories2 = {
    '危险车辆': 20,
    '普通车辆': 150,
    '特种车辆': 5
}
let carCategories3 = {
    '危险车辆': 20,
    '特种车辆': 5,
    '普通车辆': 150
}
let carCategories4 = {
    '危险车辆': 10,
    '普通车辆': 150,
    '特种车辆': 5,
    
}
let carSpeed1 = 0.7
let carSpeed2 = 0.4
let carSpeed3 = 0.15
let carSpeed4 = 0.7
let myChart1 = echarts.init(document.getElementsByClassName('barCharts')[0]);
let myChart2 = echarts.init(document.getElementsByClassName('leftBottom')[0]);
let myChart3 = echarts.init(document.getElementsByClassName('guageChart')[0]);
let myChart4 = echarts.init(document.getElementsByClassName('rightBottom')[0]);
// Draw the chart
console.log(myChart1);
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
            left: 'left'
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
                    fontSize: 20,
                    distance: -60,
                    rotate: 'tangential',
                    formatter: function (value) {
                        if (value === 0.125) {
                            return '重度拥堵';
                        } else if (value === 0.375) {
                            return '轻微拥堵';
                        } else if (value === 0.625) {
                            return '畅通';
                        }
                        return '';
                    }
                },
                title: {
                    offsetCenter: [0, '-10%'],
                    fontSize: 20
                },
                detail: {
                    fontSize: 30,
                    offsetCenter: [0, '-35%'],
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
    // Draw the chart
    let option4 = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
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
    const videoElement = document.querySelector('video');
    const recordButton = document.getElementById('record');
    const textarea = document.getElementById('tips');
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');

    let lastTimeUpdate = 0;

    recordButton.addEventListener('click', () => {
        videoElement.play();
    });

    let offset = 0; // global variable to shift the wave drawing to simulate progression
    let currentDrawPosition = 0; // global variable to track drawing position for animation effect

    let currentWaveProperties = getRandomWaveProperties();

    function getRandomWaveProperties() {
        return {
            baseAmplitude: 30 + Math.random() * 40,  // Amplitude between 30 and 70
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

    let words = {
        'hello': '用户: 你好，小轩',
        'systemHello': '小轩: 哎，我在',
        'enter': '用户: 请进入湖工商场景',
        'entering': '小轩: 好的，稍等，正在进入湖南工商大学场景',
        'double': '用户: 将车辆增加一倍',
        'processing': '小轩: 正在处理您的请求，请耐心等待',
        'success': '小轩: 语音识别成功',
        'codeSuccess': '小轩: 代码生成成功',
        'sceneSuccess': '小轩: 三维场景像素流生成中',
        'close': '用户: 将西二环上坡路段封闭',
        'viewChange': '用户: 切换到消防车跟随视角',
    }
    let code = {
        'code1': `dbclear if error
        fname = 'auto_created_model';
        close_system(fname, 0)
        h = new_system(fname);
        add_block('drivingsim3d/Simulation 3D Scene Configuration', 'auto_created_model/test');
        set_param('auto_created_model/test', 'SceneDesc', 'US city block');
        sim(fname);
        %  0 表示关闭而不保存
        close_system(fname, 0)`,
        'code2': `roadCenters = [-199.96 144.04 0; -200 100 0];
        marking = [laneMarking('Unmarked') laneMarking('Solid', 
        'Color', [0.98 0.86 0.36]) laneMarking('Unmarked')];
        laneSpecification = lanespec(2, 'Width', 3, 'Marking', marking);
        road(scenario, roadCenters, 'Lanes', laneSpecification, 'Name', 'Road2');`,
        'code3': `scenario = drivingScenario()
        roadCenters = [125 -25 0;125 -100 0];
        marking = [laneMarking('Solid') laneMarking('Dashed') laneMarking('Solid')];
        laneSpecification = lanespec(2, 'Width', 4, 'Marking', marking);
        road(scenario, roadCenters, 'Lanes', laneSpecification, 'Name', 'Road2');
        drivingScenarioDesigner(scenario);`,
        'code4': `scenario = drivingScenario()
        roadCenters = [-250 0 0;-200 0 0];
        laneSpecification = lanespec([4 4], 'Width', 4);
        road(scenario, roadCenters, 'Lanes', laneSpecification, 'Name', 'Road6');
        drivingScenarioDesigner(scenario);`
    }
    videoElement.addEventListener('timeupdate', () => {
        const currentTime = videoElement.currentTime;

        if (currentTime >= 8.7 && lastTimeUpdate < 8.7) {
            typeText(words.hello, drawWave, true);
        }
        if (currentTime >= 11.2 && lastTimeUpdate < 11.2) {
            typeText(words.systemHello, drawWave, false);
        }
        if (currentTime >= 13.1 && lastTimeUpdate < 13.1) {
            typeText(words.enter, drawWave, true);
            
        }
        if (currentTime >= 14 && lastTimeUpdate < 14) {
            typeText(words.entering, drawWave, false);
            renderAllCharts(roadData1, carSpeed1, carCategories1);
        }
        if (currentTime >= 22.7 && lastTimeUpdate < 22.7) {
            typeText(words.double, drawWave, true);
        }
        if (currentTime >= 23.5 && lastTimeUpdate < 23.5) {
            typeText(words.processing, drawWave, false);
        }
        if (currentTime >= 29 && lastTimeUpdate < 29) {
            typeText(words.success, drawWave, false);
        }
        if (currentTime >= 34 && lastTimeUpdate < 34) {
            typeText(words.codeSuccess, drawWave, false);
            clearCharts();
            renderAllCharts(roadData2, carSpeed2, carCategories2);
        }
        if (currentTime >= 37 && lastTimeUpdate < 37) {
            typeText(words.sceneSuccess, drawWave, false);

        }
        if (currentTime >= 39 && lastTimeUpdate < 39) {
            typeText(code.code1 + code.code2, drawWave, false);
        }
        if (currentTime >= 103 && lastTimeUpdate < 103) {
            typeText(words.close, drawWave, true);
        }
        if (currentTime >= 108 && lastTimeUpdate < 108) {
            typeText(words.processing, drawWave, false);
        }
        if (currentTime >= 113 && lastTimeUpdate < 113) {
            typeText(words.success, drawWave, false);
        }
        if (currentTime >= 123 && lastTimeUpdate < 123) {
            typeText(words.codeSuccess, drawWave, false);
        }
        if (currentTime >= 130 && lastTimeUpdate < 130) {
            typeText(words.sceneSuccess, drawWave, false);
            clearCharts();
            renderAllCharts(roadData3, carSpeed3, carCategories3);
        }
        if (currentTime >= 132 && lastTimeUpdate < 132) {
            typeText(code.code3 + code.code4, drawWave, false);
        }
        if (currentTime >= 181 && lastTimeUpdate < 181) {
            typeText(words.viewChange, drawWave, true);
        }
        if (currentTime >= 185 && lastTimeUpdate < 185) {
            typeText(words.processing, drawWave, false);
        }
        if (currentTime >= 194 && lastTimeUpdate < 194) {
            typeText(words.success, drawWave, false);
        }
        if (currentTime >= 203 && lastTimeUpdate < 203) {
            typeText(words.codeSuccess, drawWave, false);
        }
        if (currentTime >= 212 && lastTimeUpdate < 212) {
            typeText(words.sceneSuccess, drawWave, false);
            clearCharts();
            renderAllCharts(roadData4, carSpeed4, carCategories4);
        }
        if (currentTime >= 214 && lastTimeUpdate < 214) {
            typeText(code.code2 + code.code3, drawWave, false);
        }




        lastTimeUpdate = currentTime;
    });
});


