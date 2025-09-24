import React from 'react';
import './index.scss';

export default function Overview() {
    return (
        <div className="overview">
            <div className="title">
                <span>交通数据感知</span>
            </div>
            <div className="data-container">
                <div className="row-container">
                    <div className="data-item">
                        <span className="description">交通拥堵指数</span>
                        <span className="number">{4.3}<span className="unit"></span>  </span>
                    </div>
                    <div className="data-item">
                        <span className="description">拥堵里程</span>
                        <span className="number"><span className="unit">Km</span> {1.1} </span>
                    </div>
                </div>
                <div className="row-container">
                    <div className="data-item">

                        <span className="number"> {19}<span className="unit">个</span> </span>
                        <span className="description">拥堵路段总数</span>
                    </div>
                    <div className="data-item">

                        <span className="number"><span className="unit">min</span> {25} </span>
                        <span className="description">平均拥堵时长</span>
                    </div>
                </div>

            </div>
        </div>
    )
}
