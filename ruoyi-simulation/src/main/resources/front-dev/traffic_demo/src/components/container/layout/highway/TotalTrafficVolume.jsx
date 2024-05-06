import React from 'react'
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
import { ReactComponent as CarIcon } from 'assets/icon/icon-car.svg';
import styles from './css/totalTrafficVolume.module.scss';

export default function TotalTrafficVolume() {
    return (
        <div className={styles.slidertrafficFlow}>
            <h1 className={styles.sliderLabel}><span><TriangleIcon /></span>过车流量</h1>
            <div className="content-container">
                <span className="text"><span><CarIcon /></span>机动车过车总量</span>
                <span className="number">1635<span className="unit">辆</span></span>
            </div>
        </div>
    )
}
