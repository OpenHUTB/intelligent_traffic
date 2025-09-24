import React from 'react'
import Map from './Map/Map';
import TrafficSummary from './TrafficSummary';
import TrafficCompare from './TrafficCompare';
import styles from './css/rightSide.module.scss';
export default function RightSide() {
    return (
        <div className={styles.rightSide}>
            <TrafficSummary />
            {/* <Map /> */}
            <TrafficCompare />
        </div>
    )

}