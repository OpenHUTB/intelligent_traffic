// ScrollAlert.js

import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/scrollAlert.module.scss';
import gif from 'assets/img/alert.gif';
import { useSelector, useDispatch } from 'react-redux';
import { setListItems } from 'stores/digitalTwin/scrollAlertSlice';

export default function ScrollAlert() {
    const dispatch = useDispatch();

    // Get listItems from Redux store
    const listItems = useSelector((state) => state.scrollAlert.listItems);

    const scrollContainer = useRef(null);
    const [currentItem, setCurrentItem] = useState(null);

    // Event listener to update listItems in Redux
    useEffect(() => {
        const handleScrollAlertDataChanged = (event) => {
            console.log('ScrollAlert Data Changed:', event.detail);
            dispatch(setListItems(event.detail));
        };

        window.addEventListener('scrollAlertDataChanged', handleScrollAlertDataChanged);

        return () => {
            window.removeEventListener('scrollAlertDataChanged', handleScrollAlertDataChanged);
        };
    }, [dispatch]);

    useEffect(() => {
        const startAutoScroll = () => {
            const scrollAmount = 2.5; // Adjust for faster/slower scrolling

            const interval = setInterval(() => {
                const container = scrollContainer.current;
                if (container) {
                    // When you've scrolled to the end of the original content, reset to the top
                    if (container.scrollTop >= container.scrollHeight / 2) {
                        container.scrollTop = 0; // Reset to start without user noticing
                    } else {
                        container.scrollTop += scrollAmount;
                    }
                }
            }, 300); // Adjust the interval for faster/slower scrolling

            return interval;
        };

        const intervalId = startAutoScroll();

        // Filter items with high status (assuming you have a 'status' field)
        const highStatusItems = listItems.filter((item) => item.status === '极高');

        // Function: Randomly select an item
        const getRandomItem = () => {
            if (highStatusItems.length > 0) {
                const randomIndex = Math.floor(Math.random() * highStatusItems.length);
                return highStatusItems[randomIndex];
            } else {
                return null;
            }
        };

        // Set the initial item
        setCurrentItem(getRandomItem());

        // Update currentItem every 3 seconds
        const randomItemIntervalId = setInterval(() => {
            setCurrentItem(getRandomItem());
        }, 3000);

        // Cleanup
        return () => {
            clearInterval(intervalId);
            clearInterval(randomItemIntervalId);
        };
    }, [listItems]);

    const renderList = listItems.map((item, index) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}></span>
                <span className={styles.name}>{item.name}</span>
                <span className={styles.status}>{item.time}</span>
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