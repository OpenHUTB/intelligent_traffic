import React, { useEffect } from 'react'
import styles from './css/controlStrategy.module.scss';
import { ReactComponent as Signal } from 'assets/icon/signal.svg';
import { useSelector, useDispatch } from 'react-redux';
import { setControlStrategyData } from 'stores/junctionLight/controlStrategySlice';

export default function ControlStrategy() {
  const dispatch = useDispatch();
  const { day, time, controlMode1, controlMode2 } = useSelector((state) => state.controlStrategy);

  // 星期列表
  const days = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'];

  useEffect(() => {
    const handleControlStrategyChanged = (event) => {
      console.log('ControlStrategy Data Changed:', event.detail);
      // event.detail中应包含 { day: '星期三', time: 'xx时段', controlMode1: 'xxx', controlMode2: 'xxx' }之类的数据
      dispatch(setControlStrategyData(event.detail));
    };

    window.addEventListener('controlStrategyDataChanged', handleControlStrategyChanged);

    return () => {
      window.removeEventListener('controlStrategyDataChanged', handleControlStrategyChanged);
    };
  }, [dispatch]);

  return (
    <div className={styles.controlStrategy}>
      <header className={styles.title}>
        <span>控制策略</span>
      </header>
      <main className={styles.strategyMain}>
        <div className={styles.buttonGroup}>
          <span>星期</span>
          <Signal />
          {days.map((d) => (
            <button 
              key={d} 
              className={d == day ? styles.active : ''} 
            >
              {d}
            </button>
          ))}
        </div>
        <div className={styles.timeContainer}>
          <label htmlFor="time">{`时段`}</label>
          <Signal />
          <select name="time" id="time" value={time} onChange={(e) => { /* 如果需要本地交互，可以在这里dispatch更新 */ }}>
            {/* 根据需要可以设置更多option，以下仅示例 */}
            <option value="平峰(09:00-16:00)">平峰(09:00-16:00)</option>
            <option value="早高峰(07:00-09:00)">早高峰(07:00-09:00)</option>
            <option value="晚高峰(17:00-19:00)">晚高峰(17:00-19:00)</option>
          </select>
        </div>

        <div className={styles.controlMode}>
          <label htmlFor="controlMode">{`控制模式`}</label>
          <Signal />
          <select name="controlMode" id="controlMode1" value={controlMode1} onChange={(e) => { /* 如需本地交互dispatch更新 */ }}>
            <option value="主路优先">主路优先</option>
            <option value="辅路优先">辅路优先</option>
            <option value="手动控制">手动控制</option>
          </select>
          <select name="controlMode" id="controlMode2" value={controlMode2} onChange={(e) => { /* 如需本地交互dispatch更新 */ }}>
            <option value="自动调控">自动调控</option>
            <option value="感应控制">感应控制</option>
          </select>

        </div>
      </main>
    </div>
  )
}