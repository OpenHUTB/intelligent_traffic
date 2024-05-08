import React from 'react'
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
import { ReactComponent as CarIcon } from 'assets/icon/icon-car.svg';
import styles from './css/totalTrafficVolume.module.scss';

export default function TotalTrafficVolume() {
    return (
        <div className={styles.slidertrafficFlow}>
            <div className={styles.title}>累计交通量</div>
            <div className={styles.contentContainer}>
                <span className={styles.text}><span><CarIcon /></span>今日交通量</span>
                <div className={styles.dataContainer}>
                    <span className={styles.number}>1635<span className={styles.unit}> 上行 (入省)</span></span>
                    <span className={styles.number}>1635<span className={styles.unit}> 下行 (出省)</span></span>
                </div>
            </div>
            <div className={styles.contentContainer}>
                <span className={styles.text}><span><CarIcon /></span>昨日累计</span>
                <div className={styles.dataContainer}>
                    <span className={styles.number}>1635<span className={styles.unit}> 上行 (入省)</span></span>
                    <span className={styles.number}>1635<span className={styles.unit}> 下行 (出省)</span></span>
                </div>
            </div>
            {/* <div className={styles.sliderLabel}>
                <span>
                    <TriangleIcon /> 昨日累计:
                </span>
                <div className={styles.dataContainer}>
                    <span >上行 1635 辆</span>
                    <span >下行 1635 辆</span>
                </div>
            </div> */}

        </div>
    )
}
