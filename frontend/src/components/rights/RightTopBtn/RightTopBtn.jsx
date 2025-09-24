import React, { useEffect, useState } from 'react'
import styles from './index.module.scss'
import BTNICON from 'assets/image/Frame.png'

export default function RightTopBtn({ headerTitle }) {
  // State to manage the visibility of the settings container
  const [settingShow, setSettingShow] = useState(false)
  // Function to toggle the visibility of the settings container
  const handleSettingShow = () => {
    setSettingShow(true)
  }

  const handleConfirm = () => {
    setSettingShow(false)
    window.updateTrafficInfo('index', 1)
    window.updateTrafficInfo('road', 2)
    window.updateTrafficInfo('congestion', 3)
    window.updateTrafficInfo('time', 4)
  }
  const [updateSpeed, setUpdateSpeed] = useState('速度1')
  return (
    <div className={styles.RightTopBtn}>
      <img src={BTNICON} alt='' />
      <span className={styles.title} onClick={handleSettingShow}>
        {headerTitle}
      </span>
      {settingShow && (
        <div className={styles.settingContainer}>
          <div className={styles.title}>监控指数设置</div>
          <div className={styles.settingItem}>
            <div className={styles.street}>
              <span className={styles.label}>监测街道</span>
              <select
                value={updateSpeed}
                onChange={(e) => setUpdateSpeed(e.target.value)}
                className={styles.selectBox}
              >
                <option value='' disabled>
                  请选择
                </option>
                <option value='旺龙路'>旺龙路</option>
                <option value='望青路'>望青路</option>
                <option value='尖山路'>尖山路</option>
                <option value='岳麓西大道'>岳麓西大道</option>
                <option value='青山路'>青山路</option>
              </select>
            </div>
            <div className={styles.street}>
              <span className={styles.label}>监测类型</span>
              <select
                value={updateSpeed}
                onChange={(e) => setUpdateSpeed(e.target.value)}
                className={styles.selectBox}
              >
                <option value='' disabled>
                  请选择
                </option>
                <option value='交通拥堵指数'>交通拥堵指数</option>
                <option value='拥堵路段数'>拥堵路段数</option>
                <option value='拥堵长度'>拥堵长度</option>
                <option value='拥堵时长'>拥堵时长</option>
              </select>
            </div>
            <div className={styles.street}>
              <span className={styles.label}>异常类型</span>
              <select
                value={updateSpeed}
                onChange={(e) => setUpdateSpeed(e.target.value)}
                className={styles.selectBox}
              >
                <option value='' disabled>
                  请选择
                </option>
                <option value='逆行'>逆行</option>
                <option value='异常停车'>异常停车</option>
                <option value='超高速'>超高速</option>
              </select>
            </div>
          </div>
          <div className={styles.confirmBtn}>
            <button className={styles.actionButton} onClick={handleConfirm}>
              确定
            </button>
            <button
              className={styles.actionButton}
              onClick={() => setSettingShow(false)}
            >
              取消
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
