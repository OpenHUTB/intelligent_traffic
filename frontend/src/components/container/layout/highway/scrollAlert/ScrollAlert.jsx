import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/scrollAlert.module.scss';
import gif from 'assets/img/alert.gif';

export default function TrafficRank() {
    const staticListItems = [{
        name: "逆行",
        position: "G60 k987",
        status: "极高",
    },
    {
        name: "异常停车",
        position: "G60 k987",
        status: "高",
    },
    {
        name: "超高速",
        status: "高",
        position: "G60 k984",
    },
    {
        name: "超低速",
        status: "普通",
        position: "G60 k988"
    },
    {
        name: "占用应急车道",
        status: "普通",
        position: "G60 k985",
    },
    {
        name: "非机动车闯入",
        status: "极高",
        position: "G60 k984",
    },
    {
        name: "行人闯入",
        status: "极高",
        position: "G60 k983",
    },
    {
        name: "逆行",
        status: "极高",
        position: "G60 k988",
    }
    ];

    const scrollContainer = useRef(null);
    const [currentItem, setCurrentItem] = useState(null);

    useEffect(() => {
        const startAutoScroll = () => {
            const scrollAmount = 2.5; // Adjust for faster/slower scrolling

            const interval = setInterval(() => {
                const container = scrollContainer.current;
                if (container) {
                    // When you've scrolled to the end of the original content, reset to the top
                    if (container.scrollTop >= (container.scrollHeight / 2)) {
                        container.scrollTop = 0; // Set to start without user noticing
                    } else {
                        container.scrollTop += scrollAmount;
                    }
                }
            }, 300); // Adjust the interval for faster/slower scrolling

            return interval;
        };

        const intervalId = startAutoScroll();

        const highStatusItems = staticListItems.filter(item => item.status === "极高");

        // 函数：随机选择一个对象
        const getRandomItem = () => {
            const randomIndex = Math.floor(Math.random() * highStatusItems.length);
            return highStatusItems[randomIndex];
        };

        // 设置初始显示的对象
        setCurrentItem(getRandomItem());

        // 每 3 秒钟更新一次显示的对象
        const randomItemIntervalId = setInterval(() => {
            setCurrentItem(getRandomItem());
        }, 3000);

        // 清除定时器
        return () => {
            clearInterval(intervalId);
            clearInterval(randomItemIntervalId);
        };
    }, []);

    const renderList = staticListItems.map((item, index) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}>{index + 1}</span>
                <span className={styles.name}>{item.name}</span>
                <span className={styles.positionText}>{item.position}</span>
                <span className={`${styles.status} ${(item.status.includes("极") ? styles.red : (item.status.includes("高") ? styles.yellow : ""))}`}>
                    {item.status}
                </span>
            </div>
        );
    });

    if (!currentItem) {
        return <div>加载中...</div>;
    }

    return (
        <div className={styles.trafficViolation}>
            <div className={styles.title}>
                <span>实时告警信息</span>
            </div>
            <div className={styles.emergency}>
                <img className={styles.gif} src={gif} alt="" /><span className={styles.alertText}>{`${currentItem.name}   ${currentItem.position}   ${currentItem.status} `}</span>
            </div>
            <div className={styles.rankContianer}>
                <span>序号</span>
                <span className={styles.violationName}> 类型</span>
                <span className={styles.violationPosition}>位置</span>
                <span>告警级别</span>
            </div>
            <div className={styles.listContainer} ref={scrollContainer}>
                {renderList}
                {renderList}
            </div>
        </div>
    );
}