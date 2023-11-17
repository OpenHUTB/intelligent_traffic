import React, { useState, useEffect } from 'react';
import './index.scss';

export default function Weather() {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const date = new Date().toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
    const dayOfWeek = new Date().toLocaleDateString('zh-CN', {
        weekday: 'long'
    });
    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentTime(new Date().toLocaleTimeString());
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, []);
    return (
        <div className="weather-time-container">
            <div className="time-contianer">
                <div className="date">{date} {dayOfWeek}</div>
                <div className="time">{currentTime}</div>
            </div>
            <div className="weather-container">
                <div className="temperature"></div>
                <div className="weather"></div>
            </div>

        </div>
    )
}
