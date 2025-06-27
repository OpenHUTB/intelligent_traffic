import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setResultTrackData } from 'stores/junctionLight/resultTrackSlice';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
import { ReactComponent as UP } from 'assets/icon/icon-up.svg';
import { ReactComponent as DOWN } from 'assets/icon/icon-down.svg';
import styles from './css/resultTrack.module.scss';

export default function ResultTrack() {
  const dispatch = useDispatch();
  const { timeRange, district1, district2, direction, data } = useSelector((state) => state.resultTrack);

  useEffect(() => {
    const handleResultTrackDataChanged = (event) => {
      console.log('ResultTrack Data Changed:', event.detail);
      // event.detail可包含: { timeRange: 'xx-xx', district1: 'xxx', district2: 'xxx', direction: 'xxx', data: [...] }
      dispatch(setResultTrackData(event.detail));
    };

    window.addEventListener('resultTrackDataChanged', handleResultTrackDataChanged);

    return () => {
      window.removeEventListener('resultTrackDataChanged', handleResultTrackDataChanged);
    };
  }, [dispatch]);

  const renderList = data.map((item, index) => (
    <div className={styles.listItem} key={index}>
      <span className={styles.name}>{item.name}</span>
      <span className={styles.averageTime}>{item.averageTime}</span>
      <div className={styles.timeCompareBox}><span className={styles.timeCompare}>{item.timeCompare} </span> {item.timeDirection? <UP/> : <DOWN/>}</div>
      <span className={styles.averageParking}>{item.averageParking}</span>
      <div className={styles.parkCompareBox}> <span className={styles.parkingCompare}>{item.parkingCompare}  </span> {item.parkingDirection ? <UP/> : <DOWN/>} </div>
    </div>
  ));

  return (
    <div className={styles.resultTrack}>
      <div className={styles.title}>协调效果跟踪</div>
      <header className={styles.header}>
        <span className={styles.text}>
          <TriangleIcon />
          通过区间时间与停车次数统计
        </span>
        <span>统计时间：<span className={styles.time}>{timeRange}</span></span>
      </header>
      <div className={styles.district}>
        <label htmlFor="district">{`协调区间： `}</label>
        <select
          name="district"
          id="district"
          value={district1}
          onChange={(e) => dispatch(setResultTrackData({ district1: e.target.value }))}
        >
          <option value={district1}>{district1}</option>
        </select>
        <select
          name="district"
          id="district2"
          value={district2}
          onChange={(e) => dispatch(setResultTrackData({ district2: e.target.value }))}
        >
          <option value={district2}>{district2}</option>
        </select>
      </div>
      <div className={styles.buttonGroup}>
        <span className={styles.name}>{`协调方向： `}</span>
        {['东向西', '西向东', '南向北', '北向南'].map((d) => (
          <button
            key={d}
            className={d == direction ? styles.activeBtn : styles.button}
            onClick={() => dispatch(setResultTrackData({ direction: d }))}
          >
            {d}
          </button>
        ))}
      </div>

      <div className={styles.dataPresent}>
        <div className={styles.dataTitle}>
          <span>数据来源</span>
          <span>平均通行时间</span>
          <span className={styles.street}>时间同比</span>
          <span>平均停车次数</span>
          <span>次数同比</span>
        </div>
        <div className={styles.listContainer}>{renderList}</div>
      </div>
    </div>
  );
}