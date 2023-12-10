import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setInfo } from 'stores/trafficInfoSlice';

import './index.scss';

export default function Overview() {
    const info = useSelector((state) => state.trafficInfo);
    const dispatch = useDispatch();

    console.log(info);
    useEffect(() => {
        const handleTrafficInfoChange = (event) => {
            const key = Object.keys(event.detail)[0];
            const value = event.detail[key];
            const newInfo = { ...info, [key]: value };
            dispatch(setInfo(newInfo));

        };

        window.addEventListener('TrafficInfoChanged', handleTrafficInfoChange);
    }, [info, dispatch]);

    return (
        <div className="overview">
            <div className="title">
                <span>交通数据感知</span>
            </div>
            <div className="data-container">
                <div className="row-container">
                    <div className="data-item">
                        <span className="description">车辆数</span>
                        <span className="number">{info.car}<span className="unit">辆</span></span>
                    </div>
                    <div className="data-item">
                        <span className="description">路面里程</span>
                        <span className="number"><span className="unit">Km</span> {info.road} </span>
                    </div>
                </div>
                <div className="row-container">
                    <div className="data-item">

                        <span className="number"> {info.congestion}<span className="unit">个</span> </span>
                        <span className="description">拥堵路段个数</span>
                    </div>
                    <div className="data-item">

                        <span className="number"><span className="unit">Sec</span> {info.time} </span>
                        <span className="description">平均等待时长</span>
                    </div>
                </div>

            </div>
        </div>
    )
}
