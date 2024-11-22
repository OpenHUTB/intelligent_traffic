import React, { useEffect, useRef } from 'react';
import styles from '../css/even.module.scss';
export default function JamIndex() {

    const evenListItems = [{
        name: "旺龙路",
        status: "拥堵",
        direction: "方向： 东向西",
        length: "2.1",
        index: "6.2"
    }, {
        name: "尖山路",
        status: "拥堵",
        direction: "方向： 东向西",
        length: "1.2",
        index: "4.8"
    }]


    const evenList = evenListItems.map((item, index = 0) => {
        return (
            <div className={styles.evenListItem} key={index}>
                <span className={styles.name}>{item.name}</span>
                <div className={styles.contentContainer}>
                    <span className={styles.status}>{item.status}</span>
                    <span className={styles.direction}>{item.direction}</span>
                    <span className={styles.position}>{item.length} km</span>
                    <span className={styles.time}>拥堵指数{item.index}</span>
                </div>

            </div>
        )
    })
    // console.log(renderList);

    return (
        <div className={styles.even}>
            <div className={styles.title}>
                <span>拥堵警情</span>
            </div>
            {/* <div className={styles.listContainer}>
                {renderList}
            </div> */}
            <div className={styles.evenList}>
                {evenList}
            </div>
        </div>
    )
}
