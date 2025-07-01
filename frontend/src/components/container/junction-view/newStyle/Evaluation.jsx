import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import styles from './css/evaluation.module.scss';
import { setEvaluationData } from 'stores/junctionLight/evaluationSlice';

export default function Evaluation() {
  const dispatch = useDispatch();
  const { preIndex, optIndex, preSpeed, optSpeed, preContent,optContent } = useSelector((state) => state.evaluation);

  useEffect(() => {
    const handleEvaluationDataChanged = (event) => {
      console.log('Evaluation Data Changed:', event.detail);
      // event.detail应为{ peakHourFlow: number, twelveHourFlow: number, position: string, content: string }
      dispatch(setEvaluationData(event.detail));
    };

    window.addEventListener('evaluationDataChanged', handleEvaluationDataChanged);

    return () => {
      window.removeEventListener('evaluationDataChanged', handleEvaluationDataChanged);
    };
  }, [dispatch]);

  return (
    <div className={styles.evaluation}>
      <div className={styles.title}>
        <span>效果评价</span>
      </div>
      <div className={styles.dataContainer}>
        <div className={styles.compare}>
          <div className={styles.dataTitle}>优化前</div>
          <span className={styles.index}>平均拥堵指数<span className={styles.number}>{preIndex}</span> </span>
          <span className={styles.speed}>平均车速 <span className={styles.number}>{preSpeed}</span> km/h</span>
        </div>
      
        <div className={styles.compare}>
          <div className={styles.dataTitle}>优化后</div>
          <span className={styles.index}>平均拥堵指数<span className={styles.number}>{optIndex}</span> </span>
          <span className={styles.speed}>平均车速 <span className={styles.number}>{optSpeed}</span> km/h</span>
        </div> 
        
      </div>
      <div className={styles.optimiseRecord}>
        <span className={styles.recordTitle}>控制方案（优化前）</span>
        <div className={styles.recordContainer}>
          <span className={styles.rank}></span>
          <div className={styles.contentContainer}>
            <span className={styles.content}>详情：{preContent}</span>
          </div>
        </div>
        
      </div>
      <div className={styles.optimiseRecord}>
        <span className={styles.recordTitle}>控制方案（优化后）</span>
        <div className={styles.recordContainer}>
          <span className={styles.rank}></span>
          <div className={styles.contentContainer}>
            <span className={styles.content}>详情：{optContent}</span>
          </div>
        </div>
        
      </div>
    </div>
  );
}