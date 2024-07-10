import React from 'react'
// import Weather from './weather/Weather';
import TrafficInfo3Guage from '../homeview/TrafficInfo3Guage';
import TotalTrafficVolume from './TotalTrafficVolume';
import TrafficSpeed from './TrafficSpeed';
import TrafficCompare from '../homeview/TrafficCompare';
import styles from './css//leftside.module.scss';
import OperationDetect from './OperationDetect';


export default function LeftSide() {
    return (
        <div className={styles.leftside}>
            {/* <Weather /> */}
            <TrafficInfo3Guage />
            <TotalTrafficVolume />
            {/* <TrafficSpeed /> */}
            {/* <OperationDetect /> */}
            <TrafficCompare />
        </div>
    )
}
