import React from 'react'
import Map from './Map/Map';
import styles from './css/rightSide.module.scss';
export default function RightSide() {
    return (
        <div className={styles.rightSide}>
            <Map />
        </div>
    )

}