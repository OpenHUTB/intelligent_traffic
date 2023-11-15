import React from 'react';
import './index.scss';
import 'bootstrap/dist/css/bootstrap.min.css';
import ProgressBar from 'react-bootstrap/ProgressBar';
export default function ViolationOverview() {

    const violationList = [
        { name: "未礼让行人", value: 95 },
        { name: "违法停车", value: 80 },
        { name: "违法占道", value: 40 },
    ]

    const renderList = violationList.map((item, index) => {
        return (
            <div className="list-item">
                <span className='index'></span>
                <span>{item.name}</span>
                <span className="number">{item.value}<span className="unit">件</span></span>
                <ProgressBar min={0} max={100} now={item.value} />
            </div>
        )

    })

    return (
        <div className="violation-overview">
            <section className="violation-data">
                <div className="title">
                    <span>智能感知事件</span>
                </div>
                <div className="data-display">
                    <div className="overview-container">
                        <div className="total">
                            <span className='title'>事件总数</span>
                            <span className="number">{233}<span className="unit">件</span></span>
                        </div>
                        <div className="last-month">
                            <span className='title'>上一周期</span>
                            <span className='number'>183</span>
                        </div>
                        <div className="compare">
                            <span className='title'>环比</span>
                            <span className='number'>1.3%</span>
                        </div>
                    </div>
                    <div className="violation-chart-container">
                        <div className="violation-chart"></div>
                        <div className="chart-data-display">
                            {
                                renderList
                            }

                        </div>
                    </div>
                </div>

            </section>

        </div>
    )
}
