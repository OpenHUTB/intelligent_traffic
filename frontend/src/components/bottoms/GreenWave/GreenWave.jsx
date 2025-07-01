import React, { useEffect, useRef, useState } from 'react'
import styles from './index.module.scss'
import alert from 'assets/image/alert.png'

export default function GreenWave() {
  const [mapNavigation, setMapNavigation] = useState('方案一')
  const [roadControl, setRoadControl] = useState('')
  const [twinRule, setTwinRule] = useState('')
  const [simulationControl, setSimulationControl] = useState('')
  const [detectionType, setDetectionType] = useState('')
  const [viewAngle, setViewAngle] = useState('30°')
  const [updateSpeed, setUpdateSpeed] = useState('速度1')

  // 默认应用按钮是active的
  const [activeButton, setActiveButton] = useState('apply')

  const handleApply = () => {
    setActiveButton('apply')
    console.log('应用设置')
  }

  const handleClear = () => {
    setActiveButton('clear')
    setMapNavigation('方案一')
    setRoadControl('')
    setTwinRule('')
    setSimulationControl('')
    setDetectionType('')
    setViewAngle('30°')
    setUpdateSpeed('速度1')
  }

  return (
    <div className={styles.greenWave}>
      <div className={styles.title}>
        <span>绿波道路设置</span>
      </div>

      <div className={styles.mainContent}>
        <div className={styles.contentWrapper}>
          <div className={styles.leftPanel}>
            {/* 地图导航 */}
            <div className={styles.functionSection}>
              <div className={styles.controlRow}>
                <div className={styles.controlItem}>
                  <span className={styles.label}>地图导航</span>
                  <div className={styles.radioGroup}>
                    {['方案一', '方案二', '方案三'].map((option) => (
                      <label key={option} className={styles.radioItem}>
                        <input
                          type='radio'
                          name='mapNavigation'
                          value={option}
                          checked={mapNavigation === option}
                          onChange={(e) => setMapNavigation(e.target.value)}
                        />
                        <span>{option}</span>
                      </label>
                    ))}
                  </div>
                </div>
              </div>
            </div>

            {/* 路口控制 */}
            <div className={styles.functionSection}>
              <div className={styles.controlRow}>
                <div className={styles.controlItem}>
                  <span className={styles.label}>路口控制</span>
                  <select
                    value={roadControl}
                    onChange={(e) => setRoadControl(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='控制1'>控制1</option>
                    <option value='控制2'>控制2</option>
                  </select>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>孪生规则</span>
                  <select
                    value={twinRule}
                    onChange={(e) => setTwinRule(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='规则1'>规则1</option>
                    <option value='规则2'>规则2</option>
                  </select>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>模拟控制</span>
                  <select
                    value={simulationControl}
                    onChange={(e) => setSimulationControl(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      请选择
                    </option>
                    <option value='模拟1'>模拟1</option>
                    <option value='模拟2'>模拟2</option>
                  </select>
                </div>
              </div>
            </div>

            {/* 检测类型 */}
            <div className={styles.functionSection}>
              <div className={styles.controlRow}>
                <div className={styles.controlItem}>
                  <span className={styles.label}>检测类型</span>
                  <select
                    value={detectionType}
                    onChange={(e) => setDetectionType(e.target.value)}
                    className={styles.selectBox}
                  >
                    <option value='' disabled>
                      模型1
                    </option>
                    <option value='模型1'>模型1</option>
                    <option value='模型2'>模型2</option>
                  </select>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>视角控制</span>
                  <div className={styles.radioGroup}>
                    {['30°', '60°', '90°'].map((angle) => (
                      <label key={angle} className={styles.radioItem}>
                        <input
                          type='radio'
                          name='viewAngle'
                          value={angle}
                          checked={viewAngle === angle}
                          onChange={(e) => setViewAngle(e.target.value)}
                        />
                        <span>{angle}</span>
                      </label>
                    ))}
                  </div>
                </div>

                <div className={styles.controlItem}>
                  <span className={styles.label}>更新速度</span>
                  <div className={styles.radioGroup}>
                    {['速度1', '速度2', '速度3'].map((speed) => (
                      <label key={speed} className={styles.radioItem}>
                        <input
                          type='radio'
                          name='updateSpeed'
                          value={speed}
                          checked={updateSpeed === speed}
                          onChange={(e) => setUpdateSpeed(e.target.value)}
                        />
                        <span>{speed}</span>
                      </label>
                    ))}
                  </div>
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
              应用
            </button>
            <button
              className={`${styles.actionButton} ${
                activeButton === 'clear' ? styles.active : ''
              }`}
              onClick={handleClear}
            >
              清空
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
