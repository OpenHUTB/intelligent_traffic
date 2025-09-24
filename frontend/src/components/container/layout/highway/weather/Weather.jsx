
import React from 'react';

import styles from '../css/weather.module.scss';

export default function Weather() {

    return (
        <div className={styles.weatherContainer}>
            <div className={styles.title}>气象状况</div>

            <div className={styles.weatherDetails}>
                <span className={styles.windDirection}> 风向 <span className={styles.number}>{5}</span>  .       </span>
                <span className={styles.windSpeed}>风速<span className={styles.number}>{3}</span> m/s</span>
                <span className={styles.rainVolume}>降水量 <span className={styles.number}>{0}</span> mm</span>
                <span className={styles.moisture}>湿度 <span className={styles.number}>{58}</span> %</span>
            </div>
        </div>
    )
}
