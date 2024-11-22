import React from 'react';
import styles from '../css/optimiseOverview.module.scss';
import { ReactComponent as UP } from 'assets/icon/icon-up.svg';
import { ReactComponent as DOWN } from 'assets/icon/icon-down.svg';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
export default function OptimiseOverview() {
    return (
        <div className={styles.optimise}>
            <div className={styles.title}>信号优化总览</div>
            <header className={styles.header}>
                <span><TriangleIcon />优化效果</span>
                <div className={styles.buttonGroup}>
                    <button className={styles.activeBtn}>自动优化</button>
                    <button className={styles.button}>人工优化</button>
                </div>
            </header>
            <div className={styles.optimiseContainer}>

                <div className={styles.item}>
                    <span className={styles.number}>2.42 min <DOWN /></span>
                    <span className={styles.text}>平均延误</span>
                </div>
                <div className={styles.item}>
                    <span className={styles.number}>7.0% <DOWN /></span>
                    <span className={styles.text}>拥堵里程</span>
                </div>
                <div className={styles.item}>
                    <span className={styles.number}>5.2%<UP /></span>
                    <span className={styles.text}>平均速度</span>
                </div>
            </div>
        </div>

    );
};

