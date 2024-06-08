import React from 'react'
import Weather from './weather/Weather';
import TotalTrafficVolume from './TotalTrafficVolume';
import TrafficSpeed from './TrafficSpeed';
import styles from './css//leftside.module.scss';
import OperationDetect from './OperationDetect'
export default function LeftSide() {
    return (
        <div className={styles.leftside}>
            {/* <Weather /> */}
            <TotalTrafficVolume />
            <TrafficSpeed />
            <OperationDetect />
        </div>
    )
}
