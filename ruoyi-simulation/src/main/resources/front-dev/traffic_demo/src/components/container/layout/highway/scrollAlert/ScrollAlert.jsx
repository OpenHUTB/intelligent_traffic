import React, { useEffect, useRef } from 'react'
import styles from '../css/scrollAlert.module.scss';
import gif from 'assets/img/alert.gif'
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
            }, 10000); // Adjust the interval for faster/slower scrolling

            return () => clearInterval(interval); // Cleanup on component unmount
        }

        // startAutoScroll();
    }, []);


    const staticListItems = [{
        name: "逆行",
        position: "G60 100",
        status: "极高",
    },
    {
        name: "异常停车",
        position: "G60 100",
        status: "高",
    },
    {
        name: "超高速",
        status: "高",
        position: "G60 100",
    },
    {
        name: "超低速",
        status: "普通",
        position: "G60 100"
    },
    {
        name: "占用应急车道",
        status: "普通",
        position: "G60 100",
    },
    {
        name: "非机动车闯入",
        status: "极高",
        position: "G60 100",
    },
    {
        name: "行人闯入",
        status: "极高",
        position: "G60 100",
    },
    {
        name: "逆行",
        status: "极高",
        position: "G60 100",
    }
    ];

    const renderList = staticListItems.map((item, index = 0) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}>{index + 1}</span>
                <span className={styles.name}>{item.name}</span>
                <span className={styles.positionText}>{item.position}</span>
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
                <img className={styles.gif} src={gif} alt="" /><span className={styles.alertText}>{`${staticListItems[0].name}   ${staticListItems[0].position}   ${staticListItems[0].status} `}</span>
            </div>
            <div className={styles.rankContianer}>
                <span>序号</span>
                <span className={styles.violationName}> 类型</span>
                <span className={styles.violationPosition}>位置</span>
                <span>告警级别</span>
            </div>
            <div className={styles.listContainer} ref={scrollContainer}>
                {renderList}
            </div>
        </div>
    )
}
