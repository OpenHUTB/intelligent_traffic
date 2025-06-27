
  
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
   * 每隔 interval 毫秒自动调整红绿灯时长
   * @param {number} interval - 间隔时间(毫秒)，默认5000ms（5秒）
   */
  function autoAdjustTrafficLights(interval = 5000, greenDurationTime = 4, redDurationTime = 3) {
    setInterval(() => {
      // 深拷贝状态
      trafficState = JSON.parse(JSON.stringify(trafficState));
  
      for (const direction in trafficState) {
        if (Object.hasOwn(trafficState, direction)) {
          for (const lane in trafficState[direction]) {
            if (Object.hasOwn(trafficState[direction], lane)) {
              const laneData = trafficState[direction][lane];
  
              // 根据车道类型调整时长
              if (lane === 'forward') {
                // 直行车道增加绿灯4秒
                laneData.greenDurationTime += greenDurationTime;
              } else if (lane === 'left') {
                // 左转车道增加红灯3秒
                laneData.redDurationTime += redDurationTime;
              }
            }
          }
        }
      }
  
      // 通知前端数据已更新
      window.lightControlDataChanged(trafficState);
    }, interval);
  }
  
  // 将函数挂载到全局对象上，以便在控制台直接调用
  window.autoAdjustTrafficLights = autoAdjustTrafficLights;