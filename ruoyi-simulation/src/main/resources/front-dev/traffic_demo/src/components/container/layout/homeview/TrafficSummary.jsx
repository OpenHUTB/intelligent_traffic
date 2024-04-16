import React, { useEffect } from 'react';

import styles from './css/trafficSummary.module.scss';

export default function TrafficSummary() {



    return (
        <div className={styles.trafficSummary}>
            <div className={styles.title}>区域概览</div>

            <div className={styles.dataContainer}>
                <span className={styles.road}>道路长度 <span className={styles.number}>{5}</span> km</span>
                <span className={styles.onRoadCar}>在途车辆<span className={styles.number}>{885}</span> 辆</span>
                <span className={styles.people}>行人数量 <span className={styles.number}>{214}</span> 位</span>
                <span className={styles.capacity}>路网饱和度 <span className={styles.number}>{20}</span> %</span>
            </div>
        </div>
    )
}



// <div className={styles.dataContainer}>
//     <div className="title">
//         <span>交通数据感知</span>
//     </div>
//     <div className="data-container">
//         <div className="row-container">
//             <div className="data-item">
//                 <span className="description">交通拥堵指数</span>
//                 <span className="number">{4.3}<span className="unit"></span>  </span>
//             </div>
//             <div className="data-item">
//                 <span className="description">拥堵里程</span>
//                 <span className="number"><span className="unit">Km</span> {1.1} </span>
//             </div>
//         </div>
//         <div className="row-container">
//             <div className="data-item">

//                 <span className="number"> {19}<span className="unit">个</span> </span>
//                 <span className="description">拥堵路段总数</span>
//             </div>
//             <div className="data-item">

//                 <span className="number"><span className="unit">min</span> {25} </span>
//                 <span className="description">平均拥堵时长</span>
//             </div>
//         </div>

//     </div>
// </div>
