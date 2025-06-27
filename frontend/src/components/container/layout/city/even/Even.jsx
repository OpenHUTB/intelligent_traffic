import React, { useEffect, useRef } from 'react';
import styles from '../css/even.module.scss';
export default function Even() {

    const staticListItems = [{
        name: "枫林三路",
        status: "严重拥堵",
    },
    {
        name: "咸嘉湖路",
        status: "中度拥堵",
    },
    {
        name: "桐梓坡路",
        status: "轻度拥堵",
    },
    {
        name: "玉兰路",
        status: "正常",
    },
    ];
    const evenListItems = [{
        name: "日常养护",
        status: "正在施工",
        position: "事件位置： 枫林三路",
        time: "计划时间： 2021-06-06 12:00 - 2021-06-06 18:00"
    }, {
        name: "日常养护",
        status: "正在施工",
        position: "事件位置： 咸嘉湖路",
        time: "计划时间： 2021-06-06 12:00 - 2021-06-06 18:00"
    }, {
        name: "日常养护",
        status: "正在施工",
        position: "事件位置： 桐梓坡路",
        time: "计划时间： 2021-06-06 12:00 - 2021-06-06 18:00"
    }, {
        name: "日常养护",
        status: "正在施工",
        position: "事件位置： 玉兰路",
        time: "计划时间： 2021-06-06 12:00 - 2021-06-06 18:00"
    }]

    const renderList = staticListItems.map((item, index = 0) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}>{index + 1}</span>
                <span className={styles.name}>{item.name}</span>
                <span className={`${styles[(item.status.includes("拥堵")) ? 'red' : (item.status.includes("畅通")) ? 'green' : '']} ${styles.status}`}>{item.status}</span>
            </div >
        )
    })

    const evenList = evenListItems.map((item, index = 0) => {
        return (
            <div className={styles.evenListItem} key={index}>
                <span className={styles.name}>{item.name}</span>
                <span className={styles.status}>{item.status}</span>
                <span className={styles.position}>{item.position}</span>
                <span className={styles.time}>{item.time}</span>
            </div>
        )
    })
    // console.log(renderList);

    return (
        <div className={styles.even}>
            <div className={styles.title}>
                <button>计划性事件</button>
                <button>非计划性事件</button>
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
