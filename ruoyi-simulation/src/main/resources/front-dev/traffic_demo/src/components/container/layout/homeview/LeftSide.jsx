import React from 'react'
import TrafficInfo3Guage from './TrafficInfo3Guage';
import TrafficRank from './TrafficRank/TrafficRank';
import TrafficCompare from './TrafficCompare';
import styles from './css/leftSide.module.scss';
export default function LeftSide() {
    return (
        <div className={styles.leftSide}>
            <TrafficInfo3Guage />
            <TrafficRank />
            {/* <TrafficCompare /> */}
        </div>
    )
}
