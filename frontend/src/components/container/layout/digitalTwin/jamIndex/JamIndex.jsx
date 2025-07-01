import React, { useEffect, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import styles from '../css/even.module.scss';
import { setJamIndex } from 'stores/digitalTwin/jamIndexSlice';
export default function JamIndex() {


    const dispatch = useDispatch();
    const jamIndex = useSelector((state) => state.jamIndex);
    console.log(jamIndex);
    useEffect(() => {
        const handleJamIndexChanged = (event) => {
            console.log(event.detail);
            // const key = Object.keys(event.detail);
            // const value = event.detail[key];
            // console.log(key, value);
            dispatch(setJamIndex(event.detail));
        };
        window.addEventListener('jamIndexChanged', handleJamIndexChanged);
        return () => {
            window.removeEventListener('jamIndexChanged', handleJamIndexChanged);
        }
    }, [dispatch])

    const evenList = jamIndex.map((item, index = 0) => {
        console.log('jamIndex', jamIndex);
        return (
            <div className={styles.evenListItem} key={index}>
                <span className={styles.name}>{item.name}</span>
                <div className={styles.contentContainer}>
                    <span className={styles.status}>{item.status}</span>
                    <span className={styles.direction}>平均速度<br />{item.speed}km/h</span>
                    <span className={styles.position}>拥堵趋势<br />{item.trend}% </span>
                    <span className={styles.time}>拥堵指数<br />{item.index}</span>
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
