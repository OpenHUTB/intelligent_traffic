import React from 'react'
import { useSelector, useDispatch } from 'react-redux';
import styles from './junction.module.scss'

export default function JunctionControl() {
    const junctionControl = useSelector((state) => state.junctionControl);
    const { name, status, controlPattern } = junctionControl;
    return (
        <div className={styles.junctionControl}>
            <div className={styles.controlPattern}>
                <span className={styles.name}>路口名称</span>
                <span className={styles.value}>{name}</span>
            </div>
            <div className={styles.controlPattern}>
                <span className={styles.name}>联机状态</span>
                <span className={styles.value}>{status}</span>
            </div>
            <div className={styles.controlPattern}>
                <span className={styles.name}>调控方案</span>
                <span className={styles.value}>{controlPattern}</span>
            </div>
        </div>
    )
}
