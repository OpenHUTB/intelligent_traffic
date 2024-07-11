import React from 'react';
import styles from './css/guage.module.scss';
export default function Guage() {
    return (
        <div className={styles.guage}>
            <div className={styles.title}>交通运行指数</div>
            <div className={styles.guageContainer}>

                <div className={styles.item}></div>
                <div className={styles.item}></div>
                <div className={styles.item}></div>
            </div>
        </div>

    );
};

