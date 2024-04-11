import React from 'react'
import TrafficInfo3Guage from './TrafficInfo3Guage';
import styles from './css/leftSide.module.scss';
export default function LeftSide() {
    return (
        <div className={styles.LeftSide}>
            <TrafficInfo3Guage />
        </div>
    )
}
