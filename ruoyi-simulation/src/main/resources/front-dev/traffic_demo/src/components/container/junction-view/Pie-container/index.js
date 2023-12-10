import React, { useEffect, useState } from 'react'
import { ReactComponent as NavIcon } from 'assets/icon/icon-nav.svg';
import { ReactComponent as Car } from 'assets/icon/car.svg';
import { ReactComponent as LineUp } from 'assets/icon/Lineup1.svg';
import { ReactComponent as Parking } from 'assets/icon/parking.svg';
import { ReactComponent as Total } from 'assets/icon/time2.svg';
import { ReactComponent as C } from 'assets/icon/C3.svg';
export default function index() {
    //set the traffic monitor pie chart;
    const Counter = ({ counts, time = 500 }) => {
        const [count, setCount] = useState(0);
        useEffect(() => {
            let startTime = Date.now();
            let duration = 2000;
            const timer = setInterval(() => {
                setCount(() => {
                    let after = ((Date.now() - startTime) / duration * counts).toFixed(1);
                    if (after > counts) {
                        clearInterval(timer);
                        after = counts;
                    }
                    return after;
                });
            }, 16);
            return () => clearInterval(timer);
        }, [counts]);
        return count;
    }
    return (
        <div className="pie-container">
            <div className="title"><span className='svg'><NavIcon /></span><span>路口运行信息</span></div>
            <div id="traffic-monitor-piechart" >
                <div className='SVG-container'>
                    <Car /><LineUp /><Parking /><Total />
                </div>
                <div className="description">
                    <span>路口评级</span>
                    <span>排队长度</span>
                    <span>停车次数</span>
                    <span>平均延误</span>
                </div>
                <div className="textData">
                    <span id='grade'><C /></span>
                    <span className="number"><Counter counts={20.7} /><span className="unit">m</span></span>
                    <span className="number"><Counter counts={15} /><span className="unit">次</span></span>
                    <span className="number"><Counter counts={23.5} /><span className="unit">s</span></span>

                </div>
            </div>
        </div>
    )
}
