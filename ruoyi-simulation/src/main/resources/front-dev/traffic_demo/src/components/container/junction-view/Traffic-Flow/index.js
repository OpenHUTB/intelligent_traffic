import React from 'react'
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { ReactComponent as TriangleIcon } from 'assets/icon/icon-triangle.svg';
import { ReactComponent as CarIcon } from 'assets/icon/icon-car.svg';

export default function Index() {
    return (
        <div className="traffic-flow-container">
            <div className="title"><span className='svg'><NavIcon /></span><span>实时态势监控</span></div>
            <div className="slider-container">
                <h1 className="slider-label"><span><TriangleIcon /></span>拥堵指数</h1>
                <input
                    type="range"
                    min="0"
                    max="10"
                    step="1"
                    defaultValue="7"
                    className="custom-range"
                />
                <div className="slider-values">
                    <span>1</span>
                    <span>1.4</span>
                    <span>1.8</span>
                    <span>2.2</span>
                    <span></span>
                </div>
                <div className="slider-describe">
                    <span>畅通</span>
                    <span>轻度拥堵</span>
                    <span>中度拥堵</span>
                    <span>严重拥堵</span>
                    <span></span>
                </div>
            </div>
            <div className="slider-trafficFlow">
                <h1 className="slider-label"><span><TriangleIcon /></span>过车流量</h1>
                <div className="content-container">
                    <span className="text"><span><CarIcon /></span>机动车过车总量</span>
                    <span className="number">1635<span className="unit">辆</span></span>
                </div>
            </div>
        </div>
    )
}
