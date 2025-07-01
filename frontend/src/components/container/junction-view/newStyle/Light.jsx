import React, { useState, useEffect } from 'react';
import styles from './css/light.module.scss';


const Light = ({ initialLight, redDuration, greenDuration }) => {
    const [light, setLight] = useState(initialLight || 'red');
    const [prevLight, setPrevLight] = useState(null);

    // 状态转换逻辑
    useEffect(() => {
        let timer;

        if (light === 'red') {
            timer = setTimeout(() => {
                setPrevLight('red');
                setLight('yellow');
            }, redDuration);
        } else if (light === 'green') {
            timer = setTimeout(() => {
                setPrevLight('green');
                setLight('yellow');
            }, greenDuration);
        } else if (light === 'yellow') {
            timer = setTimeout(() => {
                setLight(prevLight === 'red' ? 'green' : 'red');
            }, 3000); // 黄灯持续时间固定为3秒
        }

        return () => {
            clearTimeout(timer);
        };
    }, [light, redDuration, greenDuration, prevLight]);

    // 渲染组件
    return (
        <div>
            <div className={`${styles.lights} ${styles[light]}`}></div>

        </div>
    );
};

export default Light;