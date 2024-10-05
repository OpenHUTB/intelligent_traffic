import React from 'react';

import styles from './css/rightside.module.scss';
import OptimiseOverview from './OptimiseOverview/OptimiseOverview';
import OperationAnalysis from './operationAnalysis/OperationAnalysis';
import ControlModule from './controlModule/ControlModule';
import ScrollAlert from './scrollAlert/ScrollAlert';
export default function RightSide() {
    return (
        <div className={styles.rightside}>
            <OptimiseOverview />
            <OperationAnalysis />
            <ControlModule />
            <ScrollAlert />
        </div>
    )
}
