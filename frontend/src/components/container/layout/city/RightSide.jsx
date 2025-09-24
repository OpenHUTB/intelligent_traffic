import React from 'react';

import styles from './css/rightside.module.scss';
import ViolationStatistic from './violationStatistic/ViolationStatistic';
import Even from './even/Even';
export default function RightSide() {
    return (
        <div className={styles.rightside}>
            <ViolationStatistic />
            <Even />
        </div>
    )
}
