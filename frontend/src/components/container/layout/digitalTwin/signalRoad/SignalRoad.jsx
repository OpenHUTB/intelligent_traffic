// SignalRoad.js

import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/signalJunction.module.scss';
import { ReactComponent as UP } from 'assets/icon/icon-up.svg';
import { ReactComponent as DOWN } from 'assets/icon/icon-down.svg';
import { useSelector, useDispatch } from 'react-redux';
import { setListItems } from 'stores/digitalTwin/signalRoadSlice';

export default function SignalRoad() {
    const dispatch = useDispatch();

    // Get listItems from Redux store
    const listItems = useSelector((state) => state.signalRoad.listItems);

    // Event listener to update listItems in Redux
    useEffect(() => {
        const handleSignalRoadDataChanged = (event) => {
            console.log('SignalRoad Data Changed:', event.detail);
            dispatch(setListItems(event.detail));
        };

        window.addEventListener('signalRoadDataChanged', handleSignalRoadDataChanged);

        return () => {
            window.removeEventListener('signalRoadDataChanged', handleSignalRoadDataChanged);
        };
    }, [dispatch]);


    const renderList = listItems.map((item, index) => {
        return (
            <div className={styles.listItem} key={index}>
                <span className={styles.rank}>{item.name}</span>
                <span className={styles.name}>{item.index.toFixed(2)}</span>
                <span className={styles.name}>{item.speed.toFixed(2)}</span>
                <div className={styles.trendBox}>
                    <span className={styles.trend}>{item.trend.toFixed(2)}%</span>
                    {item.trendDirection ? <UP /> : <DOWN />}
                </div>
            </div>
        );
    });

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
            <div className={styles.listContainer}>{renderList}</div>
        </div>
    );
}