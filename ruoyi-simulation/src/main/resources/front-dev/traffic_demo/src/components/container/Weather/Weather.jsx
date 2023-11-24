import React, { useState, useEffect } from 'react';
import './index.scss';
import AMapLoader from '@amap/amap-jsapi-loader';
import { ReactComponent as SunIcon } from 'assets/icon/icon-sun.svg';
import { ReactComponent as CloudIcon } from 'assets/icon/icon-cloud.svg';
import { ReactComponent as RainIcon } from 'assets/icon/icon-rain.svg';
import { ReactComponent as SnowIcon } from 'assets/icon/icon-snow.svg';

export default function Weather() {
    const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
    const [temperatureNumber, setTemperatureNumber] = useState('');
    const [weatherStr, setWeatherStr] = useState('');
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


    // using the AMapLoader.load method to load JSAPI to set the weather;
    useEffect(() => {
        AMapLoader.load({
            key: "d8ab80d5c581443cc3862879e172edff",
            version: "2.0",
            plugins: ['AMap.Weather'],
        }).then((AMap) => {
            const weather = new AMap.Weather();
            weather.getLive('长沙市', (err, data) => {
                if (!err) {
                    const { temperature, weather } = data;
                    setTemperatureNumber(temperature);
                    setWeatherStr(weather);
                }
            });
        });
    }, []);
    return (
        <div className="weather-time-container">
            <div className="time-contianer">
                <div className="date">
                    <span>{date}</span>
                    <span>{dayOfWeek}</span>
                </div>
                <div className="time">{currentTime}</div>

            </div>
            <div className="weather-container">
                <div className="temperature">{`温度： ${temperatureNumber} °C`} </div>
                <div className="weather">
                    {weatherStr.includes("晴") ? <span className="weatherIcon"><SunIcon /></span> : null}
                    {weatherStr.includes("云") ? <span className="weatherIcon"><CloudIcon /></span> : null}
                    {weatherStr.includes("雨") ? <span className="weatherIcon"><RainIcon /></span> : null}
                    {weatherStr.includes("雪") ? <span className="weatherIcon"><SnowIcon /></span> : null}
                </div>
            </div>

        </div>
    )
}
