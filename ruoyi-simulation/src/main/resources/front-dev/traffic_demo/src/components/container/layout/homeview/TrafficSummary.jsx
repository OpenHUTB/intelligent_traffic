import React from 'react';

import styles from './css/trafficSummary.module.scss';

export default function TrafficSummary() {



    return (
        <div className={styles.trafficSummary}>
            <div className={styles.title}>区域概览</div>

            <div className={styles.dataContainer}>
                <span className={styles.road}>道路长度 <span className={styles.number}>{5}</span> km</span>
                <span className={styles.onRoadCar}>在途车辆<span className={styles.number}>{885}</span> 辆</span>
                {/* <span className={styles.people}>行人数量 <span className={styles.number}>{214}</span> 位</span> */}
                <span className={styles.capacity}>路网饱和度 <span className={styles.number}>{20}</span> %</span>
                <span className={styles.junction}>路口数量 <span className={styles.number}>{20}</span> </span>
                {/* <span className={styles.cloudjunction}>拥堵路口数量 <span className={styles.number}>{5}</span> </span> */}
            </div>
        </div>
    )
}
