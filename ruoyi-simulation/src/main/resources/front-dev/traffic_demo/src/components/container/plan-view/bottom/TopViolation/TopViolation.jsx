import React from 'react';
import './index.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import ProgressBar from 'react-bootstrap/ProgressBar';


export default function TopViolation() {

    const violationList = [
        { name: "未礼让行人", value: 95 },
        { name: "违法停车", value: 80 },
        { name: "违法占道", value: 40 },
        { name: "闯红灯", value: 30 },
        { name: "违法超车", value: 20 },
    ]

    const renderList = violationList.map((item, index) => {
        return (
            <div className="list-item">
                <span className='index'>{'0' + (index + 1)}</span>
                <span>{item.name}</span>
                <span className="number">{item.value}<span className="unit">件</span></span>
                <ProgressBar min={0} max={100} now={item.value} />
            </div>
        )

    })
    return (
        <div className="top-violation">
            <div className="title">
                <span>智能感知事件高发类型TOP5</span>
            </div>
            <div className="list-container">
                {renderList}
            </div>
        </div>
    )
}
