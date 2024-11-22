import React from 'react'
import styles from '../css/controlModule.module.scss';
export default function ControlModule() {
    return (
        <div className={styles.controlModule}>
            <div className={styles.title}>
                <span>路口控制模式</span>
            </div>
            <div className={styles.contentContainer}>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>自适应控制</span>
                    <span className={styles.value}>14</span>
                </div>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>动态调控</span>
                    <span className={styles.value}>7</span>
                </div>
                <div className={styles.controlPattern}>
                    <span className={styles.name}>人工调控</span>
                    <span className={styles.value}>2</span>
                </div>
            </div>
        </div>
    )
}
