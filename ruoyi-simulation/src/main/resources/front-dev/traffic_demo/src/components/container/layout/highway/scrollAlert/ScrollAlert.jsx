import React, { useEffect, useRef } from 'react'
import styles from '../css/scrollAlert.module.scss';
export default function TrafficRank() {

    // pedestrain optimization list
    const scrollContainer = useRef(null);
    useEffect(() => {
        const startAutoScroll = () => {
            const container = scrollContainer.current;
            const scrollAmount = 2.5; // Adjust for faster/slower scrolling

            const interval = setInterval(() => {
                // When you've scrolled to the end of the original content, reset to the top
                if (container.scrollTop >= (container.scrollHeight / 2)) {
                    container.scrollTop = 0; // Set to start without user noticing
                } else {
                    container.scrollTop += scrollAmount;
                }
            }, 1000); // Adjust the interval for faster/slower scrolling

            return () => clearInterval(interval); // Cleanup on component unmount
        }

        startAutoScroll();
    }, []);


    const staticListItems = [{
        name: "逆行",
        status: "极高",
    },
    {
        name: "异常停车",
        status: "高",
    },
    {
        name: "超高速",
        status: "高",
    },
    {
        name: "超低速",
        status: "普通",
    },
    {
        name: "占用应急车道",
        status: "普通",
    },
    {
        name: "非机动车闯入",
        status: "极高",
    },
    {
        name: "行人闯入",
        status: "极高",
    },
    {
        name: "逆行",
        status: "极高",
    }
    ];

    const renderList = staticListItems.map((item, index = 0) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}>{index + 1}</span>
                <span className={styles.name}>{item.name}</span>
                <span className={`${styles.status} ${(item.status.includes("极") ? styles.red : (item.status.includes("高") ? styles.yellow : ""))}`}>
                    {item.status}
                </span>
            </div >
        )
    })


    return (
        <div className={styles.trafficViolation}>
            <div className={styles.title}>
                <span>实时告警信息</span>
            </div>
            <div className={styles.emergency}>
                <span>非常紧急</span>
            </div>
            <div className={styles.rankContianer}>
                <span>序号</span>
                <span className={styles.violationName}>告警信息类型</span>
                <span>告警级别</span>
            </div>
            <div className={styles.listContainer} ref={scrollContainer}>
                {renderList}
                {renderList}
            </div>
        </div>
    )
}
