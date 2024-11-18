import React, { useEffect, useRef } from 'react';
import styles from '../css/signalJunction.module.scss';
import { ReactComponent as UP } from 'assets/icon/icon-up.svg';
import { ReactComponent as DOWN } from 'assets/icon/icon-down.svg';
export default function SignalRoad() {

    // pedestrain optimization list
    const scrollContainer = useRef(null);
    // useEffect(() => {
    //     const startAutoScroll = () => {
    //         const container = scrollContainer.current;
    //         const scrollAmount = 2.5; // Adjust for faster/slower scrolling

    //         const interval = setInterval(() => {
    //             // When you've scrolled to the end of the original content, reset to the top
    //             if (container.scrollTop >= (container.scrollHeight / 2)) {
    //                 container.scrollTop = 0; // Set to start without user noticing
    //             } else {
    //                 container.scrollTop += scrollAmount;
    //             }
    //         }, 50); // Adjust the interval for faster/slower scrolling

    //         return () => clearInterval(interval); // Cleanup on component unmount
    //     }

    //     startAutoScroll();
    // }, []);


    const staticListItems = [{
        name: "青山路",
        index: 1,
        speed: 20,
        trend: 18.53,
    },
    {
        name: "旺龙路",
        index: 3,
        speed: 30,
        trend: 20.53,
    },
    {
        name: "岳麓西大道",
        index: 4,
        speed: 40,
        trend: 15.53,
    },
    {
        name: "尖山路",
        index: 2,
        speed: 50,
        trend: 11.53,
    }
    ];

    const renderList = staticListItems.map((item, index = 0) => {
        return (
            <div className={styles.listItem}>
                <span className={styles.rank}>{item.name}</span>
                <span className={styles.name}>{item.index}</span>
                <span className={styles.name}>{item.speed}</span>
                <span className={styles.trend}>{item.trend}% {item.speed > 40 ? <UP /> : <DOWN />}</span>
                {/* <span className={`${styles[(item.status.includes("拥堵")) ? 'red' : (item.status.includes("畅通")) ? 'green' : '']} ${styles.status}`}>{item.status}</span> */}
            </div >
        )
    })

    // console.log(renderList);

    return (
        <div className={styles.trafficRank}>
            <div className={styles.title}>
                <span>信控道路</span>
            </div>
            <div className={styles.rankContainer}>
                <span>路段名称</span>
                <span className={styles.street}>拥堵指数</span>
                <span>平均速度</span>
                <span>拥堵趋势</span>
            </div>
            <div className={styles.listContainer} ref={scrollContainer}>
                {renderList}
                {/* {renderList} */}
            </div>
        </div>
    )
}
