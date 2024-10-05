import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/scrollAlert.module.scss';
import gif from 'assets/img/alert.gif';

export default function ScrollAlert() {
    const staticListItems = [{
        name: `在青山路与岳麓西大道交叉口,西方向左转  红灯增加3秒,绿灯减少1秒;直行绿灯增加10秒,红灯减少5秒`,
        time: "09:12:34",
    },
    {
        name: `在岳麓西大道与旺龙路交叉口，南向北方向左转  红灯增加2秒，绿灯减少2秒；直行绿灯增加8秒，红灯减少4秒`,
        time: "09:15:31",

    },
    {
        name: `在旺龙路与青山路交叉口，西向东方向左转 红灯增加4秒，绿灯减少2秒；直行绿灯增加12秒，红灯减少6秒`,
        time: "09:22:11",

    },
    {
        name: `在青山路与旺龙路交叉口，北向南方向左转 红灯增加5秒，绿灯减少3秒；直行绿灯增加15秒，红灯减少7秒`,
        time: "09:23:21",
    },
    {
        name: `占在岳麓西大道与青山路交叉口，东向西方向左转 红灯增加1秒，绿灯减少1秒；直行绿灯增加6秒，红灯减少3秒`,
        time: "09:25:31",
    },

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
                <span className={styles.rank}></span>
                <span className={styles.name}>{item.name}</span>
                <span className={styles.status}>
                    {item.time}
                </span>
            </div>
        );
    });

    return (
        <div className={styles.trafficViolation}>
            <div className={styles.title}>
                <span>实时优化播报</span>
            </div>
            <div className={styles.listContainer} ref={scrollContainer}>
                {renderList}
                {renderList}
            </div>
        </div>
    );
}