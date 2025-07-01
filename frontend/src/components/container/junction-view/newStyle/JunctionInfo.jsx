import React, { useEffect } from 'react';
import styles from './css/junctionInfo.module.scss';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
import { useSelector, useDispatch } from 'react-redux';
import { setJunctionInfo } from 'stores/junctionLight/junctionInfoSlice';

export default function JunctionInfo() {
  const dispatch = useDispatch();
  const { district1, district2, carLanes, pedestrianLanes } = useSelector((state) => state.junctionInfo);

  useEffect(() => {
    const handleJunctionInfoDataChanged = (event) => {
      console.log('JunctionInfo Data Changed:', event.detail);
      // event.detail中应包含{ district1: 'xxx', district2: 'xxx', carLanes: number, pedestrianLanes: number }
      dispatch(setJunctionInfo(event.detail));
    };

    window.addEventListener('junctionInfoDataChanged', handleJunctionInfoDataChanged);

    return () => {
      window.removeEventListener('junctionInfoDataChanged', handleJunctionInfoDataChanged);
    };
  }, [dispatch]);

  return (
    <div className={styles.jucntionInfo}>
      <header className={styles.title}>
        <span>路口信息</span>
      </header>
      <main className={styles.infoMain}>
        <div className={styles.district}>
          <TriangleIcon />
          <label htmlFor="district">{`路口位置:  `}</label>
          <select
            name="district"
            id="district"
            value={district1}
            onChange={(e) => dispatch(setJunctionInfo({ district1: e.target.value }))}
          >
            <option value={district1}>{district1}</option>
          </select>
          <select
            name="district"
            id="district2"
            value={district2}
            onChange={(e) => dispatch(setJunctionInfo({ district2: e.target.value }))}
          >
            <option value={district2}>{district2}</option>
          </select>
        </div>
        <div className={styles.roadInfo}>
          <span className={styles.car}>机动车道<span className={styles.number}>{carLanes}</span> 车道</span>
          <span className={styles.pedestrain}>非机动车道 <span className={styles.number}>{pedestrianLanes}</span> 车道</span>
        </div>
      </main>
    </div>
  )
}