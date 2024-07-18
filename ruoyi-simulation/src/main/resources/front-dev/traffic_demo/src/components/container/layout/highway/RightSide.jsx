import React from 'react';
// import OperationDetect from './OperationDetect'
import FunctionICons from './functionIcons/FunctionIcon';
import Map from '../homeview/Map/Map';
import ScrollAlert from './scrollAlert/ScrollAlert';
import styles from './css/rightside.module.scss';
import TrafficCompare from '../homeview/TrafficCompare';
import TrafficSpeed from './TrafficSpeed';
export default function RightSide() {
    return (
        <div className={styles.rightside}>
            {/* <FunctionICons /> */}
            <ScrollAlert />
            {/* <Map /> */}
            <TrafficCompare />
            <TrafficSpeed />
        </div>
    )
}
