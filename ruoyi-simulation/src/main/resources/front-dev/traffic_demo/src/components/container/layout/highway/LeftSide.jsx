import React from 'react'
import Weather from './weather/Weather';
import TotalTrafficVolume from './TotalTrafficVolume';
import styles from './css//leftside.module.scss';
export default function LeftSide() {
    return (
        <div className={styles.leftside}>
            <Weather />
            <TotalTrafficVolume />
        </div>
    )
}
