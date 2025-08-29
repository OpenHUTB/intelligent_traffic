import React, { useEffect, useRef, useState } from 'react'
import styles from './index.module.scss'
import alert from 'assets/image/alert.png'

export default function CruiserSetting() {
  const [roadControl, setRoadControl] = useState('')
  const [simulationControl, setSimulationControl] = useState('')
  const [viewAngle, setViewAngle] = useState('30°')
  const [updateSpeed, setUpdateSpeed] = useState('速度1')
  const [weather, setWeather] = useState('') // 新增天气状态

  // 默认应用按钮是active的
  const [activeButton, setActiveButton] = useState('apply')

  // 路线映射
  const routeMap = {
    路线1: [6, 7, 5],
    路线2: [5, 7, 6],
  }

  const handleApply = async () => {
    setActiveButton('apply')
    console.log('应用设置')

    // 如果选择了路线，发送POST请求
    if (simulationControl && routeMap[simulationControl]) {
      try {
        const requestData = {
          pitch: 0.0,
          speed: 50.0,
          weather: weather || 'ClearSunset',
          junctionIdList: routeMap[simulationControl],
        }

        const response = await fetch(
          'http://localhost:8080/simulation/section/cruise',
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData),
          }
        )

        if (response.ok) {
          console.log('请求发送成功')
          const result = await response.json()
          console.log('响应结果:', result)
        } else {
          console.error('请求失败:', response.status, response.statusText)
        }
      } catch (error) {
        console.error('网络错误:', error)
      }
    }
  }

  const handleClear = () => {
    setActiveButton('clear')
    setRoadControl('')
    setSimulationControl('')
    setViewAngle('30°')
    setUpdateSpeed('速度1')
    setWeather('') // 重置天气
  }

  return (
    <div className={styles.CruiserSetting}>
      <div className={styles.title}>
        <span>巡查设置</span>
      </div>

      <div className={styles.mainContent}>
        <div className={styles.contentWrapper}>
          <div className={styles.leftPanel}>
            {/* 路口控制 */}
            <div className={styles.functionSection}>
              <div className={styles.controlRow}>
                <div className={styles.controlItem}>
                  <span className={styles.label}>漫游速度</span>
                  <select
                    value={updateSpeed}
                    onChange={(e) => setUpdateSpeed(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='速度1'>速度1</option>
                    <option value='速度2'>速度2</option>
                    <option value='速度3'>速度3</option>
                    <option value='速度4'>速度4</option>
                    <option value='速度5'>速度5</option>
                  </select>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>漫游角度</span>
                  <select
                    value={viewAngle}
                    onChange={(e) => setViewAngle(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='30'>30°</option>
                    <option value='60'>60°</option>
                    <option value='90'>90°</option>
                    <option value='120'>120°</option>
                    <option value='150'>150°</option>
                    <option value='180'>180°</option>
                  </select>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>漫游路线</span>
                  <select
                    value={simulationControl}
                    onChange={(e) => setSimulationControl(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='路线1'>路线1</option>
                    <option value='路线2'>路线2</option>
                  </select>
                </div>
                <div className={styles.controlItem}>
                  <span className={styles.label}>漫游天气</span>
                  <select
                    value={weather}
                    onChange={(e) => setWeather(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='ClearNoon'>正午晴天</option>
                    <option value='ClearSunset'>傍晚晴天</option>
                    <option value='CloudyNoon'>正午多云</option>
                    <option value='CloudySunset'>傍晚多云</option>
                    <option value='WetNoon'>正午湿地(地面潮湿)</option>
                    <option value='WetSunset'>傍晚湿地</option>
                    <option value='WetCloudyNoon'>正午多云湿地</option>
                    <option value='WetCloudySunset'>傍晚多云湿地</option>
                    <option value='MidRainyNoon'>正午中等强度降雨</option>
                    <option value='MidRainSunset'>傍晚中等降雨</option>
                    <option value='WetRainyNoon'>正午湿地+降雨</option>
                    <option value='WetRainySunset'>傍晚湿地+降雨</option>
                    <option value='HardRainNoon'>正午大雨</option>
                    <option value='HardRainSunset'>傍晚大雨</option>
                  </select>
                </div>
              </div>
            </div>
          </div>

          <div className={styles.rightPanel}>
            <button
              className={`${styles.actionButton} ${
                activeButton === 'apply' ? styles.active : ''
              }`}
              onClick={handleApply}
            >
              开始
            </button>
            <button
              className={`${styles.actionButton} ${
                activeButton === 'clear' ? styles.active : ''
              }`}
              onClick={handleClear}
            >
              重置
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
