// 初始状态对象
let trafficState = {
    north: {
        left: {
            redDurationTime: 30,
            greenDurationTime: 40,
            initialLight: 'red',
        },
        forward: {
            redDurationTime: 35,
            greenDurationTime: 45,
            initialLight: 'green',
        },
    },
    east: {
        left: {
            redDurationTime: 28,
            greenDurationTime: 38,
            initialLight: 'red',
        },
        forward: {
            redDurationTime: 33,
            greenDurationTime: 43,
            initialLight: 'green',
        },
    },
    south: {
        left: {
            redDurationTime: 32,
            greenDurationTime: 42,
            initialLight: 'red',
        },
        forward: {
            redDurationTime: 37,
            greenDurationTime: 47,
            initialLight: 'green',
        },
    },
    west: {
        left: {
            redDurationTime: 25,
            greenDurationTime: 35,
            initialLight: 'red',
        },
        forward: {
            redDurationTime: 30,
            greenDurationTime: 40,
            initialLight: 'green',
        },
    },
};

/**
 * 每隔指定时间(默认5秒)自动调整红绿灯时长的函数
 * @param {number} interval - 间隔时间(毫秒)，默认5000ms，即5秒
 * @param {number} greenIncrement - 每次循环增加的绿灯时长（秒），默认5秒
 * @param {number} redDecrement - 每次循环减少的红灯时长（秒），默认5秒
 */
function autoAdjustTrafficLights(interval = 5000, greenIncrement = 5, redDecrement = 5) {
  setInterval(() => {
    // 深拷贝状态以避免直接修改全局引用（根据需求可选）
    trafficState = JSON.parse(JSON.stringify(trafficState));

    for (const direction in trafficState) {
      if (Object.hasOwn(trafficState, direction)) {
        for (const lane in trafficState[direction]) {
          if (Object.hasOwn(trafficState[direction], lane)) {
            const laneData = trafficState[direction][lane];
            // 增加绿灯时长
            laneData.greenDurationTime += greenIncrement;
            // 减少红灯时长，最少为5秒
            laneData.redDurationTime = Math.max(laneData.redDurationTime - redDecrement, 5);
          }
        }
      }
    }

    // 调用 junctionInfoDataChanged 通知前端监听者数据更新
    window.junctionInfoDataChanged(trafficState);
  }, interval);
}

// 将autoAdjustTrafficLights挂载到window以便在console直接调用
window.autoAdjustTrafficLights = autoAdjustTrafficLights;