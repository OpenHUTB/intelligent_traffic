import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { setInfo } from 'stores/trafficInfoSlice';

import './index.scss';

export default function Overview() {
    const info = useSelector((state) => state.trafficInfo);
    const dispatch = useDispatch();

    console.log(info);
    useEffect(() => {
        const handleTrafficInfoChange = (event) => {
            console.log(event.detail);
            const newInfo = { ...info, ...event.detail };
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
                        <span className="description">交通拥堵指数</span>
                        <span className="number">{info.index}<span className="unit"></span>  </span>
                    </div>
                    <div className="data-item">
                        <span className="description">拥堵里程</span>
                        <span className="number"><span className="unit">Km</span> {info.road} </span>
                    </div>
                </div>
                <div className="row-container">
                    <div className="data-item">

                        <span className="number"> {info.congestion}<span className="unit">个</span> </span>
                        <span className="description">拥堵路段总数</span>
                    </div>
                    <div className="data-item">

                        <span className="number"><span className="unit">min</span> {info.time} </span>
                        <span className="description">平均拥堵时长</span>
                    </div>
                </div>

            </div>
        </div>
    )
}
