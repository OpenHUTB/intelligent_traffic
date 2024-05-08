import React from 'react'
import styles from './css/operationDetect.module.scss';
export default function OperationDetect() {
    return (
        <div className={styles.operationDetect}>
            <div className={styles.title}>运行事故检测</div>
            <div className={styles.content}>
                <div className={styles.number}><span>{0}</span><span className={styles.description}> 已确认 </span></div>
                <div className={styles.number}><span>{0}</span><span className={styles.description}> 处理中 </span></div>
                <div className={styles.number}><span>{0}</span><span className={styles.description}> 误报 </span></div>
                <div className={styles.number}><span>{0}</span><span className={styles.description}> 已完结 </span></div>
                <div className={styles.number}><span>{0}</span><span className={styles.description}> 总数 </span></div>
            </div>

        </div>
    )
}
