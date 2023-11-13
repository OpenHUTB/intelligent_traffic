import React from 'react';
import { ReactComponent as RoadIcon } from '../../../../../assets/icon/icon-road.svg';
import { ReactComponent as JunctionIcon } from '../../../../../assets/icon/icon-junction.svg';
import { ReactComponent as TrafficLightIcon } from '../../../../../assets/icon/traffic-light-icon.svg';
import './style.scss';

export default function Index(props) {
    console.log(props)
    const { speed, distance, lights } = props.randomRoadStatus;
    return (
        <section className="leftTop">
            <div className="title">
                <span className="notification-text">道路概况</span>
            </div>
            <div className="statistic">
                <div className="stat-item">
                    <span className="icon"><JunctionIcon /></span>
                    <span className="label">路口速率(km/h)</span>
                    <span className="value">{speed}</span>
                </div>
                <div className="stat-item">
                    <span className="icon"><RoadIcon /></span>
                    <span className="label">道路里程(km)</span>
                    <span className="value">{distance}</span>
                </div>
                <div className="stat-item">
                    <span className="icon"><TrafficLightIcon /></span>
                    <span className="label">信号灯</span>
                    <span className="value">{lights}</span>
                </div>
            </div>
        </section>
    )
}
