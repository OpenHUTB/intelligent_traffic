import React, { useState, useEffect } from 'react'
import styles from './css/timeProgress.module.scss';
import { useSelector, useDispatch } from 'react-redux';
import { setLight } from 'stores/junctionLight/lightControlSlice';
export default function TimeProgress() {
    const trafficLights = useSelector((state) => state.lightControl);
    console.log("trafficlights:", trafficLights);
    console.log("trafficlights map:", trafficLights instanceof Map);
    console.log("trafficlights keys:", Object.keys(trafficLights));
    let redDuration = 37;
    let yellowDuration = 3;
    let greenDuration = 47;
    const totalDuration = redDuration + yellowDuration + greenDuration;

    const bars = Object.entries(trafficLights).flatMap(([direction, movements]) =>
        Object.entries(movements).map(([movement, light]) => {
            const { redDurationTime, greenDurationTime } = light;
            const totalDuration = redDurationTime + greenDurationTime + 3;

            return (
                <div className={styles.bar} key={`${direction}-${movement}`}>
                    <div
                        className={styles.red}
                        style={{
                            width: `${(redDurationTime / totalDuration) * 100}%`,
                        }}
                    >
                        {redDurationTime}
                    </div>
                    <div
                        className={styles.yellow}
                        style={{
                            width: `${(3 / totalDuration) * 100}%`,
                        }}
                    >
                        3
                    </div>
                    <div
                        className={styles.green}
                        style={{
                            width: `${(greenDurationTime / totalDuration) * 100}%`,
                        }}
                    >
                        {greenDurationTime}
                    </div>
                </div>
            );
        })
    );
    return (
        <div className={styles.timeProgress}>
            <header className={styles.title}>
                <span>时间进度</span>
            </header>
            <main className={styles.progressMain}>
                <div className={styles.directions}>
                    <div className={styles.direction}>北向左转</div>
                    <div className={styles.direction}>北向直行</div>
                    <div className={styles.direction}>东向左转</div>
                    <div className={styles.direction}>东向直行</div>
                    <div className={styles.direction}>南向左转</div>
                    <div className={styles.direction}>南向直行</div>
                    <div className={styles.direction}>西向左转</div>
                    <div className={styles.direction}>西向直行</div>
                </div>
                <div className={styles.bars}>
                    {bars}
                </div>
            </main>
        </div>
    )
}
